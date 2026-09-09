package pl.advansoft.aachen.order.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.advansoft.aachen.order.domain.OrderService;

import java.time.Instant;

@Component
class OrderProcessingJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderProcessingJob.class);

    private final OrderService orderService;

    OrderProcessingJob(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(cron = "${orders.new-orders-job-cron}")
    public void processNewOrders() {
        LOGGER.info("Processing New Orders at {}", Instant.now());
        orderService.processNewOrders();
    }
}
