package spring.integration.example.app.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.IntegrationFlow;
import spring.integration.example.integration.IIntegrationFlowFactory;
import spring.integration.example.integration.SharedFailedFlow;
import spring.integration.example.model.UpdateEmailEvent;
import spring.integration.example.model.UpdateSecretEvent;

@Configuration
@EnableIntegration
@ComponentScan({
        "spring.integration.example.integration",
})
@IntegrationComponentScan
public class PublisherApplicationConfig {
    private IIntegrationFlowFactory integrationFlowFactory;

    @Autowired
    public PublisherApplicationConfig(IIntegrationFlowFactory integrationFlowFactory) {
        this.integrationFlowFactory = integrationFlowFactory;
    }

    private static final String UPDATE_SECRET_MESSAGE_CHANNEL = "UPDATE_SECRET_MESSAGE_CHANNEL";
    private static final String UPDATE_EMAIL_MESSAGE_CHANNEL = "UPDATE_EMAIL_MESSAGE_CHANNEL";

    /**
     * Setup the flow that will take messages from the UPDATE_SECRET_MESSAGE_CHANNEL
     * messages will be published to the defined exchange using the defined routing key
     */
    @Bean
    public IntegrationFlow updateSecretEventFlow() {
        return integrationFlowFactory.buildPublishingIntegrationFlow("ACCOUNT", "account.update.secret", UPDATE_SECRET_MESSAGE_CHANNEL);
    }

    /**
     * Setup the flow that will take messages from the UPDATE_EMAIL_MESSAGE_CHANNEL
     * messages will be published to the defined exchange using the defined routing key
     */
    @Bean
    public IntegrationFlow updateEmailEventFlow() {
        return integrationFlowFactory.buildPublishingIntegrationFlow("ACCOUNT", "account.update.email", UPDATE_EMAIL_MESSAGE_CHANNEL);
    }

    @MessagingGateway(errorChannel = SharedFailedFlow.CHANNEL_NAME)
    public interface IAccountEventPublisher {
        /**
         * Adds messages to the UPDATE_SECRET_MESSAGE_CHANNEL
         */
        @Gateway(requestChannel = UPDATE_SECRET_MESSAGE_CHANNEL)
        void updateSecret(UpdateSecretEvent updateSecretEvent);

        /**
         * Adds messages to the UPDATE_EMAIL_MESSAGE_CHANNEL
         */
        @Gateway(requestChannel = UPDATE_EMAIL_MESSAGE_CHANNEL)
        void updateEmail(UpdateEmailEvent updateEmailEvent);
    }
}
