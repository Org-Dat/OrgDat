package Filters;

import java.sql.*;


public class DatabaseConnection {
	private String url;
	private final String user = "postgres";
	private final String password = "";
	public  Connection conn = null;
	public PreparedStatement stmt = null; 
	 public DatabaseConnection(String database_name){
	       dbConnection(database_name);   
	   } 
	   
	   public void dbConnection(String db_name){
	       url = ""+db_name;
	       	try {
				Class.forName("org.postgresql.Driver");
				this.conn = DriverManager.getConnection(url, user, password);
			} catch (SQLException sqlEx) {
				return;
			} catch (ClassNotFoundException e) {
				return;
			}
	   }
}
