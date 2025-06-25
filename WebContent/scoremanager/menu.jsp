<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:import url="base.jsp">
    <c:param name="title">得点管理システム メニュー</c:param>
    <c:param name="scripts"></c:param>
    <c:param name="content">
        <div class="menu-title">メニュー</div>
        <div class="menu-cards">
            <div class="menu-card menu-student">
                <a href="StudentList.action">学生管理</a>
            </div>
            <div class="menu-card menu-score">
                <div class="menu-score-title">成績管理</div>
                <a href="TestRegist.action">成績登録</a>
                <a href="TestList.action">成績参照</a>
            </div>
            <div class="menu-card menu-subject">
                <a href="SubjectList.action">科目管理</a>
            </div>
        </div>
    </c:param>
</c:import>