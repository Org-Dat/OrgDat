package Filters;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class DatabaseFilter extends HttpServlet implements Filter {
	protected DatabaseConnection dc;
	RoleChecker roleFinder ;
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		roleFinder = new RoleChecker();
		PrintWriter out = response.getWriter();
		try {			
			Cookie[] cookies = request.getCookies();
			long user_id =  roleFinder.getUserId(cookies);
			if(user_id == -1) {
				throw new Exception();
			}
			dc = new DatabaseConnection("Orgdat");
			String org_name = request.getParameter("org_name");
			String db_name = request.getParameter("db_name");
			if(org_name == null) {
				throw new Exception();
			}
			String role = roleFinder.dbRole(org_name, db_name, user_id);
			 if(role == null) {
				role = roleFinder.orgRole(org_name, user_id);
				if(role.contains("owner")) {
					chain.doFilter(req, res);
				}else {
					throw new Exception();
				}
			}else if(role.contains("owner")) {
				chain.doFilter(req, res);
			}else {
				throw new Exception();
			}
			
		}catch(Exception e) {
			out.write("403.forbidden");
		}
   }
}
	