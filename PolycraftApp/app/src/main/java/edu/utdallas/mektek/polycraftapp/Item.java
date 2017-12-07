package edu.utdallas.mektek.polycraftapp;

import java.io.File;
import java.util.ArrayList;

/**
 * Author:  Carla Vazquez
 * Date:    11/8/17
 * Version: 1.0
 * Item - a class that extends SuperNode and contains item information
 */

public class Item extends SuperNode {
    private String name;                 //a String object to hold the name of the Item
    private String url = "";             //a String object to hold the link to the corresponding wiki page
    protected boolean isNatural = false; //a boolean value indicating whether an Item is naturally occurring
    private int index;                   //an int value holding the index of an Item in its Recipe's parent ArrayList
    private int height;                  //an int value holding the height of an Item in the Tree

    //ITEMS ONLY HAVE ONE CHILD AND ONE PARENT

    /**
     * Item constructor
     * @param id - takes an id value for the item node
     * @param name - takes the name of the item
     * @param img - takes the name of the image file
     * @param url - takes the url link to the corresponding wiki page
     * @param isNatural - indicates whether the item is naturally occuring
     */
    public Item (String id, String name, File img, String url, int isNatural){
        super(id, new ArrayList<SuperNode>(), new ArrayList<SuperNode>(), img);
        this.name = name;
        this.url = url;
        if(isNatural == 1) {
        		this.isNatural = true;
        }
    }

    /**
     * toString
     * @return name - a String object containing the name of the Item
     */
    @Override
    public String toString() {
    		return name;
    }

    /**
     * getId
     * @return nodeId - a String object containing the id specific to this Item
     */
    @Override
    public String getId() {
        return nodeId;
    }

    /**
     * getParents
     * @return parents - a SuperNode ArrayList containing the parents of the item
     */
    @Override
    public ArrayList<SuperNode> getParents() {
        return parents;
    }

    /**
     * getChildren
     * @return children - a SuperNode ArrayList containing the children of the item
     */
    @Override
    public ArrayList<SuperNode> getChildren() {
        return children;
    }

    /**
     * getImage
     * @return image - the filename of the image file
     */
    @Override
    public File getImage() {
        return image;
    }

    /**
     * getName
     * @return name - the name of the Item node
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * setParents
     * @param par - a SuperNode ArrayList that it sets equal to the private variable parents
     */
    @Override
    public void setParents(ArrayList<SuperNode> par) {
        this.parents = par;
    }

    /**
     * setChildren
     * @param chi - a SuperNode ArrayList that it sets equal to the private variable children
     */
    @Override
    public void setChildren(ArrayList<SuperNode> chi){
        this.children = chi;
    }

    /**
     * search
     * @param id - the id we are searching the node for
     * @return the SuperNode of the found node
     */
    @Override
    public SuperNode search(String id){
        if (this.getName().equals(id)) {
            return this;
        }
        else{
            return this.getChildren().get(0).search(id);
        }
    }

    /**
     * setHeight
     * @param height - an int value that the function sets equal to private variable height
     */
    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * getHeight
     * @return height - an int value containing the height of the item in the Tree
     */
    @Override
    public int getHeight(){
        return height;
    }

    /**
     * getUrl
     * @return url - a String object holding the like to the Item's corresponding wiki page
     */
    public String getUrl() { return url;  }

    /**
     * isNatural
     * @return isNatural - a boolean value indicating whether or not the Item is naturally occurring
     */
    public boolean isNatural() {
        return isNatural;
    }

    /**
     * getIndex
     * @return index - an int representing the index of the Item
     */
    public int getIndex(){
        return index;
    }

    /**
     * setIndex
     * @param index - an int value that is set to private variable index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * getFileNamePath
     * gets the png asset for a given node; used to display image on tree
     * @return String - asset path name
     */
    public String getFileNamePath(){
        String pngFileName = this.getImage().getName();
        String[] pngArray = pngFileName.split("File:");
        String assetName = pngArray[1].toLowerCase();
        return assetName;
    }
}

