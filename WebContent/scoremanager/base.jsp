<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><c:out value="${param.title}" /></title>
    <link rel="stylesheet" href="../css/menu.css">
    <c:if test="${not empty param.scripts}">
      ${param.scripts}
    </c:if>
</head>
<body>
    <!-- ヘッダー -->
    <div class="header-bar">
        <div class="header-inner">
            <span class="header-title">得点管理システム</span>
            <span class="header-user">
                ${teacher.id} 様
                <a class="header-logout" href="logout.jsp">ログアウト</a>
            </span>
        </div>
    </div>
    <div class="main-content">
        <!-- 共通サイドメニュー -->
        <nav class="menu-side">
            <a href="menu.jsp">メニュー</a>
            <a href="StudentList.action">学生管理</a>
            <a href="TestRegist.action">成績登録</a>
            <a href="TestList.action">成績参照</a>
            <a href="SubjectList.action">科目管理</a>
        </nav>
        <!-- 各ページごとの内容（contentパラメータ） -->
        <main class="menu-center">
            <c:out value="${param.content}" escapeXml="false"/>
        </main>
    </div>
    <!-- フッター -->
    <div class="footer-bar">
      © 2023 TIC<br>
      大原学園
    </div>
</body>
</html>