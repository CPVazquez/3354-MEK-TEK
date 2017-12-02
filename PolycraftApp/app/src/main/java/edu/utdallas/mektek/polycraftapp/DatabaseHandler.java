package edu.utdallas.mektek.polycraftapp;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
	
	public void getItemID(String item) {
		try {
				String command = SQLquery.selectIdsAndNames;
				command += " WHERE itemName LIKE '%" + item + "%'";
				Cursor rs =   database.rawQuery(command,null);
	
				debugPrinter(rs);
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void printList(String rootItem) {
		try {
				System.out.println(getProcessTree(rootItem));
				System.out.println(getRecipeId(rootItem).toString());
			
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

	public Tree getProcessTree(String searchValue) throws SQLException {

        Item searchedItem=createItem(searchValue);
        if (searchedItem == null){
            return null;
        }
		Tree myTree = new Tree(searchedItem);

		ArrayList<String> recipeIds = getRecipeId(searchValue);
		for(String rowId : recipeIds) {
			Recipe recipe = createRecipe(rowId);
			myTree.addNode(recipe);
		}
		return myTree;
	}
	

	private Recipe createRecipe(String rowId) throws SQLException {
		
		String query = SQLquery.querySpecificRecipeDetails(rowId);
		Recipe newRecipe = null;
		
		Cursor rs =   database.rawQuery(query,null);



        for(int row = 0; row<rs.getCount(); row++) {
            rs.moveToPosition(row);
			ArrayList<String> parents = new ArrayList<String>();
			ArrayList<Integer> parQ = new ArrayList<Integer>();
			
			//TODO: Encapsulate these calls into a do-while to handle multiple input columns
			ArrayList<String> children = new ArrayList<>();
			ArrayList<Integer> childQuantity = new ArrayList<Integer>();
			children.add(rs.getString(rs.getColumnIndex("input1")));
			
			try {
				childQuantity.add(Integer.parseInt(rs.getString(rs.getColumnIndex("inQuant1"))));
			} catch (NumberFormatException e1) {
				childQuantity.add(0);
			}
			
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
				parQ.add(parQuantity);
				i++;
			}while (i<maxColumns && par.length()>0);
			
			ArrayList<SuperNode> parentItems = new ArrayList<SuperNode>();
			for (String itemName : parents) {

				parentItems.add(createItem(itemName));
			}
			
			ArrayList<SuperNode> childItems = new ArrayList<SuperNode>();
			for (String itemName : children) {
				childItems.add(createItem(itemName));
			}
			
			newRecipe = new Recipe("DistillationColumn", rowId, parentItems, childItems, new File("/Distillation_Column.ping"), parQ, childQuantity);
			ArrayList<SuperNode> recArr= new ArrayList<>();
			recArr.add(newRecipe);
			for (SuperNode it : parentItems){
				it.setChildren(recArr);
			}
		}
		
		return newRecipe;
	}

	private Item createItem(String itemName) throws SQLException {
		
		String query = SQLquery.queryItemDetails(itemName);
		Item newItem = null;
		Cursor rs =   database.rawQuery(query,null);
		//debugPrinter(rs);
		if(rs.getCount()<=0) {
            return null;
        }
		rs.moveToPosition(0);
			String gameId = rs.getString(rs.getColumnIndex("gameID"));
			String itmName = rs.getString(rs.getColumnIndex("itemName"));
			File itmImage = new File(rs.getString(rs.getColumnIndex("itemImage")));
			String itemURL = rs.getString(rs.getColumnIndex("itemURL"));
			int itemNatural = parseInt(rs.getString(rs.getColumnIndex("itemNatural")));
			newItem = new Item(gameId, itmName, itmImage, itemURL, itemNatural);
			return newItem;

		

		//return newItem;
	}
	
	private boolean checkBaseCase(Item inputItem) {
			
		
		return false;
	}
	
	private ArrayList <String> getRecipeId(String searchValue) throws SQLException {
		ArrayList <String> data = new ArrayList<String>();
		if(searchValue==null) {// || data.contains(searchValue)){
				return data;
			}
		if(checkBaseCase(searchValue)) {
			//data.add("");
			return data;
		}
		
		Cursor rs = queryDBRecipeId(searchValue);
		rs.moveToPosition(0);
		if(!rs.isAfterLast()) {

			data.add(rs.getString(rs.getColumnIndex("rowid")));
			
			data.addAll(getRecipeId(rs.getString(rs.getColumnIndex("input1"))));
		}
		else {

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
		int params = maxColumns;
		String query = SQLquery.queryDistillRecipeRowId(params);

		String[] selectionArgs = new String[params];
		for(int i = 0; i < params; i++) {
			selectionArgs[i]=searchValue;
		}
		return database.rawQuery(query,selectionArgs);

	}
	
	
	private Cursor queryDB(String searchValue) throws SQLException {
		int params = maxColumns;
		String query = SQLquery.queryDistillRecipeData(params);

		String[] selectionArgs = new String[params];
		for(int i = 1; i <= params; i++) {
			selectionArgs[i]=searchValue;
		}
		return database.rawQuery(query,selectionArgs);

	}

	
	private void debugPrinter(Cursor rs) throws SQLException {
	    int startpos=rs.getPosition();
		int columns = rs.getColumnCount();
		for(int i = 0; i<columns; i++) {
			String columnname = rs.getColumnName(i);
			System.out.print(columnname + "\t\t");
		}
		System.out.print("\n");

		rs.moveToPosition(1);
		for(int row = 0; row<rs.getCount(); row++) {
            rs.moveToPosition(row);

            for(int i = 0; i<columns; i++) {
				String value = rs.getString(i);
				System.out.print(value + "\t\t");
			}
			System.out.print("\n");
		}
		rs.moveToPosition(startpos);
	}
/*

	 public static void main(String[] args) {
	     DatabaseHandler items;
		 items = new DatabaseHandler(context);
		 items.printList("Flask (Ethylene)");
	     items.printList("Vial (Pentane Isomers)");
	     
	     items.printList("Bucket");
	     items.printList("Drum (Light Olefins)");
	     items.printList("Drum (n-Butane)");
	     items.printList("Cartridge (Ethane)");
	    }
	    */
}
