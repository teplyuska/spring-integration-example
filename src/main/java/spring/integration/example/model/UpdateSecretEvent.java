package spring.integration.example.model;

public class UpdateSecretEvent {
    public String accountId;
    public String newSecret;

    public UpdateSecretEvent() {
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getNewSecret() {
        return newSecret;
    }

    public void setNewSecret(String newSecret) {
        this.newSecret = newSecret;
    }
}