module org.example.camera {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires webcam.capture;
    requires javafx.swing;
    requires javafx.base;


    opens com.nbicocchi.javafx.camera to javafx.fxml;
    exports com.nbicocchi.javafx.camera;
    exports com.nbicocchi.javafx.camera.common;
    opens com.nbicocchi.javafx.camera.common to javafx.fxml;
    exports com.nbicocchi.javafx.camera.effects;
    opens com.nbicocchi.javafx.camera.effects to javafx.fxml;
    exports com.nbicocchi.javafx.camera.controller;
    opens com.nbicocchi.javafx.camera.controller to javafx.fxml;
}