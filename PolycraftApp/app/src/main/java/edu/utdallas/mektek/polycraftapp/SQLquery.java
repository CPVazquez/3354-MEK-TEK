package edu.utdallas.mektek.polycraftapp;

/**
 * SQLQuery is a helper class that contains all of the SQLQuery strings used in our databaseHandler calls.
 * This class also contains static functions that returns parameterized queries.
 * This class also builds queries and returns them.
 */
public final class SQLquery {
	public static final String selectItems = "SELECT * FROM iteminfo";
	public static final String selectRecipes = "SELECT * FROM distillrecipe";
	public static final String selectRowIds = "SELECT rowid, input1 FROM distillrecipe";
	public static final  String selectIsNatural = "SELECT itemNatural FROM iteminfo";

    /**
     * Query DistillRecipe Table's output columns, returning only the rowId and input1 columns
     *
     * @param columns_to_check number of output columns to check (though 9 columns exist, only a
     *                         maximum of 7 are used currently, in the formulas provided by PolycraftWorld
     *                         development team)
     * @return the built SQLQuery with ? where the query value should go.
     */
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

    /**
     * query the distillrecipe table, returning the entire row that matches the input rowID
     * @param rowId the rowID of the table the user wants back
     * @return a string containing the proper query to EXACTLY search for the rowID.
     */
	public static final String querySpecificRecipeDetails(String rowId) {
		String query = selectRecipes;
		query += " WHERE rowid LIKE '" + rowId + "'";
		query += " LIMIT 1"; //prevent multiple items from being called here.
		return query;
	}

    /**
     * Query the itemdetails table to see if the searched item name is a "naturally occurring" item.
     * @param searchedItem the name of the item to check
     * @return a SQLQuery that accomplishes this task.
     */
	public static final String queryItemIsNatural(String searchedItem) {
		
		String query = selectIsNatural;
		query += " WHERE itemName LIKE '%" + searchedItem + "%'";
		query += " LIMIT 1"; //prevent multiple items from being called here.
		return query;
	}

    /**
     * Get all details of an item in the itemdetail table, passing in the name of the item
     * @param searchedItem name of the item to get
     * @return a SQLQuery that accomplishes this task
     */
	public static final String queryItemDetails(String searchedItem) {
		String query = selectItems;
		query += " WHERE itemName LIKE '%" + searchedItem + "%'";
		query += " LIMIT 1"; //prevent multiple items from being called here.
		return query;
	}

    /**
     * get all details of an item in the itemdetail table, passing in the ID of the item
     * @param gameID the ID of the item we want details on
     * @return a SQLQuery that can do this.
     */
	public static final String queryItemDetailsWithId(String gameID) {
		String query = selectItems;
		query += " WHERE gameID LIKE '%" + gameID + "%'";
		query += " LIMIT 1"; //prevent multiple items from being called here.
		return query;
	}
}
