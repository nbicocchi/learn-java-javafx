package us.teamronda.briscola.api.game;

import us.teamronda.briscola.api.cards.ICard;
import us.teamronda.briscola.api.player.IPlayer;

/**
 * This interface represents a simple game loop
 */
public interface GameLoop {

    /**
     * This method starts the game
     */
    void start();

    /**
     * This method stops the game
     * and prepares for the next game
     */
    void stop();

    /**
     * This method checks if the game is ongoing
     * @return {@code true} if the game should continue, {@code false} otherwise
     */
    boolean isGameOngoing();

    /**
     * This method represents a single turn of a player
     * @param player {@link IPlayer} who is playing
     * @param playedCard {@link ICard} selected by the player
     */
    void tick(IPlayer player, ICard playedCard);

    /**
     * This method returns the player we are waiting for
     * to advance to the next tick
     * @return {@link IPlayer} object
     */
    IPlayer getWhoIsPlaying();
}
