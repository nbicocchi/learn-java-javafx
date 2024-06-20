package com.bertons.expensetracker.controller;

import com.bertons.expensetracker.persistence.dao.UserRepository;
import com.bertons.expensetracker.persistence.model.User;
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

    public static final int MIN_WIDTH = 670;
    public static final int MIN_HEIGHT = 180;
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

        //userRepository.deleteAll();
        //userRepository.save(new User("user", String.valueOf("user".hashCode())));
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

        FXMLLoader loader = new FXMLLoader(getClass().getResource("main_page-view.fxml"));
        Parent root = loader.load();
        MainPageViewController controller = loader.getController();
        controller.initDataSource(hikariDataSource);

        Stage stage = (Stage) anchorPane.getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Your Expense Tracker");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        stage.setOnCloseRequest(e -> {
            try {
                controller.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
}
