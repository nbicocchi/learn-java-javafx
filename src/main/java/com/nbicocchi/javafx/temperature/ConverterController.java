package com.nbicocchi.javafx.temperature;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class ConverterController {
    @FXML
    private Button btConvert;
    @FXML
    private ChoiceBox<String> chSelector;
    @FXML
    private TextField tfC;
    @FXML
    private TextField tfF;

    @FXML
    public void initialize() {
        chSelector.getItems().removeAll(chSelector.getItems());
        chSelector.getItems().addAll("C->F", "F->C");
        chSelector.getSelectionModel().select("C->F");
    }

    @FXML
    void onConvertClick(ActionEvent event) {
        if (chSelector.getSelectionModel().getSelectedItem().equals("C->F")) {
            double f = (Double.valueOf(tfC.getText()) * 1.8) + 32;
            tfF.setText(String.valueOf(f));
        } else {
            double c = (Double.valueOf(tfF.getText()) - 32) / 1.8;
            tfC.setText(String.valueOf(c));
        }
    }
}
