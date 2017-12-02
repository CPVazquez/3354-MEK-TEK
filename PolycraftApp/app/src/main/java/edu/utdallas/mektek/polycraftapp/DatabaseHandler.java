package edu.utdallas.mektek.polycraftapp;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.*;
import java.util.ArrayList;
import java.io.File;
import java.nio.file.Paths;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

@SuppressWarnings("unused")
public class DatabaseHandler extends SQLiteAssetHelper{
	//private String databaseName;
	private static final String DBNAME = "PolycraftAppData.db";
	private SQLiteDatabase database;
	private int maxColumns = 7;
	private static final int VERSION = 1;
	private static DatabaseHandler dbHandler; //Singleton Design Pattern


	private DatabaseHandler(Context context) {//}, String name, SQLiteDatabase.CursorFactory factory, int version) {
		super(context, DBNAME, null, VERSION);
		//if(dbHandler == null)
		//	dbHandler = new SQLiteAssetHelper(context, DBNAME, null, VERSION);
		//return dbHandler;
	}

	public DatabaseHandler getInstance(Context context){
		if(dbHandler == null){
			dbHandler = new DatabaseHandler(context);
		}
		return dbHandler;
	}

	public void open() {
		this.database = dbHandler.getReadableDatabase();
	}

	public void close() {
		if(this.database != null){
			this.database.close();
		}
	}
	
	public void getItemID(String item) {
		try {
				//conn = DriverManager.getConnection(databaseName);
				String command = SQLquery.IDandName;
				command += " WHERE itemName LIKE '%" + item + "%'";
				Cursor rs = database.rawQuery(command,null);
	
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

		Tree myTree = new Tree(createItem(searchValue));
		ArrayList<String> recipeIds = getRecipeId(searchValue);
		for(String rowId : recipeIds) {
			Recipe item = createRecipe(rowId);
			myTree.addNode(item);
		}
		return myTree;
	}
	

	private Recipe createRecipe(String rowId) throws SQLException {
		
		String query = SQLquery.querySpecificRecipeDetails(rowId);
		Recipe newRecipe = null;
		
		ResultSet rs = pstmt.executeQuery();
		

		
		while(rs.next()) {
			ArrayList<String> parents = new ArrayList<String>();
			ArrayList<Integer> parQ = new ArrayList<Integer>();
			
			//TODO: Encapsulate these calls into a do-while to handle multiple input columns
			ArrayList<String> children = new ArrayList<>();
			ArrayList<Integer> childQuantity = new ArrayList<Integer>();
			children.add(rs.getString("input1"));
			
			try {
				childQuantity.add(Integer.parseInt(rs.getString("inQuant1")));
			} catch (NumberFormatException e1) {
				childQuantity.add(0);
			}
			
			int i=1;
			String par;
			int parQuantity;
			do{
				par=rs.getString("output"+i);
				parents.add(par);
				try {
					parQuantity = Integer.parseInt(rs.getString("outQuant" + i));
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
			
		}
		
		return newRecipe;
	}

	private Item createItem(String itemName) throws SQLException {
		
		String query = SQLquery.queryItemDetails(itemName);
		Item newItem = null;
		
		ResultSet rs = pstmt.executeQuery();
		
		while(rs.next()) {
			newItem = new Item(rs.getString("gameID"), rs.getString("itemName"), new File(rs.getString("itemImage")), rs.getString("itemURL"), Integer.parseInt(rs.getString("itemNatural")));
			return newItem;
		}
		
		throw new SQLException("ERROR ITEM NOT FOUND");
		
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
		
		ResultSet rs = queryDBRecipeId(searchValue);
		if(rs.next()) {

			data.add(rs.getString("rowid"));
			
			data.addAll(getRecipeId(rs.getString("input1")));
		}
		else {
			data.add("");
			
		}
		return data;
	}
	
	
	
	private boolean checkBaseCase(String searchValue) throws SQLException {
		
		String query = SQLquery.queryItemIsNatural(searchValue);

		ResultSet rs = stmt.executeQuery(); //
		
		while(rs.next()) {
			String holder = rs.getString(1); //TODO: change magic number 1 to a static final int
			if(holder.contains("1")) {
				return true;
			}
		}
		
		return false;
	}

	
	private ResultSet queryDBRecipeId(String searchValue) throws SQLException {
		int params = maxColumns;
		String query = SQLquery.queryDistillRecipeRowId(params);
		
		for(int i = 1; i <= params; i++) {
			stmt.setString(i, searchValue);
		}	
		return stmt.executeQuery();

	}
	
	
	private ResultSet queryDB(String searchValue) throws SQLException {
		int params = maxColumns;
		String query = SQLquery.queryDistillRecipeData(params);
		
		for(int i = 1; i <= params; i++) {
			stmt.setString(i, searchValue);
		}	
		return stmt.executeQuery();

	}

	
	private void debugPrinter(Cursor rs) throws SQLException {
		int columns = rs.getColumnCount();
		for(int i = 0; i<columns; i++) {
			String columnname = rs.getColumnName(i+1);
			System.out.print(columnname + "\t\t");
		}
		System.out.print("\n");


		while(!rs.isAfterLast()) {
			for(int i = 0; i<columns; i++) {
				String value = rs.getString(i+1);
				System.out.print(value + "\t\t");
			}
			System.out.print("\n");
			rs.move(1);
		}
		
		rs.close();
	}


	// public static void main(String[] args) {
	     //DatabaseHandler items = new DatabaseHandler("test.db",7);
	    //items.printList("Flask (Ethylene)");
	    // items.printList("Vial (Pentane Isomers)");
	     
	     //items.printList("Bucket");
	     //items.printList("Drum (Light Olefins)");
	     //items.printList("Drum (n-Butane)");
	     //items.printList("Cartridge (Ethane)");
	 //   }
}
