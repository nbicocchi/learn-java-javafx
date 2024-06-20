package com.marduini.javafx.tictactoeAI.model;

import java.util.Arrays;

public class State {
    // This is the class used to represent the states of the board, with a string array
    private int position;
    private String[] state;

    public State(int position, String[] state) {
        this.position = position;
        this.state = state;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String[] getState() {
        return state;
    }

    /**
     * returns the state of the i-pane of the current state (String[] state)
     * @param i the i-pane which we want to take the fill of the board
     * @return returns the state of the i-pane of the current state
     */
    public String getStateIndex(int i){
        return state[i];
    }

    public void setState(String[] state) {
        this.state = state;
    }

    public void changeState(int i, String player){
        state[i] = player;
    }

    @Override
    public String toString() {
        return "State{" +
                "position=" + position +
                ", state=" + Arrays.toString(state) +
                '}';
    }
}
