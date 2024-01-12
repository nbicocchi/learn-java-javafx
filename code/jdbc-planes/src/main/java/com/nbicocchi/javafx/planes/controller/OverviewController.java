package com.nbicocchi.javafx.planes.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nbicocchi.javafx.planes.persistence.model.Plane;
import com.nbicocchi.javafx.planes.util.UtilsDB;
import com.zaxxer.hikari.HikariConfig;
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

public class OverviewController {
    @FXML private ComboBox<String> cbCategory;
    @FXML private DatePicker dcFirstFlight;
    @FXML private TextField tfLength;
    @FXML private TextField tfName;
    @FXML private TextField tfWingSpan;
    @FXML private TableView<Plane> txView;
    @FXML private TextField tfSearch;
    private ObservableList<Plane> planes;
    private HikariDataSource dataSource;

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
        name.setOnEditCommit(e -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement updatePlane = connection.prepareStatement("UPDATE planes SET name=? WHERE uuid=?")) {
                updatePlane.setString(1, e.getNewValue());
                updatePlane.setString(2, e.getRowValue().getUUID().toString());
                updatePlane.executeUpdate();
                e.getRowValue().setName(e.getNewValue());
            } catch (SQLException ex) {
                new Alert(Alert.AlertType.ERROR, "Database Error").showAndWait();
            }
        });

        length.setPrefWidth(150);
        length.setCellValueFactory(new PropertyValueFactory<>("length"));
        length.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        length.setOnEditCommit(e -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement updatePlane = connection.prepareStatement("UPDATE planes SET length=? WHERE uuid=?")) {
                updatePlane.setDouble(1, e.getNewValue());
                updatePlane.setString(2, e.getRowValue().getUUID().toString());
                updatePlane.executeUpdate();
                e.getRowValue().setLength(e.getNewValue());
            } catch (SQLException ex) {
                new Alert(Alert.AlertType.ERROR, "Database Error").showAndWait();
            }
        });

        wingspan.setPrefWidth(150);
        wingspan.setCellValueFactory(new PropertyValueFactory<>("wingspan"));
        wingspan.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        wingspan.setOnEditCommit(e -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement updatePlane = connection.prepareStatement("UPDATE planes SET wingspan=? WHERE uuid=?")) {
                updatePlane.setDouble(1, e.getNewValue());
                updatePlane.setString(2, e.getRowValue().getUUID().toString());
                updatePlane.executeUpdate();
                e.getRowValue().setLength(e.getNewValue());
            } catch (SQLException ex) {
                new Alert(Alert.AlertType.ERROR, "Database Error").showAndWait();
            }
        });

        firstFlight.setPrefWidth(150);
        firstFlight.setCellValueFactory(new PropertyValueFactory<>("firstFlight"));
        firstFlight.setCellFactory(TextFieldTableCell.forTableColumn(new LocalDateStringConverter()));
        firstFlight.setOnEditCommit(e -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement updatePlane = connection.prepareStatement("UPDATE planes SET firstFlight=? WHERE uuid=?")) {
                updatePlane.setDate(1, Date.valueOf(e.getNewValue()));
                updatePlane.setString(2, e.getRowValue().getUUID().toString());
                updatePlane.executeUpdate();
                e.getRowValue().setFirstFlight(e.getNewValue());
            } catch (SQLException ex) {
                new Alert(Alert.AlertType.ERROR, "Database Error").showAndWait();
            }
        });

        category.setPrefWidth(150);
        category.setCellValueFactory(new PropertyValueFactory<>("category"));
        category.setCellFactory(ComboBoxTableCell.forTableColumn(FXCollections.observableArrayList(planeTypes)));
        category.setOnEditCommit(e -> {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement updatePlane = connection.prepareStatement("UPDATE planes SET category=? WHERE uuid=?")) {
                updatePlane.setString(1, e.getNewValue());
                updatePlane.setString(2, e.getRowValue().getUUID().toString());
                updatePlane.executeUpdate();
                e.getRowValue().setCategory(e.getNewValue());
            } catch (SQLException ex) {
                new Alert(Alert.AlertType.ERROR, "Database Error").showAndWait();
            }
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

        try {
            dbConnection();
            loadData();
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "Database Error").showAndWait();
        }
    }

    private void dbConnection() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(UtilsDB.JDBC_Driver_PostgreSQL);
        config.setJdbcUrl(UtilsDB.JDBC_URL_PostgreSQL);
        config.setLeakDetectionThreshold(2000);
        dataSource = new HikariDataSource(config);
    }

    private void loadData() throws SQLException {
        planes.clear();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement getPlanes = connection.prepareStatement("SELECT * FROM planes");
             ResultSet rs = getPlanes.executeQuery()) {
                    while (rs.next()) {
                        planes.add(new Plane(
                                UUID.fromString(rs.getString("uuid")),
                                rs.getString("name"),
                                rs.getDouble("length"),
                                rs.getDouble("wingspan"),
                                convertSQLDateToLocalDate(rs.getDate("firstFlight")),
                                rs.getString("category")));
                    }
        }
    }

    public static LocalDate convertSQLDateToLocalDate(Date SQLDate) {
        java.util.Date date = new java.util.Date(SQLDate.getTime());
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
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
                    insertDBPlane(plane);
                    planes.add(plane);
                }
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, "JSON import failed").showAndWait();
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "Database failed").showAndWait();
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
                new Alert(Alert.AlertType.ERROR, "Could not save data").showAndWait();
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
            insertDBPlane(plane);
            planes.add(plane);
        } catch (SQLException e) {
            new Alert(Alert.AlertType.ERROR, "SQL Error").showAndWait();
        }
    }

    void insertDBPlane(Plane plane) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement insertPlane = connection.prepareStatement("INSERT INTO planes (uuid, name, length, wingspan, firstFlight, category) VALUES (?, ?, ?, ?, ?, ?)")) {
            insertPlane.setString(1, plane.getUUID().toString());
            insertPlane.setString(2, plane.getName());
            insertPlane.setDouble(3, plane.getLength());
            insertPlane.setDouble(4, plane.getWingspan());
            insertPlane.setDate(5, Date.valueOf(plane.getFirstFlight()));
            insertPlane.setString(6, plane.getCategory());
            insertPlane.executeUpdate();
        }
    }

    @FXML
    void onRemoveClicked() {
        Plane plane = txView.getSelectionModel().getSelectedItem();
        if (plane != null) {
            try {
                removeDBPlane(plane);
                planes.remove(plane);
            } catch (SQLException e) {
                new Alert(Alert.AlertType.ERROR, "SQL Error").showAndWait();
            }
        }
    }

    void removeDBPlane(Plane plane) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement deletePlane = connection.prepareStatement("DELETE FROM planes WHERE uuid=?")) {
            deletePlane.setString(1, plane.getUUID().toString());
            deletePlane.executeUpdate();
        }
    }

    @FXML
    void onAboutClicked() {
        new Alert(Alert.AlertType.INFORMATION, "Plane Manager v0.1").showAndWait();
    }
}
