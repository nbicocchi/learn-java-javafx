package com.nbicocchi.javafx.paint;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public class PaintController {
    @FXML private Canvas canvas;
    @FXML private ColorPicker colorPicker;
    @FXML private TextField brushSize;
    @FXML private CheckBox eraser;

    public void initialize() {
        GraphicsContext g = canvas.getGraphicsContext2D();
        canvas.setOnMouseDragged(e -> {
            double size = Double.parseDouble(brushSize.getText());
            double x = e.getX() - size / 2;
            double y = e.getY() - size / 2;
            if (eraser.isSelected()) {
                g.clearRect(x, y, size, size);
            } else {
                g.setFill(colorPicker.getValue());
                g.fillRect(x, y, size, size);
            }
        });
    }

    public void onSave() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(null);
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        if (file != null) {
            try {
                Image snapshot = canvas.snapshot(null, null);
                ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", file);
            } catch (IOException e) {
                new Alert(Alert.AlertType.ERROR, "Could not save data").showAndWait();
            }
        }
    }

    public void onExit() {
        Platform.exit();
    }
}
