package edu.utdallas.mektek.polycraftapp.beans;

/**
 * The type Point 2D.
 * THIS CLASS WAS IMPORTED IN AND REMAINS LARGELY UNCHANGED
 * CHANGES ARE NOTED
 */
public class Point2D {

    protected double x;

    protected double y;

    /**
     * Instantiates a new Point 2 d.
     *
     * @param x the x
     * @param y the y
     */
    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Instantiates a new Point 2 d.
     */
    public Point2D() {
        this.x = 0;
        this.y = 0;
    }

    /**THIS FUNCTION ADDED BY US
     * inRange
     * checks whether a tap on a node is valid
     * @param xTest - a float representing the x coordinate of the tap
     * @param yTest - a float representing the y coordinate of the tap
     * @param range - an int representing the range in which the tap is valid
     * @param scale - when scaled the new scale must be taken into acount
     * @return
     */
    public boolean inRange(float xTest,float yTest,int range, float scale){
        double newRange = range/scale;
        return xTest <= x + newRange && xTest >= x - newRange
                && yTest<= y + 2*newRange && yTest >= y - 1.25*newRange;
    }
    /**
     * Gets x.
     *
     * @return the x
     */
    public double getX() {
        return this.x;
    }

    /**
     * Gets y.
     *
     * @return the y
     */
    public double getY() {
        return this.y;
    }

    /**
     * Sets location.
     *
     * @param x the x
     * @param y the y
     */
    public void setLocation(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets location.
     *
     * @param p the p
     */
    public void setLocation(Point2D p) {
        this.x = p.x;
        this.y = p.y;
    }
}
