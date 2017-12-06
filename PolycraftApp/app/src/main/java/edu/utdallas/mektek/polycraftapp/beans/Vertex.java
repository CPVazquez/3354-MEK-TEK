package edu.utdallas.mektek.polycraftapp.beans;

import android.graphics.drawable.Drawable;

import net.xqhs.graphs.graph.Node;

//import edu.utdallas.mektek.polycraftapp.*;

/**
 * The type Vertex.
 */
public class Vertex {

    private Node node;

    //public SuperNode node;

    private Point2D position; //carla added

    private Drawable icon; //not carla added

    private String id;

    private boolean isRecipe;

    public String getId() {
        return id;
    }

    public void setPosition(Point2D point){
        this.position = point;
    }

    public Point2D getPosition(){
        return position;
    }
    //end carla added

    /**
     * Instantiates a new Vertex.
     *
     * @param node the node
     * @param icon the icon
     */
    public Vertex(final Node node, final Drawable icon, final String iden) {
        this.node = node;
        this.icon = icon;
        this.id = iden;
        this.isRecipe = false;
    }

    public Vertex(final Node node, final Drawable icon, final String iden, boolean isRec){
        this.node = node;
        this.icon = icon;
        this.id = iden;
        this.isRecipe = isRec;
    }

    /**
     * Gets node.
     *
     * @return the node
     */
    public Node getNode() {
        return node;
    }

    /**
     * Sets node.
     *
     * @param node the node
     */
    public void setNode(final Node node) {
        this.node = node;
    }

    /**
     * Gets icon.
     *
     * @return the icon
     */
    public Drawable getIcon() {
        return icon;
    }

    /**
     * Sets icon.
     *
     * @param icon the icon
     */
    public void setIcon(final Drawable icon) {
        this.icon = icon;
    }

    public boolean isRecipe(){
        return this.isRecipe;
    }
}