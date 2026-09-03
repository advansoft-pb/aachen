package pl.advansoft.aachen.order.domain;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.advansoft.aachen.order.domain.models.CreateOrderRequest;
import pl.advansoft.aachen.order.domain.models.CreateOrderResponse;

@RestController
@RequestMapping("/api/orders")
class OrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    private final SecurityService securityService;

    OrderController(OrderService orderService, SecurityService securityService) {
        this.orderService = orderService;
        this.securityService = securityService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateOrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request) {
        String userName = securityService.getLoginUserName();
        LOGGER.info("Creating order for user: {}", userName);
        return orderService.createOrder(userName, request);
    }
}
