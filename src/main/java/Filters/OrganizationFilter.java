package Filters;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;
import javax.servlet.http.*;

public class OrganizationFilter extends HttpServlet implements Filter{
	protected DatabaseConnection dc;
	RoleChecker roleFinder ;
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
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
			if(org_name == null) {
				throw new Exception();
			}
			String role = roleFinder.orgRole(org_name, user_id);
			 if(role.equals("owner")) {
				chain.doFilter(req, res);
			}else {
				throw new Exception();
			}
		}catch(Exception e) {
			out.write("403.forbidden");
		};
		
	}
   
}
