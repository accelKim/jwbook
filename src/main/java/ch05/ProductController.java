package ch05;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebServlet("/pcontrol")
public class ProductController extends HttpServlet {
  ProductService service;

  public ProductController() {
    service = new ProductService();

  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String action = req.getParameter("action");
    String view = "";
    if (action == null) {
      getServletContext().getRequestDispatcher("/pcontrol?action=list")
          .forward(req, resp);
    } else if (action.equals("list")) {
      view = list(req, resp);
    } else if (action.equals("info")) {
      view = info(req, resp);
    } else if (action.equals("doDelete")) {
      doDelete(req, resp);
    } else if (action.equals("doPut")) {
      doPut(req, resp);
    }
    getServletContext().getRequestDispatcher("/ch05/" + view)
        .forward(req, resp);
  }

  private String list(HttpServletRequest req, HttpServletResponse resp) {
    req.setAttribute("products", service.findAll());
    return "productList.jsp";
  }

  private String info(HttpServletRequest req, HttpServletResponse resp) {
    req.setAttribute("p", service.findById(req.getParameter("id")));
    return "productInfo.jsp";
  }

  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    super.doDelete(req, resp);
  }

  @Override
  protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    super.doPut(req, resp);
  }
}



