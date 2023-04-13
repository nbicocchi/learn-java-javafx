package com.nbicocchi.javafx.address;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class PersonEditDialogController {
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField streetField;
    @FXML private TextField cityField;
    @FXML private TextField postalCodeField;
    @FXML private DatePicker birthdayDatePicker;
    Person person;

    @FXML
    public void initialize() {
        firstNameField.textProperty().addListener((observable, oldValue, newValue) -> person.firstNameProperty().set(newValue));
        lastNameField.textProperty().addListener((observable, oldValue, newValue) -> person.lastNameProperty().set(newValue));
        streetField.textProperty().addListener((observable, oldValue, newValue) -> person.streetProperty().set(newValue));
        cityField.textProperty().addListener((observable, oldValue, newValue) -> person.cityProperty().set(newValue));
        postalCodeField.textProperty().addListener((observable, oldValue, newValue) -> person.postalCodeProperty().set(Integer.parseInt(newValue)));
        birthdayDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> person.birthdayProperty().set(newValue));
    }

    void update() {
        firstNameField.textProperty().set(person.getFirstName());
        lastNameField.textProperty().set(person.getLastName());
        streetField.textProperty().set(person.getStreet());
        cityField.textProperty().set(person.getCity());
        postalCodeField.textProperty().set(String.valueOf(person.getPostalCode()));
        birthdayDatePicker.valueProperty().set(person.getBirthday());
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
        update();
    }
}