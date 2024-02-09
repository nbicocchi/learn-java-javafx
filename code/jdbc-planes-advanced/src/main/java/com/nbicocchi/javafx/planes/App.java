package com.nbicocchi.javafx.planes;

import com.nbicocchi.javafx.planes.controller.OverviewController;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class App extends Application {
    private static final String JDBC_Driver = "org.postgresql.Driver";
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/jdbc_schema?user=user&password=secret&ssl=false";
    public static final List<String> planeTypes = List.of("Airliner", "Bomber", "Ekranoplan", "Flying boat", "Outsize cargo", "Transport");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        HikariDataSource hikariDataSource = initDataSource(JDBC_Driver, JDBC_URL);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("planes-view.fxml"));
        Parent root = loader.load();
        OverviewController controller = loader.getController();
        controller.initDataSource(hikariDataSource);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Planes");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private HikariDataSource initDataSource(String JDBC_Driver, String JDBC_URL) {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(JDBC_Driver);
        config.setJdbcUrl(JDBC_URL);
        config.setLeakDetectionThreshold(2000);
        return new HikariDataSource(config);
    }
}
