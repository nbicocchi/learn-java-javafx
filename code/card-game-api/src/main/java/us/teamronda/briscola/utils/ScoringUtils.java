package us.teamronda.briscola.utils;

import lombok.experimental.UtilityClass;
import us.teamronda.briscola.api.cards.ICard;

import java.util.Collection;

/**
 * Small utility class to hold some scoring-related methods and constants
 */

@UtilityClass
public class ScoringUtils {

    public final int MAX_POINTS = 120;

    /**
     * Calculate the worth of a collection of cards
     *
     * @param cards a {@link Collection} of {@link ICard} objects
     * @return an integer value
     */
    public int calculatePoints(Collection<ICard> cards) {
        return cards.stream().mapToInt(ICard::getPoints).sum();
    }
}
