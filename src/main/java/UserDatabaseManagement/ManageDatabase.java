package UserDatabaseManagement;

import javax.servlet.*;
import javax.servlet.http.*;

import java.io.*;
import java.sql.*;
import Filters.*;
public class ManageDatabase extends HttpServlet {
	
	DatabaseConnection dc;
    private String deleteOrNot;
    
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter(); 
		String db_name = request.getParameter("db_name");
		dc  = new DatabaseConnection("");
		if (isCorrect(db_name) == true) {
			try {	
				String reqURI = request.getRequestURI();
				String org_name = request.getParameter("org_name");
				    boolean isCorrect = false;
				    if(reqURI.equals("/createDB")) {
				    	isCorrect =createDB(db_name,org_name) ;
				    }else if(reqURI.equals("/renameDB")) {
				    	String rename = request.getParameter("db_rename");
				    	if(isCorrect(rename) == true) {
				    	   isCorrect = renameDB(db_name,org_name,rename);
				    	}
				    	
				    }else if(reqURI.equals("/deleteDB")) {
				    	isCorrect = deleteDB(db_name,org_name);
				    }
					if( isCorrect == true) {
						out.write("");
					}else {
						out.write("");
					}			    
				
			}catch(Exception e) {
			  out.write("");
			} 
			
		}
	}

	private boolean isCorrect(String db_name) {
		boolean correct = db_name.matches("^[a-z][a-z0-9]{2,29}$");
		return correct;
	}

	private boolean createDB(String dbName,String org_name) {
		try {
			String sqlQuery = "";
			sqlQuery = "insert into db_mangament(db_name,db_roles,org_name) values(?,?,?)";
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1, dbName);
			dc.stmt.setString(2, "owner");
			dc.stmt.setString(3, org_name);
			dc.stmt.executeUpdate();				
			sqlQuery = "create database ?";	
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1, dbName);
			dc.stmt.executeUpdate();
			dc.conn.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private boolean renameDB(String dbName,String org_name,String newDb_name) {

		try {
			String sqlQuery = "";
			sqlQuery = "update db_manament set db_name=? where db_name=? and org_name=?";
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1, newDb_name);
			dc.stmt.setString(2, dbName);
			dc.stmt.setString(3, org_name);
			dc.stmt.executeUpdate();				
			sqlQuery = "alter database ? rename to ?";	
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1, dbName);
			dc.stmt.setString(2, newDb_name);
			dc.stmt.executeUpdate();
			dc.conn.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	
	private boolean deleteDB(String dbName,String org_name) {

		try {
			String sqlQuery = "";
			sqlQuery = "delete from db_mangament where db_name=? and org_name =?";
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1, dbName);
			dc.stmt.setString(2, org_name);
			dc.stmt.executeUpdate();
			sqlQuery = "drop database ?";	
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1, dbName);
			dc.stmt.executeUpdate();
			dc.conn.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	

}