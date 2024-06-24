package com.twentyone;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main application class, it starts the stages and scenes
 */
public class App extends Application {

    private static Scene scene;
    private static Stage stage;
    private static GameManager gameManager;
    

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("menu"));
        stage.setScene(scene);
        stage.show();
        App.stage = stage;
    }

    @Override
    public void stop(){
        gameManager.terminateGame();
    }

    /**
     * change the scene to a new one
     */
    static void setRoot(String fxml) throws IOException {
        scene = new Scene(loadFXML(fxml));
        stage.setScene(scene);
    }

     /**
     *  loads a fxml from his resource name
     */   
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();//starts the application
    }

    public static Scene getScene() {
        return scene;
    }

    public static void setScene(Scene scene) {
        App.scene = scene;
    }

    public static GameManager getGameManager() {
        return gameManager;
    }

    public static void setGameManager(GameManager gameManager) {
        App.gameManager = gameManager;
    }

    
}