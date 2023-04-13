package com.nbicocchi.javafx.jdbc.planes;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.LocalDateStringConverter;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class PlanesFileController {
    @FXML private ComboBox<String> cbCategory;
    @FXML private DatePicker dcFirstFlight;
    @FXML private TextField tfLength;
    @FXML private TextField tfName;
    @FXML private TextField tfWingSpan;
    @FXML private TableView<Plane> txView;
    @FXML private TextField tfSearch;
    private ObservableList<Plane> planes;

    public void initialize() {
        planes = FXCollections.observableArrayList();
        FilteredList<Plane> filteredData = new FilteredList<>(planes, plane -> true);
        List<String> planeTypes = List.of("Airliner", "Bomber", "Ekranoplan", "Flying boat", "Outsize cargo", "Transport");
        cbCategory.getItems().removeAll();
        cbCategory.getItems().addAll(planeTypes);
        cbCategory.getSelectionModel().select("Airliner");
        TableColumn<Plane, String> name = new TableColumn<>("Name");
        TableColumn<Plane, Double> length = new TableColumn<>("Length (m)");
        TableColumn<Plane, Double> wingspan = new TableColumn<>("Wing Span (m)");
        TableColumn<Plane, LocalDate> firstFlight = new TableColumn<>("First Flight");
        TableColumn<Plane, String> category = new TableColumn<>("Category");
        name.setPrefWidth(150);
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        name.setCellFactory(TextFieldTableCell.forTableColumn());
        name.setOnEditCommit(e -> e.getRowValue().setName(e.getNewValue()));
        length.setPrefWidth(150);
        length.setCellValueFactory(new PropertyValueFactory<>("length"));
        length.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        length.setOnEditCommit(e -> e.getRowValue().setLength(e.getNewValue()));
        wingspan.setPrefWidth(150);
        wingspan.setCellValueFactory(new PropertyValueFactory<>("wingspan"));
        wingspan.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        wingspan.setOnEditCommit(e -> e.getRowValue().setWingspan(e.getNewValue()));
        firstFlight.setPrefWidth(150);
        firstFlight.setCellValueFactory(new PropertyValueFactory<>("firstFlight"));
        firstFlight.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
        firstFlight.setOnEditCommit(e -> e.getRowValue().setFirstFlight(e.getNewValue()));
        category.setPrefWidth(150);
        category.setCellValueFactory(new PropertyValueFactory<>("category"));
        category.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(planeTypes)));
        category.setOnEditCommit(e -> e.getRowValue().setCategory(e.getNewValue()));
        txView.getColumns().add(name);
        txView.getColumns().add(length);
        txView.getColumns().add(wingspan);
        txView.getColumns().add(firstFlight);
        txView.getColumns().add(category);
        txView.setEditable(true);
        txView.setTableMenuButtonVisible(true);
        txView.setItems(filteredData);
        tfSearch.textProperty().addListener(obs -> {
            String filter = tfSearch.getText();
            if (filter == null || filter.length() == 0) {
                filteredData.setPredicate(plane -> true);
            } else {
                filteredData.setPredicate(plane -> plane.getName().toLowerCase().contains(filter.toLowerCase()));
            }
        });
    }

    @FXML
    void onImportClicked() {
        FileChooser fileChooser = new FileChooser();
        File f = fileChooser.showOpenDialog(null);
        if (f != null) {
            try {
                planes.clear();
                planes.addAll(PlaneUtils.loadFromFile(f.toPath()));
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, "Read Error").showAndWait();
            }
        }
    }

    @FXML
    void onExportClicked() {
        FileChooser fileChooser = new FileChooser();
        File f = fileChooser.showSaveDialog(null);
        if (f != null) {
            try {
                PlaneUtils.saveToFile(planes, f.toPath());
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, "Write Error").showAndWait();
            }
        }
    }

    @FXML
    void onQuitClicked() {
        Platform.exit();
    }

    @FXML
    void onAddClicked() {
        planes.add(new Plane(tfName.getText(), Double.parseDouble(tfLength.getText()), Double.parseDouble(tfWingSpan.getText()), dcFirstFlight.getValue(), cbCategory.getValue()));
    }

    @FXML
    void onRemoveClicked() {
        planes.remove(txView.getSelectionModel().getSelectedItem());
    }

    @FXML
    void onAboutClicked() {
        new Alert(Alert.AlertType.INFORMATION, "Plane Manager v0.1").showAndWait();
    }
}
