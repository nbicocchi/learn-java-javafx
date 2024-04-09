package com.nbicocchi.javafx.fractals.controller;

import com.nbicocchi.javafx.fractals.model.FractalBean;
import com.nbicocchi.javafx.fractals.model.FractalRenderer;
import com.nbicocchi.javafx.fractals.model.MandelbrotRenderer;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import net.mahdilamb.colormap.Colormaps;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class FractalsController {
    FractalBean viewArea;
    FractalBean complexArea;
    FractalRenderer renderer;
    @FXML
    private ImageView imageView;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private ChoiceBox<String> cbColorMap;
    @FXML
    private Label lbInteractions;
    @FXML
    private Label lbZoom;

    @FXML
    void onReset() {
        initialize();
    }

    public void initialize() {
        cbColorMap.getItems().clear();
        cbColorMap.getItems().addAll("Diff", "Picnic", "Topo", "BrBG", "Curl", "RdYlBu", "BuGn", "DarkMint");
        cbColorMap.getSelectionModel().select("Diff");
        cbColorMap.setOnAction(event -> {
            renderer.setColormap(Colormaps.get(cbColorMap.getValue()));
            updateGUI();
        });
        viewArea = new FractalBean(0, (int) imageView.getFitWidth(), 0, (int) imageView.getFitHeight());
        renderer = new MandelbrotRenderer(viewArea);
        complexArea = renderer.getComplexArea();
        renderer.setColormap(Colormaps.get(cbColorMap.getValue()));
        updateGUI();
    }

    private void updateGUI() {
        imageView.setImage(renderer.render());
        lbInteractions.setText(String.format("interactions = %d", renderer.getIterations()));
        String zoom = String.format("%e", renderer.initialComplexArea().areaRatio(complexArea));
        String exp = zoom.substring(zoom.indexOf('e'));
        lbZoom.setText(String.format("zoom = %sx", exp));
    }

    @FXML
    void onHelp() {
        String context = """
                Arrow keys  ->  Move view
                W/S  ->  Change zoom
                E/D  ->  Change iterations 
                Q    ->  Quit
                """;
        String header = """
                Fractal Viewer v1.0-beta
                Author: Nicola Bicocchi
                """;
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(header);
        alert.setContentText(context);
        alert.showAndWait();
    }

    @FXML
    void onSave() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName("fractal.png");
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                Image image = renderer.render(new FractalBean(0, 4096, 0, 2160));
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException ignored) {
            }
        }
    }

    public void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.UP) {
            complexArea.moveUp();
        } else if (event.getCode() == KeyCode.DOWN) {
            complexArea.moveDown();
        } else if (event.getCode() == KeyCode.LEFT) {
            complexArea.moveLeft();
        } else if (event.getCode() == KeyCode.RIGHT) {
            complexArea.moveRight();
        } else if (event.getCode() == KeyCode.E) {
            renderer.setIterations(Math.min(65536, renderer.getIterations() * 2));
        } else if (event.getCode() == KeyCode.D) {
            renderer.setIterations(Math.max(2, renderer.getIterations() / 2));
        } else if (event.getCode() == KeyCode.W) {
            applyZoom(0.5);
        } else if (event.getCode() == KeyCode.S) {
            applyZoom(1.5);
        } else if (event.getCode() == KeyCode.Q) {
            Platform.exit();
        }
        updateGUI();
    }

    private void applyZoom(double zoomFactor) {
        double xMinNew = complexArea.getxMin() - ((complexArea.getWidth() * zoomFactor) - complexArea.getWidth()) / 2;
        double yMinNew = complexArea.getyMin() - ((complexArea.getHeight() * zoomFactor) - complexArea.getHeight()) / 2;
        double xMaxNew = xMinNew + (complexArea.getWidth() * zoomFactor);
        double yMaxNew = yMinNew + (complexArea.getHeight() * zoomFactor);
        complexArea.setxMax(xMaxNew);
        complexArea.setyMax(yMaxNew);
        complexArea.setxMin(xMinNew);
        complexArea.setyMin(yMinNew);
    }
}
