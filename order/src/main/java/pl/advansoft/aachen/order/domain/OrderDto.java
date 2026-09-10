package pl.advansoft.aachen.order.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import pl.advansoft.aachen.order.domain.models.Address;
import pl.advansoft.aachen.order.domain.models.Customer;
import pl.advansoft.aachen.order.domain.models.OrderItem;
import pl.advansoft.aachen.order.domain.models.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record OrderDto(
        String orderNumber,
        String userName,
        Set<OrderItem> items,
        Customer customer,
        Address deliveryAddress,
        OrderStatus status,
        String comments,
        LocalDateTime createdAt) {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public BigDecimal getTotalAmount() {
        return items
                .stream()
                .map(item -> item.price().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
