package pl.advansoft.aachen.order.domain;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import pl.advansoft.aachen.order.ApplicationProperties;
import pl.advansoft.aachen.order.domain.models.OrderCancelledEvent;
import pl.advansoft.aachen.order.domain.models.OrderCreatedEvent;
import pl.advansoft.aachen.order.domain.models.OrderDeliveredEvent;
import pl.advansoft.aachen.order.domain.models.OrderErrorEvent;

@Component
class OrderEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ApplicationProperties properties;

    OrderEventPublisher(RabbitTemplate rabbitTemplate, ApplicationProperties properties) {
        this.rabbitTemplate = rabbitTemplate;
        this.properties = properties;
    }

    public void publish(OrderCreatedEvent event) {
        send(properties.newOrdersQueue(), event);
    }

    public void publish(OrderDeliveredEvent event) {
        send(properties.deliveredOrdersQueue(), event);
    }

    public void publish(OrderCancelledEvent event) {
        send(properties.cancelledOrdersQueue(), event);
    }

    public void publish(OrderErrorEvent event) {
        send(properties.errorOrdersQueue(), event);
    }

    private void send(String routingKey, Object payload) {
        rabbitTemplate.convertAndSend(properties.orderEventsExchange(), routingKey, payload);
    }
}
