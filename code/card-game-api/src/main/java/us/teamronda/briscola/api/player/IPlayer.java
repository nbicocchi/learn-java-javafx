package us.teamronda.briscola.api.player;

import us.teamronda.briscola.api.cards.ICard;
import us.teamronda.briscola.objects.Deck;

import java.util.Collection;
import java.util.List;

/**
 * This interfaces define some generic methods
 * needed for the Player object
 */
public interface IPlayer {

    String getUsername();

    /**
     * Add a card to the player's hand.
     * It fails silently if {@code card} is null.
     *
     * @param card {@link ICard} object
     */
    void addCard(ICard card);

    /**
     * Removes a card from the player's hand
     * at a certain index.
     *
     * @param index an integer between 0 and the size of the hand (exclusive)
     * @return {@link ICard} object
     * @throws IllegalArgumentException if an invalid index is passed to this function
     */
    ICard pollCard(int index);

    /**
     * Removes a card from the player's hand
     *
     * @param card {@link ICard} object to be removed
     * @throws IllegalArgumentException if the card argument is null
     */
    void pollCard(ICard card);

    /**
     * Add cards from a {@code deck} until the player
     * has a full hand.
     *
     * @param deck {@link us.teamronda.briscola.api.deck.IDeck} object to draw from
     */
    void fillHand(Deck deck);

    /**
     * Calculates the cards' worth
     * and adds them to the points of this player.
     *
     * @param cards a Collection of {@link ICard cards}
     * @return an integer representing the worth of the cards
     */
    int addPoints(Collection<ICard> cards);

    /**
     * Calculates the cards' worth
     * and subtracts them to the points of this player.
     *
     * @param cards a Collection of {@link ICard cards}
     * @return an integer representing the worth of the cards
     */
    int subtractPoints(Collection<ICard> cards);

    List<ICard> getHand();

    int getPoints();

    boolean isBot();
}
