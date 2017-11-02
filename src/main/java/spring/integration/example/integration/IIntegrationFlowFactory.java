package spring.integration.example.integration;

import org.springframework.integration.dsl.IntegrationFlow;

public interface IIntegrationFlowFactory {
    IntegrationFlow buildConsumingIntegrationFlow(String fromExchange, String exchangeRoutingKey, String toMessageChannel, String errorMessageChannel, int concurrentConsumers, int prefetchCount);

    IntegrationFlow buildPublishingIntegrationFlow(String toExchange, String exchangeRoutingKey, String fromMessageChannel);
}
