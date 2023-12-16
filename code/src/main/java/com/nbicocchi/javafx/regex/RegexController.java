package com.nbicocchi.javafx.regex;

import javafx.fxml.FXML;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;
import org.fxmisc.richtext.CodeArea;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class RegexController {
    @FXML
    private VBox input_box;
    @FXML
    private Label lbStatus;
    @FXML
    private TableView<MatchInformation> tbMatch;
    @FXML
    private TableColumn<MatchInformation, String> tcIndex;
    @FXML
    private TableColumn<MatchInformation, String> tcMatch;
    @FXML
    private TableColumn<MatchInformation, Integer> tcNumber;
    private ArrayList<IndexRange> matches = new ArrayList<>();
    private final CodeArea taInput = new CodeArea();
    private final CodeArea taRegex = new CodeArea();

    public void initialize() {
        tcNumber.setCellValueFactory(new PropertyValueFactory<>("matchNumber"));
        tcNumber.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        tcIndex.setCellValueFactory(new PropertyValueFactory<>("indexRange"));
        tcIndex.setCellFactory(TextFieldTableCell.forTableColumn());
        tcMatch.setCellValueFactory(new PropertyValueFactory<>("match"));
        tcMatch.setCellFactory(TextFieldTableCell.forTableColumn());
        input_box.getChildren().add(1, taRegex);
        input_box.getChildren().add(3, taInput);
        taInput.textProperty().addListener((observable, oldValue, newValue) -> checkMatches());
        taRegex.textProperty().addListener((observable, oldValue, newValue) -> checkMatches());
    }

    @FXML
    void checkMatches() {
        matches = doMatch();
        showMatchesTable();
        highlightMatch();
    }

    // performs match on the input string by regex and return the ArrayList of IndexRange representing matches.
    public ArrayList<IndexRange> doMatch() {
        ArrayList<IndexRange> matches = new ArrayList<>();
        String regex = taRegex.getText(), toMatch = taInput.getText();
        try {
            if (!regex.isEmpty()) {
                Matcher matcher = Pattern.compile(regex).matcher(toMatch);
                while (matcher.find()) {
                    matches.add(new IndexRange(matcher.start(), matcher.end()));
                }
                lbStatus.setText(String.format("%d matches", matches.size()));
            }
        } catch (PatternSyntaxException e) {
            lbStatus.setText(e.getMessage());
        }
        return matches;
    }

    public void showMatchesTable() {
        tbMatch.getItems().clear();
        String inputString = taInput.getText();
        int n = 0;
        for (IndexRange irange : matches) {
            String matchString = inputString.substring(irange.getStart(), irange.getEnd());
            tbMatch.getItems().add(new MatchInformation(n + 1, irange.toString(), matchString));
            n += 1;
        }
    }

    public void highlightMatch() {
        taRegex.clearStyle(0, taRegex.getLength());
        taInput.clearStyle(0, taInput.getLength());
        matches.forEach(irange -> taInput.setStyleClass(irange.getStart(), irange.getEnd(), "-fx-fill: red;"));
    }
}
