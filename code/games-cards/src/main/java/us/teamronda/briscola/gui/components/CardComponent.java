package us.teamronda.briscola.gui.components;

import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import us.teamronda.briscola.GameLogic;
import us.teamronda.briscola.api.cards.ICard;
import us.teamronda.briscola.api.player.IPlayer;
import us.teamronda.briscola.gui.controllers.TableController;

/**
 * This custom JavaFX component represents a card in the game.
 * It is composed of two sides: the front and the back {@link Rectangle Rectangles}, held together by a {@link StackPane}.
 * <p>
 * When we need to show only one side of a card, the rectangle representing the other side is scaled to 0
 * using the method {@link Rectangle#setScaleX(double)}. This action is animated using a {@link ScaleTransition}
 * in the {@link CardComponent#flip()} method.
 */
public class CardComponent extends StackPane {

    // Some constants used throughout this class
    private static final long ANIMATION_DURATION = 125L;
    public static final int CARD_WIDTH = 89;
    public static final int CARD_HEIGHT = 168;

    // The front and back of the card
    private final Rectangle front;
    private final Rectangle back;

    // If a flip animation is in progress,
    // we need to wait for it to finish.
    private boolean transitioning;
    // True if the card is facing down
    private boolean obscured;
    // True if the card is clickable by the player
    private final boolean playable;

    public CardComponent(ICard card, boolean playable) {
        this(card, playable, false);
    }

    public CardComponent(ICard card, boolean playable, boolean obscured) {
        this.transitioning = false;
        this.playable = playable;
        this.obscured = obscured;

        // Set the card's dimensions
        this.setPrefSize(CARD_WIDTH, CARD_HEIGHT);
        this.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        // Create the front and back of the card
        // and make only one visible
        front = createSide(CardAssets.getCardImage(card));
        back = createSide(CardAssets.BACK);
        if (obscured) {
            front.setScaleX(0);
        } else {
            back.setScaleX(0);
        }
        this.getChildren().addAll(back, front);

        // Do stuff when the card is clicked
        this.setOnMouseClicked(event -> {
            if (!playable) return;

            // Prevent double-clicking
            this.setOnMouseClicked(MouseEvent::consume);

            // Block the handBox of the player:
            // if we do not do so, we are able to play
            // multiple cards even if we have played one already.
            TableController.getInstance().updateHandStatus(true);

            // Get the player who is playing
            IPlayer player = GameLogic.getInstance().getWhoIsPlaying();
            // Remove from their hand the card that was played
            player.pollCard(card);
            // Play the card
            GameLogic.getInstance().tick(player, card);
        });
    }

    public void flip() {
        // Wait for the animation to complete
        // if there is one in progress
        if (transitioning) return;
        transitioning = true;

        /*
        Thank you, Sergey, for allowing me not to work with 3D objects.
        https://stackoverflow.com/a/19896794
        */
        ScaleTransition hideSide = new ScaleTransition(Duration.millis(ANIMATION_DURATION * 2L), obscured ? back : front);
        hideSide.setFromX(1);
        hideSide.setToX(0);

        ScaleTransition showSide = new ScaleTransition(Duration.millis(ANIMATION_DURATION * 2L), obscured ? front : back);
        showSide.setFromX(0);
        showSide.setToX(1);

        showSide.setOnFinished(event -> {
            obscured = !obscured;
            transitioning = false;
        });
        hideSide.setOnFinished(event -> showSide.play());
        hideSide.play();
    }

    /**
     * Rotate the card horizontally,
     * only used for the trump card.
     */
    public void rotateHorizontally() {
        RotateTransition rotation = new RotateTransition(Duration.millis(ANIMATION_DURATION), this);
        rotation.setAxis(new Point3D(0, 0, 1)); // Rotate on the Z axis
        rotation.setByAngle(90);
        rotation.play();
    }

    /**
     * Creates a side of the card
     *
     * @param image {@link Image} to use as the side
     * @return a {@link Rectangle} object
     */
    private Rectangle createSide(Image image) {
        Rectangle rectangle = new Rectangle(CARD_WIDTH, CARD_HEIGHT);
        rectangle.setFill(new ImagePattern(image));
        rectangle.setArcHeight(10D);
        rectangle.setArcWidth(10D);

        // If this card is clickable, we add a hover effect
        if (playable) {
            TranslateTransition animation = new TranslateTransition();
            animation.setNode(this);
            animation.setFromX(this.getLayoutX());
            animation.setFromY(this.getLayoutY());
            animation.setToY(this.getLayoutY() - 10); // Move the card slightly upwards
            animation.setDuration(Duration.millis(ANIMATION_DURATION));

            rectangle.setOnMouseEntered(event -> {
                animation.stop();
                animation.setRate(1);
                animation.play();
            });

            rectangle.setOnMouseExited(event -> {
                animation.stop();
                animation.setRate(-1); // This reverses the animation
                animation.play();
            });
        }

        return rectangle;
    }
}
