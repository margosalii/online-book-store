package mate.academy.store.service.order;

import java.util.Set;
import mate.academy.store.dto.order.RequestOrderDto;
import mate.academy.store.dto.order.ResponseOrderDto;
import mate.academy.store.dto.order.UpdateOrderRequestDto;
import mate.academy.store.dto.orderitem.OrderItemDto;

public interface OrderService {
    ResponseOrderDto placeNewOrder(Long userId, RequestOrderDto requestOrderDto);

    Set<ResponseOrderDto> getAllUsersOrders(Long userId);

    ResponseOrderDto updateOrderStatus(Long id, UpdateOrderRequestDto requestDto);

    OrderItemDto getItemByOrderIdAndItemId(Long orderId, Long id);

    Set<OrderItemDto> getItemsByOrderId(Long id);
}
