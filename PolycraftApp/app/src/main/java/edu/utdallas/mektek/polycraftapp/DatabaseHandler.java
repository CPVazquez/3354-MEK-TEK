package edu.utdallas.mektek.polycraftapp;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.io.File;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import static java.lang.Integer.parseInt;

@SuppressWarnings("unused")
public class DatabaseHandler extends SQLiteAssetHelper{
	private static final String DBNAME = "PolycraftAppData.db";
	private SQLiteDatabase database;
	private int maxColumns = 7;
	private static final int VERSION = 1;
	private static DatabaseHandler dbHandler; //Singleton Design Pattern


	private DatabaseHandler(Context context) {
		super(context, DBNAME, null, VERSION);// Call to SQLiteAssetHelper constructor
	}

	public static DatabaseHandler getInstance(Context context){
		if(dbHandler == null){
			dbHandler = new DatabaseHandler(context);
		}
		return dbHandler;// singleton
	}

	public void open() {
        this.database = dbHandler.getReadableDatabase();
	}//open connection to database

	public void close() {
		if(this.database != null){
			this.database.close();
		}//closes open database
	}

	/*CARLA: Use this to get your item.
        Be sure to call this properly from mainActivity:

		this.dbh = DatabaseHandler.getInstance(this);
		dbh.open() //connects to sql database -> maybe anshu is taking care of this command in the "onCreate() function?"
		dbh.getItemWithId("123") //will fail if .open() has not been called earlier in the function -> maybe Anshu is taking care of this?
		dbh.close() //call this at the end to prevent memory leakage -> maybe Anshu is taking care of this on "onPause()"?
		*/
    public Item getItemWithId(String gameId) throws SQLException {
        Item item;
	    try {
            item=createItem(gameId, 1);
        }catch(SQLException ex){
	        throw ex;
        }
	 return item;
    }


    /*
		CARLA: use this to get your recipe

	 */

    public Recipe getRecipeWithId(String rowID) throws SQLException{
        try {
            return createRecipe(rowID);
        }catch(SQLException ex){
            throw ex;
        }
    }


    private Item createItem(String key, int method) throws SQLException, ArrayIndexOutOfBoundsException {

        String[] queries = {SQLquery.queryItemDetails(key), SQLquery.queryItemDetailsWithId(key)};
        Item newItem;
        String query;

        if(method!=0 && method!=1){
            throw new ArrayIndexOutOfBoundsException();
        }
        query = queries[method];

        Cursor rs = database.rawQuery(query,null);
        if(rs.getCount()<=0) {
            throw new SQLException("No such item.");
        }
        rs.moveToFirst();

        String gameId = rs.getString(rs.getColumnIndex("gameID"));
        String itmName = rs.getString(rs.getColumnIndex("itemName"));
        File itmImage = new File(rs.getString(rs.getColumnIndex("itemImage")));
        String itemURL = rs.getString(rs.getColumnIndex("itemURL"));
        int itemNatural = parseInt(rs.getString(rs.getColumnIndex("itemNatural")));

        newItem = new Item(gameId, itmName, itmImage, itemURL, itemNatural);
        return newItem;
    }

	public String getItemID(String searchValue) {
				String command = SQLquery.selectIdsAndNames;
				command += " WHERE itemName LIKE '%" + searchValue + "%'";
				Cursor rs =   database.rawQuery(command,null);
				return rs.getString(rs.getColumnIndex("gameID"));

	}
	public String getRecipeId(String searchValue) throws SQLException {
        ArrayList<String> ancestorIds;
        try {
            ancestorIds = getRowIdOfAncestors(searchValue);
        }catch(SQLException ex){
            throw ex;
        }
        return ancestorIds.get(0);
    }

    public Tree getProcessTree(String searchValue) throws SQLException {

        Item searchedItem;
	    try {
             searchedItem = createItem(searchValue,0);
        }
        catch (SQLException ex) {
            throw ex;
        }


		Tree myTree = new Tree(searchedItem);
		ArrayList<String> recipeIds = getRowIdOfAncestors(searchValue);
		try{
		for(String rowId : recipeIds) {
			Recipe recipe = createRecipe(rowId);
			myTree.addNode(recipe);
		}}
		catch(SQLException ex) {
		    throw ex;
        }

		return myTree;
	}
	

	private Recipe createRecipe(String rowId) throws SQLException {
        Recipe newRecipe;
        try {
            String query = SQLquery.querySpecificRecipeDetails(rowId);
            Cursor rs = database.rawQuery(query, null);
            rs.moveToFirst();

            ItemList children = new ItemList(rs).childList();
            ArrayList<SuperNode> childItems = children.getChildItems();
            ArrayList<Integer> childQuant = children.getChildQuant();


            ItemList parents = new ItemList(rs).parentList();
            ArrayList<SuperNode> parentItems = parents.getParentItems();
            ArrayList<Integer> parentQuant = parents.getParentQuant();


            newRecipe = new Recipe("DistillationColumn", rowId, parentItems, childItems, new File("/Distillation_Column.ping"), parentQuant, childQuant);
            setAsChild(newRecipe, parentItems);

            rs.close();
        }catch(SQLException ex){
            throw ex;
        }
		return newRecipe;
	}

    private void setAsChild(Recipe newRecipe, ArrayList<SuperNode> parentItems) {
        ArrayList<SuperNode> recArr= new ArrayList<>();
        recArr.add(newRecipe);
        for (SuperNode it : parentItems){
            it.setChildren(recArr);
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

		try {
            Cursor rs = queryDBRecipeId(searchValue);
            rs.moveToFirst();
            if (!rs.isAfterLast()) {

                data.add(rs.getString(rs.getColumnIndex("rowid")));

                data.addAll(getRowIdOfAncestors(rs.getString(rs.getColumnIndex("input1"))));
            } else {

            }
        }catch(SQLException ex){
		    throw ex;
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
            //throw new SQLException();
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
                parents.add(par);
                try {
                    parQuantity = parseInt(rs.getString(rs.getColumnIndex("outQuant" + i)));
                }catch(NumberFormatException e) {
                    parQuantity = 0;
                }
                parentQuant.add(parQuantity);
                i++;
            }while (i<maxColumns && par.length()>0);

            parentItems = new ArrayList<SuperNode>();
            for (String itemName : parents) {

                parentItems.add(createItem(itemName,0));
            }
            return this;
        }

        public ItemList childList() throws SQLException {
            //TODO: Encapsulate these calls into a do-while to handle multiple input columns
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
                childItems.add(createItem(itemName,0));
            }
            return this;
        }
    }//ItemList Class

}
