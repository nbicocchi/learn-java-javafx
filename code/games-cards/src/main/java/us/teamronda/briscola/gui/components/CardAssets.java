package us.teamronda.briscola.gui.components;

import javafx.scene.image.Image;
import lombok.experimental.UtilityClass;
import us.teamronda.briscola.api.cards.CardType;
import us.teamronda.briscola.api.cards.ICard;
import us.teamronda.briscola.api.cards.Seed;

import java.util.HashMap;
import java.util.Map;

/**
 * This class handles all the cards' assets
 */

@UtilityClass
public class CardAssets {

    // The path where all the images are stored
    private final String CARD_ASSETS_PATH = "/assets/cards/";

    // This image reprensents the back of a card
    public final Image BACK = new Image(CardAssets.class.getResource(CARD_ASSETS_PATH + "back.png").toString());
    // This map holds all the cards' images
    // The key is the card's id (e.g. "asso_bastoni"), calculated using CardAssets#getId
    private final Map<String, Image> CARDS_IMAGES = new HashMap<>();

    /**
     * Load all cards' assets in one go
     */
    public void load() {
        // Just loop over all the possible combinations of seeds and types
        // and load the corresponding image
        for (Seed seed : Seed.values()) {
            for (CardType type : CardType.values()) {
                String id = getId(seed, type);
                CARDS_IMAGES.put(id, new Image(CardAssets.class.getResource(CARD_ASSETS_PATH + id + ".png").toString()));
            }
        }
    }

    /**
     * Get the image representation of a card
     *
     * @param card {@link ICard} object
     * @return an {@link Image} object
     */
    public Image getCardImage(ICard card) {
        return CARDS_IMAGES.get(getId(card.getSeed(), card.getType()));
    }

    private String getId(Seed seed, CardType type) {
        return type.name().toLowerCase() + "_" + seed.name().toLowerCase();
    }
}
