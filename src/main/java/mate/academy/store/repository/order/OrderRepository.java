package mate.academy.store.repository.order;

import java.util.Set;
import mate.academy.store.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Set<Order> findAllByUserId(Long id);
}
