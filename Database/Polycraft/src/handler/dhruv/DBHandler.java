package handler.dhruv;
import java.sql.*;
import java.nio.file.Paths;

public final class DBHandler {
	private static DBHandler dbhandler;
	private static String database;
	private static boolean connectionEstablished = false;
	private Connection conn;
	
	
	private DBHandler(String db) {
		connectionEstablished = test_connect(db);
	}
	
	public static DBHandler getInstance() {
		if(dbhandler == null || connectionEstablished == false) {
			return dbhandler = new DBHandler("test.db");
		}
		else return dbhandler;
	}
	
	private boolean test_connect(String db) {
	     conn = null;
	       String path=Paths.get(".").toAbsolutePath().normalize().toString();

	        try {
	            // db parameters
	            database = "jdbc:sqlite:"+path+"/"+db;
	            // create a connection to the database
	            conn = DriverManager.getConnection(database);
	            
	            System.out.println("Connection to SQLite has been established.");
	            return true;
	            
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	            return false;
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
}
