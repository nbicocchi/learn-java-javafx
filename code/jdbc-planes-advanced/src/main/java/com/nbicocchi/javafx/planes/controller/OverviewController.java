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
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

public class OverviewController {
    @FXML private TableView<Part> tvParts;
    @FXML private TableView<Plane> tvPlanes;
    @FXML private TextField tfSearch;

    private final ObservableList<Plane> planes = FXCollections.observableArrayList();
    private final ObservableList<Part> parts = FXCollections.observableArrayList();
    private PlaneRepository planeRepository;

    public void initDataSource(HikariDataSource hikariDataSource) {
        this.planeRepository = new PlaneRepository(hikariDataSource);
        Iterable<Plane> planesFound = planeRepository.findAll();
        planes.addAll(StreamSupport.stream(planesFound.spliterator(), false).toList());
    }

    public void initialize() {
        initializeTableViewPlanes();
        initializeTableViewParts();
    }

    private void initializeTableViewPlanes() {
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

        SortedList<Plane> sortedList = new SortedList<>(planes, Comparator.comparing(Plane::getName));
        FilteredList<Plane> filteredList = new FilteredList<>(sortedList, plane -> true);
        tvPlanes.setItems(filteredList);
        tvPlanes.getColumns().addAll(name, length, wingspan, firstFlight, category);
        tvPlanes.setEditable(true);
        tvPlanes.setTableMenuButtonVisible(true);

        tvPlanes.getSelectionModel().getSelectedItems().addListener(
                (ListChangeListener<Plane>) change -> {
                    if (Objects.nonNull(change.getList().get(0).getParts())) {
                        parts.setAll(change.getList().get(0).getParts());
                    } else {
                        parts.clear();
                    }
                });

        tfSearch.textProperty().addListener(obs -> {
            String filter = tfSearch.getText();
            if (filter == null || filter.isEmpty()) {
                filteredList.setPredicate(plane -> true);
            } else {
                filteredList.setPredicate(plane -> plane.getName().toLowerCase().contains(filter.toLowerCase()));
            }
        });
    }

    private void initializeTableViewParts() {
        TableColumn<Part, String> code = new TableColumn<>("Code");
        TableColumn<Part, String> description = new TableColumn<>("Description");
        TableColumn<Part, Double> duration = new TableColumn<>("Duration (years)");

        code.setPrefWidth(150);
        code.setCellValueFactory(new PropertyValueFactory<>("partCode"));
        code.setCellFactory(TextFieldTableCell.forTableColumn());
        code.setOnEditCommit(event -> {
            Part selectedPart = event.getRowValue();
            selectedPart.setPartCode(event.getNewValue());
            planeRepository.save(selectedPart.getPlane());
        });

        description.setPrefWidth(150);
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        description.setCellFactory(TextFieldTableCell.forTableColumn());
        description.setOnEditCommit(event -> {
            Part selectedPart = event.getRowValue();
            selectedPart.setDescription(event.getNewValue());
            planeRepository.save(selectedPart.getPlane());
        });

        duration.setPrefWidth(150);
        duration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        duration.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        duration.setOnEditCommit(event -> {
            Part selectedPart = event.getRowValue();
            selectedPart.setDuration(event.getNewValue());
            planeRepository.save(selectedPart.getPlane());
        });

        tvParts.getColumns().addAll(code, description, duration);
        tvParts.setItems(parts);
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
                new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
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
                new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
            }
        }
    }

    @FXML
    void onQuitClicked() {
        Platform.exit();
    }

    @FXML
    void onAddPlaneClicked() throws IOException {
        AddPlaneDialog dialog = new AddPlaneDialog();
        Optional<Plane> optionalPlane = dialog.showAndWait();
        if (optionalPlane.isPresent()) {
            try {
                Plane saved = planeRepository.save(optionalPlane.get());
                planes.add(saved);
            } catch (RuntimeException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
            }
        }
    }

    @FXML
    void onRemovePlaneClicked() {
        Plane selectedItem = tvPlanes.getSelectionModel().getSelectedItem();
        if (Objects.nonNull(selectedItem)) {
            try {
                planeRepository.deleteById(selectedItem.getId());
                planes.remove(selectedItem);
            } catch (RuntimeException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
            }
        }
    }

    @FXML
    void onAddPartClicked() throws IOException {
        Plane selectedPlane = tvPlanes.getSelectionModel().getSelectedItem();
        if (Objects.nonNull(selectedPlane)) {
            AddPartDialog dialog = new AddPartDialog();
            Optional<Part> optionalPart = dialog.showAndWait();
            if (optionalPart.isPresent()) {
                try {
                    parts.add(optionalPart.get());
                    selectedPlane.addPart(optionalPart.get());
                    System.out.println(selectedPlane);
                    planeRepository.save(selectedPlane);
                } catch (RuntimeException e) {
                    new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
                }
            }
        }
    }

    @FXML
    void onRemovePartClicked() {
        Plane selectedPlane = tvPlanes.getSelectionModel().getSelectedItem();
        Part selectedPart = tvParts.getSelectionModel().getSelectedItem();
        if (Objects.nonNull(selectedPlane) && Objects.nonNull(selectedPart)) {
            try {
                parts.remove(selectedPart);
                selectedPlane.removePart(selectedPart);
                planeRepository.save(selectedPlane);
            } catch (RuntimeException e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage(), ButtonType.OK).showAndWait();
            }
        }
    }

    @FXML
    void onAboutClicked() {
        new Alert(Alert.AlertType.INFORMATION, "Plane Manager v0.1").showAndWait();
    }
}
