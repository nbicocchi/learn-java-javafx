package com.nbicocchi.javafx.login;

public interface Validator {
    void connect();
    boolean checkCredentials(String username, String password);
}
