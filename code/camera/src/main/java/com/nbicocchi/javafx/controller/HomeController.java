package com.nbicocchi.javafx.controller;

import com.nbicocchi.javafx.effects.Flip;
import com.nbicocchi.javafx.effects.LiveEffect;
import javafx.collections.ObservableMap;
import com.nbicocchi.javafx.common.WebcamListener;
import com.nbicocchi.javafx.common.WebcamUtils;
import com.nbicocchi.javafx.common.AlertWindows;
import com.nbicocchi.javafx.common.FrameShowThread;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import static java.lang.Thread.interrupted;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;

import com.github.sarxos.webcam.Webcam;

public class HomeController {
    @FXML
    private ImageView webcamImageView;
    @FXML
    private StackPane stackPane;
    @FXML
    private ChoiceBox<Webcam> webcamChoiceBox;
    private static ObservableList<Webcam> webcams;
    @FXML
    private ToggleButton applyEffectButton;
    @FXML
    private ChoiceBox<String> liveEffectChoiceBox;
    private Set<String> keySet;
    private ObservableMap<String, LiveEffect> liveEffects;
    @FXML
    private Text FPSTray;
    @FXML
    private RadioButton stabilityTray;

    public void initialize() {
        initLiveEffects();
        initWebcamChoiceBox();
        if (webcams.isEmpty()) {
            Platform.runLater(this::disableInterface);
            Thread webcamWaiter = getWebcamWaiterThread();
            webcamWaiter.setDaemon(true);
            webcamWaiter.start();
        } else {
            initWebcam();
        }
    }

    private void initWebcamChoiceBox() {
        webcams = FXCollections.observableArrayList();
        new WebcamListener(webcams);
        webcamChoiceBox.setItems(webcams);
        webcams.addListener((ListChangeListener<Webcam>) change -> webcamChoiceBox.setItems(webcams));
    }

    private void initWebcam() {
        webcamChoiceBox.getSelectionModel().selectFirst();
        Webcam activeWebcam = webcamChoiceBox.getSelectionModel().getSelectedItem();
        webcamChoiceBox.setValue(activeWebcam);
        WebcamUtils.startUpWebcam(activeWebcam, null);
        FrameShowThread frameShowThread = new FrameShowThread(webcamChoiceBox, activeWebcam, webcamImageView, FPSTray, stabilityTray);
        initFrameShowThread(frameShowThread);
    }
    private Thread getWebcamWaiterThread() {
        return new Thread(() -> {
            System.out.println("WebcamWaiter: no such webcam detected. Waiting for webcams...");
            Platform.runLater(this::showErrorImage);
            FPSTray.setText("FPS: --");
            while (!interrupted()) {
                if (!webcams.isEmpty()) {
                    break;
                }
            }
            Platform.runLater(this::initWebcam);
            enableInterface();
            System.out.println("WebcamWaiter: webcam" + webcams.getFirst() + " found.");
        });
    }
    private void showErrorImage() {
        ImageView errorImage = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("errorImage/ErrorImage.png"))));
        Flip.viewportFlipper(errorImage);
        webcamImageView.setImage(errorImage.snapshot(null, null));
    }

    private void initFrameShowThread(FrameShowThread thread) {
        Objects.requireNonNull(thread, "Thread cannot be null");
        thread.startShowingFrame();
    }

    private void initLiveEffects() {
        liveEffects = FXCollections.observableHashMap();
        liveEffects.put("Flip", new Flip());
        liveEffects = FXCollections.unmodifiableObservableMap(liveEffects);
        Set<String> keySet = Collections.unmodifiableSet(liveEffects.keySet());
        liveEffectChoiceBox.setItems(FXCollections.observableList(keySet.stream().toList()));
        liveEffectChoiceBox.setValue("Flip");

        for (String key : keySet) {
            liveEffects.get(key).enable();
        }
        String selectedKey = liveEffectChoiceBox.getSelectionModel().getSelectedItem();
        liveEffects.get(selectedKey).toggle(webcamImageView);
    }

    public void disableInterface() {
        Parent root = stackPane.getScene().getRoot();
        root.disableProperty().setValue(true);
        for (String key : keySet) {
            liveEffects.get(key).disable();
        }
    }

    public void enableInterface() {
        for (String key : keySet) {
            liveEffects.get(key).enable();
        }
        Parent root = stackPane.getScene().getRoot();
        root.disableProperty().setValue(false);
    }

    @FXML
    private void toggleEffect() {
        String selectedKey = liveEffectChoiceBox.getSelectionModel().getSelectedItem();
        LiveEffect effect = liveEffects.get(selectedKey);
        if (effect.isDisabled()) {
            throw new RuntimeException("Flip is currently disabled.");
        }
        effect.toggle(webcamImageView);
        applyEffectButton.setText(effect.getStatus());
    }

    @FXML
    private void takePicture() {
        try {
            Image caption = webcamImageView.getImage();
            handleSave(caption);
        } catch (Exception e) {
            AlertWindows.showFailedToTakePictureAlert();
            throw new RuntimeException(e);
        }
    }
    private void handleSave(Image picture) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("save-dialog.fxml"));

        DialogPane saveDialog = loader.load();
        DialogController dialogController = loader.getController();
        dialogController.initPreview(picture);
        dialogController.initTypeChoiceBox();
        // snapshot method renders the current node into a WritableImage object
        // (Writable Image extends Image).

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Save Image");
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.setDialogPane(saveDialog);
        dialog.initOwner(stackPane.getScene().getWindow());
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            dialogController.save();
            dialog.close();
            event.consume();
        });
        dialog.setResizable(false);
        dialog.showAndWait();
    }
}