package databasehandler;

import  java.util.HashMap;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by trishaire on 11/8/17.
 */

public class Recipe extends SuperNode {
    private String inventory;
    private HashMap<String, Integer> childQuant;
    private HashMap<String, Integer> parentQuant;

    public Recipe(String id, ArrayList<SuperNode> par, ArrayList<SuperNode> child, File img, String in, HashMap<String, Integer> parQ, HashMap<String, Integer> chiQ){
        super(id, par, child, img);
        childQuant = chiQ;
        parentQuant = parQ;
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
}
