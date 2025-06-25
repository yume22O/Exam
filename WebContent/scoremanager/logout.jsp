<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:import url="base.jsp">
    <c:param name="title">得点管理システム</c:param>
    <c:param name="content">
        <section class="logout-section">
            <div class="logout-row">
                <span class="logout-label">ログアウト</span>
            </div>
            <div class="logout-message">
                ログアウトしました
            </div>
            <div class="logout-login-link">
                <a href="Login.action">ログイン</a>
            </div>
        </section>
    </c:param>
    <c:param name="scripts"></c:param>
</c:import>