/**
 * This Servlet is managing the Organization.
 * 
 */
package UserDatabaseManagement;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;

import Filters.*;
public class ManageOrganization extends HttpServlet {
	
	String deleteOrNot = "";
	DatabaseConnection dc;
	protected void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException {
         PrintWriter out = response.getWriter();
         
        try{
            String orgName = request.getParameter("org_name");              
			String reqURI=request.getRequestURI();
            if (isCorrect(orgName) == true){
                boolean isCorrect = false;
                if(reqURI.equals("/createOrg")) {
                	isCorrect = createOrg(orgName);
                }else if(reqURI.equals("/renameOrg")) {
                	
                }else if(reqURI.equals("/deleteOrg")) {
                	ArrayList<String> databases = getDatabases(orgName);
                	isCorrect= deleteOrg(databases,orgName);
                }
                if(isCorrect == true) {
                	out.write("Organization successfully create ");
                }else {
                	out.write("Organization creation failed ");
                }                
            }else{
                out.write("Organization creation failed ");
            }
        }catch(Exception ce){
            out.write("Organization creation failed ");
        }
	}

	private boolean isCorrect(String orgName) {
		boolean correct = orgName.matches("^[a-z0-9]{2,25}$");
		return correct;
	}

	private ArrayList<String> getDatabases(String org_name){
		ArrayList<String> databaseNameList = new ArrayList<String>();
		String sqlQuery = "selete db_name from db_manament where org_name=?";
		try {
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1, org_name);
			ResultSet db_names = dc.stmt.executeQuery();
			while(db_names.next()) {
				databaseNameList.add(db_names.getString(1));
			}
		} catch (SQLException e) {
			return  new ArrayList<String>();
		}
		return databaseNameList;
	}
	private boolean createOrg(String name) throws ClassNotFoundException {
		
		try {
			 String sqlQuery = "insert into org_mangament(org_name,org_roles) values(?,?)";
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1, name);
			dc.stmt.setString(2, "owner");
			dc.stmt.executeUpdate();
			dc.conn.close();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	private boolean renameOrg(String new_name,String old_name) {
		try {
			String sqlQuery = "update org_manament set org_name=? where org_name=?";
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1, new_name);
			dc.stmt.setString(2, old_name);
			dc.stmt.executeUpdate();
			dc.conn.close();
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	private boolean deleteOrg(ArrayList<String> databaseNames,String org_name) {
		try {
			String sqlQuery = "delete from db_mangament where db_name=?";
			dc.stmt = dc.conn.prepareStatement(sqlQuery);
			dc.stmt.setString(1, org_name);
			dc.stmt.executeUpdate();
			Statement stmt = dc.conn.createStatement();
			for(String database : databaseNames) {
				stmt.executeUpdate("drop database "+database);
			}
			
			dc.conn.close();
			return true;
		}catch(Exception e) {
			return false;
		}
	}
}