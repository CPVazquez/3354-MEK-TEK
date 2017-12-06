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
    private int height;
    
    //parent is polycraft output
    //child is polycraft input


    /**
     * Constructor for the Recipe class, none of the inputs should be null
     * @param id takes an id value for the Recipe node
     * @param par takes an array list of parent nodes for the Recipe node
     * @param child takes an array list of child nodes for the recipe node
     * @param img takes the image name as a String for the recipe node
     * @param in takes the inventory of the Recipe
     * @param parQ takes an array list of the quantities of the parent nodes
     * @param chiQ takes an array list of the quantities of the child nodes
     */
    public Recipe(String id, ArrayList<SuperNode> par, ArrayList<SuperNode> child,
    		File img, String in, HashMap<SuperNode, Integer> parQ, HashMap<SuperNode, Integer> chiQ){
        super(id, par, child, img);
        childQuant = chiQ;
        parentQuant = parQ;
        this.inventory = in;
    }

    /**
     * Constructor for the Recipe class, none of the inputs should be null
     * @param in takes the inventory of the Recipe
     * @param id takes an id value for the Recipe node
     * @param par takes an array list of parent nodes for the Recipe node
     * @param child takes an array list of child nodes for the recipe node
     * @param img takes the image name as a String for the recipe node
     * @param parQ takes an array list of the quantities of the parent nodes
     * @param chiQ takes an array list of the quantities of the child nodes
     */
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
    public SuperNode search(String id) {
        if (this.getId().equals(id)) {
            return this;
        }
        else{
           for (SuperNode par : this.getParents()){
               if(par.getName().equals(id)){
                   return par;
               }
           }

            return this.getChildren().get(0).search(id);
        }
    }

    @Override
    public void setHeight(int h) {
        height=h;
        for (int i=0;i<parents.size();i++){
            parents.get(i).setHeight(height);
        }

    }

    public int getHeight(){
        return this.height;
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

    @Override
    public void setParents(ArrayList<SuperNode> par) {
        this.parents=par;
    }

    /**
     * a method for retrieving the quantity required for a child node
     * @param id given the string id of the node
     * @return integer value of the quantity required
     */
    public Integer getChildQuant(String id) {
        return childQuant.get(id);
    }

    /**
     * a method for retrieving the quantity required for a parent node
     * @param id given the string id of the node
     * @return integer value of the quantity required
     */
    public Integer getParentQuant(String id) {
        return parentQuant.get(id);
    }
    
    public String toString() {
    	return this.children.get(0).toString();
    }


    public String getName(){
        return this.inventory;
    }

    public ArrayList<Integer> getChildQuantities(){ return this.childQuantities; }

    public ArrayList<Integer> getParentQuantities(){ return this.parentQuantities; }


}
