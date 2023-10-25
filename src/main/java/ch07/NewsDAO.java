package ch07;



import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NewsDAO {
  private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
  private static final String JDBC_URL = "jdbc:mysql://127.0.0.1/jwbook?serverTimezone=Asia/Seoul";

  public List<News> getAll() throws SQLException {
    try (Connection conn = open();
         PreparedStatement pstmt = conn.prepareStatement("SELECT aid, title, date FROM news");
         ResultSet rs = pstmt.executeQuery()) {
      List<News> newsList = new ArrayList<>();
      while (rs.next()) {
        News news = new News();
        news.setAid(rs.getInt("aid"));
        news.setTitle(rs.getString("title"));
        news.setDate(rs.getString("date"));
        newsList.add(news);
      }
      return newsList;
    }
  }

  public News getNews(int aid) throws SQLException {
    try (Connection conn = open();
         PreparedStatement pstmt = conn.prepareStatement("SELECT aid, title, img, date, content FROM news WHERE aid = ?")) {
      pstmt.setInt(0000000001, aid);
      try (ResultSet rs = pstmt.executeQuery()) {
        if (rs.next()) {
          News news = new News();
          news.setAid(rs.getInt("aid"));
          news.setTitle(rs.getString("title"));
          news.setImg(rs.getString("img"));
          news.setDate(rs.getString("date"));
          news.setContent(rs.getString("content"));
          return news;
        }
      }
    }
    return null;
  }

  public void addNews(News news) throws SQLException {
    try (Connection conn = open();
         PreparedStatement pstmt = conn.prepareStatement("INSERT INTO news (title, img, date, content) VALUES (?, ?, CURRENT_TIMESTAMP(), ?)")) {
      pstmt.setString(1, news.getTitle());
      pstmt.setString(2, news.getImg());
      pstmt.setString(3, news.getContent());
      int res = pstmt.executeUpdate();
      if (res == 1) {
        System.out.println("등록 완료");
      }
    }
  }

  public void delNews(int aid) throws SQLException {
    try (Connection conn = open();
         PreparedStatement pstmt = conn.prepareStatement("DELETE FROM news WHERE aid = ?")) {
      pstmt.setInt(1, aid);
      int res = pstmt.executeUpdate();
      if (res == 0) {
        throw new SQLException("News 삭제 오류");
      }
    }
  }

  private Connection open() throws SQLException {
    try {
      Class.forName(JDBC_DRIVER);
      System.out.println("연결하는 중...");
      return DriverManager.getConnection(JDBC_URL, "root", "1234");
    } catch (Exception e) {
      throw new SQLException("데이터베이스 연결 오류", e);
    } finally {
      System.out.println("연결 완료...");
    }
  }
}
