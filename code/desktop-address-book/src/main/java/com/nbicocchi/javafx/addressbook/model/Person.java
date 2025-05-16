package com.nbicocchi.javafx.addressbook.persistence.model;

import java.time.LocalDate;

public class Person {
    private String firstName;
    private String lastName;
    private String street;
    private Integer postalCode;
    private String city;
    private LocalDate birthday;

    public Person() {

    }

    public Person(String firstName, String lastName, String street, Integer postalCode, String city, LocalDate birthDay) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.postalCode = postalCode;
        this.city = city;
        this.birthday = birthDay;
    }

    public Person(Person other) {
        this.firstName = other.getFirstName();
        this.lastName = other.getLastName();
        this.street = other.getStreet();
        this.postalCode = other.getPostalCode();
        this.city = other.getCity();
        this.birthday = other.getBirthday();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }
}