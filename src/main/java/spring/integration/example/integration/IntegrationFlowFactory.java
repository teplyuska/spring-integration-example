package spring.integration.example.integration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.amqp.Amqp;
import org.springframework.integration.dsl.support.Transformers;
import org.springframework.stereotype.Component;

/**
 * Adapter is unidirectional
 * Gateway is bidirectional
 */
@Component
public class IntegrationFlowFactory implements IIntegrationFlowFactory {
    @Autowired
    private CachingConnectionFactory connectionFactory;

    @Autowired
    private AmqpAdmin amqpAdmin;

    /**
     * Returns a one way (Amqp.inboundAdapter) inbound integration flow which will deliver messages to the given {@code toMessageChannel}
     * based on the given {@code fromExchange} and {@code exchangeRoutingKey}.
     */
    @Override
    public IntegrationFlow buildConsumingIntegrationFlow(String fromExchange, String exchangeRoutingKey, String toMessageChannel, String errorMessageChannel, int concurrentConsumers, int prefetchCount) {
        Queue queue = buildDurableQueue("auto-generated-queue-name-based-on-project");
        amqpAdmin.declareQueue(queue);

        Exchange exchange = buildDurableTopicExchange(fromExchange);
        amqpAdmin.declareExchange(exchange);

        Binding binding = buildBinding(queue, exchange, exchangeRoutingKey);
        amqpAdmin.declareBinding(binding);

        return IntegrationFlows.from(Amqp.inboundAdapter(getListenerContainer(queue, concurrentConsumers, prefetchCount))
                .errorChannel(errorMessageChannel))
                .transform(Transformers.fromJson())
                .channel(toMessageChannel)
                .get();
    }

    /**
     * Returns a one way (Amqp.outboundAdapter) outbound integration flow which will deliver messages to the given {@code toExchange} using the given {@code exchangeRoutingKey}
     */
    @Override
    public IntegrationFlow buildPublishingIntegrationFlow(String toExchange, String exchangeRoutingKey, String fromMessageChannel) {
        Exchange exchange = buildDurableTopicExchange(toExchange);
        amqpAdmin.declareExchange(exchange);

        return IntegrationFlows.from(fromMessageChannel)
                .transform(Transformers.toJson())
                .handle(Amqp.outboundAdapter(getAmqpTemplate())
                        .exchangeName(toExchange)
                        .routingKey(exchangeRoutingKey))
                .get();
    }

    private AmqpTemplate getAmqpTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }

    private SimpleMessageListenerContainer getListenerContainer(Queue queue, int concurrentConsumers, int prefetchCount) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(queue);
        container.setConcurrentConsumers(concurrentConsumers);
        container.setPrefetchCount(prefetchCount);
        return container;
    }

    private Queue buildDurableQueue(String name) {
        return QueueBuilder
                .durable(name)
                .build();
    }

    private Exchange buildDurableTopicExchange(String exchange) {
        return ExchangeBuilder
                .topicExchange(exchange)
                .durable(true)
                .build();
    }

    private Binding buildBinding(Queue queue, Exchange exchange, String exchangeRoutingKey) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(exchangeRoutingKey)
                .noargs();
    }
}
