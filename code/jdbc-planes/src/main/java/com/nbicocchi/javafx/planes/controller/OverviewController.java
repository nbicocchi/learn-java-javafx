package com.nbicocchi.javafx.planes.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nbicocchi.javafx.planes.persistence.dao.PlaneRepository;
import com.nbicocchi.javafx.planes.persistence.model.Plane;
import com.zaxxer.hikari.HikariDataSource;
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
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class OverviewController {
    @FXML private ComboBox<String> cbCategory;
    @FXML private DatePicker dcFirstFlight;
    @FXML private TextField tfLength;
    @FXML private TextField tfName;
    @FXML private TextField tfWingSpan;
    @FXML private TableView<Plane> txView;
    @FXML private TextField tfSearch;
    private ObservableList<Plane> planes;
    HikariDataSource hikariDataSource;
    private PlaneRepository planeRepository;

    public void initDataSource(HikariDataSource hikariDataSource) {
        this.hikariDataSource = hikariDataSource;
        this.planeRepository = new PlaneRepository(hikariDataSource);
        Iterable<Plane> savedPlanes = planeRepository.findAll();
        planes.addAll(StreamSupport.stream(savedPlanes.spliterator(), false).collect(Collectors.toList()));
    }

    public void initialize() throws SQLException {
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
        name.setOnEditCommit(event -> {
            Plane selectedPlane = event.getRowValue();
            selectedPlane.setName(event.getNewValue());;
            planeRepository.save(selectedPlane);
        });


        length.setPrefWidth(150);
        length.setCellValueFactory(new PropertyValueFactory<>("length"));
        length.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        length.setOnEditCommit(event -> {
            Plane selectedPlane = event.getRowValue();
            selectedPlane.setLength(event.getNewValue());;
            planeRepository.save(selectedPlane);
        });

        wingspan.setPrefWidth(150);
        wingspan.setCellValueFactory(new PropertyValueFactory<>("wingspan"));
        wingspan.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        wingspan.setOnEditCommit(event -> {
            Plane selectedPlane = event.getRowValue();
            selectedPlane.setWingspan(event.getNewValue());;
            planeRepository.save(selectedPlane);
        });

        firstFlight.setPrefWidth(150);
        firstFlight.setCellValueFactory(new PropertyValueFactory<>("firstFlight"));
        firstFlight.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
        firstFlight.setOnEditCommit(event -> {
            Plane selectedPlane = event.getRowValue();
            selectedPlane.setFirstFlight(event.getNewValue());
            planeRepository.save(selectedPlane);
        });

        category.setPrefWidth(150);
        category.setCellValueFactory(new PropertyValueFactory<>("category"));
        category.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(planeTypes)));
        category.setOnEditCommit(event -> {
            Plane selectedPlane = event.getRowValue();
            selectedPlane.setCategory(event.getNewValue());;
            planeRepository.save(selectedPlane);
        });

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
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            try {
                List<Plane> tmp = mapper.readValue(file, new TypeReference<>() {});
                for (Plane plane : tmp) {
                    planes.add(planeRepository.save(plane));
                }
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR).showAndWait();
            }
        }
    }

    @FXML
    void onExportClicked() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.registerModule(new JavaTimeModule());
                mapper.writerWithDefaultPrettyPrinter().writeValue(file, planes);
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR).showAndWait();
            }
        }
    }

    @FXML
    void onQuitClicked() {
        Platform.exit();
    }

    @FXML
    void onAddClicked() {
        Plane plane = new Plane(tfName.getText(), Double.parseDouble(tfLength.getText()), Double.parseDouble(tfWingSpan.getText()), dcFirstFlight.getValue(), cbCategory.getValue());
        try {
            planes.add(planeRepository.save(plane));
        } catch (RuntimeException e) {
            new Alert(Alert.AlertType.ERROR).showAndWait();
        }
    }

    @FXML
    void onRemoveClicked() {
        Plane plane = txView.getSelectionModel().getSelectedItem();
        if (plane != null) {
            try {
                planeRepository.deleteById(plane.getId());
                planes.remove(plane);
            } catch (RuntimeException e) {
                new Alert(Alert.AlertType.ERROR).showAndWait();
            }
        }
    }

    @FXML
    void onAboutClicked() {
        new Alert(Alert.AlertType.INFORMATION, "Plane Manager v0.1").showAndWait();
    }
}
