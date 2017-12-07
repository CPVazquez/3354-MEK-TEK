package edu.utdallas.mektek.polycraftapp;

import java.util.ArrayList;
import java.io.*;

/**
 * Author:  Carla Vazquez
 * Date:    11/8/17
 * Version: 1.0
 * SuperNode - an abstract node class that all nodes of the tree will fall under.
 * will be used in the Tree class.
 */

public abstract class SuperNode implements Serializable{
    protected String nodeId;                 //a String object to hold the id of the node
    protected ArrayList<SuperNode> parents;  //a SuperNode ArrayList to hold the parents of the SuperNode
    protected ArrayList<SuperNode> children; //a SuperNode ArrayList to hold the parents of the SuperNode
    protected File image;                    //a String Object to hold the name of the file containing the image

    /**
     * SuperNode constructor
     * @param id - the String id of the node
     * @param par - a SuperNode ArrayList holding the parents of the SuperNode
     * @param chil - a SuperNode ArrayList holding the child of the SuperNode
     * @param img - the String of the file name of the image
     */
    public SuperNode(String id, ArrayList<SuperNode> par, ArrayList<SuperNode> chil, File img){
        nodeId = id;
        parents = par;
        children = chil;
        image = img;
    }

    //The following are abstract methods to be overwritten in subclasses

    @Override
    public String toString() {
    		return nodeId;
    }

    public abstract String getId();

    public abstract ArrayList<SuperNode> getParents();

    public abstract ArrayList<SuperNode> getChildren();

    public abstract File getImage();

    public abstract String getName();

    public abstract void setParents(ArrayList<SuperNode> par);

    public abstract void setChildren(ArrayList<SuperNode> chi);

    public abstract SuperNode search(String id) ;

    public abstract void setHeight(int numSteps);

    public abstract int getHeight();

    public abstract String getFileNamePath();
}
