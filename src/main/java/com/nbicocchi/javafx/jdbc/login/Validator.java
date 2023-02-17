package com.nbicocchi.javafx.jdbc.login;

public interface Validator {
    void connect();
    boolean checkCredentials(String username, String password);
}
