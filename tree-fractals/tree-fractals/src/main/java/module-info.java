module com.example.treefractals {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.javafx.treefractals to javafx.fxml;
    exports com.javafx.treefractals;
}