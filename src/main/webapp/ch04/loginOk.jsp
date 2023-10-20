<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
</head>
<body>
<h2>Hello World!</h2>
<%
    String id = "";
    id =session.getAttribute("id").toString()
    //out.println(id);
    if(id.equals("")) {
    out.println("id를 입력해주세요");
    } else {
    out.println(id + "님 반갑습니다.");
    }
%>
</body>
</html>