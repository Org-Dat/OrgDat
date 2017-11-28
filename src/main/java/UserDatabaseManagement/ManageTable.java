/**
  * This Servlet used to manage the table.
  * 
  * @author : Obeth Samuel
  * 
  * @Date : 26.11.2017
  */
package UserDatabaseManagement;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import Filters.*;
import com.google.gson.*;
public class ManageTable extends HttpServlet {
	DatabaseConnection dc = null;
	PreparedStatement stmt = null;
	long user_id;
     /**
       * This Method used to split table manage work to other method. 
       * 
       * @Params : request HttpServletRequest , response HttpServletResponse
       * 
       * @Return : this method return the work response.
       */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
	    PrintWriter writer = response.getWriter();
	    try{
	       String org_name = request.getParameter("org_name");
	       String db_name = request.getParameter("db_name");
	       String table_name = request.getParameter("table_name");
	       if(isCorrect(table_name) == false || isCorrect(org_name) == false||isCorrect(db_name) == false){
	           throw new Exception();
	       }
		dc = new DatabaseConnection("OrgDat");
		user_id = getUserId(request.getCookies());
		String reqURI = request.getRequestURI();
		if(reqURI.equals("/createTable") == true){
		    String columns = request.getParameter("columnArray");
		    if(createTable(org_name,db_name,table_name,columns) == true){
		        
		        writer.write("");
		    }else{
		        throw new Exception();
		    }
		 }else if(reqURI.equals("/renameTable") == true){
		     String newTable = request.getParameter("newtable_name");
		     if(isCorrect(newTable) == false){
		         throw new Exception();
		     }
		     if(renameTable(org_name,db_name,table_name,newTable) == true){
		        writer.write("");
		    }else{
		        throw new Exception();
		    } 
		 }else if(reqURI.equals("/deleteTable") == true){
		     if(deleteTable(org_name,db_name,table_name) == true){
		        writer.write("");
		    }else{
		        throw new Exception();
		    }
		 }else{
		     throw new Exception();
		 }
	    }catch(Exception e){
	        writer.write("Bad Request");
	    }
	}
	
    /**
      * This private method used to create new table in database.
      * 
      * @Params :  String org_name , String db_name , String table_name
      * 
      * @Return : if table successfully create return true, else false;
      */ 
      
	private boolean createTable(String org_name, String db_name,
			String table_name,String columnNames) {
		try {
			String sqlQuery = "insert into table_management (roles,table_name,db_name,org_name,user_id) values(?,?,?,?,?)";
			stmt = dc.conn.prepareStatement(sqlQuery);
			stmt.setString(1, "co-owner");
			stmt.setString(2, table_name);
			stmt.setString(3, db_name);
			stmt.setString(4, org_name);
			stmt.setLong(5, user_id);
			stmt.executeUpdate();
			
			JsonParser parser = new JsonParser();
            JsonElement tradeElement = parser.parse(columnNames);
            JsonArray trade = tradeElement.getAsJsonArray();
			dc.dbConnection(db_name);
			sqlQuery = "create table ?(?)";
			StringBuilder s = new StringBuilder("");
			for(JsonElement eachColumn : trade){
			    JsonArray column = eachColumn.getAsJsonArray();
			    String columnType = "";
			    for(JsonElement typeAndName : column){
			        columnType = columnType + typeAndName+" ";
			    }
			    s.append(columnType+",");
			}
			stmt = dc.conn.prepareStatement(sqlQuery);
			stmt.setString(1, table_name);
			stmt.setString(2, s.toString());
			stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
    /**
      * This private method user to find user id
      * 
      * @Params : Cookie[] cookie
      * 
      * @Return : if vaild user return his/her user id ,else return -1
      */
	
	private long getUserId(Cookie[] cookies) {
		ServletContext context = getServletContext();
		for (Cookie cookie : cookies) {
			if ((cookie.getName()).equals("iambdt")) {
				return (long) context.getAttribute(cookie.getValue());
			}
		}
		return -1;
	}
	
    /**
      * This private method used to find table name is correct or wrong
      * 
      * @Params : String table_name
      * 
      * @Return : if name match to regex return true,else return false
      */
      
    private boolean isCorrect(String table_name){
        return table_name.matches("^[a-z][a-z0-9]{3,30}$");
    }
    
    /**
      * This private methos used to delete table from database
      * 
      * @Params : String org_name ,String db_name , String table_name
      * 
      * @Return : if table delete successfully it's return true, else return false 
      */
      
	private boolean deleteTable(String org_name, String db_name,
			String table_name) {
		try {
			String sqlQuery = "delete from table_manament where org_name=? and db_name=? and table_name=?";
			stmt = dc.conn.prepareStatement(sqlQuery);
			stmt.setString(1, org_name);
			stmt.setString(2, db_name);
			stmt.setString(3, table_name);
			stmt.executeUpdate();
			dc.dbConnection(db_name);
			sqlQuery = "drop table ?";
			stmt = dc.conn.prepareStatement(sqlQuery);
			stmt.setString(1, table_name);
			stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
    /**
      * This private method used to rename the table
      * 
      * @Params : String org_name ,String db_name , String oldtable_name 
      * 
      * @Return : if table name successfully changed it's return true, else return false 
      */
	private boolean renameTable(String org_name, String db_name,
			String table_oldname, String table_newname) {
		try {
			String sqlQuery = "update table_manament set table_name=? where org_name=? and db_name=? and table_name=?";
			stmt = dc.conn.prepareStatement(sqlQuery);
			stmt.setString(1, table_newname);
			stmt.setString(2, org_name);
			stmt.setString(3, db_name);
			stmt.setString(4, table_oldname);
			stmt.executeUpdate();
			dc.dbConnection(db_name);
			sqlQuery = "alter table ? rename to ?";
			stmt = dc.conn.prepareStatement(sqlQuery);
			stmt.setString(1, table_oldname);
			stmt.setString(2, table_newname);
			stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}