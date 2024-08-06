package mate.academy.store.service.orderitem;

import java.util.Set;
import mate.academy.store.dto.orderitem.OrderItemDto;
import mate.academy.store.model.OrderItem;

public interface OrderItemService {
    OrderItemDto save(OrderItem orderItem);

    OrderItemDto getItemByOrderIdAndItemId(Long orderId, Long id);

    Set<OrderItemDto> getItemsByOrderId(Long id);
}
