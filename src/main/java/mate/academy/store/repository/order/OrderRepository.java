package mate.academy.store.repository.order;

import java.util.Set;
import mate.academy.store.model.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = "orderItems")
    Set<Order> findAllByUserId(Long id);
}
