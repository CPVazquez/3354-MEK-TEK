package edu.utdallas.mektek.polycraftapp;

import net.xqhs.graphs.graph.Node;

import java.util.ArrayList;

import giwi.org.networkgraph.beans.Dimension;
import giwi.org.networkgraph.beans.Point2D;

public class Tree {

    private SuperNode targetNode;
    private SuperNode pointerNode;
    private String contents="";
    private int numSteps=0;

    public Tree(SuperNode target){

        this.targetNode = target;
        contents+=targetNode.toString()+":\n";
        this.pointerNode = null;
    }

    // Attach this node to pointerNode
    public void addAllNodes(ArrayList<SuperNode> nodes) {
    		//iterator for arraylist
    		//addNode(nodes.next();)
    }
    
    public void addNode(SuperNode nodeToAdd){
    	contents+=nodeToAdd.toString()+"\n";
    	numSteps++;
    	nodeToAdd.setHeight(numSteps);
        // This tree was just created, add the node to target
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
            for (SuperNode child : this.pointerNode.getChildren()) {
                // Loop through nodeToAdd's parents
                for (SuperNode parent : nodeToAdd.getParents()) {
                    // Parent and child IDs match we make the connection here
                    if(child.getId().equals(parent.getId())){
                        child = parent;
                        this.pointerNode = nodeToAdd;
                        break;
                    }
                }
            }
        }
    }

    public void deleteNode(SuperNode node){
        // Unimplemented
    }

    public SuperNode get(String id){
        return targetNode.search(id);
    }

    public Point2D getPosition(Dimension d, Node drawnode){
        SuperNode datanode= get(drawnode.getLabel());
        int x= getX(datanode,d);
        int y= getY(datanode,d);
        return new Point2D(x,y);
    }

    private int getY(SuperNode datanode, Dimension d) {
        return datanode.height;
    }

    private int getX(SuperNode datanode, Dimension d) {
        if (datanode instanceof  Recipe){
            return d.getWidth()/2;
        }
        else if (datanode instanceof Item){
           return ((Item)datanode).getIndex();
        }

        else {
            return 0;
        }
    }

    public ArrayList<SuperNode> getDrawnNodes()
    {
        return null;
    }

    public SuperNode getTargetNode(){
        return this.targetNode;
    }
    
    public String toString() {
    	
    	return numSteps+" steps to get "+contents;
    }
}