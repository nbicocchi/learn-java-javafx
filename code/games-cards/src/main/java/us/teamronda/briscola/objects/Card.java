package us.teamronda.briscola.objects;

import us.teamronda.briscola.api.cards.CardType;
import us.teamronda.briscola.api.cards.ICard;
import us.teamronda.briscola.api.cards.Seed;

/**
 * Object representing a playing card
 *
 * @param type Number or figure of the card
 * @param seed Seed of the card
 */
public record Card(CardType type, Seed seed) implements ICard {

    @Override
    public CardType getType() {
        return type;
    }

    @Override
    public Seed getSeed() {
        return seed;
    }

    @Override
    public int getPoints() {
        return switch (type) {
            case ASSO -> 11;
            case TRE -> 10;
            case FANTE -> 2;
            case CAVALLO -> 3;
            case RE -> 4;
            default -> 0;
        };
    }
}
