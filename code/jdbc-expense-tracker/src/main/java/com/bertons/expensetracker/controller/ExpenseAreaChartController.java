package com.bertons.expensetracker.controller;

import com.bertons.expensetracker.persistence.model.Expense;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

import java.io.Closeable;
import java.time.Month;
import java.util.List;
import java.util.Objects;

public class ExpenseAreaChartController implements DataToCharts, Closeable {
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private ComboBox<Integer> yearsComboBox;
    @FXML
    private AreaChart<String, Number> areaChart;

    private MainPageViewController mainPageViewController;
    private final XYChart.Series<String, Number> series = new XYChart.Series<>();
    private final ObservableList<Integer> comboBoxChoices = FXCollections.observableArrayList();

    @Override
    public void initCharts(ObservableList<Expense> expenses, MainPageViewController mainPageViewController) {
        this.mainPageViewController = mainPageViewController;
        initializeYearsComboBox(expenses);

        final ObservableList<String> months = FXCollections.observableArrayList(Month.JANUARY.toString(), Month.FEBRUARY.toString(), Month.MARCH.toString(), Month.APRIL.toString(), Month.MAY.toString(), Month.JUNE.toString(), Month.JULY.toString(), Month.AUGUST.toString(), Month.SEPTEMBER.toString(), Month.OCTOBER.toString(), Month.NOVEMBER.toString(), Month.DECEMBER.toString());
        xAxis.setCategories(months);
        yAxis.setLabel("Monthly Amount");

        initializeDataSeries(series);
        updateCharts(expenses);
    }

    @Override
    public void updateCharts(ObservableList<Expense> expenses) {
        resetSeries(series);
        updateYearsComboBox(expenses);

        if(yearsComboBox.getItems().isEmpty())
        {
            System.out.println("Empty list combobox");
            return;
        }

        if(yearsComboBox.getSelectionModel().getSelectedItem() == null) {
            yearsComboBox.getSelectionModel().selectFirst();
        }

        expenses.stream().filter(expense -> expense.getDate().getYear() == yearsComboBox.getValue()).forEach(expense -> series.getData().forEach(data -> {
            if(Objects.equals(data.getXValue(), expense.getDate().getMonth().toString()))
            {
                data.setYValue(data.getYValue().doubleValue() + expense.getAmount());
            }
        }));

        areaChart.getData().clear();
        areaChart.getData().add(series);
    }

    private void resetSeries(XYChart.Series<String, Number> series) {
        series.getData().forEach(data -> data.setYValue(0));
    }
    private void initializeDataSeries(XYChart.Series<String, Number> series) {
        series.getData().add(new XYChart.Data<>(Month.JANUARY.toString(), 0));
        series.getData().add(new XYChart.Data<>(Month.FEBRUARY.toString(), 0));
        series.getData().add(new XYChart.Data<>(Month.MARCH.toString(), 0));
        series.getData().add(new XYChart.Data<>(Month.APRIL.toString(), 0));
        series.getData().add(new XYChart.Data<>(Month.MAY.toString(), 0));
        series.getData().add(new XYChart.Data<>(Month.JUNE.toString(), 0));
        series.getData().add(new XYChart.Data<>(Month.JULY.toString(), 0));
        series.getData().add(new XYChart.Data<>(Month.AUGUST.toString(), 0));
        series.getData().add(new XYChart.Data<>(Month.SEPTEMBER.toString(), 0));
        series.getData().add(new XYChart.Data<>(Month.OCTOBER.toString(), 0));
        series.getData().add(new XYChart.Data<>(Month.NOVEMBER.toString(), 0));
        series.getData().add(new XYChart.Data<>(Month.DECEMBER.toString(), 0));
    }
    private void initializeYearsComboBox(ObservableList<Expense> expenses) {
        yearsComboBox.getItems().removeAll();
        comboBoxChoices.addAll(expenses.stream().map(e -> e.getDate().getYear()).distinct().toList());
        yearsComboBox.setItems(comboBoxChoices);
        yearsComboBox.getSelectionModel().selectFirst();
    }

    private void updateYearsComboBox(ObservableList<Expense> expenses) {
        List<Integer> years = expenses.stream().map(e -> e.getDate().getYear()).distinct().toList();

        for(int y1 : years) {
            boolean found = false;
            for(int y2 : comboBoxChoices) {
                if (y1 == y2) {
                    found = true;
                    break;
                }
            }

            if(!found)
            {
                comboBoxChoices.add(y1);
            }
        }
    }

    public void OnMenuFileCloseButton_Click(ActionEvent actionEvent) {
        close();
    }

    @Override
    public void close()
    {
        areaChart.getScene().getWindow().hide();
    }

    public void OnMenuHelpAboutButton_Click(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Expense Tracker");
        alert.setContentText("""
                Author:
                Davide Bertoni
                
                This is the area chart visualizing the expenses through the year
                """);
        alert.showAndWait();
    }

    public void OnYearsComboBoxSelectYear(ActionEvent actionEvent) {
        ObservableList<Expense> expenses = mainPageViewController.getExpenses();
        updateCharts(expenses);
    }
}
