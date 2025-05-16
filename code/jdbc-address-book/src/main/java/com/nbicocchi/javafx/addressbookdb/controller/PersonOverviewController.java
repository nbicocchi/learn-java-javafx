package com.nbicocchi.javafx.addressbookdb.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nbicocchi.javafx.addressbookdb.persistence.model.Person;
import com.nbicocchi.javafx.addressbookdb.persistence.repository.PersonRepository;
import com.zaxxer.hikari.HikariDataSource;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;

import java.io.IOException;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PersonOverviewController {
    HikariDataSource hikariDataSource;
    PersonRepository personRepository;

    @FXML
    private TableView<Person> personTable;
    @FXML
    private TableColumn<Person, String> firstNameColumn;
    @FXML
    private TableColumn<Person, String> lastNameColumn;

    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label postalCodeLabel;
    @FXML
    private Label birthdayLabel;

    public void initDataSource(HikariDataSource hikariDataSource) {
        this.hikariDataSource = hikariDataSource;
        this.personRepository = new PersonRepository(hikariDataSource);

        ObservableList<Person> persons = FXCollections.observableArrayList();
        Iterable<Person> savedPersons = personRepository.findAll();
        persons.addAll(StreamSupport.stream(savedPersons.spliterator(), false).collect(Collectors.toList()));
        personTable.setItems(persons);
    }

    /**
     * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
     */
    @FXML
    public void initialize() throws JsonProcessingException {
        // Initialize the person table with the two columns.
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        // Clear person details.
        showPersonDetails(null);

        // Listen for selection changes and show the person details when changed.
        personTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showPersonDetails(newValue));
    }

    /**
     * Fills all text fields to show details about the person.
     * If the specified person is null, all text fields are cleared.
     *
     * @param person the person or null
     */
    private void showPersonDetails(Person person) {
        if (person != null) {
            firstNameLabel.setText(person.getFirstName());
            lastNameLabel.setText(person.getLastName());
            streetLabel.setText(person.getStreet());
            postalCodeLabel.setText(String.valueOf(person.getPostalCode()));
            cityLabel.setText(person.getCity());
            birthdayLabel.setText(person.getBirthday().toString());
        } else {
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            streetLabel.setText("");
            postalCodeLabel.setText("");
            cityLabel.setText("");
            birthdayLabel.setText("");
        }
    }

    /**
     * Returns the index of the selected person in the TableView component
     *
     * @return the index of the selected person
     */
    int selectedIndex() {
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex < 0) {
            throw new NoSuchElementException();
        }
        return selectedIndex;
    }

    /**
     * Shows a simple warning dialog
     */
    void showNoPersonSelectedAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No Selection");
        alert.setHeaderText("No Person Selected");
        alert.setContentText("Please select a person in the table.");
        alert.showAndWait();
    }

    @FXML
    private void handleDeletePerson() {
        try {
            int selectedIndex = selectedIndex();
            personRepository.delete(personTable.getSelectionModel().getSelectedItem());
            personTable.getItems().remove(selectedIndex);
        } catch (NoSuchElementException e) {
            showNoPersonSelectedAlert();
        }
    }

    @FXML
    public void handleNewPerson() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("person-edit-view.fxml"));
            DialogPane view = loader.load();
            PersonEditDialogController controller = loader.getController();

            // Set an empty person into the controller
            controller.setPerson(new Person("First Name", "Last Name", "Street", 0, "City", LocalDate.now()));

            // Create the dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("New Person");
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setDialogPane(view);

            // Show the dialog and wait until the user closes it
            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if (clickedButton.orElse(ButtonType.CANCEL) == ButtonType.OK) {
                personRepository.save(controller.getPerson());
                personTable.getItems().add(controller.getPerson());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleEditPerson() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("person-edit-view.fxml"));
            DialogPane view = loader.load();
            PersonEditDialogController controller = loader.getController();

            // Set the person into the controller.
            int selectedIndex = selectedIndex();
            controller.setPerson(new Person(personTable.getItems().get(selectedIndex)));

            // Create the dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Edit Person");
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setDialogPane(view);

            // Show the dialog and wait until the user closes it
            Optional<ButtonType> clickedButton = dialog.showAndWait();
            if (clickedButton.orElse(ButtonType.CANCEL) == ButtonType.OK) {
                personRepository.save(controller.getPerson());
                personTable.getItems().set(selectedIndex, controller.getPerson());
            }
        } catch (NoSuchElementException e) {
            showNoPersonSelectedAlert();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBirthdayStatistics() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("birthday-statistics-view.fxml"));
            DialogPane view = loader.load();
            BirthdayStatisticsController controller = loader.getController();

            // Set the person into the controller.
            controller.setPersonData(personTable.getItems());

            // Create the dialog
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Birthday Statistics");
            dialog.initModality(Modality.WINDOW_MODAL);
            dialog.setDialogPane(view);

            // Show the dialog and wait until the user closes it
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Address Application");
        alert.setHeaderText("About");
        alert.setContentText("Author: nbicocchi@unimore.it");
        alert.showAndWait();
    }

    @FXML
    private void handleExit() {
        System.exit(0);
    }
}
