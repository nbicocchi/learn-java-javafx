package com.nbicocchi.javafx.planes.controller;

import com.nbicocchi.javafx.planes.App;
import com.nbicocchi.javafx.planes.persistence.model.Part;
import com.nbicocchi.javafx.planes.persistence.model.Plane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.Modality;

import java.io.IOException;
import java.util.UUID;

public class AddPartDialog extends Dialog<Part> {
    @FXML private TextField tfPartID;
    @FXML private TextField tfDescription;
    @FXML private TextField tfDuration;

    public void initialize() {
        tfPartID.setText(UUID.randomUUID().toString());
    }

    public AddPartDialog() throws IOException {
        super();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("add-part-view.fxml"));
        loader.setController(this);
        setDialogPane(loader.load());
        setTitle("Add Part");
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        setResultConverter(buttonType -> {
            if (buttonType == ButtonType.APPLY) {
                return new Part(null,
                        tfPartID.getText(),
                        tfDescription.getText(),
                        Double.parseDouble(tfDuration.getText()));
            }
            return null;
        });
    }
}
