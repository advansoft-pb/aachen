package pl.advansoft.aachen.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.advansoft.aachen.order.domain.models.OrderStatus;

import java.util.List;
import java.util.Optional;

interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByStatus(OrderStatus status);

    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    default void updateOrderStatus(String orderNumber, OrderStatus status) {
        OrderEntity order = findByOrderNumber(orderNumber).orElseThrow();
        order.setStatus(status);
        save(order);
    }
}
