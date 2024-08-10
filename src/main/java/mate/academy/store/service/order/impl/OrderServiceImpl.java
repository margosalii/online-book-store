package mate.academy.store.service.order.impl;

import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.store.dto.order.RequestOrderDto;
import mate.academy.store.dto.order.ResponseOrderDto;
import mate.academy.store.dto.order.UpdateOrderRequestDto;
import mate.academy.store.dto.orderitem.OrderItemDto;
import mate.academy.store.mapper.OrderItemMapper;
import mate.academy.store.mapper.OrderMapper;
import mate.academy.store.model.CartItem;
import mate.academy.store.model.Order;
import mate.academy.store.model.OrderItem;
import mate.academy.store.model.ShoppingCart;
import mate.academy.store.model.Status;
import mate.academy.store.repository.order.OrderRepository;
import mate.academy.store.repository.orderitem.OrderItemRepository;
import mate.academy.store.repository.shopping.cart.ShoppingCartRepository;
import mate.academy.store.service.order.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    @Transactional
    public ResponseOrderDto placeNewOrder(Long userId, RequestOrderDto requestOrderDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Can't find users shopping cart by user id: "
                    + userId));

        Order order = new Order();
        Set<OrderItem> orderItems = createSetOfOrderItems(order, shoppingCart);

        order.setOrderItems(orderItems);
        order.setTotal(calculateTotal(orderItems));
        order.setShippingAddress(requestOrderDto.getShippingAddress());
        order.setUser(shoppingCart.getUser());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Status.PENDING);

        shoppingCart.clearCart();
        shoppingCartRepository.save(shoppingCart);

        Order savedOrder = orderRepository.save(order);

        orderItems.forEach(orderItemRepository::save);
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public List<ResponseOrderDto> getAllUsersOrders(Long userId) {
        return orderRepository.findAllByUserId(userId)
            .stream()
            .map(orderMapper::toDto)
            .toList();
    }

    @Override
    @Transactional
    public ResponseOrderDto updateOrderStatus(Long id, UpdateOrderRequestDto requestDto) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find order by id: " + id));
        orderMapper.updateOrderFromDto(requestDto, order);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderItemDto getItemByOrderIdAndItemId(Long orderId, Long id) {
        return orderItemMapper
            .toDto(orderItemRepository.findByOrderIdAndId(orderId, id).orElseThrow(
                () -> new EntityNotFoundException(String.format(
                    "Can't find order item by order id: %s and item id: %s", orderId, id))));
    }

    @Override
    public List<OrderItemDto> getItemsByOrderId(Long id) {
        return orderItemRepository.findAllByOrderId(id)
            .stream()
            .map(orderItemMapper::toDto)
            .toList();
    }

    private Set<OrderItem> createSetOfOrderItems(Order order, ShoppingCart shoppingCart) {
        Set<OrderItem> orderItems = new HashSet<>();
        Set<CartItem> cartItems = shoppingCart.getCartItems();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(cartItem.getBook());
            orderItem.setPrice(cartItem.getBook().getPrice());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }
        return orderItems;
    }

    private BigDecimal calculateTotal(Set<OrderItem> orderItems) {
        BigDecimal total = BigDecimal.valueOf(0);
        for (OrderItem orderItem : orderItems) {
            total = total.add(orderItem.getPrice());
        }
        return total;
    }
}
