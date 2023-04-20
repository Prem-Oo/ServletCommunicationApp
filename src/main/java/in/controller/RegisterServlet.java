package in.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet(
		urlPatterns = { "/reg" }, 
		initParams = { 
				@WebInitParam(name = "jdbcURL", value = "jdbc:mysql://localhost:3306/localdb"), 
				@WebInitParam(name = "user", value = "root"), 
				@WebInitParam(name = "password", value = "mysql@123")
		})
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	Connection connection=null;
	PreparedStatement pstmt=null;

	@Override
	public void init() throws ServletException {
		ServletConfig config = getServletConfig();
		String url = config.getInitParameter("jdbcURL");
		String user = config.getInitParameter("user");
		String password = config.getInitParameter("password");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			 connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int rowsEffected=0;
		String name = request.getParameter("name");
		int age=Integer.parseInt(request.getParameter("age")) ;
		String number=request.getParameter("number");
		String query="insert into servletRegistration(`name`,`age`,`number`) values(?,?,?)";
			
		if(connection!=null) {
			try {
				pstmt=connection.prepareStatement(query);
				response.setContentType("text/html");
				PrintWriter out = response.getWriter();
				
				
				if(age<19 || age>30) {
					// send error page
				response.sendError(504, "age is not sufficient");
					
				}
				else {
					// register successful
					if(pstmt!=null) {
						pstmt.setString(1, name);
						pstmt.setInt(2, age);
						pstmt.setString(3, number);
						 rowsEffected = pstmt.executeUpdate();
						if(rowsEffected==1) {
							out.println("<html><head><title>Output</title></head>");
							out.println("<body align='center'>");
							out.println("<h1 style='color:green'> Registration Succesful</h1>");
							out.println("<table>");
							out.println("<tr><td>Name</td><td>"+name+"</td></tr>");
							out.println("<tr><td>age</td><td>"+age+"</td></tr>");
							out.println("<tr><td>number</td><td>"+number+"</td></tr>");
							out.println("</table>");
							out.println("</body>");
							out.println("</html>");
							out.close();
							}
						 
					}
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

}
