package com.twentyone;

/**
 * manage the physic ball
 */
public class Ball {
    
    private Coord pos;
    private Coord direction;//contain the direction and the velocity
    private boolean throwing;//if the ball is being thrown and it is moving 
    private double radius;

    public static final double FRICTION_COEFF = 0.4;
    public static final Coord GRAVITY_FORCE = new Coord(0, -300);


    public Ball(Coord pos, double radius) {
        this.pos = pos;
        this.radius = radius;
        this.direction = new Coord(0, 0);
        this.throwing = false;
    }

    public void setThrowing(boolean throwing) {
        this.throwing = throwing;
    }

    public boolean getThrowing() {
        return this.throwing;
    }

    public Coord getPos() {
        return pos;
    }

    public void setPos(Coord pos) {
        this.pos = pos;
    }

    public Coord getDirection() {
        return direction;
    }


    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public Coord getCenter() {
        return new Coord(pos.x + radius, pos.y + radius);
    }

    public void setDirection(Coord direction) {
        this.direction = direction;
    }



    /**
     * manage the physic and the movement of the ball
     */
    public void applyForce(double deltatime, Collision collision) {
        if (collision != null) {
            if (collision.type == Collision.SurfaceName.floor)
                direction.invertY();
            else
                direction.invertX();

            direction = direction.multiply(1 - FRICTION_COEFF);//apply friction
            if (direction.norm() < 0.01)
                direction = new Coord();//stops the ball
        }
        direction.y -= GRAVITY_FORCE.y * deltatime;//apply gravity
        pos = pos.sum(direction.multiply(deltatime));//move the ball



    }

}
