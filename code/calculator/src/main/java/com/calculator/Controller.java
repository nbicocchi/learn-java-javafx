package com.calculator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import net.objecthunter.exp4j.tokenizer.UnknownFunctionOrVariableException;

import java.io.IOException;
import java.util.Objects;

public class Controller {
    @FXML
    public Button btn0;

    @FXML
    public Button btn1;

    @FXML
    public Button btn2;

    @FXML
    public Button btn3;

    @FXML
    public Button btn4;

    @FXML
    public Button btn5;

    @FXML
    public Button btn6;

    @FXML
    public Button btn7;

    @FXML
    public Button btn8;

    @FXML
    public Button btn9;

    @FXML
    public Button btnC;

    @FXML
    public Button btnCA;

    @FXML
    public Button btnDiv;

    @FXML
    public Button btnEq;

    @FXML
    public Button btnMulti;

    @FXML
    public Button btnPoint;

    @FXML
    public Button btnSub;

    @FXML
    public Button btnSum;

    @FXML
    public Label lblAnswer;

    @FXML
    public TextField txtShow;

    Calculations calc = new Calculations();

    public Stage stage;
    public Scene scene;
    public Parent root;

    /**
     * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
     */
    public void initialize() {
        double answer = 0;
        lblAnswer.setText(String.format("%.02f", answer));
        buttonsFunctions();
    }

    /**
     * Switches the basic calculator to the scientific one
     */
    public void switchToScientific(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("scientific-view.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Scientific Calculator");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * All the buttons setOnAction functions.
     */
    private void buttonsFunctions(){
        btnC.setOnAction(event -> {
            int lngt = txtShow.getText().length();
            if(lngt != 0){
                txtShow.setText(txtShow.getText().substring(0, lngt-1));
            }
        });

        btnCA.setOnAction(event -> {
            txtShow.clear();
        });

        btnPoint.setOnAction(event -> {
            txtShow.setText(txtShow.getText() != null ? txtShow.getText()+"." : "0.");
        });

        btnEq.setOnAction(event -> {
            try {
                lblAnswer.setText(calc.evaluate(txtShow.getText()));
            } catch(UnknownFunctionOrVariableException u){
                lblAnswer.setText(null);
                calc.handleError(u.toString().substring(69));
            } catch(IllegalArgumentException i){
                lblAnswer.setText(null);
                calc.handleError(i.toString().substring(35));
            } catch(ArithmeticException e) {
                lblAnswer.setText(null);
                calc.handleError(e.toString().substring(31));
            }
        });

        btnSum.setOnAction(event -> {
            txtShow.setText(txtShow.getText() != null ? txtShow.getText()+"+" : "+");
        });

        btnSub.setOnAction(event -> {
            txtShow.setText(txtShow.getText() != null ? txtShow.getText()+"-" : "-");
        });

        btnMulti.setOnAction(event -> {
            txtShow.setText(txtShow.getText() != null ? txtShow.getText()+"*" : "*");
        });

        btnDiv.setOnAction(event -> {
            txtShow.setText(txtShow.getText() != null ? txtShow.getText()+"/" : "/");
        });

        btn0.setOnAction(event -> {
            txtShow.setText(txtShow.getText() != null ? txtShow.getText()+"0" : "0");
        });

        btn1.setOnAction(event -> {
            txtShow.setText(txtShow.getText() != null ? txtShow.getText()+"1" : "1");
        });

        btn2.setOnAction(event -> {
            txtShow.setText(txtShow.getText() != null ? txtShow.getText()+"2" : "2");
        });

        btn3.setOnAction(event -> {
            txtShow.setText(txtShow.getText() != null ? txtShow.getText()+"3" : "3");
        });

        btn4.setOnAction(event -> {
            txtShow.setText(txtShow.getText() != null ? txtShow.getText()+"4" : "4");
        });

        btn5.setOnAction(event -> {
            txtShow.setText(txtShow.getText() != null ? txtShow.getText()+"5" : "5");
        });

        btn6.setOnAction(event -> {
            txtShow.setText(txtShow.getText() != null ? txtShow.getText()+"6" : "6");
        });

        btn7.setOnAction(event -> {
            txtShow.setText(txtShow.getText() != null ? txtShow.getText()+"7" : "7");
        });

        btn8.setOnAction(event -> {
            txtShow.setText(txtShow.getText() != null ? txtShow.getText()+"8" : "8");
        });

        btn9.setOnAction(event -> {
            txtShow.setText(txtShow.getText() != null ? txtShow.getText()+"9" : "9");
        });
    }
}
