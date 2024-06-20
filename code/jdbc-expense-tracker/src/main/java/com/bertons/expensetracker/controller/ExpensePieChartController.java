package com.bertons.expensetracker.controller;

import com.bertons.expensetracker.persistence.model.Expense;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.io.Closeable;
import java.text.DecimalFormat;

public class ExpensePieChartController implements DataToCharts, Closeable {
    @FXML
    private Label percentageLabel2;
    @FXML
    private Label percentageLabel1;
    @FXML
    private PieChart payingMethodsPieChart;
    @FXML
    private PieChart expenseTypesPieChart;

    ObservableList<PieChart.Data> expenseTypesPieChartData = FXCollections.observableArrayList();
    ObservableList<PieChart.Data> payingMethodsPieChartData = FXCollections.observableArrayList();

    @Override
    public void initCharts(ObservableList<Expense> expensesData, MainPageViewController mainPageViewController) {
        initExpenseTypesPieChartData();
        insertDataIntoExpenseTypesPieChartData(expensesData);
        expenseTypesPieChart.getData().setAll(expenseTypesPieChartData);
        addMouseEventsToPieChart(expenseTypesPieChart, percentageLabel1);

        initPayingMethodsPieChartData();
        insertDataIntoPayingMethodsPieChartData(expensesData);
        payingMethodsPieChart.getData().setAll(payingMethodsPieChartData);
        addMouseEventsToPieChart(payingMethodsPieChart, percentageLabel2);
    }

    @Override
    public void updateCharts(ObservableList<Expense> expensesData) {
        updateExpenseTypesPieChartData(expensesData);
        updatePayingMethodPieChartData(expensesData);
    }

    private void updateExpenseTypesPieChartData(ObservableList<Expense> expensesData) {
        expenseTypesPieChartData.get(0).setPieValue(0);
        expenseTypesPieChartData.get(1).setPieValue(0);
        expenseTypesPieChartData.get(2).setPieValue(0);
        expenseTypesPieChartData.get(3).setPieValue(0);
        expenseTypesPieChartData.get(4).setPieValue(0);
        expenseTypesPieChartData.get(5).setPieValue(0);
        insertDataIntoExpenseTypesPieChartData(expensesData);
        percentageLabel1.setText("");
    }
    private void updatePayingMethodPieChartData(ObservableList<Expense> expensesData) {
        payingMethodsPieChartData.get(0).setPieValue(0);
        payingMethodsPieChartData.get(1).setPieValue(0);
        insertDataIntoPayingMethodsPieChartData(expensesData);
        percentageLabel2.setText("");
    }

    private void initExpenseTypesPieChartData() {
        expenseTypesPieChartData.clear();
        expenseTypesPieChartData.add(new PieChart.Data(Expense.ExpenseType.Miscellaneous.toString(), 0));
        expenseTypesPieChartData.add(new PieChart.Data(Expense.ExpenseType.Car.toString(), 0));
        expenseTypesPieChartData.add(new PieChart.Data(Expense.ExpenseType.Debt.toString(), 0));
        expenseTypesPieChartData.add(new PieChart.Data(Expense.ExpenseType.EatingOut.toString(), 0));
        expenseTypesPieChartData.add(new PieChart.Data(Expense.ExpenseType.Groceries.toString(), 0));
        expenseTypesPieChartData.add(new PieChart.Data(Expense.ExpenseType.Subscription.toString(), 0));
    }
    private void initPayingMethodsPieChartData() {
        payingMethodsPieChartData.clear();
        payingMethodsPieChartData.add(new PieChart.Data(Expense.PayingMethod.Cash.toString(), 0));
        payingMethodsPieChartData.add(new PieChart.Data(Expense.PayingMethod.Card.toString(), 0));
    }

    private void addMouseEventsToPieChart(final PieChart pieChart, final Label label) {
        for(final PieChart.Data data : pieChart.getData()) {
            data.getNode().addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
                System.out.println("mouse click");
                label.setTranslateX(mouseEvent.getSceneX() - label.getLayoutX());
                label.setTranslateY(mouseEvent.getSceneY() - label.getLayoutY());

                double totValue = 0;
                for(final PieChart.Data data1 : pieChart.getData()) {
                    totValue += data1.getPieValue();
                }
                DecimalFormat formatter = new DecimalFormat("#0.00");
                label.setText(formatter.format(data.getPieValue() / totValue * 100) + "%");
            });
        }
        label.setViewOrder(pieChart.getViewOrder() - 1);
    }

    private void insertDataIntoExpenseTypesPieChartData(ObservableList<Expense> expensesData) {
        for (Expense expense : expensesData) {
            switch (expense.getExpenseType()) {
                case Miscellaneous:
                    expenseTypesPieChartData.getFirst().setPieValue(expenseTypesPieChartData.getFirst().getPieValue() + expense.getAmount());
                    break;
                case Car:
                    expenseTypesPieChartData.get(1).setPieValue(expenseTypesPieChartData.get(1).getPieValue() + expense.getAmount());
                    break;
                case Debt:
                    expenseTypesPieChartData.get(2).setPieValue(expenseTypesPieChartData.get(2).getPieValue() + expense.getAmount());
                    break;
                case EatingOut:
                    expenseTypesPieChartData.get(3).setPieValue(expenseTypesPieChartData.get(3).getPieValue() + expense.getAmount());
                    break;
                case Groceries:
                    expenseTypesPieChartData.get(4).setPieValue(expenseTypesPieChartData.get(4).getPieValue() + expense.getAmount());
                    break;
                case Subscription:
                    expenseTypesPieChartData.get(5).setPieValue(expenseTypesPieChartData.get(5).getPieValue() + expense.getAmount());
                    break;
            }
        }
    }
    private void insertDataIntoPayingMethodsPieChartData(ObservableList<Expense> expensesData) {
        for (Expense expense : expensesData) {
            switch (expense.getPayingMethod()) {
                case Cash -> payingMethodsPieChartData.get(0).setPieValue(payingMethodsPieChartData.get(0).getPieValue() + expense.getAmount());
                case Card -> payingMethodsPieChartData.get(1).setPieValue(payingMethodsPieChartData.get(1).getPieValue() + expense.getAmount());
            }
        }
    }

    public void OnMenuFileCloseButton_Click(ActionEvent actionEvent) {
        close();
    }

    @Override
    public void close()
    {
        payingMethodsPieChart.getScene().getWindow().hide();
    }

    public void OnMenuHelpAboutButton_Click(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Expense Tracker");
        alert.setContentText("""
                Author:
                Davide Bertoni
                
                This is the pie chart visualizing the expenses
                
                version 0.1
                """);
        alert.showAndWait();
    }
}
