package us.teamronda.briscola.api.player;

import lombok.AccessLevel;
import lombok.Getter;
import us.teamronda.briscola.api.cards.ICard;
import us.teamronda.briscola.objects.Deck;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class AbstractPlayer implements IPlayer {

    // These are default values related to our implementation
    private static final int DEFAULT_SIZE_HAND = 3;
    private static final int DEFAULT_STARTING_POINTS = 0;

    private final String username;
    @Getter
    private final List<ICard> hand;

    // Values used in overridden methods
    @Getter(AccessLevel.NONE)
    private final boolean bot;
    @Getter(AccessLevel.NONE)
    protected int points;

    public AbstractPlayer(String username) {
        this(username, false);
    }

    public AbstractPlayer(String username, boolean bot) {
        this.username = username;
        this.bot = bot;

        this.hand = new ArrayList<>(DEFAULT_SIZE_HAND);
        this.points = DEFAULT_STARTING_POINTS;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void addCard(ICard card) {
        if (card != null) hand.add(card);
    }

    @Override
    public ICard pollCard(int index) {
        if (index < 0 || index >= hand.size()) {
            throw new IllegalArgumentException("The card index is invalid!");
        }

        return hand.remove(index);
    }

    @Override
    public void pollCard(ICard card) {
        if (card == null) {
            throw new IllegalArgumentException("The card cannot be null!");
        }

        hand.remove(card);
    }

    @Override
    public void fillHand(Deck deck) {
        // Some small checks to avoid looping if unnecessary
        if (deck == null || deck.isEmpty()) return;

        // If we do not store the initial size of the hand,
        // it will change when we add cards!
        int initialSize = hand.size();

        for (int i = 0; i < DEFAULT_SIZE_HAND - initialSize; i++) {
            addCard(deck.popCardFromTop());
        }
    }

    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public boolean isBot() {
        return bot;
    }

    @Override
    public String toString() {
        return "Player{" +
                "username=" + username + ", " +
                "points=" + points + ", " +
                "hand=" + hand +
                '}';
    }
}