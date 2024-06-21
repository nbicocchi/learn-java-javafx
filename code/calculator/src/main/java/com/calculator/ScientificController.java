package com.calculator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import net.objecthunter.exp4j.tokenizer.UnknownFunctionOrVariableException;

import java.io.IOException;
import java.util.Objects;

public class ScientificController extends Controller{
    @FXML
    private Button btnAbs;

    @FXML
    private Button btnExp;

    @FXML
    private Button btnFact;

    @FXML
    private Button btnBin;

    @FXML
    private Button btnLog;

    @FXML
    private Button btnMod;

    @FXML
    private Button btnPi;

    @FXML
    private Button btnPow;

    @FXML
    private Button btnSqrt;

    Calculations calc = new Calculations();

    /**
     * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
     */
    @Override
    public void initialize() {
        double answer = 0;
        lblAnswer.setText(String.format("%.02f", answer));
        buttonsFunctions();
    }

    /**
     * Switches the scientific calculator to the basic one
     */
    public void switchToNormal(ActionEvent event) throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("view.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Calculator");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * All the buttons setOnAction functions.
     */
    public void buttonsFunctions() {
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
        btnExp.setOnAction(event -> {
            txtShow.setText(txtShow.getText() != null ? txtShow.getText()+"e^(" : "e^(");
        });

        btnAbs.setOnAction(event -> {
            txtShow.setText(txtShow.getText() != null ? txtShow.getText()+"||" : "||");
        });

        btnFact.setOnAction(event -> {
            txtShow.setText(txtShow.getText() != null ? txtShow.getText()+"!" : "!");
        });

        btnBin.setOnAction(event -> {
            if(calc.controlToBin(txtShow.getText())){
                lblAnswer.setText(Integer.toBinaryString(Integer.parseInt(txtShow.getText())));
            }else{
                lblAnswer.setText(null);
                calc.handleError("Convert only numbers");
            }
        });

        btnLog.setOnAction(event -> {
            txtShow.setText(txtShow.getText() != null ? txtShow.getText()+"log(" : "log(");
        });

        btnPow.setOnAction(event -> {
            txtShow.setText(txtShow.getText() != null ? txtShow.getText()+"^" : "^");
        });

        btnMod.setOnAction(event -> {
            txtShow.setText(txtShow.getText() != null ? txtShow.getText()+"%" : "%");
        });

        btnSqrt.setOnAction(event -> {
            txtShow.setText(txtShow.getText() != null ? txtShow.getText()+"sqrt(" : "sqrt(");
        });

        btnPi.setOnAction(event -> {
            txtShow.setText(txtShow.getText() != null ? txtShow.getText()+"π" : "π");
        });
    }
}
