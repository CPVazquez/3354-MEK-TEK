package databasehandler;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.nio.file.Paths;


public class DatabaseHandler{
	
	static String dbtest="chinook.db";
	private String database;
	private Connection conn;
	
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

	 public static void main(String[] args) {
	     DatabaseHandler items = new DatabaseHandler("test.db");   
	     items.createTable();
	    }
}
