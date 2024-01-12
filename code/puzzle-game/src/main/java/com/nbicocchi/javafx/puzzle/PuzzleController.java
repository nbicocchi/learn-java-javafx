package com.nbicocchi.javafx.puzzle;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.random.RandomGenerator;

public class PuzzleController {
    @FXML
    private ChoiceBox<String> cbPuzzleFormat;
    @FXML
    private Label lbSolved;
    @FXML
    private TilePane tilePane;
    private List<Tile> tiles;

    public void initialize() {
        cbPuzzleFormat.getItems().addAll("3x3", "4x4", "5x5");
        cbPuzzleFormat.getSelectionModel().select("3x3");
    }

    @FXML
    void onLoad() {
        FileChooser open = new FileChooser();
        open.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg"));
        File selectedFile = open.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                int xTiles = Integer.parseInt(cbPuzzleFormat.getValue().split("x")[0]);
                int yTiles = Integer.parseInt(cbPuzzleFormat.getValue().split("x")[1]);
                List<Image> images = getImages(selectedFile.getAbsolutePath(), xTiles, yTiles);
                buildTiles(images, xTiles, yTiles);
                updateTiles();
            } catch (FileNotFoundException | NullPointerException | IllegalArgumentException e) {
                new Alert(Alert.AlertType.ERROR, "Something went wrong...").showAndWait();
            }
        }
    }

    @FXML
    void onShuffle() {
        if (tiles != null) {
            shuffleTiles();
            updateTiles();
        }
    }

    void shuffleTiles() {
        RandomGenerator rnd = RandomGenerator.getDefault();
        for (int i = 0; i < tiles.size(); i++) {
            Tile a = tiles.get(rnd.nextInt(tiles.size()));
            Tile b = tiles.get(rnd.nextInt(tiles.size()));
            if (a != b) {
                swapTiles(a, b);
            }
        }
    }

    void updateTiles() {
        orderTilesByPosition();
        tilePane.getChildren().clear();
        for (Tile p : tiles) {
            tilePane.getChildren().add(p);
        }
    }

    void swapTiles(Tile empty, Tile clicked) {
        Point2D tmp = new Point2D(clicked.current.getX(), clicked.current.getY());
        clicked.current = empty.current;
        empty.current = tmp;
    }

    void orderTilesByPosition() {
        tiles.sort((o1, o2) -> {
            int cmp = (int) (o1.current.getY() - o2.current.getY());
            if (cmp == 0) {
                cmp = (int) (o1.current.getX() - o2.current.getX());
            }
            return cmp;
        });
    }

    List<Image> getImages(String imagePath, int xTiles, int yTiles) throws FileNotFoundException {
        int tileWidth = (int) tilePane.getWidth() / xTiles;
        int tileHeight = (int) tilePane.getHeight() / yTiles;
        Image image = new Image(new FileInputStream(imagePath), xTiles * tileWidth, yTiles * tileHeight, false, false);
        List<Image> images = new ArrayList<>();
        for (int y = 0; y < yTiles; y++) {
            for (int x = 0; x < xTiles; x++) {
                images.add(new WritableImage(image.getPixelReader(), x * tileWidth, y * tileHeight, tileWidth, tileHeight));
            }
        }
        return images;
    }

    void buildTiles(List<Image> images, int xTiles, int yTiles) {
        tiles = new LinkedList<>();
        for (int y = 0; y < yTiles; y++) {
            for (int x = 0; x < xTiles; x++) {
                Tile p = new Tile(images.get(y * xTiles + x), new Point2D(x, y));
                p.setOnAction(event -> {
                    Tile clicked = (Tile) event.getSource();
                    Tile empty = getEmptyTile();
                    if (areSwappable(clicked, empty)) {
                        swapTiles(empty, clicked);
                    }
                    updateTiles();
                    updateSolved();
                });
                tiles.add(p);
            }
        }
        tiles.get(tiles.size() - 1).setEmpty();
    }

    Tile getEmptyTile() {
        Tile emptyTile = null;
        for (Tile p : tiles) {
            if (p.isEmpty()) {
                emptyTile = p;
                break;
            }
        }
        return emptyTile;
    }

    boolean areSwappable(Tile a, Tile b) {
        if ((Math.abs(a.current.getX() - b.current.getX()) == 1) && (a.current.getY() == b.current.getY())) {
            return true;
        }
        return (Math.abs(a.current.getY() - b.current.getY()) == 1) && (a.current.getX() == b.current.getX());
    }

    void updateSolved() {
        if (isSolved()) {
            lbSolved.setText("Solved!");
        } else {
            lbSolved.setText("Unsolved");
        }
    }

    boolean isSolved() {
        for (Tile p : tiles) {
            if (!p.isOK())
                return false;
        }
        return true;
    }
}
