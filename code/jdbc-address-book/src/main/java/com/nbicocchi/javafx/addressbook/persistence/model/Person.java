package com.nbicocchi.javafx.addressbook.persistence.model;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class Person {
    @EqualsAndHashCode.Include
    Long Id;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;
    @NonNull
    private String street;
    @NonNull
    private Integer postalCode;
    @NonNull
    private String city;
    @NonNull
    private LocalDate birthday;

    public Person(Person other) {
        this.Id = other.Id;
        this.firstName = other.getFirstName();
        this.lastName = other.getLastName();
        this.street = other.getStreet();
        this.postalCode = other.getPostalCode();
        this.city = other.getCity();
        this.birthday = other.getBirthday();
    }
}