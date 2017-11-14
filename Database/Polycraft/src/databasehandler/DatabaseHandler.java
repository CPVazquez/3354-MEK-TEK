package databasehandler;
import java.sql.*;
import java.util.ArrayList;
import java.nio.file.Paths;

public class DatabaseHandler{
	public boolean debug = true;
	static String dbtest="chinook.db";
	private String database;
	private Connection conn;
	
	private class SearchData{
		public String name;
		public int row;
		
		public SearchData(String nm, int rw) {
			name=nm;
			row=rw;
		}
		
		@Override
		public boolean equals(Object o) {
			if (o==null)
				return false;
			if (((SearchData)o).name.equals(this.name))
				return true;
			else
				return false;
		}
		
		@Override
		public String toString() {
			return this.name+" "+this.row;
		}
	}
	public DatabaseHandler(String dbname) {
		connect(dbname);
	}

	public void connect(String dbname) {
       conn = null;
       String path=Paths.get(".").toAbsolutePath().normalize().toString();

        try {
            // db parameters
            database = "jdbc:sqlite:"+path+"/"+dbname;
            // create a connection to the database
            conn = DriverManager.getConnection(database);
            
            System.out.println("Connection to SQLite has been established.");
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
	}//connect	

	public void createTable() {
        
        // SQL statement for creating a new table
        String command = "CREATE TABLE IF NOT EXISTS warehouses (\n"
                + "	id integer PRIMARY KEY,\n"
                + "	name text NOT NULL,\n"
                + "	capacity real\n"
                + ");";
        try {
			this.conn = DriverManager.getConnection(database);
			Statement stmt=this.conn.createStatement();
	        stmt.execute(command);
	        System.out.println(command);


        }
       catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
	
	public void getItemID(String item) {
		try {
			conn = DriverManager.getConnection(database);
			String command = SQLquery.IDandName;
			command += " WHERE itemName LIKE '%" + item + "%'";
			//System.out.println(command);
			Statement stmt = conn.createStatement();
			//CachedRowSetImpl crs = new CachedRowSetImpl();
			ResultSet rs = stmt.executeQuery(command);

			debugPrinter(rs);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		} finally {
			try {
				if(conn != null) {conn.close();}
				
			} catch(SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}
	
	public void printList(String rootItem) {
		try {
			conn = DriverManager.getConnection(database);
			
			System.out.println(getResults(rootItem).toString());

		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}

	}
	
	
	@SuppressWarnings("unlikely-arg-type")
	private ArrayList <SearchData> getResults(String searchValue) throws SQLException {
		ArrayList <SearchData> data = new ArrayList<SearchData>();
		//ArrayList <Integer> rows = new ArrayList<Integer>();
		if(searchValue==null || data.contains(searchValue))
			{
				System.out.println("null");
				return data;
			}

		ResultSet rs = queryDB(searchValue);
		if(rs.next()) {
			data.add(new SearchData(searchValue,Integer.parseInt(rs.getString(1))));
		}
		
		rs = queryDB(searchValue);
		while(rs.next()) {
			if(checkBaseCase(rs.getString(2))) {
				data.add(new SearchData(rs.getString(2),Integer.parseInt(rs.getString(1))));
				return data;
			}
		}
		//rs.beforeFirst();
		rs = queryDB(searchValue);
		
		while(rs.next()) {
			data.addAll(getResults(rs.getString(2)));
			return data;
		}
		
		return data;
	}
	
	private boolean checkBaseCase(String searchValue) throws SQLException {
		
		String query = SQLquery.queryItemIsNatural(searchValue);
		PreparedStatement stmt = conn.prepareStatement(query);
		
		ResultSet rs = stmt.executeQuery(); //
		
		while(rs.next()) {
			String holder = rs.getString(1);
			if(holder.contains("1")) {
				return true;
			}
		}
		
		return false;
	}
	
	private ResultSet queryDB(String searchValue) throws SQLException {
		int params = 7;
		String query = SQLquery.queryDistillRecipeData(params);
	//	System.out.println(query);
		
		PreparedStatement stmt = conn.prepareStatement(query);
		for(int i = 1; i <= params; i++) {
			stmt.setString(i, searchValue);
		}	
		return stmt.executeQuery();

	}

	public PreparedStatement StatementPrepper(String query, int params, String searchValue) throws SQLException {
		PreparedStatement stmt = null;
		
		stmt = conn.prepareStatement(query);
		for(int i = 1; i <= params; i++) {
			stmt.setString(i, searchValue);
		}
		System.out.println(stmt.toString());
		
		return stmt;
	}
	
	public void debugPrinter(ResultSet rs) throws SQLException {
		ResultSetMetaData rsms = rs.getMetaData();
		int columns = rsms.getColumnCount();
		for(int i = 0; i<columns; i++) {
			String columnname = rsms.getColumnLabel(i+1);
			System.out.print(columnname + "\t\t");
		}
		System.out.print("\n");
	
		while(rs.next()) {
			for(int i = 0; i<columns; i++) {
				String value = rs.getString(i+1);
				System.out.print(value + "\t\t");
			}
			System.out.print("\n");
		}
		
		rs.close();
	}

	 public static void main(String[] args) {
	     DatabaseHandler items = new DatabaseHandler("test.db");   
	     //items.getItemID("Para");
	     items.printList("Flask (Ethylene)");
	     
	    }
}
