package us.teamronda.briscola.objects;

import lombok.Getter;
import us.teamronda.briscola.api.cards.CardType;
import us.teamronda.briscola.api.cards.ICard;
import us.teamronda.briscola.api.cards.Seed;
import us.teamronda.briscola.api.deck.AbstractDeck;

import java.util.Collections;

@Getter
public class Deck extends AbstractDeck {

    private ICard trumpCard;

    @Override
    public void create() {
        // Remove any cards from the previous game
        cards.clear();

        // Populate the deck
        for (CardType type : CardType.values()) {
            for (Seed seed : Seed.values()) {
                cards.add(new Card(type, seed));
            }
        }

        this.shuffle();

        trumpCard = popCardFromTop();
        cards.addLast(trumpCard);
    }

    @Override
    public void shuffle() {
        Collections.shuffle(cards);
    }

    /**
     * Checks if a card has the same seed of the
     * trump card.
     *
     * @param card Card object to check
     */
    public boolean hasTrumpSeed(ICard card) {
        return trumpCard != null && trumpCard.getSeed().equals(card.getSeed());
    }
}
