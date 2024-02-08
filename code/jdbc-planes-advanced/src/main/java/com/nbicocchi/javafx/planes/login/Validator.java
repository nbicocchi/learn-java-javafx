package com.nbicocchi.javafx.planes.login;

public interface Validator {
    void connect();
    boolean checkCredentials(String username, String password);
}
