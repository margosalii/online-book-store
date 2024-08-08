package mate.academy.store.repository.orderitem;

import java.util.Set;
import mate.academy.store.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Set<OrderItem> findAllByOrderId(Long id);
}
