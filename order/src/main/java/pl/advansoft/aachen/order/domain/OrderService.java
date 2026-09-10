package pl.advansoft.aachen.order.domain;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.advansoft.aachen.order.domain.models.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class OrderService {

    private static final Set<String> DELIVERY_ALLOWED_COUNTRIES = Set.of("POLAND", "GERMANY", "UK");

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final OrderEventService orderEventService;

    OrderService(OrderRepository orderRepository,
                 OrderValidator orderValidator,
                 OrderEventService orderEventService) {
        this.orderRepository = orderRepository;
        this.orderValidator = orderValidator;
        this.orderEventService = orderEventService;
    }

    public List<OrderSummary> findOrders(String userName) {
        return orderRepository.findByUserName(userName);
    }

    public Optional<OrderDto> findUserOrder(String userName, String orderNumber) {
        return orderRepository.findByUserNameAndOrderNumber(userName, orderNumber).map(OrderMapper::convertToDto);
    }

    public CreateOrderResponse createOrder(String userName, CreateOrderRequest request) {
        orderValidator.validate(request);
        OrderEntity newOrder = OrderMapper.convertToEntity(request);
        newOrder.setUserName(userName);
        OrderEntity savedOrder = orderRepository.save(newOrder);
        LOGGER.info("Created Order with orderNumber = {}", savedOrder.getOrderNumber());
        OrderCreatedEvent orderCreatedEvent = OrderEventMapper.buildOrderCreatedEvent(savedOrder);
        orderEventService.save(orderCreatedEvent);
        return new CreateOrderResponse(savedOrder.getOrderNumber());
    }

    public void processNewOrders() {
        List<OrderEntity> orders = orderRepository.findByStatus(OrderStatus.NEW);
        LOGGER.info("Found {} new orders to process", orders.size());
        for (OrderEntity order : orders) {
            process(order);
        }
    }

    private void process(OrderEntity order) {
        try {
            if (canBeDelivered(order)) {
                LOGGER.info("OrderNumber: {} can be delivered", order.getOrderNumber());
                orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.DELIVERED);
                orderEventService.save(OrderEventMapper.buildOrderDeliveredEvent(order));
            } else {
                LOGGER.info("OrderNumber: {} cannot be delivered", order.getOrderNumber());
                orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.CANCELLED);
                orderEventService.save(OrderEventMapper.buildOrderCancelledEvent(order,
                        "Can't deliver to the location"));
            }
        } catch (RuntimeException ex) {
            LOGGER.error("Failed to process Order with orderNumber: {}", order.getOrderNumber(), ex);
            orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.ERROR);
            orderEventService.save(OrderEventMapper.buildOrderErrorEvent(order, ex.getMessage()));
        }
    }

    private boolean canBeDelivered(OrderEntity order) {
        return DELIVERY_ALLOWED_COUNTRIES.contains(order.getDeliveryAddress().country().toUpperCase());
    }
}
