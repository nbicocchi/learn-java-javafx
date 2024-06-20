package com.marduini.javafx.tictactoeAI.model;

import java.util.ArrayList;

public class Game {
    /***
     * This method picks the best move for the O-player in our game
     * @param state the current state of the board, at turn of the AI
     * @return the best move for the O-player(Maximizer) in order to win the current game (integer between 0-8)
     */
    public int minMaxDecision(State state){
        ArrayList<State> possibleMoves = successorsOf(state);   // The list of the possible states associated with the possible moves from the given state
        ArrayList<Integer> movesList = new ArrayList<>();       // The list of the values associated to the possible moves

        // For each substate of the current state, we add to the movesList the value for the O-player
        for (State states: possibleMoves) {
            movesList.add(minValue(states));
        }

        int max = movesList.getFirst();
        int bestIndex = 0;

        for (int i = 1; i < movesList.size(); i++) {

            if( movesList.get(i) > max){
                max = movesList.get(i);
                bestIndex = i;
            }
        }
        return possibleMoves.get(bestIndex).getPosition();
    }

    /**
     * Picks best option for the O-player(AI) - Maximizer player
     */
    private int maxValue(State state){
        if (isTerminal(state)){
            return utilityOf(state);
        }
        int v = (int) -Double.POSITIVE_INFINITY;

        for (State possibleMove: successorsOf(state)) {
            v = Math.max(v, minValue(possibleMove));
        }
        return v;
    }

    // In this case the player X will be a user, but the AI assumes that the user will always choose the perfect move
    /**
     *  Picks best option for the X-player(USER) - Minimizer player
     */
    private int minValue(State state){
        if (isTerminal(state)){
            return utilityOf(state);            // If the current state is terminal, minValue returns the value of the final state
        }

        int v = (int) Double.POSITIVE_INFINITY;
        for (State possibleMove: successorsOf(state)) {

            v = Math.min(v, maxValue(possibleMove));        // Search for the best move relating to the best move for the opponent
        }
        return v;
    }

    /**
     * Returns true if the state passed is terminal, so there aren't moves available
     * @param state current state of the board
     * @return boolean
     */
    public boolean isTerminal(State state) {
        int takenSpots = 0;     // Contains the number of spots that are taken in the current state
        for (int a = 0; a < 9; a++) {
            if(state.getStateIndex(a).equals("X") || state.getStateIndex(a).equals("O") ){
                takenSpots++;
            }

            // Check if the state is a win for a player or not
            String line = checkState(state, a);

            //Check for Winners
            if (line.equals("XXX")) {
                return true;
            } else if (line.equals("OOO")) {
                return true;
            }

            // Note that this is the check for a tie, because every spot is taken but nobody win!
            if(takenSpots == 9){
                return true;
            }
        }
        return false;
    }

    /***
     * returns 1 if player O(AI) wins, -1 if player X(USER) wins, 0 otherwise
     * @param state the current state of the board
     * @return the integer value of the play
     */
    private int utilityOf(State state){
        for (int a = 0; a < 8; a++) {
            String line = checkState(state, a);
            // Check if a player win
            if (line.equals("OOO")) {
                return 1;
            } else if (line.equals("XXX")) {
                return -1;
            }
        }
        return 0;
    }

    /**
     * Returns the state of the line a passed in order to control if it leads to a win
     * @param state the current state of the board
     * @param a the line in which check the win
     * @return the string with the board's symbols in the line checked
     */
    private String checkState(State state, int a) {
        return switch (a) {
            case 0 -> state.getStateIndex(0) + state.getStateIndex(1) + state.getStateIndex(2);
            case 1 -> state.getStateIndex(3) + state.getStateIndex(4) + state.getStateIndex(5);
            case 2 -> state.getStateIndex(6) + state.getStateIndex(7) + state.getStateIndex(8);
            case 3 -> state.getStateIndex(0) + state.getStateIndex(3) + state.getStateIndex(6);
            case 4 -> state.getStateIndex(1) + state.getStateIndex(4) + state.getStateIndex(7);
            case 5 -> state.getStateIndex(2) + state.getStateIndex(5) + state.getStateIndex(8);
            case 6 -> state.getStateIndex(0) + state.getStateIndex(4) + state.getStateIndex(8);
            case 7 -> state.getStateIndex(2) + state.getStateIndex(4) + state.getStateIndex(6);
            default -> "";
        };
    }

    /**
     * Returns all the possible (sub)state for the current state passed
     * @param state the current state of the game's board
     * @return An ArrayList of States
     */
    private ArrayList<State> successorsOf(State state){
        ArrayList<State> possibleMoves = new ArrayList<>();
        int xMoves = 0;
        int yMoves = 0;
        String player;

        // We are calculating the players' turn
        for (String s: state.getState()) {
            if (s.equals("X")) {
                xMoves++;
            }else if(s.equals("O")){
                yMoves++;
            }
        }

        // If we have less X symbols, it's turn of player X, otherwise it's turn of player O
        if(xMoves <= yMoves){
            player = "X";
        } else {
            player = "O";
        }

        // Create all possible successive states for the current state passed
        for (int i = 0; i < 9; i++) {
            String[] newState = state.getState().clone();

            if(newState[i].compareTo("X") != 0 && newState[i].compareTo("O") != 0){
                newState[i] = player;
                possibleMoves.add(new State(i, newState));
            }
        }
        return possibleMoves;
    }
}
