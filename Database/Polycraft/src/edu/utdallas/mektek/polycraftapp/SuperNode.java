package edu.utdallas.mektek.polycraftapp;

import java.util.ArrayList;
import java.io.*;
/**
 * Created by trishaire on 11/8/17.
 */

public abstract class SuperNode {
    protected String nodeId;
    protected ArrayList<SuperNode> parents;
    protected ArrayList<SuperNode> children;
    protected File image;
    protected Long drawnId;

    public SuperNode(String id, ArrayList<SuperNode> par, ArrayList<SuperNode> chil, File img){
        nodeId = id;
        parents = par;
        children = chil;
        image = img;
    }

    public abstract String getId();
    public abstract ArrayList<SuperNode> getParents();
    public abstract ArrayList<SuperNode> getChildren();
    public abstract File getImage();
    //public abstract Long getDrawnId();

    public abstract void setDrawnId(Long id);
}
