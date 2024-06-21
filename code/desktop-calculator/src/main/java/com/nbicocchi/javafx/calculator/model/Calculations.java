package com.nbicocchi.javafx.calculator.model;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import net.objecthunter.exp4j.tokenizer.UnknownFunctionOrVariableException;

import java.util.Optional;

public class Calculations {
    public boolean abs = false;
    public boolean fact = false;

    /**
     * Controls that the String given contains only numbers
     * @param string the calculator input to analize
     * @return "true" if it's ok, "false" if it's bad
     */
    public boolean controlToBin(String string){
        boolean go = false;
        String legit = "0123456789";
        for(int j = 0; j < string.length(); j++){
            go = false;
            for(int i = 0; i < legit.length(); i++){
                if(string.toCharArray()[j] == legit.toCharArray()[i]){
                    go = true;
                    break;
                }
            }
            if(!go)
                break;
        }
        return go;
    }

    /**
     * Calculates the factorial of a given number
     * @param number number for wich the factorial is calculated
     * @return the factorial
     */
    public static long factorial(int number) {
        int i,fact=1;
        for(i=1;i<=number;i++)
            fact=fact*i;
        return fact;
    }

    /**
     * Shows an alert with the error given
     * @param string the error to be shown
     */
    public void handleError(String string){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(string);
            Optional<ButtonType> result = alert.showAndWait();
    }

    /**
     * Does the math with the given string
     * @param string the calculator input
     * @return the output result after math
     */
    public String evaluate(String string) throws IllegalArgumentException {
        //---------ABS----------------
        if(string.startsWith("|")){
            string = string.substring(1, string.length()-1);
            abs = true;
        }else{
            abs = false;
        }
        //---------FACTORIAL---------------
        if(string.endsWith("!")){
            string = string.substring(0, string.length()-1);
            fact = true;
        }else{
            fact = false;
        }
        Expression expression = new ExpressionBuilder(string).build();
        try {
            double result = expression.evaluate();
            if(abs && (result < 0))
                result *= -1;
            if(fact)
                result = factorial((int)result);
            return String.format("%.02f", result);
        } catch(UnknownFunctionOrVariableException u){
            handleError(u.toString().substring(69));
            return null;
        } catch(IllegalArgumentException i){
            handleError(i.toString().substring(35));
            return null;
        } catch(ArithmeticException e) {
            handleError(e.toString().substring(31));
            return null;
        }
    }
}
