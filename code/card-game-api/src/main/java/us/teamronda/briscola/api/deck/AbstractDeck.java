package us.teamronda.briscola.api.deck;

import us.teamronda.briscola.api.cards.ICard;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides some generic methods which
 * can be used in every card game
 */
public abstract class AbstractDeck implements IDeck {

    private static final int DEFAULT_DECK_SIZE = 40;

    // We used a List to take advantage of the built-in
    // Collections#shuffle static method to handle deck shuffling
    protected final List<ICard> cards;

    public AbstractDeck() {
        this.cards = new ArrayList<>(DEFAULT_DECK_SIZE);
    }

    @Override
    public void addCard(ICard card, int index) {
        // Range check on the index
        if (index < 0 || index > getCardsRemaining()) {
            throw new IndexOutOfBoundsException("Index must be between 0 and " + getCardsRemaining());
        }

        cards.add(index, card);
    }

    @Override
    public ICard peekCard(int index) {
        // Range check on the index
        if (index < 0 || index > getCardsRemaining() - 1) {
            throw new IndexOutOfBoundsException("Index must be between 0 and " + (getCardsRemaining() - 1));
        }

        // Return null if there are no cards
        if (isEmpty()) return null;

        return cards.get(index);
    }

    @Override
    public ICard popCard(int index) {
        // Range check on the index
        if (index < 0 || index > getCardsRemaining() - 1) {
            throw new IndexOutOfBoundsException("Index must be between 0 and " + (getCardsRemaining() - 1));
        }
        // Return null if there are no cards
        if (isEmpty()) return null;

        return cards.remove(index);
    }

    /**
     * @return an <b>IMMUTABLE</b> List of cards left in the deck
     */
    public List<ICard> getCards() {
        return List.copyOf(cards);
    }

    /**
     * @return the starting size of the deck
     */
    public int getMaxSize() {
        return DEFAULT_DECK_SIZE;
    }

    @Override
    public int getCardsRemaining() {
        return cards.size();
    }

    @Override
    public boolean isEmpty() {
        return cards.isEmpty();
    }
}

