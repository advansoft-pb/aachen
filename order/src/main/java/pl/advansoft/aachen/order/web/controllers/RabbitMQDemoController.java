package pl.advansoft.aachen.order.web.controllers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.advansoft.aachen.order.ApplicationProperties;

@RestController
class RabbitMQDemoController {

    private final RabbitTemplate template;
    private final ApplicationProperties properties;

    RabbitMQDemoController(RabbitTemplate template, ApplicationProperties properties) {
        this.template = template;
        this.properties = properties;
    }

    @PostMapping("/send")
    public void sendMessage(@RequestBody MyMessage message) {
        template.convertAndSend(
                properties.orderEventsExchange(),
                message.routingKey(),
                message.payload());
    }
}

record MyMessage(String routingKey, MyPayload payload) {
}

record MyPayload(String content) {
}

