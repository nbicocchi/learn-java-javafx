package com.javafx.treefractals;

import javafx.animation.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.util.Duration;

import java.util.random.RandomGenerator;

public class FractalsController {

    @FXML
    private Button btClear;

    @FXML
    private Button btDraw;

    @FXML
    private Button btLoad;

    @FXML
    private CheckBox chkPickColor;

    @FXML
    private CheckBox chkRndColor;

    @FXML
    private ComboBox<String> cmbChoice;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private Canvas myCanvas;

    @FXML
    private Spinner<Double> spDuration;

    @FXML
    private Spinner<Integer> spLeftBranch;

    @FXML
    private Spinner<Integer> spRightBranch;

    @FXML
    private TextField txtUserFractal;

    private GraphicsContext gc;

    private int red, blue, green;
    private int angleLeft, angleRight, animationDuration;
    private Alert alert;

    private final static double screenWidthScale = 2.0;
    private final static double screenHeightScale = 0.88589;
    private final static double canvasWidthScale = 2.033;

    private final static double branchThicknessScale = 0.6;
    private final static double branchLengthScale = 0.8;

    public void initialize() {
        double screenWidth = Screen.getPrimary().getBounds().getWidth();
        double screenHeight = Screen.getPrimary().getBounds().getHeight();

        myCanvas.setWidth(screenWidth * screenWidthScale);
        myCanvas.setHeight(screenHeight * screenHeightScale);

        gc = myCanvas.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(myCanvas.getLayoutX(), myCanvas.getLayoutY(), myCanvas.getWidth(), myCanvas.getHeight());

        alert = new Alert(Alert.AlertType.INFORMATION);

        cmbChoice.setItems(FXCollections.observableArrayList("15-15 GREEN", "30-30 SIENNA", "90-90 OLIVE",
                                                             "30-130 PURPLE", "60-120 YELLOW", "105-75 BLUE",
                                                             "19-89 PINK", "82-8 FUCHSIA", "12-26 SALMON",
                                                             "69-45 TURQUOISE"));

        settings();

        spLeftBranch.setEditable(true);
        spLeftBranch.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 150, 0, 1));

        spRightBranch.setEditable(true);
        spRightBranch.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 150, 0, 1));

        spDuration.setEditable(true);
        spDuration.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(1, 3, 1, 0.1));
    }

    private void settings() {
        btClear.setDisable(true);
        btDraw.setDisable(true);
        btLoad.setDisable(false);

        chkPickColor.setDisable(false);
        chkRndColor.setDisable(false);
        txtUserFractal.setDisable(false);
        spLeftBranch.setDisable(false);
        spRightBranch.setDisable(false);
        spDuration.setDisable(false);
        cmbChoice.setDisable(false);

        colorPicker.setDisable(true);

        if(chkPickColor.isSelected()) {
            colorPicker.setValue(Color.WHITE);
            chkPickColor.setSelected(false);
        }

        if(chkRndColor.isSelected()) {
            chkRndColor.setSelected(false);
        }
    }

    public void onComboItemPicked() {
        if(!cmbChoice.getSelectionModel().isEmpty()) {
            String choice = cmbChoice.getSelectionModel().getSelectedItem();

            switch(choice) {
                case "30-30 SIENNA" -> {
                    spLeftBranch.getValueFactory().setValue(30);
                    spRightBranch.getValueFactory().setValue(30);
                    colorPicker.setValue(Color.SIENNA);
                }
                case "15-15 GREEN" -> {
                    spLeftBranch.getValueFactory().setValue(15);
                    spRightBranch.getValueFactory().setValue(15);
                    colorPicker.setValue(Color.GREEN);
                }
                case "30-130 PURPLE" -> {
                    spLeftBranch.getValueFactory().setValue(30);
                    spRightBranch.getValueFactory().setValue(130);
                    colorPicker.setValue(Color.PURPLE);
                }
                case "60-120 YELLOW" -> {
                    spLeftBranch.getValueFactory().setValue(60);
                    spRightBranch.getValueFactory().setValue(120);
                    colorPicker.setValue(Color.YELLOW);
                }
                case "105-75 BLUE" -> {
                    spLeftBranch.getValueFactory().setValue(105);
                    spRightBranch.getValueFactory().setValue(75);
                    colorPicker.setValue(Color.BLUE);
                }
                case "19-89 PINK" -> {
                    spLeftBranch.getValueFactory().setValue(19);
                    spRightBranch.getValueFactory().setValue(89);
                    colorPicker.setValue(Color.PINK);
                }
                case "82-8 FUCHSIA" -> {
                    spLeftBranch.getValueFactory().setValue(82);
                    spRightBranch.getValueFactory().setValue(8);
                    colorPicker.setValue(Color.FUCHSIA);
                }
                case "12-26 SALMON" -> {
                    spLeftBranch.getValueFactory().setValue(12);
                    spRightBranch.getValueFactory().setValue(26);
                    colorPicker.setValue(Color.SALMON);
                }
                case "69-45 TURQUOISE" -> {
                    spLeftBranch.getValueFactory().setValue(69);
                    spRightBranch.getValueFactory().setValue(45);
                    colorPicker.setValue(Color.TURQUOISE);
                }
                case "90-90 OLIVE" -> {
                    spLeftBranch.getValueFactory().setValue(90);
                    spRightBranch.getValueFactory().setValue(90);
                    colorPicker.setValue(Color.OLIVE);
                }
            }

            if(chkRndColor.isSelected()) {
                chkRndColor.setSelected(false);
            }

            chkPickColor.setSelected(true);
            colorPicker.setDisable(false);
            btDraw.setDisable(false);
            btClear.setDisable(true);
        }
    }

    public void onRandomColorPicked() {
        red = RandomGenerator.getDefault().nextInt(255);
        blue = RandomGenerator.getDefault().nextInt(255);
        green = RandomGenerator.getDefault().nextInt(255);

        btDraw.setDisable(false);

        if(chkPickColor.isSelected()) {
            colorPicker.setValue(Color.WHITE);
            colorPicker.setDisable(true);
            chkPickColor.setSelected(false);
        }
    }

    public void onPickColorPicked() {
        btDraw.setDisable(false);
        colorPicker.setDisable(false);

        if(chkRndColor.isSelected()) {
            chkRndColor.setSelected(false);
        }
    }

    public void onInfoClicked() {

        alert.setTitle("Usage Information");
        alert.setHeaderText(null);
        alert.setContentText("""
                                        Welcome to the 2D Tree Fractals application!
                                        You can have fun building your own tree fractal from scratch
                                        or choose from a variety of premade ones!
                                     
                                        If by any chance you have issues undestanding how to use
                                        this application, make sure to check the README file!
                                        There you will find a more thorough explanation on how
                                        this works, so that nothing will be on the way of
                                        your experience!
                                     
                                        Now let your imagination run free and be sure
                                        to let others know of this app!
                                     """);
        alert.showAndWait();
    }

    public void onLoadClicked() {
        String[] userParams = txtUserFractal.getText().split("\\.");

        alert.setAlertType(Alert.AlertType.ERROR);
        alert.setTitle("Invalid string inserted!");
        alert.setHeaderText(null);
        alert.setHeight(175);
        alert.setWidth(500);

        if(userParams.length != 4) {

            alert.setContentText("""
                                             Number of params wrong!
                                             Please insert exactly 4 params!
                                         """);
            alert.showAndWait();
        } else {
            try {
                Color c = Color.valueOf(userParams[0].toUpperCase());
                if(chkRndColor.isSelected()){
                    chkRndColor.setSelected(false);
                }
                chkPickColor.setSelected(true);
                colorPicker.setValue(c);
                colorPicker.setDisable(false);

                angleLeft = Integer.parseInt(userParams[1]);
                angleRight = Integer.parseInt(userParams[2]);

                animationDuration = Integer.parseInt(userParams[3]);
            } catch(Exception e) {
                alert.setContentText("""
                                                 Wrong string format!
                                                 An example of valid string: GREEN.20.50.2000
                                             """);
                alert.showAndWait();
                return;
            }


            if((angleLeft < 0 || angleLeft > 150) || (angleRight < 0 || angleRight > 150) || (animationDuration < 1000 || animationDuration > 3000)) {

                alert.setContentText("""
                                                 Params out of bounds!
                                                 Second and third param must be a value between 0 and 150 (included)!
                                                 Fourth param must be a value between 1000 and 3000 (included)!
                                             """);
                alert.showAndWait();
            } else {
                spLeftBranch.getValueFactory().setValue(angleLeft);
                spRightBranch.getValueFactory().setValue(angleRight);
                spDuration.getValueFactory().setValue((double) animationDuration / 1000);
                btDraw.setDisable(false);
            }
        }
    }

    public void onClearClicked() {
        gc.fillRect(0, 0, myCanvas.getWidth(), myCanvas.getHeight());
        settings();
        cmbChoice.getSelectionModel().clearSelection();
    }

    public void onDrawClicked() {

        chkPickColor.setDisable(true);
        chkRndColor.setDisable(true);
        colorPicker.setDisable(true);
        txtUserFractal.setDisable(true);
        spLeftBranch.setDisable(true);
        spRightBranch.setDisable(true);
        spDuration.setDisable(true);
        btLoad.setDisable(true);
        btDraw.setDisable(true);
        cmbChoice.setDisable(true);

        if(chkPickColor.isSelected()) {
            red = (int) (colorPicker.getValue().getRed() * 255);
            blue = (int) (colorPicker.getValue().getBlue() * 255);
            green = (int) (colorPicker.getValue().getGreen() * 255);
        }

        drawBranch(new Point2D(myCanvas.getWidth() / canvasWidthScale, myCanvas.getHeight() - 230),
                   -90, myCanvas.getHeight() / 7, 1, 10, 1, Color.rgb(red, green, blue));
    }

    /**
     * A recursive method that draws a single branch given a starting point, the rotation, the length of said branch, its thickness and its color.
     * For each pair of recursive calls the length, thickness and color of the branches change gradually.
     *
     * @param start:                the starting point of the branch.
     * @param rotation:             the rotation the branch has to follow.
     * @param length:               the length of the branch.
     * @param thicknessFactorScale: the factor that scales the thickness of the branch.
     * @param thickness:            the thickness of the branch.
     * @param depth:                a counter used to match the cange of the color to the depth of the branch.
     * @param color:                the color of the branch.
     */
    private void drawBranch(Point2D start, double rotation, double length, double thicknessFactorScale, double thickness, int depth, Color color) {
        angleLeft = spLeftBranch.getValue();
        angleRight = spRightBranch.getValue();

        gc.setStroke(color);
        Color newColor = brigtherColor(depth, color.deriveColor(15, 1, 1, 1));

        Point2D end = findEndPoint(start, rotation, length);

        animateBranch(start, end, thickness, thicknessFactorScale, () -> {
            gc.setLineWidth(thickness / thicknessFactorScale);
            gc.strokeLine(start.getX(), start.getY(), end.getX(), end.getY());

            if(length > 4) {
                drawBranch(end, rotation - angleLeft, length * branchLengthScale, thicknessFactorScale / branchThicknessScale, thickness, depth + 1, newColor);
                drawBranch(end, rotation + angleRight, length * branchLengthScale, thicknessFactorScale / branchThicknessScale, thickness, depth + 1, newColor);
            } else {
                btClear.setDisable(false);
                btDraw.setDisable(true);

            }
        });
    }

    /**
     * A method that animates the drawing of each branch. It requires the starting and end point of said branch, its width and width scale factor.
     * It also needs a runnable interface to make use of its run method.
     * It creates a gradual, fluid and steady growth for the branch.
     *
     * @param start:                the starting point of the branch.
     * @param end:                  the ending point of the branch.
     * @param branchThickness:      the thickness of the branch.
     * @param thicknessFactorScale: the factor that scales the thickness of the branch.
     * @param onFinish:             runnable interface that has to be executed at the end of the animation.
     */
    private void animateBranch(Point2D start, Point2D end, double branchThickness, double thicknessFactorScale, Runnable onFinish) {
        DoubleProperty lengthProperty = new SimpleDoubleProperty(0);

        animationDuration = (int) (spDuration.getValue() * 1000);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(lengthProperty, 0)),
                new KeyFrame(Duration.millis(animationDuration), new KeyValue(lengthProperty, end.distance(start)))
        );

        timeline.setOnFinished(event -> onFinish.run());

        lengthProperty.addListener((observable, oldValue, newValue) -> {
            double animatedLength = newValue.doubleValue();
            Point2D animatedEnd = interpolatePoint(start, end, animatedLength / end.distance(start));

            gc.setLineWidth(branchThickness / thicknessFactorScale);
            gc.strokeLine(start.getX(), start.getY(), animatedEnd.getX(), animatedEnd.getY());
        });

        timeline.play();
    }

    /**
     * A method that calculates the approximate value of the next point to draw on a branch.
     * The point calculeted using this method are the key to a fluid and steady animation.
     *
     * @param start: the starting point of the branch.
     * @param end: the ending point of the branch.
     * @param fraction: the remaining distance beetween the calculated point and the end point.
     * @return the calculated point.
     */
    private Point2D interpolatePoint(Point2D start, Point2D end, double fraction) {
        double x = start.getX() + fraction * (end.getX() - start.getX());
        double y = start.getY() + fraction * (end.getY() - start.getY());
        return new Point2D(x, y);
    }

    /**
     * A method that creates a brigher version of a given color considering the depth of the branch.
     *
     * @param depth: the depth of the branch.
     * @param color: the color of the branch.
     * @return the newly created color
     */
    private Color brigtherColor(int depth, Color color) {
        int r = (int) (color.getRed() * 255);
        int b = (int) (color.getBlue() * 255);
        int g = (int) (color.getGreen() * 255);

        int alpha = 50 + (depth * 10);

        return Color.rgb(r, g, b, alpha / 255.0);
    }

    /**
     * A method that finds the end point of the branch given the starting point, the rotation the branch has to follow and its lenght.
     *
     * @param branchStart:    the starting point of the branch.
     * @param branchRotation: the rotation the branch has to follow.
     * @param branchLength:   the length of the branch.
     * @return the ending point of the branch.
     */
    private Point2D findEndPoint(Point2D branchStart, double branchRotation, double branchLength) {
        double radians = Math.toRadians(branchRotation);

        double x = branchStart.getX();
        double y = branchStart.getY();

        x = x + (branchLength * Math.cos(radians));
        y = y + (branchLength * Math.sin(radians));

        return new Point2D(x, y);
    }
}
