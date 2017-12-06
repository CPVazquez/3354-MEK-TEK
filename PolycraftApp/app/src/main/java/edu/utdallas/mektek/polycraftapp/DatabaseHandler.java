package edu.utdallas.mektek.polycraftapp;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.io.File;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import static java.lang.Integer.parseInt;

/**
 * DatabaseHandler is a Singleton class that extends SQLiteAssetHelper, an Android SDK Class that
 * Provides useful helper classes to interact with a SQLiteAsset.
 *
 * An instance of this Class is instantiated when mainActivity is called. This class provides the
 * following functionality:
 * getItemWithId - queries and returns an item given its ID (Called by DetailView)
 * getRecipeWithId - queries and returns a Recipe object, given its ID (Called by RecipeDetail)
 * getProcessTree - creates and returns a Tree object containing pertinent SuperNodes
 * (called by Search within MainActivity)
 *
 * All of these calls must be wrapped in a DatabaseHandler.open() and DatabaseHandler.close() to
 * prevent memory leak and prevent SQLExceptions from being thrown.
 */
public class DatabaseHandler extends SQLiteAssetHelper{
	private static final String DBNAME = "PolycraftAppData.db";
	private SQLiteDatabase database;
	private int maxColumns = 7;
	private static final int VERSION = 1;
	private static DatabaseHandler dbHandler; //Singleton Design Pattern

    /**
     * Private Constructor called by getInstance to maintain only one instance of this system.
     * This constructor is defined by SQLiteAssetHelper
     * @param context the ApplicationContext for which this is created.
     */
	private DatabaseHandler(Context context) {
		super(context, DBNAME, null, VERSION);// Call to SQLiteAssetHelper constructor
	}

    /**
     * Use this function to get the current instance of this Class.
     * @param context the ApplicationContext (i.e. main thread) this is called from
     * @return the instance of this object, stored in dbHandler above.
     */
	public static DatabaseHandler getInstance(Context context){
		if(dbHandler == null){
			dbHandler = new DatabaseHandler(context);
		}
		return dbHandler;// singleton
	}

    /**
     * Connects this object to the database by opening a "Read-Only" database
     *
     * Please note, by Android APK, we can still write to this database, but if for some reason
     * the database is being accessed, this will not block the thread, still returning a read-only pointer
     */
	public void open() {
        this.database = dbHandler.getReadableDatabase();
	}

    /**
     * Closes active database connection to prevent memory leak or resource blockage.
     */
	public void close() {
		if(this.database != null){
			this.database.close();
		}//closes open database
	}


    /**
     * Returns an Item Object given an itemID to be searched in the database.
     *
     * Usage (From mainActivity)
     *      dbh = DatabaseHandler.getInstance(getContext());
     *      dbh.open();
     *      Item myItem = dbh.getItemWithId([itemID]);
     *      dbh.close();
     *
     * @param gameId ItemID that should be searched in the Database.
     * @return returns an {@link Item} object that has the matching ID from the Database.
     * @throws ItemNotFoundException if ItemID passed does not exist in the database.
     */
    public Item getItemWithId(String gameId) throws ItemNotFoundException {
        Item item;
	    try {
            item=createItem(gameId, 1);
        }catch(ItemNotFoundException ex){
	        throw ex;
        }
	 return item;
    }


    /**
     * Similar to getItemWithId above, this queries the DistillRecipe Table in the SQLite database
     * with a given recipe ID and returns a Recipe object containing the data requested.
     *
     * @param rowID the id of the recipe we are searching for.
     * @return a {@link Recipe} object containing all the information in the rowID passed in
     * @throws SQLException if rowID does not exist on the table or points to null values
     */
    public Recipe getRecipeWithId(String rowID) throws SQLException{
        try {
            return createRecipe(rowID);
        }catch(SQLException ex){
            throw ex;
        }
    }

    /**
     * Parameterized method to build an item object, depending on whether the user passes in the
     * Item's ID number or the Item's Name. Based on Minecraft's design, both must be Unique and
     * both are valid parameters to query with.
     * @param key String containing either the name or the id of the item in question
     * @param method 0 if key is a Name, 1 if key is a ID
     * @return a {@link Item} object containing the details requested, matching the name/ID passed.
     * @throws ItemNotFoundException if the key does not match what is in the table
     * @throws ArrayIndexOutOfBoundsException if the method parameter is not a 0 or 1.
     */
    private Item createItem(String key, int method) throws ItemNotFoundException, ArrayIndexOutOfBoundsException {
        String[] queries = {SQLquery.queryItemDetails(key), SQLquery.queryItemDetailsWithId(key)};
        Item newItem;
        String query;

        if(method!=0 && method!=1){
            throw new ArrayIndexOutOfBoundsException();
        }
        query = queries[method];

        Cursor rs = database.rawQuery(query,null);
        if(rs.getCount()<=0) {
            throw new ItemNotFoundException("No such item.");
        }
        rs.moveToFirst();

        String gameId = rs.getString(rs.getColumnIndex("gameID"));
        String itmName = rs.getString(rs.getColumnIndex("itemName"));
        File itmImage = new File(rs.getString(rs.getColumnIndex("itemImage")));
        String itemURL = rs.getString(rs.getColumnIndex("itemURL"));
        int itemNatural = parseInt(rs.getString(rs.getColumnIndex("itemNatural")));

        newItem = new Item(gameId, itmName, itmImage, itemURL, itemNatural);
        rs.close();
        return newItem;
    }

//	public String getItemID(String searchValue) {
//				String command = SQLquery.selectIdsAndNames;
//				command += " WHERE itemName LIKE '%" + searchValue + "%'";
//				Cursor rs =   database.rawQuery(command,null);
//				return rs.getString(rs.getColumnIndex("gameID"));
//
//	}
//	public String getRecipeId(String searchValue) throws SQLException {
//        ArrayList<String> ancestorIds;
//        try {
//            ancestorIds = getRowIdOfAncestors(searchValue);
//        }catch(SQLException ex){
//            ex.printStackTrace();
//            throw ex;
//        }
//        return ancestorIds.get(0);
//    }


    /**
     * Main Process called by Searching that queries the database, performs a recursive Depth-First
     * Search, and returns a tree object containing connected SuperNodes. The Tree is built from the
     * item to the first "naturally occuring" item that is computed. This search is performed
     * sequentially on the SQLite database, returning first the row with the smallest rowID
     * (closest to the 'top').
     *
     * This will return just the base item if a base item is searched.
     *
     * @param searchValue the input NAME of the item we want to build a tree around. The query performs
     *                    an EXACT MATCH search, so typos are not tolerated.
     * @return a {@link Tree} object containing the matched tree.
     * @throws SQLException If an invalid input is entered or the searched item doesn't exist in the database.
     */
    public Tree getProcessTree(String searchValue) throws SQLException {

        Item searchedItem;
	    try {
             searchedItem = createItem(searchValue,0);
        }
        catch (ItemNotFoundException ex) {
	        ex.printStackTrace();
            throw ex;
        }
        if(!searchedItem.getName().equals(searchValue)){
	        throw new ItemNotFoundException("False match.");
        }

		Tree myTree = new Tree(searchedItem);
		ArrayList<String> recipeIds = getRowIdOfAncestors(searchValue);
		try{
		for(String rowId : recipeIds) {
			Recipe recipe = createRecipe(rowId);
			myTree.addNode(recipe);
		}}
		catch(RecipeNotFoundException ex) {
		    ex.printStackTrace();
		    throw ex;
        }

		return myTree;
	}
	

	private Recipe createRecipe(String rowId) throws SQLException {
        Recipe newRecipe;
        try {
            String query = SQLquery.querySpecificRecipeDetails(rowId);
            Cursor rs = database.rawQuery(query, null);
            if (rs.getCount()<=0){
                throw new RecipeNotFoundException(query);
            }
            rs.moveToFirst();

            ItemList children = new ItemList(rs).childList();
            ArrayList<SuperNode> childItems = children.getChildItems();
            ArrayList<Integer> childQuant = children.getChildQuant();


            ItemList parents = new ItemList(rs).parentList();
            ArrayList<SuperNode> parentItems = parents.getParentItems();
            ArrayList<Integer> parentQuant = parents.getParentQuant();


            newRecipe = new Recipe("DistillationColumn", rowId, parentItems, childItems, new File("/Distillation_Column.ping"), parentQuant, childQuant);
            setAsChild(newRecipe, parentItems);
            setAsParent(newRecipe, childItems);

            rs.close();
        }catch(RecipeNotFoundException ex){
            ex.printStackTrace();
            throw ex;
        }catch (ItemNotFoundException ex){
            ex.printStackTrace();
            throw ex;
        }catch(SQLException ex){
            ex.printStackTrace();
            throw ex;
        }
		return newRecipe;
	}

    private void setAsParent(Recipe newRecipe, ArrayList<SuperNode> childItems) {
        ArrayList<SuperNode> recArr= new ArrayList<>();
        recArr.add(newRecipe);
        for (int i=0; i<childItems.size(); i++){
            childItems.get(i).setParents(recArr);

            if(childItems.get(i).getChildren().size()==0){
                ((Item)childItems.get(i)).isNatural=true;
            }
        }
    }

    private void setAsChild(Recipe newRecipe, ArrayList<SuperNode> parentItems) {
        ArrayList<SuperNode> recArr= new ArrayList<>();
        recArr.add(newRecipe);
        for(int i=0; i<parentItems.size(); i++) {
          parentItems.get(i).setChildren(recArr);
        }

    }

	
	private ArrayList <String> getRowIdOfAncestors(String searchValue) throws SQLException {
		ArrayList <String> data = new ArrayList<String>();
		if(searchValue==null) {// || data.contains(searchValue)){
				return data;
			}
		if(checkBaseCase(searchValue)) {
			//data.add("");
			return data;
		}

		Cursor rs;
		try {
            rs = queryDBRecipeId(searchValue);
            rs.moveToFirst();


        }catch(RecipeNotFoundException ex){
		    ex.printStackTrace();
		    return data;
        }

        if (!rs.isAfterLast()) {

            data.add(rs.getString(rs.getColumnIndex("rowid")));

            data.addAll(getRowIdOfAncestors(rs.getString(rs.getColumnIndex("input1"))));
        } else {

        }
		return data;
	}

	
	private boolean checkBaseCase(String searchValue) throws SQLException {
		String query = SQLquery.queryItemIsNatural(searchValue);
		Cursor rs = database.rawQuery(query,null); //
		for(int row = 1; row<rs.getCount(); row++) {
			rs.moveToPosition(row);
			String holder = rs.getString(5); //TODO: change magic number 1 to a static final int
			if(holder.contains("1")) {
				return true;
			}
		}
		return false;
	}

	
	private Cursor queryDBRecipeId(String searchValue) throws SQLException {
        Cursor rs;
		int params = maxColumns;
		String query = SQLquery.queryDistillRecipeRowId(params);
		String[] selectionArgs = new String[params];
		for(int i = 0; i < params; i++) {
			selectionArgs[i]=searchValue;
		}
        rs = database.rawQuery(query, selectionArgs);
        if(rs.getCount()<=0){
            throw new RecipeNotFoundException(query);
        }
        return rs;
	}


    private class ItemList {
        private Cursor rs;
        private ArrayList<Integer> parentQuant;
        private ArrayList<SuperNode> parentItems;
        private ArrayList<Integer> childQuant;
        private ArrayList<SuperNode> childItems;

        public ItemList(Cursor rs) {
            this.rs = rs;
        }

        public ArrayList<Integer> getParentQuant() {
            return parentQuant;
        }

        public ArrayList<Integer> getChildQuant() {
            return childQuant;
        }

        public ArrayList<SuperNode> getChildItems() {
            return childItems;
        }

        public ArrayList<SuperNode> getParentItems() {
            return parentItems;
        }

        public ItemList parentList() throws SQLException {
            ArrayList<String> parents = new ArrayList<String>();
            parentQuant = new ArrayList<Integer>();
            int i=1;
            String par;
            int parQuantity;
            do{
                par = rs.getString(rs.getColumnIndex("output" + i));
                if(par.equals("")) break;
                parents.add(par);
                try {
                    parQuantity = parseInt(rs.getString(rs.getColumnIndex("outQuant" + i)));
                }catch(NumberFormatException e) {
                    parQuantity = 0;
                }
                parentQuant.add(parQuantity);
                i++;
            }while (i<=maxColumns && par.length()>0);

            parentItems = new ArrayList<SuperNode>();
            int j=0;
            for (String itemName : parents) {

                parentItems.add(createItem(itemName,0));
                ((Item) parentItems.get(j)).setIndex(j);
                j++;
            }
            return this;
        }

        public ItemList childList() throws ItemNotFoundException {
            //TODO: FUTURE: Encapsulate these calls into a do-while to handle multiple input columns
            ArrayList<String> children = new ArrayList<>();
            childQuant = new ArrayList<Integer>();
            children.add(rs.getString(rs.getColumnIndex("input1")));

            try {
                childQuant.add(Integer.parseInt(rs.getString(rs.getColumnIndex("inQuant1"))));
            } catch (NumberFormatException e1) {
                childQuant.add(0);
            }


            childItems = new ArrayList<SuperNode>();
            for (String itemName : children) {
                try {
                    childItems.add(createItem(itemName, 0));
                }
                catch(ItemNotFoundException ex){
                    ex.printStackTrace();
                    throw ex;
                }
            }
            return this;
        }


    }//ItemList Class

    public class ItemNotFoundException extends SQLException{
        public ItemNotFoundException(String message){
            super(message);
        }
    }

    public class RecipeNotFoundException extends SQLException{
        public RecipeNotFoundException(String message){
            super(message);
        }
    }

}
