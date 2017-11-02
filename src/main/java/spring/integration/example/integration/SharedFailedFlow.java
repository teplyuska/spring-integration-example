package spring.integration.example.integration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.MessagingException;

@Configuration
public class SharedFailedFlow {
    public static final String CHANNEL_NAME = "SHARED_FAILED_CHANNEL";

    @Bean
    public IntegrationFlow sharedFailedFlowDefinition() {
        return IntegrationFlows.from(CHANNEL_NAME)
                .handle(e -> {
                    Object payload = e.getPayload();

                    if (payload instanceof MessagingException) {
                        MessagingException messageHandlingException = (MessagingException) payload;

                        System.out.println("Message Handling Exception: " + messageHandlingException.getMessage());
                    } else {
                        System.out.println("Exception: " + payload.toString());
                    }
                })
                .get();
    }
}
