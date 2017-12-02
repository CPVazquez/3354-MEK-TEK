package edu.utdallas.mektek.polycraftapp;

public final class SQLquery {
	public static final String selectItems = "SELECT * FROM iteminfo";
	public static final String selectRecipes = "SELECT * FROM distillrecipe";
	public static final String selectRowIds = "SELECT rowid, input1 FROM distillrecipe";
	public static final String selectIdsAndNames = "SELECT gameID, itemName FROM iteminfo";
	public static final  String selectIsNatural = "SELECT itemNatural FROM iteminfo";

	public static final String queryDistillRecipeData(int columns_to_check) {
		String addendum = " WHERE ";
		int params = columns_to_check;
		
		for(int i = 0; i < params-1; i++){
			addendum += "output" + (i+1) + " = ? OR ";
		}
		addendum += "output" + params + " = ?";
		String query = SQLquery.selectRecipes + addendum;
		
		return query;
	}

	public static final String queryDistillRecipeRowId(int columns_to_check) {
		String addendum = " WHERE ";
		int params = columns_to_check;
		
		for(int i = 0; i < params-1; i++){
			addendum += "output" + (i+1) + " = ? OR ";
		}
		addendum += "output" + params + " = ?";
		String query = SQLquery.selectRowIds + addendum;
		
		return query;
	}
	
	public static final String querySpecificRecipeDetails(String rowId) {
		String query = selectRecipes;
		query += " WHERE rowid LIKE '" + rowId + "'";
		query += " LIMIT 1"; //prevent multiple items from being called here.
		return query;
	}
	
	public static final String queryItemIsNatural(String searchedItem) {
		
		String query = selectIsNatural;
		query += " WHERE itemName LIKE '%" + searchedItem + "%'";
		query += " LIMIT 1"; //prevent multiple items from being called here.
		return query;
	}
	
	public static final String queryItemDetails(String searchedItem) {
		String query = selectItems;
		query += " WHERE itemName LIKE '%" + searchedItem + "%'";
		query += " LIMIT 1"; //prevent multiple items from being called here.
		return query;
	}
}
