<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="../css/studentupdate.css">

<c:import url="base.jsp">
    <c:param name="title">得点管理システム</c:param>
    <c:param name="scripts"></c:param>
    <c:param name="content">
        <section>
            <h2>成績管理</h2>

			<!-- 絞り込みフォーム -->
            <form action="TestRegist.action" method="get">
                <label>入学年度：
                    <select name="entYear">
                        <c:forEach var="y" items="${}">
                            <option value="${y}">${y}</option>
                        </c:forEach>
                    </select>
                </label>

                <label>クラス：
                    <select name="classNum">
                        <option value="">------</option>
                        <c:forEach var="c" items="${classList}">
                            <option value="${c}">${c}</option>
                        </c:forEach>
                    </select>
                </label>

                <label>科目：
                    <select name="subjectCd">
                        <option value="">------</option>
                        <c:forEach var="s" items="${subjectList}">
                            <option value="${s.cd}">${s.name}</option>
                        </c:forEach>
                    </select>
                </label>

                <label>回数：
                    <select name="no">
                        <option value="">--選択--</option>
                        <option value="1">1回目</option>
                        <option value="2">2回目</option>
                        <option value="3">3回目</option>
                        <!-- 必要に応じて追加 -->
                    </select>
                </label>

                <input type="submit" value="検索">
            </form>

            <!-- 絞り込み結果表示 -->
            <c:if test="${not empty testList}">
                <table border="1">
                    <tr>
                        <th>生徒番号</th>
                        <th>生徒名</th>
                        <th>クラス</th>
                        <th>科目</th>
                        <th>回数</th>
                        <th>得点</th>
                    </tr>
                    <c:forEach var="test" items="${testList}">
                        <tr>
                            <td>${test.student.no}</td>
                            <td>${test.student.name}</td>
                            <td>${test.classNum}</td>
                            <td>${test.subject.name}</td>
                            <td>${test.no}</td>
                            <td>${test.point}</td>
                        </tr>
                    </c:forEach>
                </table>
            </c:if>


        </section>
    </c:param>
</c:import>