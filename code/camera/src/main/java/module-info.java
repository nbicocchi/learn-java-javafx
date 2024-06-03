module org.example.camera {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires webcam.capture;
    requires javafx.swing;
    requires javafx.base;


    opens com.nbicocchi.javafx to javafx.fxml;
    exports com.nbicocchi.javafx;
    exports com.nbicocchi.javafx.common;
    opens com.nbicocchi.javafx.common to javafx.fxml;
    exports com.nbicocchi.javafx.effects;
    opens com.nbicocchi.javafx.effects to javafx.fxml;
    exports com.nbicocchi.javafx.controller;
    opens com.nbicocchi.javafx.controller to javafx.fxml;
}