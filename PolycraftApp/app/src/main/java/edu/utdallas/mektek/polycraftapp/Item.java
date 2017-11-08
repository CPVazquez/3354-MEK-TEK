package edu.utdallas.mektek.polycraftapp;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by trishaire on 11/8/17.
 */

public class Item extends SuperNode {
    private String name;

    public Item (String id, ArrayList<SuperNode> par, ArrayList<SuperNode> child, File img, String name){
        super(id, par, child, img);
        this.name = name;
    }

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

    public String getName() {
        return name;
    }

    @Override
    public void setDrawnId(Long id) {
        drawnId = id;
    }
}
