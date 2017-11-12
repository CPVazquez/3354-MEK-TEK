package edu.utdallas.mektek.polycraftapp;

import java.util.ArrayList;

public class Tree {

    private SuperNode targetNode;

    public Tree(String itemID){

    }

    public void addNode(String id, SuperNode node){
        SuperNode item = getNode(id);
        int t = 0;
        ArrayList<SuperNode> recPar = node.getParents();

        for(int i=0; i < recPar.size(); i++){
            if(recPar.get(i).getId().equals(id)){
                t = i;
                break;
            }
        }

        SuperNode dup = recPar.remove(t);
        dup.getChildren().remove(node);

        item.getChildren().add(node);
        node.getParents().add(item);
    }

    public void deleteNode(SuperNode node){

    }

    public SuperNode getNode(String itemID){
        return null;
    }

    public ArrayList<SuperNode> getDrawnNodes(){

        return null;
    }
}