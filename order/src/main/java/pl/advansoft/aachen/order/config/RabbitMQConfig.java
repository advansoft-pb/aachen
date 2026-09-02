package pl.advansoft.aachen.order.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.advansoft.aachen.order.ApplicationProperties;
import tools.jackson.databind.json.JsonMapper;

@Configuration
class RabbitMQConfig {

    private final ApplicationProperties properties;

    RabbitMQConfig(ApplicationProperties properties) {
        this.properties = properties;
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(properties.orderEventsExchange());
    }

    @Bean
    Queue newOrdersQueue() {
        return QueueBuilder.durable(properties.newOrdersQueue()).build();
    }

    @Bean
    Binding newOrdersQueueBinding(Queue newOrdersQueue, DirectExchange exchange) {
        return BindingBuilder
                .bind(newOrdersQueue)
                .to(exchange)
                .with(properties.newOrdersQueue());
    }

    @Bean
    Queue deliveredOrdersQueue() {
        return QueueBuilder.durable(properties.deliveredOrdersQueue()).build();
    }

    @Bean
    Binding deliveredOrdersQueueBinding(Queue deliveredOrdersQueue, DirectExchange exchange) {
        return BindingBuilder
                .bind(deliveredOrdersQueue)
                .to(exchange)
                .with(properties.deliveredOrdersQueue());
    }

    @Bean
    Queue cancelledOrdersQueue() {
        return QueueBuilder.durable(properties.cancelledOrdersQueue()).build();
    }

    @Bean
    Binding cancelledOrdersQueueBinding(Queue cancelledOrdersQueue, DirectExchange exchange) {
        return BindingBuilder
                .bind(cancelledOrdersQueue)
                .to(exchange)
                .with(properties.cancelledOrdersQueue());
    }

    @Bean
    Queue errorOrdersQueue() {
        return QueueBuilder.durable(properties.errorOrdersQueue()).build();
    }

    @Bean
    Binding errorOrdersQueueBinding(Queue errorOrdersQueue, DirectExchange exchange) {
        return BindingBuilder
                .bind(errorOrdersQueue)
                .to(exchange)
                .with(properties.errorOrdersQueue());
    }

    @Bean
    public JacksonJsonMessageConverter jacksonConverter(JsonMapper jsonMapper) {
        return new JacksonJsonMessageConverter(jsonMapper);

    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, JacksonJsonMessageConverter jacksonConverter) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jacksonConverter);
        return rabbitTemplate;
    }
}
