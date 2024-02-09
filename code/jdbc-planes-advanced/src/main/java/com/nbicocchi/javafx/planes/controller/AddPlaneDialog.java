package com.nbicocchi.javafx.planes.controller;

import com.nbicocchi.javafx.planes.App;
import com.nbicocchi.javafx.planes.persistence.model.Plane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Modality;

import java.io.IOException;

public class AddPlaneDialog extends Dialog<Plane> {
    @FXML private ComboBox<String> cbCategory;
    @FXML private DatePicker dpFirstFlight;
    @FXML private TextField tfLength;
    @FXML private TextField tfName;
    @FXML private TextField tfWingSpan;

    public void initialize() {
        cbCategory.getItems().removeAll();
        cbCategory.getItems().addAll(App.planeTypes);
        cbCategory.getSelectionModel().select("Airliner");
    }

    public AddPlaneDialog() throws IOException {
        super();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("add-plane-view.fxml"));
        loader.setController(this);
        setDialogPane(loader.load());
        setTitle("Add Plane");
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        setResultConverter(buttonType -> {
            if (buttonType == ButtonType.APPLY) {
                return new Plane(tfName.getText(),
                        Double.parseDouble(tfLength.getText()),
                        Double.parseDouble(tfWingSpan.getText()),
                        dpFirstFlight.getValue(),
                        cbCategory.getValue());
            }
            return null;
        });
    }
}
