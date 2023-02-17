package com.nbicocchi.javafx.validating;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.TextField;

import java.util.function.Predicate;

public class ValidatingTextField extends TextField {
    private final Predicate<String> validation;
    private final BooleanProperty isValid = new SimpleBooleanProperty();

    ValidatingTextField(Predicate<String> validation) {
        this.validation = validation;
        textProperty().addListener((o, oldValue, newText) -> {
            isValid.set(validation.test(newText));
        });
        isValid.set(validation.test(""));
    }

    public BooleanProperty isValidPropertyProperty() {
        return isValid;
    }
}
