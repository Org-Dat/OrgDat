package Filters;
import java.sql.*;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;

public class RoleChecker extends HttpServlet {
	DatabaseConnection dc;
	
    public String orgRole(String org_name,long user_id) {
    	dc = new DatabaseConnection("OrgDat");
    	String role =null;
    	String sqlQuery = "select roles from org_management where user_id=? and org_name=?";
    	try {
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setLong(1, user_id);
			dc.stmt.setString(2, org_name);
			ResultSet roles = dc.stmt.executeQuery();
			while(roles.next()) {
				role = roles.getString(1);
			}
			return role;
		} catch (SQLException e) {
			return role;
		}
    }
    
    public String dbRole(String org_name,String db_name,long user_id) {
    	dc = new DatabaseConnection("OrgDat");
    	String role =null;
    	String sqlQuery = "select roles from db_management where user_id=? and org_name=? and db_name=?";
    	try {
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setLong(1, user_id);
			dc.stmt.setString(2, org_name);
			dc.stmt.setString(3,db_name);
			ResultSet roles = dc.stmt.executeQuery();
			while(roles.next()) {
				role = roles.getString(1);
			}
			return role;
		} catch (SQLException e) {
			return role;
		}    	
    }
    
    public String tableRole(String org_name,String db_name,String table_name,long user_id) {
    	dc = new DatabaseConnection("OrgDat");
    	String role =null;
    	String sqlQuery = "select roles from table_management where user_id=? and org_name=? and db_name=? and table_name=?";
    	try {
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setLong(1, user_id);
			dc.stmt.setString(2, org_name);
			dc.stmt.setString(3,db_name);
			dc.stmt.setString(4, table_name);
			ResultSet roles = dc.stmt.executeQuery();
			while(roles.next()) {
				role = roles.getString(1);
			}
			return role;
		} catch (SQLException e) {
			return role;
		}  	
    }
    
    public long getUserId(Cookie[] cookies) {
		ServletContext context=getServletContext(); 
		for(Cookie cookie : cookies) {
			if(cookie.getName().equals("iambdt")) {
				return (long)context.getAttribute(cookie.getValue());
			}
		}
		return -1;
	}
}
