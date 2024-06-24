module com.twentyone {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;

    opens com.twentyone to javafx.fxml;
    exports com.twentyone;
}
