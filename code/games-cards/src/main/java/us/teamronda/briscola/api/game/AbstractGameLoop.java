package us.teamronda.briscola.api.game;

import us.teamronda.briscola.api.cards.ICard;
import us.teamronda.briscola.api.player.AbstractPlayer;
import us.teamronda.briscola.api.player.IPlayer;
import us.teamronda.briscola.objects.Deck;

import java.util.*;

public abstract class AbstractGameLoop implements GameLoop {

    // Technically we could have used a set for players,
    // but we would not have been able to use
    // Collections#shuffle and to guarantee the player order.
    private final List<IPlayer> players;

    protected Map<String, ICard> cardsPlayed;
    protected int playerIndex;

    public AbstractGameLoop() {
        this.players = new ArrayList<>();
        this.cardsPlayed = new HashMap<>();
        this.playerIndex = 0;
    }

    /**
     * This method returns a boolean for convenience purposes:
     * if false is returned then the username of the player needs to be changed.
     *
     * @param player {@link IPlayer} object to be added
     * @return true if the underlying set of players was modified
     */
    public boolean addPlayer(IPlayer player) {
        if (isUsernameDuplicate(player.getUsername())) return false;

        return players.add(player);
    }

    /**
     * Removes a player from the game
     *
     * @param player {@link IPlayer} to be removed
     */
    public void removePlayer(IPlayer player) {
        players.remove(player);
    }

    @Override
    public IPlayer getWhoIsPlaying() {
        if (playerIndex < players.size()) {
            return players.get(playerIndex);
        } else {
            return null;
        }
    }

    private boolean isUsernameDuplicate(String username) {
        return players.stream().anyMatch(player -> player.getUsername().equalsIgnoreCase(username));
    }

    /**
     * Gets the players in the game
     *
     * @return An <b>IMMUTABLE</b> List of players
     */
    public List<IPlayer> getPlayers() {
        return List.copyOf(players);
    }

    /**
     * Returns how many players are participating in the game
     */
    public int getPlayerCount() {
        return players.size();
    }

    /**
     * Utility method that fills the players' hand until
     * the limit set in {@link AbstractPlayer} is met
     *
     * @param deck The {@link us.teamronda.briscola.api.deck.IDeck} to draw from
     */
    public void fillHands(Deck deck) {
        if (deck.isEmpty()) return;

        for (IPlayer player : players) {
            player.fillHand(deck);
        }
    }

    /**
     * Used at game start to select a starting playing order
     */
    public void shufflePlayers() {
        Collections.shuffle(players);
    }

    /**
     * Make the winner play and keep the rest of the turn order
     *
     * @param winner The {@link IPlayer} that won the round
     * @throws IllegalArgumentException when the {@code winner} is not in the players list
     */
    public void orderPlayers(IPlayer winner) {
        int winnerIndex = players.indexOf(winner);
        if (winnerIndex == -1) {
            throw new IllegalArgumentException("The winner has to be in the players list!");
        }

        for (int i = 0; i < winnerIndex; i++) {
            players.addLast(players.removeFirst());
        }
    }
}
