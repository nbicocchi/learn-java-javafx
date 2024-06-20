package us.teamronda.briscola.gui.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import lombok.Getter;
import us.teamronda.briscola.GameLogic;
import us.teamronda.briscola.api.cards.ICard;
import us.teamronda.briscola.api.player.IPlayer;
import us.teamronda.briscola.gui.SceneSwitcher;
import us.teamronda.briscola.gui.components.CardAssets;
import us.teamronda.briscola.gui.components.CardComponent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TableController extends SceneSwitcher {

    @Getter
    private static TableController instance;
    private static final long DELTA_LABEL_ANIMATION_DURATION = 750L;

    @FXML
    private StackPane deckBox; // Deck rectangle
    @FXML
    private HBox playerBox; // HBox containing the player's cards
    @FXML
    private HBox opponentBox; // HBox containing the bots' cards
    @FXML
    private HBox playedCardsBox; // HBox containing the played cards

    // Labels used to display the points of players
    @FXML
    private Label opponentPointsLabel;
    @FXML
    private Label opponentDeltaPointsLabel;

    // Labels used to display variation in points
    @FXML
    private Label playerPointsLabel;
    @FXML
    private Label playerDeltaPointsLabel;

    @FXML
    private Button nextTurnBtt; // Button to go to the next turn
    @FXML
    private Label turnLabel; // Turn number label
    @FXML
    private Label timeLabel; // Timer label

    // We are formatting a time duration and not a date,
    // but this works for our purposes (the hour value gets set
    // automatically to 1 AM for some reason, but we do not show that)
    private final SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
    private Timeline timerTimeline;

    /**
     *  This method is called automatically by JavaFX
     */
    public void initialize() {
        // JavaFX uses reflection magic to access the controller
        // and inject the FXML fields, so creating a new static
        // instance will not work.
        instance = this;
        setSceneHolder(deckBox); // Arbitrarily set the node to switch the scene later (it could be set to any node)
        CardAssets.load(); // Load card assets

        GameLogic game = GameLogic.getInstance();
        game.start();

        // Hide the "Next turn" button initially
        setNextButtonVisibility(false);

        // Fill the players' hands
        game.getPlayers().forEach(this::updateHand);

        // Setup the deckbox's minimum size
        deckBox.setMinSize(CardComponent.CARD_WIDTH, CardComponent.CARD_HEIGHT);

        // Fill the deck box with downwards-facing CardComponents
        List<CardComponent> cardComponents = game.getRemainingCards().stream()
                .map(card -> new CardComponent(card, false, true))
                .collect(Collectors.toCollection(ArrayList::new));
        // Get the last card, aka our trump card
        // and make it visible by rotating it horizontally
        CardComponent trumpCard = cardComponents.getLast();
        trumpCard.rotateHorizontally();
        trumpCard.flip();

        // The first elements of the list are placed at the bottom
        // of the StackPane, so we need to reverse it
        Collections.reverse(cardComponents);

        deckBox.getChildren().addAll(cardComponents);
    }

    @FXML
    public void onNextClicked(ActionEvent event) {
        setNextButtonVisibility(false);
        GameLogic.getInstance().nextTurn();
    }

    /**
     * Enables or disables the button using the {@code visibility} parameter
     * passed to this method.
     */
    public void setNextButtonVisibility(boolean visibility) {
        nextTurnBtt.setVisible(visibility);
        nextTurnBtt.setDisable(!visibility);
    }

    public void updateHandStatus(boolean disable) {
        playerBox.setDisable(disable);
    }

    // Used to update the time every second
    public void updateTurnLabel(int turns) {
        turnLabel.setText("Turno #" + turns);
    }

    public void showDeltaPoints(boolean player, int points) {
        Label label = player ? playerDeltaPointsLabel : opponentDeltaPointsLabel;
        double deltaY = player ? 25 : -25;

        label.setText("+" + points);
        label.setVisible(true);

        Timeline animation = new Timeline();
        // Slowly decrease the opacity of the label
        KeyFrame fadeOut = new KeyFrame(
                Duration.millis(DELTA_LABEL_ANIMATION_DURATION),
                new KeyValue(label.opacityProperty(), 0D)
        );
        // Slowly move the label upwards or downwards
        // depending on the label that was chosen
        KeyFrame translate = new KeyFrame(
                Duration.millis(DELTA_LABEL_ANIMATION_DURATION),
                new KeyValue(label.translateYProperty(), deltaY)
        );
        // Add the keyframes to the animation
        animation.getKeyFrames().addAll(fadeOut, translate);
        // Reset the label's opacity and visibility
        // at the end of the animation
        animation.setOnFinished(event -> {
            label.setOpacity(1D);
            label.setVisible(false);
            label.translateYProperty().set(0D);
        });
        animation.play(); // Start the animation
    }

    /**
     * Updates the points label of the player or the opponent.
     *
     * @param opPoints     the points of the bot
     * @param playerPoints the points of the player
     */
    public void updatePointsLabel(int opPoints, int playerPoints) {
        if (opPoints == 0) {
            playerPointsLabel.setText("Punti: " + playerPoints);
        } else {
            opponentPointsLabel.setText("Punti: " + opPoints);
        }
    }

    /**
     * Update the HBox corresponding to the player's hand
     *
     * @param player {@link IPlayer} object who needs their hand updated
     */
    public void updateHand(IPlayer player) {
        // We need to know which HBox to update
        HBox box = player.isBot() ? opponentBox : playerBox;
        box.getChildren().clear(); // Remove every child

        // Map the ICards to CardComponents...
        List<CardComponent> cardComponents = player.getHand().stream()
                .map(card -> new CardComponent(card, !player.isBot(), player.isBot()))
                .collect(Collectors.toCollection(ArrayList::new));

        // ...and add them to the HBox
        box.getChildren().addAll(cardComponents);
    }

    public void addPlayedCard(ICard card) {
        playedCardsBox.getChildren().add(new CardComponent(card, false));
    }

    /**
     * Removes the last x of cards from the deckBox,
     * aka the cards that are visible.
     *
     * @param amount the amount of cards to remove
     */
    public void popDeckCards(int amount) {
        // If the deckBox has more children than the amount of cards
        // this should never happen, but better safe than sorry
        int childrenNumber = deckBox.getChildren().size();
        if (childrenNumber > amount) {
            // We are using a method exclusive to the ObservableList object:
            // https://openjfx.io/javadoc/21/javafx.base/javafx/collections/ObservableList.html#remove(int,int)

            // We need to remove the last elements in the list,
            // because they are the ones visible
            deckBox.getChildren().remove(childrenNumber - amount, childrenNumber);
        } else {
            deckBox.getChildren().clear();
        }
    }

    public void startTimer() {
        // You never know...
        if (timerTimeline != null) timerTimeline.stop();

        long startTime = System.currentTimeMillis();
        // Every second, update the timeLabel with the elapsed time
        timerTimeline = new Timeline(new KeyFrame(
                Duration.seconds(1D),
                event -> {
                    long elapsed = System.currentTimeMillis() - startTime;
                    timeLabel.setText(sdf.format(new Date(elapsed)));
                }
        ));
        timerTimeline.setCycleCount(Animation.INDEFINITE); // loop forever
        timerTimeline.play(); // start the loop
    }

    public void stopTimer() {
        if (timerTimeline != null) timerTimeline.stop();
    }

    public void clearTable() {
        opponentBox.getChildren().clear();
        playerBox.getChildren().clear();
        playedCardsBox.getChildren().clear();
    }
}
