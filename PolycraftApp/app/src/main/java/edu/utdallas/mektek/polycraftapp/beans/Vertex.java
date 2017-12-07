package edu.utdallas.mektek.polycraftapp.beans;

import android.graphics.drawable.Drawable;

import net.xqhs.graphs.graph.Node;

/**
 * The type Vertex.
 * THIS CLASS WAS IMPORTED BUT HAS BEEN CHANGED
 * CHANGES ARE NOTED
 */
public class Vertex {

    private Node node;

    private Drawable icon;

    //ADDED START
    private Point2D position; //the Point2D position on the canvas

    private String id; //String id of the node

    private boolean isRecipe; //notes whether the node is a Recipe


    /**
     * CONSTRUCTOR MODIFIED
     * Vertex Constructor
     * @param node - the Node to be drawn
     * @param icon - Drawable that hold the Node image
     * @param iden - the id of the SuperNode the Node came from
     * @param point - Point2D holding the position of where the Node will be drawn
     */
    public Vertex(final Node node, final Drawable icon, final String iden, Point2D point) {
        this.node = node;
        this.icon = icon;
        this.id = iden;
        this.isRecipe = false;
        this.position = point;
    }

    /**
     * CONSTRUCTOR MODIFIED
     * Vertex Constructor
     * @param node - the Node to be drawn
     * @param icon - Drawable that hold the Node image
     * @param iden - the id of the SuperNode the Node came from
     * @param point - Point2D holding the position of where the Node will be drawn
     * @param isRec - boolean indicating whether the Node is a Recipe
     */
    public Vertex(final Node node, final Drawable icon, final String iden, Point2D point, boolean isRec){
        this.node = node;
        this.icon = icon;
        this.id = iden;
        this.isRecipe = isRec;
        this.position = point;
    }

    /**
     * getId
     * @return id - the id of the Vertex
     */
    public String getId() {
        return id;
    }

    /**
     * setPosition
     * @param point - the Point2D where the Vertex is located
     */
    public void setPosition(Point2D point){
        this.position = point;
    }

    /**
     * getPosition
     * @return position - the Point2D where the Vertex is located
     */
    public Point2D getPosition(){
        return position;
    }

    /**
     * isRecipe
     * @return a boolean indicating whether the Vertex is a recipe
     */
    public boolean isRecipe(){
        return this.isRecipe;
    }
    //END ADDED

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

}
