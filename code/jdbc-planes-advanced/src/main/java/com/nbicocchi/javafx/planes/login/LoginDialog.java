package com.nbicocchi.javafx.planes.login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Window;
import javafx.util.Pair;

import java.io.IOException;

public class LoginDialog extends Dialog<Pair<String, String>> {
    @FXML
    private TextField tfPassword;
    @FXML
    private TextField tfUsername;

    public LoginDialog(Window owner) throws IOException {
        super();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("login-view.fxml"));
        loader.setController(this);
        setDialogPane(loader.load());
        setTitle("Login");
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);
        setResultConverter(buttonType -> {
            if (buttonType == ButtonType.APPLY) {
                return new Pair<>(tfUsername.getText(), tfPassword.getText());
            }
            return null;
        });
    }
}
