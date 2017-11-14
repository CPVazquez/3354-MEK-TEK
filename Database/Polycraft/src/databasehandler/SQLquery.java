package databasehandler;
//import java.sql.*;

public final class SQLquery {
	public static final String debugItemInfo = "SELECT * FROM iteminfo";
	public static final String debugDistill = "SELECT * FROM distillrecipe";
	public static final String distillOut = "SELECT rowid, input1, inQuant1 FROM distillrecipe";
	public static final String IDandName = "SELECT gameID, itemName FROM iteminfo";
	public static final  String NatOccur = "SELECT itemNatural FROM iteminfo";
	////public static final String outputSearch =
	
	public static final String queryDistillRecipeData(int columns_to_check) {
		String addendum = " WHERE ";
		int params = columns_to_check;
		
		for(int i = 0; i < params-1; i++){
			addendum += "output" + (i+1) + " = ? OR ";
		}
		addendum += "output" + params + " = ?";
		String query = SQLquery.debugDistill+ addendum;
		
		return query;
	}
	
	public static final String queryItemIsNatural(String searchedItem) {
		
		String query = NatOccur;		
		query += " WHERE itemName LIKE '%" + searchedItem + "%'";
		query += " LIMIT 1"; //prevent multiple items from being called here.
		return query;
	}
}
