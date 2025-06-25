<%@page contentType="text/html; charset=UTF-8" %>
<%@include file="../header.html" %>
<%@include file="base.jsp" %>

${teacher.id} すでにログアウトしています。

<form action="login.jsp" method="get">
  <button type="submit">ログイン</button>
</form>


<%@include file="../footer.html" %>
