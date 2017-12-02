package edu.utdallas.mektek.polycraftapp;
import  java.util.HashMap;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by trishaire on 11/8/17.
 */

public class Recipe extends SuperNode {
    private String inventory;
    private HashMap<SuperNode, Integer> childQuant;
    private HashMap<SuperNode, Integer> parentQuant;
    private ArrayList<Integer> childQuantities;
    private ArrayList<Integer> parentQuantities;
    
    //parent is polycraft output
    //child is polycraft input

    public Recipe(String id, ArrayList<SuperNode> par, ArrayList<SuperNode> child,
    		File img, String in, HashMap<SuperNode, Integer> parQ, HashMap<SuperNode, Integer> chiQ){
        super(id, par, child, img);
        childQuant = chiQ;
        parentQuant = parQ;
        this.inventory = in;
    }
    
    public Recipe(String in, String id, ArrayList<SuperNode> par, ArrayList<SuperNode> child,
    		File img, ArrayList<Integer> parQ, ArrayList<Integer> chiQ){
        super(id, par, child, img);
        childQuantities = chiQ;
        parentQuantities = parQ;
        this.inventory = in;
    }
    
    //Testing Constructor:
   // public Recipe()

    @Override
    public ArrayList<SuperNode> getChildren() {
        return children;
    }

    @Override
    public ArrayList<SuperNode> getParents() {
        return parents;
    }

    @Override
    public void setChildren(ArrayList<SuperNode> chi){
        this.children=chi;
    }
    @Override
    public File getImage() {
        return image;
    }

    @Override
    public String getId() {
        return nodeId;
    }

    public String getInventory() {
        return inventory;
    }

    @Override
    public void setDrawnId(Long id) {
        drawnId = id;
    }

    public Integer getChildQuant(String id) {
        return childQuant.get(id);
    }

    public Integer getParentQuant(String id) {
        return parentQuant.get(id);
    }
    
    public String toString() {
    	return this.children.get(0).toString();
    }

    public String getName(){
        return this.inventory;
    }
}
