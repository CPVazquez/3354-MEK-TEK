package edu.utdallas.mektek.polycraftapp;

import android.util.Log;
import net.xqhs.graphs.graph.Node;
import edu.utdallas.mektek.polycraftapp.beans.*;//Dimension;

/**
 * Tree - a data structure made up of SuperNodes
 */
public class Tree {
    private SuperNode targetNode;  //the Item we are searching for
    private SuperNode pointerNode; //the SuperNode we are adding SuperNodes too
    private String contents="";    //the String object holding name of the current SuperNode
    private int numSteps=0;        //the int value od the depth of the Tree

    /**
     * Tree constructor
     * @param target - the SuperNode that acts as the root of the Tree
     */
    public Tree(SuperNode target){

        this.targetNode = target;
        contents+=targetNode.toString()+":\n";
        this.pointerNode = null;
    }

    /**
     * addNode
     * @param nodeToAdd - the SuperNode being added to the Tree
     */
    public void addNode(SuperNode nodeToAdd){
    	contents+=nodeToAdd.toString()+"\n";
        numSteps++;
        nodeToAdd.setHeight(numSteps);
        Log.d("HEIL",String.valueOf(numSteps)+String.valueOf(nodeToAdd.getHeight()));

        // This tree was just created, add the SuperNode to target
        if(this.pointerNode == null){
            // Make parent and child connection
            for (SuperNode parent : nodeToAdd.getParents()){
                if(parent.getId().equals(this.targetNode.getId())){
                    // Make parent/child connection
                    this.targetNode = parent;
                    this.pointerNode = nodeToAdd;
                    break; // We done
                }
            }
        }
        else{
            // Loop through pointerNode's children
            for (int i=0; i < this.pointerNode.getChildren().size(); i++){
                // Loop through nodeToAdd's parents
                for (int j=0; j < nodeToAdd.getParents().size(); j++) {
                    // Parent and child IDs match we make the connection here
                    if(this.pointerNode.getChildren().get(i).getId().equals(nodeToAdd.getParents().get(j).getId())){
                        this.pointerNode.getChildren().set(i,nodeToAdd.getParents().get(j));
                        nodeToAdd.getParents().set(j, this.pointerNode.getChildren().get(i));
                        this.pointerNode = nodeToAdd;
                        break;
                    }
                }
            }
        }
    }

    /**
     * get
     * @param id - the String id of the node you are searching target for
     * @return the SuperNode that was found
     */
    public SuperNode get(String id){
        return targetNode.search(id);
    }

    /**
     * getPosition
     * @param d - a dimension object
     * @param drawNode - a Node object
     * @return a Point2D object holding the Node's position on the canvas
     */
    public Point2D getPosition(Dimension d, Node drawNode){
        String id= drawNode.getLabel();
        SuperNode dataNode= get(id);
        int x= getX(dataNode,d);
        int y= getY(dataNode,d);
        return new Point2D(x,y);
    }

    /**
     * getY
     * @param dataNode - a SuperNode object
     * @param d - a Dimension object
     * @return int value representing the Y coordinate of the SuperNode
     */
    private int getY(SuperNode dataNode, Dimension d) {
        Log.d("HEIGHT: ",dataNode.toString()+String.valueOf(dataNode.getHeight()));
        if(dataNode instanceof  Recipe){
            return dataNode.getHeight()*800;

        }
        if(dataNode instanceof Item){
            if (((Item) dataNode).isNatural()){
                return (dataNode.getParents().get(0).getHeight())*800+400;
            }
            return dataNode.getHeight()*800-400;
        }
        return 0;
    }

    /**
     * getX
     * @param dataNode - a SuperNode object
     * @param d - a Dimension object
     * @return int value representing the X coordinate of the SuperNode
     */
    private int getX(SuperNode dataNode, Dimension d) {
        if (dataNode instanceof  Recipe){
            return d.getWidth()/2;
        }
        else {
            if (dataNode instanceof Item) {
                int idx =((Item)dataNode).getIndex();
                return idx*600;
            }
        }
        return d.getWidth()/2;
    }

    /**
     * getTargetNode
     * @return the SuperNode targetNode
     */
    public SuperNode getTargetNode(){
        return this.targetNode;
    }

    /**
     * toString
     * @return a String message
     */
    public String toString() { return numSteps + " steps to get " + contents; }
}