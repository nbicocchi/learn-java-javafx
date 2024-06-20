package us.teamronda.briscola.api.deck;

import us.teamronda.briscola.api.cards.ICard;

/**
 * This interface defines the methods needed
 * for a generic deck of cards
 */
public interface IDeck {

    /**
     * This method initializes the deck
     */
    void create();

    /**
     * This method shuffles the deck.
     * Mostly here if a custom implementation of
     * a shuffle algorithm is needed.
     */
    void shuffle();

    /**
     * @return the number of cards in the deck currently
     */
    int getCardsRemaining();

    /**
     * @return true is the deck has no cards
     */
    boolean isEmpty();

    /**
     * Adds a card at the top of the deck.
     * Internally calls {@link #addCard(ICard, int)}
     * @param card {@link ICard} object
     */
    default void addCardToTop(ICard card) {
        addCard(card, 0);
    }

    /**
     * Adds a card at the bottom of the deck.
     * Internally calls {@link #addCard(ICard, int)}
     * @param card {@link ICard} object
     */
    default void addCardToBottom(ICard card) {
        addCard(card, getCardsRemaining());
    }

    /**
     * Adds a card to the deck at a specified index
     * @param index index an integer between 0 and {@link #getCardsRemaining()} (inclusive)
     * @throws IllegalArgumentException if the {@code index} does not respect the constraints defined above
     */
    void addCard(ICard card, int index);

    /**
     * Reads a card in a certain position from the deck without removing it.
     * @param index an integer between 0 and {@link #getCardsRemaining()} - 1  (inclusive)
     * @return a {@link ICard} object or {@code null} if the deck is empty
     * @throws IllegalArgumentException if the {@code index} does not respect the constraints defined above
     */
    ICard peekCard(int index);

    /**
     * Pops a card from the top of the deck
     * @return {@link ICard} object
     */
    default ICard popCardFromTop() {
        return popCard(0);
    }

    /**
     * Pops a card from the top of the deck
     * @param index an integer between 0 and {@link #getCardsRemaining()} - 1  (inclusive)
     * @return {@link ICard} object or {@code null} if the deck is empty
     * @throws IllegalArgumentException if the {@code index} does not respect the constraints defined above
     */
    ICard popCard(int index);
}
