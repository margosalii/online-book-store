package mate.academy.store.service.orderitem.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.store.dto.orderitem.OrderItemDto;
import mate.academy.store.mapper.OrderItemMapper;
import mate.academy.store.model.OrderItem;
import mate.academy.store.repository.orderitem.OrderItemRepository;
import mate.academy.store.service.orderitem.OrderItemService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    @Override
    public OrderItemDto save(OrderItem orderItem) {
        return orderItemMapper.toDto(orderItemRepository.save(orderItem));

    }

    @Override
    public OrderItemDto getItemByOrderIdAndItemId(Long orderId, Long id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find item by id: " + id));

        if (orderItem.getOrder().getId().equals(orderId)) {
            return orderItemMapper.toDto(orderItem);
        }
        throw new EntityNotFoundException("Can't find item by order id: " + orderId);
    }

    @Override
    public Set<OrderItemDto> getItemsByOrderId(Long id) {
        return orderItemRepository.findAllByOrderId(id)
            .stream()
            .map(orderItemMapper::toDto)
            .collect(Collectors.toSet());
    }
}




























