package com.infomedia.abacox.control.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    /**
     * Topic exchange for fire-and-forget events.
     * Routing key: {tenant}.{sourceModule}.{type}
     * Use "#" wildcard to subscribe to all, or "{schema}.#" for tenant-scoped.
     */
    public static final String EVENTS_EXCHANGE = "abacox.events";

    /**
     * Direct exchange for RPC-style queries.
     * Routing key: {targetModule}  (e.g. "orchestrator", "users")
     */
    public static final String QUERIES_EXCHANGE = "abacox.queries";

    public static final String CONTROL_QUERIES_QUEUE = "abacox.control.queries";

    @Bean
    public Queue controlQueriesQueue() {
        return new Queue(CONTROL_QUERIES_QUEUE, true);
    }

    @Bean
    public Binding controlQueriesBinding(Queue controlQueriesQueue, DirectExchange queriesExchange) {
        return BindingBuilder.bind(controlQueriesQueue).to(queriesExchange).with("control");
    }

    @Bean
    public TopicExchange eventsExchange() {
        return new TopicExchange(EVENTS_EXCHANGE, true, false);
    }

    @Bean
    public DirectExchange queriesExchange() {
        return new DirectExchange(QUERIES_EXCHANGE, true, false);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter(ObjectMapper objectMapper) {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);
        DefaultClassMapper classMapper = new DefaultClassMapper();
        classMapper.setIdClassMapping(java.util.Map.of("InternalMessage", InternalMessage.class));
        converter.setClassMapper(classMapper);
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter messageConverter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }
}
