package com.nbicocchi.javafx.planes.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nbicocchi.javafx.planes.App;
import com.nbicocchi.javafx.planes.persistence.dao.PlaneRepository;
import com.nbicocchi.javafx.planes.persistence.model.Part;
import com.nbicocchi.javafx.planes.persistence.model.Plane;
import com.zaxxer.hikari.HikariDataSource;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
import java.util.Optional;
import java.util.stream.StreamSupport;

public class OverviewController {
    @FXML private TableView<Part> tbParts;
    @FXML private TableView<Plane> tbPlanes;
    @FXML private TextField tfSearch;

    private ObservableList<Plane> planes;
    private HikariDataSource hikariDataSource;
    private PlaneRepository planeRepository;

    public void initDataSource(HikariDataSource hikariDataSource) {
        this.hikariDataSource = hikariDataSource;
        this.planeRepository = new PlaneRepository(hikariDataSource);
        Iterable<Plane> savedPlanes = planeRepository.findAll();
        planes.addAll(StreamSupport.stream(savedPlanes.spliterator(), false).toList());
    }

    public void initialize() {
        planes = FXCollections.observableArrayList();
        FilteredList<Plane> filteredData = new FilteredList<>(planes, plane -> true);



        /*
        cbCategory.getItems().removeAll();
        cbCategory.getItems().addAll(planeTypes);
        cbCategory.getSelectionModel().select("Airliner");
         */

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
            selectedPlane.setName(event.getNewValue());
            planeRepository.save(selectedPlane);
        });


        length.setPrefWidth(150);
        length.setCellValueFactory(new PropertyValueFactory<>("length"));
        length.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        length.setOnEditCommit(event -> {
            Plane selectedPlane = event.getRowValue();
            selectedPlane.setLength(event.getNewValue());
            planeRepository.save(selectedPlane);
        });

        wingspan.setPrefWidth(150);
        wingspan.setCellValueFactory(new PropertyValueFactory<>("wingspan"));
        wingspan.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        wingspan.setOnEditCommit(event -> {
            Plane selectedPlane = event.getRowValue();
            selectedPlane.setWingspan(event.getNewValue());
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
        category.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(App.planeTypes)));
        category.setOnEditCommit(event -> {
            Plane selectedPlane = event.getRowValue();
            selectedPlane.setCategory(event.getNewValue());
            planeRepository.save(selectedPlane);
        });

        tbPlanes.getColumns().add(name);
        tbPlanes.getColumns().add(length);
        tbPlanes.getColumns().add(wingspan);
        tbPlanes.getColumns().add(firstFlight);
        tbPlanes.getColumns().add(category);
        tbPlanes.setEditable(true);
        tbPlanes.setTableMenuButtonVisible(true);
        tbPlanes.setItems(filteredData);
        tfSearch.textProperty().addListener(obs -> {
            String filter = tfSearch.getText();
            if (filter == null || filter.isEmpty()) {
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
                    Plane saved = planeRepository.save(plane);
                    planes.add(saved);
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
    void onAddPlaneClicked() throws IOException {
        AddPlaneDialog loginDialog = new AddPlaneDialog();
        Optional<Plane> result = loginDialog.showAndWait();
        result.ifPresentOrElse(plane -> {
            try {
                planes.add(planeRepository.save(plane));
            } catch (RuntimeException e) {
                new Alert(Alert.AlertType.ERROR).showAndWait();
            }
        }, () -> {});
    }

    @FXML
    void onRemovePlaneClicked() {
        Plane plane = tbPlanes.getSelectionModel().getSelectedItem();
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
    void onAddPartClicked() {

    }

    @FXML
    void onRemovePartClicked() {

    }

    @FXML
    void onAboutClicked() {
        new Alert(Alert.AlertType.INFORMATION, "Plane Manager v0.1").showAndWait();
    }
}
