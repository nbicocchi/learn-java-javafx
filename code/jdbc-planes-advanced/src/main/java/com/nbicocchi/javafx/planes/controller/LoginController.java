package com.nbicocchi.javafx.planes.controller;

import com.nbicocchi.javafx.planes.persistence.dao.UserRepository;
import com.nbicocchi.javafx.planes.persistence.model.User;
import com.zaxxer.hikari.HikariDataSource;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginController {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private TextField tfUsername;
    private HikariDataSource hikariDataSource;
    private UserRepository userRepository;

    public void initDataSource(HikariDataSource hikariDataSource) {
        this.hikariDataSource = hikariDataSource;
        this.userRepository = new UserRepository(hikariDataSource);

        // insert some example users
        userRepository.deleteAll();
        userRepository.save(new User("admin", String.valueOf("admin".hashCode())));
        userRepository.save(new User("user", String.valueOf("user".hashCode())));
    }

    @FXML
    void onCancelClicked() {
        Platform.exit();
    }

    @FXML
    void onOKClicked() throws IOException {
        Iterable<User> users = userRepository.findAll();
        for (User user : users) {
            if (Objects.equals(user.getUsername(), tfUsername.getText()) &&
                    Objects.equals(user.getPassword(), String.valueOf(tfPassword.getText().hashCode()))) {
                launchApplication();
                return;
            }
        }

        Alert a = new Alert(Alert.AlertType.WARNING, "Login Failed");
        a.setHeaderText("Login failed");
        a.setContentText("Username and/or password not valid");
        a.showAndWait();
    }

    void launchApplication() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("planes-view.fxml"));
        Parent root = loader.load();
        OverviewController controller = loader.getController();
        controller.initDataSource(hikariDataSource);

        Stage stage = (Stage) anchorPane.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("YA Plane Manager");
        stage.setScene(scene);
    }
}
