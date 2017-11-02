package spring.integration.example.app.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import spring.integration.example.model.UpdateEmailEvent;
import spring.integration.example.model.UpdateSecretEvent;

@SpringBootApplication
@EnableAutoConfiguration
@Import(PublisherApplicationConfig.class)
@ComponentScan("spring.integration.example.app.publisher.controller")
public class PublisherApplication implements CommandLineRunner {
    @Autowired
    private PublisherApplicationConfig.IAccountEventPublisher accountEventPublisher;

    public static void main(String[] args) {
        new SpringApplicationBuilder(PublisherApplication.class)
                .web(true)
                .run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i = 0; i < 1000000; i++) {
            String accountId = String.valueOf(i);
            String email = String.format("test+%s@test.test", accountId);

            UpdateEmailEvent updateEmailEvent = new UpdateEmailEvent();
            updateEmailEvent.setEmail(email);
            updateEmailEvent.setAccountId(accountId);
            accountEventPublisher.updateEmail(updateEmailEvent);

            UpdateSecretEvent updateSecretEvent = new UpdateSecretEvent();
            updateSecretEvent.setNewSecret(accountId);
            updateSecretEvent.setAccountId(accountId);
            accountEventPublisher.updateSecret(updateSecretEvent);
        }
    }
}