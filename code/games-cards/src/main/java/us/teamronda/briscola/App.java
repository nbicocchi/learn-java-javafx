package us.teamronda.briscola;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        // Load the main FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gui/fxmls/start.fxml"));
        // Set the program icon
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/program_icon.png"))));
        stage.setResizable(false);
        stage.setTitle("Briscola v1.0");
        stage.setScene(new Scene(fxmlLoader.load()));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}