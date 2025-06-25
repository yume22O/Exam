<%@ page contentType="text/html; charset=UTF-8" %>
<%@include file="../header.html" %>
<link rel="stylesheet" type="text/css" href="../css/login.css">
<!DOCTYPE html>
<html>
<head>
    <title>ログイン</title>
    <link rel="stylesheet" href="login.css">
    <script>
      // パスワード表示切替
      function togglePassword() {
        var pw = document.getElementById('password');
        pw.type = pw.type === 'password' ? 'text' : 'password';
      }
    </script>
</head>
<body>
    <div class="login-container">
        <div class="login-header">ログイン</div>
        <form action="<%=request.getContextPath()%>/scoremanager/LoginExecute.action" method="post" autocomplete="off">
            <div class="login-form-area">
                <div class="login-input-area">
                    <label class="login-label" for="id">ID</label>
                    <input type="text" name="id" id="id" required autocomplete="username">
                    <label class="login-label" for="password">パスワード</label>
                    <input type="password" name="password" id="password" required autocomplete="current-password">
                    <div class="login-password-row">
                        <span></span>
                        <label class="login-showpw">
                            <input type="checkbox" onclick="togglePassword()">
                            パスワードを表示
                        </label>
                    </div>
                </div>
                <div class="login-btn-row">
                    <input type="submit" class="login-btn" value="ログイン">
                </div>
                <% String error = request.getParameter("error");
                   if ("1".equals(error)) { %>
                       <div class="login-error-msg">IDまたはパスワードが違います。</div>
                <% } %>
            </div>
        </form>
    </div>
</body>
</html>