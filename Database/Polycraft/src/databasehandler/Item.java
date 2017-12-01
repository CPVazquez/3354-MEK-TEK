package databasehandler;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by trishaire on 11/8/17.
 */

public class Item extends SuperNode {
    private String name;
    private String url = "";
    private boolean isNatural = false;

    //ITEMS ONLY HAVE ONE CHILD AND ONE PARENT
    
    
    public Item (String id, ArrayList<SuperNode> par, ArrayList<SuperNode> child, File img, String name){
        super(id, par, child, img);
        this.name = name;
    }
    
    //Constructor for empty item creation.
    public Item (String id, String name, File img, String url, int isNatural){
        super(id, new ArrayList<SuperNode>(), new ArrayList<SuperNode>(), img);
        this.name = name;
        this.url = url;
        if(isNatural == 1) {
        		this.isNatural = true;
        }
    }
    
    
    @Override
    public String toString() {
    		return name;
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

	public boolean isNatural() {
		return isNatural;
	}

}
