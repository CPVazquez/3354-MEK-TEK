package edu.utdallas.mektek.polycraftapp;

import java.util.ArrayList;

public class Tree {

    private SuperNode targetNode;
    private SuperNode pointerNode;

    public Tree(SuperNode target){

        this.targetNode = target;
        this.pointerNode = null;
    }

    // Attach this node to pointerNode
    public void addNode(SuperNode nodeToAdd){

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

    public ArrayList<SuperNode> getDrawnNodes()
    {
        return null;
    }

    public SuperNode getTargetNode(){
        return this.targetNode;
    }
}