package spring.integration.example.app.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.router.HeaderValueRouter;
import spring.integration.example.integration.IIntegrationFlowFactory;
import spring.integration.example.integration.SharedFailedFlow;
import spring.integration.example.model.UpdateEmailEvent;
import spring.integration.example.model.UpdateSecretEvent;

@Configuration
@ComponentScan({
        "spring.integration.example.integration",
})
public class ConsumerApplicationConfig {
    private IIntegrationFlowFactory integrationFlowFactory;

    @Autowired
    public ConsumerApplicationConfig(IIntegrationFlowFactory integrationFlowFactory) {
        this.integrationFlowFactory = integrationFlowFactory;
    }

    private static final String ACCOUNT_UPDATE_MESSAGE_CHANNEL = "ACCOUNT_UPDATE_MESSAGE_CHANNEL";
    private static final String ACCOUNT_UPDATE_SECRET_MESSAGE_CHANNEL = "ACCOUNT_UPDATE_SECRET_MESSAGE_CHANNEL";
    private static final String ACCOUNT_UPDATE_EMAIL_MESSAGE_CHANNEL = "ACCOUNT_UPDATE_EMAIL_MESSAGE_CHANNEL";

    /**
     * Consume message from the ACCOUNT exchange and routing key account.update.*
     * Wildcard (*) is used to subscribe to all account updates.
     * Each consuming application would define routing key based on what data they want.
     */
    @Bean
    public IntegrationFlow consumeFromQueueBoundToExchangeFlow() {
        return integrationFlowFactory.buildConsumingIntegrationFlow(
                "ACCOUNT",
                "account.update.*",
                ACCOUNT_UPDATE_MESSAGE_CHANNEL,
                SharedFailedFlow.CHANNEL_NAME, 100, 5);
    }

    /**
     * Route specific account.update.X messages to their handler.
     * Each consuming application would define their routing and handlers.
     */
    @Bean
    public IntegrationFlow printConsumedEventsFlowDefinition() {
        return IntegrationFlows.from(ACCOUNT_UPDATE_MESSAGE_CHANNEL)
                .route(headerRouter())
                .get();
    }

    @Bean
    public HeaderValueRouter headerRouter() {
        HeaderValueRouter router = new HeaderValueRouter("amqp_receivedRoutingKey");
        router.setChannelMapping("account.update.secret", ACCOUNT_UPDATE_SECRET_MESSAGE_CHANNEL);
        router.setChannelMapping("account.update.email", ACCOUNT_UPDATE_EMAIL_MESSAGE_CHANNEL);
        router.setResolutionRequired(false);
        router.setDefaultOutputChannelName(SharedFailedFlow.CHANNEL_NAME);
        return router;
    }

    @Bean
    public IntegrationFlow handleAccountSecretUpdateFlow() {
        return IntegrationFlows.from(ACCOUNT_UPDATE_SECRET_MESSAGE_CHANNEL)
                .filter(p -> p instanceof UpdateSecretEvent)
                .handle(p -> {
                    UpdateSecretEvent payload = (UpdateSecretEvent) p.getPayload();
                    System.out.println("Account: " + payload.getAccountId() + " has secret: " + payload.getNewSecret());
                })
                .get();
    }

    @Bean
    public IntegrationFlow handleAccountEmailUpdateFlow() {
        return IntegrationFlows.from(ACCOUNT_UPDATE_EMAIL_MESSAGE_CHANNEL)
                .filter(p -> p instanceof UpdateEmailEvent)
                .handle(p -> {
                    UpdateEmailEvent payload = (UpdateEmailEvent) p.getPayload();
                    System.out.println("Account: " + payload.getAccountId() + " has email: " + payload.getEmail());
                })
                .get();
    }
}
