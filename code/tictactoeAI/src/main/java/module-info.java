module com.marduini.javafx.tictactoeAI {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.marduini.javafx.tictactoeAI to javafx.fxml;
    exports com.marduini.javafx.tictactoeAI;
    opens com.marduini.javafx.tictactoeAI.controller to javafx.fxml;
    exports com.marduini.javafx.tictactoeAI.controller;
    exports com.marduini.javafx.tictactoeAI.model;
}