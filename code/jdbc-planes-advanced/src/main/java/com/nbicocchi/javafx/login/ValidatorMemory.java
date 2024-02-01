package com.nbicocchi.javafx.login;

import java.util.HashMap;
import java.util.Map;

public class ValidatorMemory implements Validator {
    Map<String, Integer> db;

    @Override
    public void connect() {
        db = new HashMap<>();
        db.put("admin", "admin".hashCode());
        db.put("user", "user".hashCode());
    }

    @Override
    public boolean checkCredentials(String username, String password) {
        if (!db.containsKey(username))
            return false;
        return db.get(username) == password.hashCode();
    }
}
