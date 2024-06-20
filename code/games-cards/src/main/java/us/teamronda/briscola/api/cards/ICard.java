package us.teamronda.briscola.api.cards;

public interface ICard {

    /**
     * Gets the card's figure or number
     *
     * @return a {@link CardType} enum
     */
    CardType getType();


    /**
     * Gets the card's seed
     *
     * @return a {@link Seed} enum
     */
    Seed getSeed();

    /**
     * Every card game has its own unique way to calculate a score.
     * This interface enables other developers to implement their own way
     * to calculate the score of a card.
     *
     * @return an integer representing the points the card is worth
     */
    int getPoints();
}
