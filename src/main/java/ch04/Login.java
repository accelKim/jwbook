package ch04;



import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class Login extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    super.doGet(req, resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String id = req.getParameter("id");
    String pwd = req.getParameter("pwd");
    System.out.println("id,pwd" + id + "," + pwd);
    HttpSession session = req.getSession();
    session.setAttribute("id", id);
    Object id1 = (String)session.getAttribute("id");
    System.out.println("session id : " + id1);
    resp.sendRedirect("/ch04/loginOk.jsp");

  }
}
