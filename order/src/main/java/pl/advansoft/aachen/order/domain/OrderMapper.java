package pl.advansoft.aachen.order.domain;

import pl.advansoft.aachen.order.domain.models.CreateOrderRequest;
import pl.advansoft.aachen.order.domain.models.OrderItem;
import pl.advansoft.aachen.order.domain.models.OrderStatus;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

class OrderMapper {

    static OrderEntity convertToEntity(CreateOrderRequest request) {

        OrderEntity newOrder = new OrderEntity();
        newOrder.setOrderNumber(UUID.randomUUID().toString());
        newOrder.setStatus(OrderStatus.NEW);
        newOrder.setCustomer(request.customer());
        newOrder.setDeliveryAddress(request.deliveryAddress());

        Set<OrderItemEntity> orderItems = new HashSet<>();

        for (OrderItem item : request.items()) {

            OrderItemEntity orderItem = new OrderItemEntity();

            orderItem.setCode(item.code());
            orderItem.setName(item.name());
            orderItem.setPrice(item.price());
            orderItem.setQuantity(item.quantity());

            orderItem.setOrder(newOrder);

            orderItems.add(orderItem);
        }

        newOrder.setItems(orderItems);

        return newOrder;
    }

    static OrderDto convertToDto(OrderEntity entity) {

        Set<OrderItem> items = entity
                .getItems()
                .stream()
                .map(item -> new OrderItem(
                        item.getCode(),
                        item.getName(),
                        item.getPrice(),
                        item.getQuantity()
                ))
                .collect(Collectors.toSet());

        return new OrderDto(
                entity.getOrderNumber(),
                entity.getUserName(),
                items,
                entity.getCustomer(),
                entity.getDeliveryAddress(),
                entity.getStatus(),
                entity.getComments(),
                entity.getCreatedAt()
        );
    }
}
