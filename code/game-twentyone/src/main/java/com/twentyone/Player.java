package com.twentyone;

public class Player {
    
    private String name;
    private int score;
    private ThrowType currentThrowType;

    public static enum ThrowType {free, rebound};

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.currentThrowType = ThrowType.free;
    }


    public String getName() {
        return name;
    }


    public int getScore() {
        return score;
    }


    public void setName(String name) {
        this.name = name;
        
    }


    public void setScore(int score) {
        this.score = score;
    }

    public void incrementScore(int points) {
        score += points;
    }


    public ThrowType getCurrentThrowType() {
        return currentThrowType;
    }


    public void setCurrentThrowType(ThrowType currentThrowType) {
        this.currentThrowType = currentThrowType;
    }


    
}
