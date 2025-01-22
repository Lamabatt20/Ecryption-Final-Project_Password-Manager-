package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PasswordEntry {

    private final StringProperty accountType;
    private final StringProperty username;
    private final StringProperty password;

    public PasswordEntry(String accountType, String username, String password) {
        this.accountType = new SimpleStringProperty(accountType);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
    }

    public String getAccountType() {
        return accountType.get();
    }

    public void setAccountType(String accountType) {
        this.accountType.set(accountType);
    }

    public StringProperty accountTypeProperty() {
        return accountType;
    }

    public String getUsername() {
        return username.get();
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public StringProperty passwordProperty() {
        return password;
    }
}
