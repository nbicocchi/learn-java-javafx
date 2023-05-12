package com.nbicocchi.javafx.games.common;

public class PVector {
    public static PVector ZERO = new PVector(0, 0);
    /**
     * The x component of the vector.
     */
    public double x;
    /**
     * The y component of the vector.
     */
    public double y;
    /**
     * The z component of the vector.
     */
    public double z;

    /**
     * Constructor for an empty vector: x, y, and z are set to 0.
     */
    public PVector() {
    }

    /**
     * Constructor for a 2D vector: z coordinate is set to 0.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     */
    public PVector(double x, double y) {
        this.x = x;
        this.y = y;
        this.z = 0;
    }

    /**
     * Constructor for a 3D vector.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param z the y coordinate.
     */
    public PVector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Add two vectors
     *
     * @param v1 a vector
     * @param v2 another vector
     *
     * @return a new vector that is the sum of v1 and v2
     */
    static public PVector add(PVector v1, PVector v2) {
        return v1.add(v2);
    }

    /**
     * Add a vector to this vector
     *
     * @param v the vector to be added
     */
    public PVector add(PVector v) {
        return new PVector(x + v.x, y + v.y, z + v.z);
    }

    /**
     * Multiply a vector by a scalar
     *
     * @param v a vector
     * @param n scalar
     *
     * @return a new vector that is v1 * n
     */
    static public PVector multiply(PVector v, double n) {
        return v.multiply(n);
    }

    /**
     * Multiply this vector by a scalar
     *
     * @param n the value to multiply by
     */
    public PVector multiply(double n) {
        return new PVector(x * n, y * n, z * n);
    }

    /**
     * Calculate the Euclidean distance between two points (considering a point as a vector object)
     *
     * @param v1 a vector
     * @param v2 another vector
     *
     * @return the Euclidean distance between v1 and v2
     */
    static public double distance(PVector v1, PVector v2) {
        return v1.distance(v2);
    }

    /**
     * Calculate the Euclidean distance between two points (considering a point as a vector object)
     *
     * @param v another vector
     *
     * @return the Euclidean distance between
     */
    public double distance(PVector v) {
        double dx = x - v.x;
        double dy = y - v.y;
        double dz = z - v.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }

    static public double dot(PVector v1, PVector v2) {
        return v1.dot(v2);
    }

    /**
     * Calculate the dot product with another vector
     *
     * @return the dot product
     */
    public double dot(PVector v) {
        return x * v.x + y * v.y + z * v.z;
    }

    static public PVector cross(PVector v1, PVector v2) {
        return v1.cross(v2);
    }

    /**
     * Return a vector composed of the cross product between this and another.
     */
    public PVector cross(PVector v) {
        double crossX = y * v.z - v.y * z;
        double crossY = z * v.x - v.z * x;
        double crossZ = x * v.y - v.x * y;
        return new PVector(crossX, crossY, crossZ);
    }

    /**
     * Calculate the angle between two vectors, using the dot product
     *
     * @param v1 a vector
     * @param v2 another vector
     *
     * @return the angle between the vectors
     */
    static public double angleBetween(PVector v1, PVector v2) {
        double dot = v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
        double v1mag = Math.sqrt(v1.x * v1.x + v1.y * v1.y + v1.z * v1.z);
        double v2mag = Math.sqrt(v2.x * v2.x + v2.y * v2.y + v2.z * v2.z);
        return Math.acos(dot / (v1mag * v2mag));
    }

    /**
     * Set x, y, and z coordinates.
     *
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param z the z coordinate.
     */
    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Set x, y, and z coordinates from a Vector3D object.
     *
     * @param v the PVector object to be copied
     */
    public void set(PVector v) {
        x = v.x;
        y = v.y;
        z = v.z;
    }

    public PVector add(double x, double y, double z) {
        return new PVector(this.x + x, this.y + y, this.z + z);
    }

    public double dot(double x, double y, double z) {
        return this.x * x + this.y * y + this.z * z;
    }

    /**
     * Limit the magnitude of this vector
     *
     * @param max the maximum length to limit this vector
     */
    public PVector limit(double max) {
        if (magnitude() < max) {
            return copy();
        }
        return normalize().multiply(max);
    }

    /**
     * Calculate the magnitude (length) of the vector
     *
     * @return the magnitude of the vector
     */
    public double magnitude() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * Get a copy of this vector.
     */
    public PVector copy() {
        return new PVector(x, y, z);
    }

    /**
     * Normalize the vector to length 1 (make it a unit vector)
     */
    public PVector normalize() {
        double magnitude = magnitude();
        if (magnitude == 0 || magnitude == 1) {
            return copy();
        }
        return multiply(1 / magnitude);
    }

    /**
     * Calculate the angle of rotation for this vector (only 2D vectors)
     *
     * @return the angle of rotation
     */
    public double heading2D() {
        double angle = Math.atan2(-y, x);
        return -1 * angle;
    }

    @Override
    public String toString() {
        return "PVector{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }
}


