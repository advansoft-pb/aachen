package pl.advansoft.aachen.order.domain;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.advansoft.aachen.order.domain.models.*;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Service
@Transactional
public class OrderEventService {

    private final Logger LOGGER = LoggerFactory.getLogger(OrderEventService.class);

    private final OrderEventRepository orderEventRepository;
    private final OrderEventPublisher orderEventPublisher;
    private final ObjectMapper objectMapper;

    OrderEventService(OrderEventRepository orderEventRepository,
                      OrderEventPublisher orderEventPublisher,
                      ObjectMapper objectMapper) {
        this.orderEventRepository = orderEventRepository;
        this.orderEventPublisher = orderEventPublisher;
        this.objectMapper = objectMapper;
    }

    public void publishOrderEvents() {
        Sort sort = Sort.by("createdAt").ascending();
        List<OrderEventEntity> events = orderEventRepository.findAll(sort);
        LOGGER.info("Found {} Order Events to be published", events.size());

        for (OrderEventEntity event : events) {
            publishEvent(event);
            orderEventRepository.delete(event);
        }
    }

    private void publishEvent(OrderEventEntity event) {
        OrderEventType eventType = event.getEventType();

        switch (eventType) {
            case ORDER_CREATED -> {
                OrderCreatedEvent orderEvent = objectMapper.readValue(event.getPayload(), OrderCreatedEvent.class);
                orderEventPublisher.publish(orderEvent);
            }

            case ORDER_DELIVERED -> {
                OrderDeliveredEvent orderEvent = objectMapper.readValue(event.getPayload(), OrderDeliveredEvent.class);
                orderEventPublisher.publish(orderEvent);
            }

            case ORDER_CANCELLED -> {
                OrderCancelledEvent orderEvent = objectMapper.readValue(event.getPayload(), OrderCancelledEvent.class);
                orderEventPublisher.publish(orderEvent);
            }

            case ORDER_PROCESSING_FAILED -> {
                OrderErrorEvent orderEvent = objectMapper.readValue(event.getPayload(), OrderErrorEvent.class);
                orderEventPublisher.publish(orderEvent);
            }

            default -> LOGGER.warn("Unsupported OrderEventType: {}", eventType);
        }
    }

    void save(OrderCreatedEvent event) {
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_CREATED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(objectMapper.writeValueAsString(event));
        orderEventRepository.save(orderEvent);
    }

    void save(OrderDeliveredEvent event) {
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_DELIVERED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(objectMapper.writeValueAsString(event));
        orderEventRepository.save(orderEvent);
    }

    void save(OrderCancelledEvent event) {
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_CANCELLED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(objectMapper.writeValueAsString(event));
        orderEventRepository.save(orderEvent);
    }

    void save(OrderErrorEvent event) {
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_PROCESSING_FAILED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(objectMapper.writeValueAsString(event));
        orderEventRepository.save(orderEvent);
    }
}
