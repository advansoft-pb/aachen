package pl.advansoft.aachen.order.domain;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.advansoft.aachen.order.domain.models.OrderCreatedEvent;
import pl.advansoft.aachen.order.domain.models.OrderEventType;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;

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

    void save(OrderCreatedEvent event) {
        OrderEventEntity orderEvent = new OrderEventEntity();
        orderEvent.setEventId(event.eventId());
        orderEvent.setEventType(OrderEventType.ORDER_CREATED);
        orderEvent.setOrderNumber(event.orderNumber());
        orderEvent.setCreatedAt(event.createdAt());
        orderEvent.setPayload(objectMapper.writeValueAsString(event));
        orderEventRepository.save(orderEvent);
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
        if (Objects.requireNonNull(eventType) == OrderEventType.ORDER_CREATED) {
            OrderCreatedEvent orderCreatedEvent = objectMapper.readValue(event.getPayload(), OrderCreatedEvent.class);
            orderEventPublisher.publish(orderCreatedEvent);
        } else {
            LOGGER.warn("Unsupported OrderEventType: {}", eventType);
        }
    }
}
