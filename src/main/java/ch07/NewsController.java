package ch07;

import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

@WebServlet("/news.nhn")
@MultipartConfig(maxFileSize = 1024 * 1024 * 2, location = "c:/Temp/img")
public class NewsController extends HttpServlet {
  private NewsDAO newsDAO;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    newsDAO = new NewsDAO();
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("utf-8");
    String action = req.getParameter("action");

    if (action == null) {
      // 기본 액션 처리
      action = "list";
    }

    String view = null;

    try {
      Method method = this.getClass().getMethod(action, HttpServletRequest.class, HttpServletResponse.class);
      view = (String) method.invoke(this, req, resp);
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (view != null) {
      if (view.startsWith("redirect:/")) {
        view = view.substring("redirect:/".length());
        resp.sendRedirect(view);
      } else {
        req.getRequestDispatcher("/ch07/" + view).forward(req, resp);
      }
    }
  }

  public String addNews(HttpServletRequest req, HttpServletResponse resp) {
    News news = new News();
    try {
      Part part = req.getPart("img");
      if (part != null) {
        String fileName = getSubmittedFileName(part);
        if (fileName != null && !fileName.isEmpty()) {
          // 파일 저장 경로 설정 (웹 어플리케이션 내부 경로)
          String uploadDir = req.getServletContext().getRealPath("/img");
          File uploadPath = new File(uploadDir);

          if (!uploadPath.exists()) {
            uploadPath.mkdirs();
          }

          // 파일명 중복 방지를 위한 고유한 파일명 생성
          String uniqueFileName = generateUniqueFileName(fileName);
          String filePath = uploadPath + File.separator + uniqueFileName;

          part.write(filePath);

          news.setImg("/img/" + uniqueFileName);
        }
        BeanUtils.populate(news, req.getParameterMap());
        newsDAO.addNews(news);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return "redirect:/news.nhn?action=list";
  }
  public String generateUniqueFileName(String originalFileName) {
    // 파일 확장자 추출
    String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

    // UUID를 사용하여 고유한 파일명 생성
    String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

    return uniqueFileName;
  }

  public String list(HttpServletRequest req, HttpServletResponse resp) {
    // 리스트 불러오는 로직 작성
    List<News> newsList;
    try {
      newsList = newsDAO.getAll();
      req.setAttribute("newsList", newsList);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "newsList.jsp"; // 적절한 뷰 이름으로 변경
  }

  public String delNews(HttpServletRequest req, HttpServletResponse resp) {
    // 뉴스 삭제 로직 작성
    int aid = Integer.parseInt(req.getParameter("aid"));
    try {
      newsDAO.delNews(aid);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return "redirect:/news.nhn?action=list";
  }

  public String getNews(HttpServletRequest req, HttpServletResponse resp) {
    // 뉴스 조회 로직 작성
    int aid = Integer.parseInt(req.getParameter("aid"));
    News news = null;
    try {
      news = newsDAO.getNews(aid);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    req.setAttribute("news", news);
    return "newsView.jsp"; // 적절한 뷰 이름으로 변경
  }

  private String getSubmittedFileName(Part part) {
    String contentDisposition = part.getHeader("content-disposition");
    if (contentDisposition == null) {
      return null;
    }
    String[] tokens = contentDisposition.split(";");
    for (String token : tokens) {
      if (token.trim().startsWith("filename")) {
        return token.substring(token.indexOf('=') + 1).trim().replace("\"", "");
      }
    }
    return null;
  }
}