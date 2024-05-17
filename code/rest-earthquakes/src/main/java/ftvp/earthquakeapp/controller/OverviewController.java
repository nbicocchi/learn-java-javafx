package ftvp.earthquakeapp.controller;

import ftvp.earthquakeapp.persistence.rest.EarthquakeRequestMaker;
import ftvp.earthquakeapp.persistence.model.Earthquake;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.List;
import java.util.stream.StreamSupport;

public class OverviewController {

    @FXML
    private Button searchButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TextField searchField;

    @FXML
    private TextField minMag;

    @FXML
    private TextField maxMag;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    private List<Earthquake> earthquakesFound = new ArrayList<>();
    private ObservableList<Earthquake> earthquakes = FXCollections.observableArrayList();
    private EarthquakeRequestMaker earthquakeRequestMaker = new EarthquakeRequestMaker();

    @FXML
    private TableView<Earthquake> tvEarthquakes;

    TableColumn<Earthquake, String> titleCol = new TableColumn<>("Title");
    TableColumn<Earthquake, Integer> magCol = new TableColumn<>("Magnitude");
    TableColumn<Earthquake, String> placeCol = new TableColumn<>("Place");
    TableColumn<Earthquake, Date> timeCol = new TableColumn<>("Time");

    public void clear(){
        earthquakesFound.clear();
        earthquakes.clear();
        tvEarthquakes.getItems().clear();
    }

    public void initialize(){
        earthquakeRequestMaker.setPlace(null);
        earthquakeRequestMaker.setMinmag(0.0);
        earthquakeRequestMaker.setMaxmag(0.0);
        earthquakeRequestMaker.setStartDate(null);
        earthquakeRequestMaker.setEndDate(null);

        clear();
        initDataSource();
        initializeTableViewProperties();
    }

    public void initDataSource(){
        earthquakesFound = earthquakeRequestMaker.getByParams();
        earthquakes.addAll(StreamSupport.stream(earthquakesFound.spliterator(), false).toList());
    }

    public void setTableView(){
        tvEarthquakes.setItems(earthquakes);
        tvEarthquakes.getColumns().setAll(titleCol, magCol, placeCol, timeCol);
    }

    @FXML
    public void initializeTableViewProperties(){

        titleCol.setPrefWidth(280);
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        magCol.setPrefWidth(90);
        magCol.setCellValueFactory(new PropertyValueFactory<>("mag"));

        placeCol.setPrefWidth(200);
        placeCol.setCellValueFactory(new PropertyValueFactory<>("place"));

        timeCol.setPrefWidth(210);
        timeCol.setCellValueFactory(new PropertyValueFactory<>("datetime"));

        setTableView();
    }

    public void refresh(){

        clear();
        earthquakesFound = earthquakeRequestMaker.getByParams();
        earthquakes.addAll(StreamSupport.stream(earthquakesFound.spliterator(), false).toList());

        setTableView();
    }

    private boolean isNumeric(String str) {
        return str.matches("\\d+");
    }

    @FXML
    void onSearchClicked() {
        deleteButton.setDisable(false);

        if(startDatePicker.getValue() == null){
            earthquakeRequestMaker.setStartDate(null);
        }else{
            earthquakeRequestMaker.setStartDate(startDatePicker.getValue().toString());
        }

        if(endDatePicker.getValue() == null){
            earthquakeRequestMaker.setEndDate(null);
        }else{
            earthquakeRequestMaker.setEndDate(endDatePicker.getValue().toString());
        }

        if(searchField.getText().isEmpty()){
            earthquakeRequestMaker.setPlace(null);
        }
        else{
            earthquakeRequestMaker.setPlace(searchField.getText());
        }

        if(maxMag.getText().isEmpty()){
            earthquakeRequestMaker.setMaxmag(0.0);
        }
        else{
            if(isNumeric(maxMag.getText())){
                earthquakeRequestMaker.setMaxmag(Double.parseDouble(maxMag.getText()));
            }
            else{
                earthquakeRequestMaker.setMaxmag(0.0);
            }
        }

        if(minMag.getText().isEmpty()){
            earthquakeRequestMaker.setMinmag(0.0);
        }else {
            if(isNumeric(minMag.getText())){
                earthquakeRequestMaker.setMinmag(Double.parseDouble(minMag.getText()));
            }
            else{
                earthquakeRequestMaker.setMinmag(0.0);
            }
        }

        refresh();
    }

    @FXML
    void onMapClicked() throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://earthquake.usgs.gov/earthquakes/map/?extent=-88.3591,-538.59375&extent=88.3591,316.40625"));
    }

    @FXML
    void onDeleteClicked() {
        searchField.clear();
        minMag.clear();
        maxMag.clear();
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
    }   

}