package UserDatabaseManagement;

import javax.servlet.http.*;
import java.sql.*;
import java.util.*;
import Filters.*;

public class ShowDetail extends HttpServlet{
	protected DatabaseConnection dc;
	protected void doGet(HttpServletRequest request,HttpServletResponse response) {
		
	}
	
	public ArrayList<String> getOrganization(long user_id){
		ArrayList<String> organizations = new ArrayList<String>();
		String sqlQuery = "select org_name from where user_id=?";
		try {
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setLong(1, user_id);
			ResultSet rs = dc.stmt.executeQuery();
			while(rs.next()) {
				organizations.add(rs.getString(1));
			}
			return organizations;
		} catch (SQLException e) {
			return new ArrayList<String>();
		}
		
	}
	
	public ArrayList<String> getDatabase(String org_name){
		ArrayList<String> databases = new ArrayList<String>();
		String sqlQuery = "select db_name from db_manament org_name=?";
		try {
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1, org_name);
			ResultSet rs = dc.stmt.executeQuery();
			while(rs.next()) {
				databases.add(rs.getString(1));
			}
			return databases;
		} catch (SQLException e) {
			return new ArrayList<String>();
		}
	}
	
	public ArrayList<String> getTables(String org_name,String db_name){
		ArrayList<String> tables = new ArrayList<String>();
		String sqlQuery = "select table_name from table_management where org_name=? and db_name=?";
		try {
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1,org_name);
			dc.stmt.setString(2,db_name);
			ResultSet rs = dc.stmt.executeQuery();
			while(rs.next()) {
				tables.add(rs.getString(1));
			}
			return tables;
		} catch (SQLException e) {
			return new ArrayList<String>();
		}
	}
	
}
