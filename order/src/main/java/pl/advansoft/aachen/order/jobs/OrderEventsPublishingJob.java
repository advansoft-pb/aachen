package pl.advansoft.aachen.order.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.advansoft.aachen.order.domain.OrderEventService;

import java.time.Instant;

@Component
public class OrderEventsPublishingJob {

    private final static Logger LOGGER = LoggerFactory.getLogger(OrderEventsPublishingJob.class);

    private final OrderEventService orderEventService;

    OrderEventsPublishingJob(OrderEventService orderEventService) {
        this.orderEventService = orderEventService;
    }

    @Scheduled(cron = "${orders.publish-order-events-job-cron}")
    public void publishOrderEvents() {
        LOGGER.info("Publishing Order Events at {}", Instant.now());
        orderEventService.publishOrderEvents();
    }
}
