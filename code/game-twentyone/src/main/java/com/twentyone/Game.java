package com.twentyone;

/**
 * handles the physics in the game
 */
public class Game {
    
    public final static double STOP_SPEED = 10;//speed where the ball change state to throwing
    public final static double THROW_HEIGHT = 150;//height where the ball spawns, ready to be thrown
    public final static double BASKET_RADIUS = 1;//radius of the basket's collider

    private Ball ball;
    private Player player1;
    private Player player2;
    private Player playingPlayer;//current player playing this turn
    private boolean terminated;//closes the game and the thread
    private boolean prevCollision;
    private boolean currentTurnBasketMade;
    private Coord basketCollider;
    private Coord freeThrowPos;
    private int pointsNumber;//points to win





    public Game(Player player1, Player player2, Ball ball, int pointsNumber) {
        this.player1 = player1;
        this.player2 = player2;
        this.playingPlayer = player1;
        this.ball = ball;
        this.pointsNumber = pointsNumber;
        this.terminated = false;
        this.currentTurnBasketMade = false;
        this.prevCollision = false;
        this.basketCollider = null;
    }

    public Ball getBall() {
        return ball;
    }

    


    public void setBasketCollider(Coord basketCollider) {
        this.basketCollider = basketCollider;
    }

    

    public Coord getFreeThrowPos() {
        return freeThrowPos;
    }

    public void setFreeThrowPos(Coord freeThrowPos) {
        this.freeThrowPos = freeThrowPos;
    }

    public boolean isTerminated() {
        return terminated;
    }

    public void terminate(){
        terminated = true;
    }

    public void nextTurn(){
        if (playingPlayer.getScore() >= pointsNumber) {//player wins
            terminate();          
        }
        else{
            if(currentTurnBasketMade){//player continue playing from free throws
                ball.setPos(freeThrowPos);
                playingPlayer.setCurrentThrowType(Player.ThrowType.free);
            }
            else{//next player plays, from rebound
                playingPlayer = (playingPlayer==player1 ? player2 : player1);
                ball.setPos(ball.getPos().sum(new Coord(0, -THROW_HEIGHT)));
                playingPlayer.setCurrentThrowType(Player.ThrowType.rebound);
            }
            currentTurnBasketMade=false;
        }
    }

    public void noteBucket(){//increment the score of a player
        int pointsToAdd = (playingPlayer.getCurrentThrowType()==Player.ThrowType.free ? 2 : 1);
        playingPlayer.incrementScore(pointsToAdd);
        currentTurnBasketMade = true;
    }


    public Collision checkCollision(Coord gameSize) {
        Coord center = ball.getCenter();
        if (center.x - 0 <= ball.getRadius() || Math.abs(center.x - gameSize.x)  <= ball.getRadius()) {//check left and right walls
            return new Collision(Collision.SurfaceName.wall);
        }
        if (Math.abs(center.y - gameSize.y) <= ball.getRadius()) {//check the floor
            return new Collision(Collision.SurfaceName.floor);
        }
        Coord distanceVector = ball.getCenter().sum(basketCollider.multiply(-1));
        if(distanceVector.norm() <= BASKET_RADIUS+ball.getRadius()){//check the basket collider
            double deltaX = Math.abs(distanceVector.x);
            double deltaY = Math.abs(distanceVector.y);
            return new Collision((deltaX>deltaY ? Collision.SurfaceName.wall : Collision.SurfaceName.floor));//how the ball bounces off
        }
        return null;
    }
    

    /**
     * handle one physic cycle
     */
    public void runLifeCycle(double deltatime, Coord gameSize) {
        if (ball.getThrowing()) {

            //prevents immediatly future collision with the same object
            Collision collision = checkCollision(gameSize);
            if(prevCollision && collision != null)
                collision = null;
            else if(prevCollision){
                prevCollision = false;
            }
            else if(collision != null){
                prevCollision = true;
            }

            

            Coord oldBallPos = ball.getCenter();
            ball.applyForce(deltatime, collision);//move the ball
            if(ball.getDirection().norm() < STOP_SPEED && ball.getCenter().y > gameSize.y-60){//if the ball stops and it is near the floor 
                try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}

                ball.setThrowing(false);
                nextTurn();
            }
            if(ball.getCenter().x < basketCollider.x && oldBallPos.x < basketCollider.x &&
               oldBallPos.y < basketCollider.y && ball.getCenter().y > basketCollider.y){//check if the ball has entered the basket
                noteBucket();
            } 
        }

    }      


    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getPlayingPlayer() {
        return playingPlayer;
    }

    public int getPointsNumber() {
        return pointsNumber;
    }

    
}
