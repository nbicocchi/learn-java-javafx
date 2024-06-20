package com.bertons.expensetracker;

import com.bertons.expensetracker.controller.LoginController;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class App extends Application {
    private static final String JDBC_Driver = "org.postgresql.Driver";
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/jdbc_schema?user=user&password=secret&ssl=false";

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        HikariDataSource hikariDataSource = initDataSource();

        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("controller/login-view.fxml"));
        Parent root = fxmlLoader.load();
        LoginController controller = fxmlLoader.getController();
        controller.initDataSource(hikariDataSource);

        Scene scene = new Scene(root);

        primaryStage.setTitle("Hello! Login in the credential manager");
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/bertons/expensetracker/images/icons/App-icon.png"))));
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private HikariDataSource initDataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(App.JDBC_Driver);
        config.setJdbcUrl(App.JDBC_URL);
        config.setLeakDetectionThreshold(2000);
        return new HikariDataSource(config);
    }
}