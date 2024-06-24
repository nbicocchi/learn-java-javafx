package com.twentyone;

/**
 * rapresent the collision type
 */
public class Collision {
    
    public static enum SurfaceName {floor, wall};


    public SurfaceName type;

    public Collision(SurfaceName type) {
        this.type = type;
    }
    
}
