package spring.integration.example.model;

public class UpdateEmailEvent {
    public String accountId;
    public String email;

    public UpdateEmailEvent() {
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}