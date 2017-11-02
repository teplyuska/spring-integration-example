package spring.integration.example.app.publisher.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spring.integration.example.app.publisher.PublisherApplicationConfig;
import spring.integration.example.model.UpdateEmailEvent;
import spring.integration.example.model.UpdateSecretEvent;

@RestController
public class PublisherController {
    private PublisherApplicationConfig.IAccountEventPublisher eventPublisher;

    @Autowired
    public PublisherController(PublisherApplicationConfig.IAccountEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @RequestMapping("account/secret")
    public void updateSecret(@RequestParam String accountId, @RequestParam String secret) {
        UpdateSecretEvent updateSecretEvent = new UpdateSecretEvent();
        updateSecretEvent.setAccountId(accountId);
        updateSecretEvent.setNewSecret(secret);
        eventPublisher.updateSecret(updateSecretEvent);
    }

    @RequestMapping("account/email")
    public void updateEmail(@RequestParam String accountId, @RequestParam String email) {
        UpdateEmailEvent updateEmailEvent = new UpdateEmailEvent();
        updateEmailEvent.setAccountId(accountId);
        updateEmailEvent.setEmail(email);
        eventPublisher.updateEmail(updateEmailEvent);
    }
}