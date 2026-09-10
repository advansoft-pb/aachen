package pl.advansoft.aachen.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.advansoft.aachen.order.domain.models.OrderStatus;
import pl.advansoft.aachen.order.domain.models.OrderSummary;

import java.util.List;
import java.util.Optional;

interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByStatus(OrderStatus status);

    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    @Query("select distinct o from OrderEntity o left join fetch o.items where o.userName = :userName and o.orderNumber = :orderNumber")
    Optional<OrderEntity> findByUserNameAndOrderNumber(String userName, String orderNumber);

    @Query("select new pl.advansoft.aachen.order.domain.models.OrderSummary(o.orderNumber, o.status) from OrderEntity o where o.userName = :userName")
    List<OrderSummary> findByUserName(String userName);

    default void updateOrderStatus(String orderNumber, OrderStatus status) {
        OrderEntity order = findByOrderNumber(orderNumber).orElseThrow();
        order.setStatus(status);
        save(order);
    }
}
