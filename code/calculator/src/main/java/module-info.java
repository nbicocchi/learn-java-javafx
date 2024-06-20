module com.graphic_calculator {
    requires javafx.controls;
    requires javafx.fxml;
    requires exp4j;


    opens com.calculator to javafx.fxml;
    exports com.calculator;
}