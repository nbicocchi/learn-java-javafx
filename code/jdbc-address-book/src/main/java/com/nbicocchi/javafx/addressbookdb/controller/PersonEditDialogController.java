package com.nbicocchi.javafx.addressbookdb.controller;

import com.nbicocchi.javafx.addressbookdb.persistence.model.Person;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class PersonEditDialogController {
    Person person;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField streetField;
    @FXML
    private TextField cityField;
    @FXML
    private TextField postalCodeField;
    @FXML
    private DatePicker birthdayDatePicker;

    @FXML
    public void initialize() {
        firstNameField.textProperty().addListener((observable, oldValue, newValue) -> person.setFirstName(newValue));
        lastNameField.textProperty().addListener((observable, oldValue, newValue) -> person.setLastName(newValue));
        streetField.textProperty().addListener((observable, oldValue, newValue) -> person.setStreet(newValue));
        cityField.textProperty().addListener((observable, oldValue, newValue) -> person.setCity(newValue));
        postalCodeField.textProperty().addListener((observable, oldValue, newValue) -> person.setPostalCode(Integer.parseInt(newValue)));
        birthdayDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> person.setBirthday(newValue));
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