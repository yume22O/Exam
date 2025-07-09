<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="../css/testregist.css">

<c:import url="base.jsp">
    <c:param name="title">得点管理システム</c:param>
    <c:param name="scripts"></c:param>
    <c:param name="content">
        <section>
            <h2>成績管理</h2>

			<!-- 絞り込みフォーム -->
            <form id="filter" action="TestRegist.action" method="get">
            <input type="hidden" name="searched" value="true" />
                <label>入学年度
                    <select name="f1">
                    	<option value="">------</option>
                        <c:forEach var="y" items="${entYearList}">
                            <option value="${y}">${y}</option>
                        </c:forEach>
                    </select>
                </label>

                <label>クラス
                    <select name="f2">
                        <option value="">------</option>
                        <c:forEach var="c" items="${classList}">
                            <option value="${c}">${c}</option>
                        </c:forEach>
                    </select>
                </label>

                <label>科目
                    <select name="f3">
                        <option value="">------</option>
                        <c:forEach var="s" items="${subjectList}">
                            <option value="${s.cd}">${s.name}</option>
                        </c:forEach>
                    </select>
                </label>

                <label>回数
                    <select name="f4">
                        <option value="">------</option>
        				<c:forEach var="n" items="${noList}">
							<option value="${n}">${n}回目</option>
        				</c:forEach>
                    </select>
                </label>

                <input type="submit" value="検索" class="btn">
            </form>

            <!-- 絞り込みフォーム未入力時　エラーメッセージ表示 -->
            <c:if test="${not empty error}">
				<div>
					${error}
				</div>
			</c:if>

            <!-- 絞り込み結果表示 -->
			<c:if test="${not empty testList}">
			    <form action="TestRegistExecute.action" method="post">
			    	<div><label>科目：${selectedSubject.name}　(${selectedNo}回目)</label></div>
			        <table border="1">
			            <tr>
			                <th>入学年度</th>
			                <th>クラス</th>
			                <th>学生番号</th>
			                <th>氏名</th>
			                <th>点数</th>
			            </tr>
			            <c:forEach var="test" items="${testList}" varStatus="status">
			                <tr>
			                    <td>${test.student.entYear}</td>
			                    <td>
			                        <input type="hidden" name="tests[${status.index}].classNum" value="${test.classNum}" />
			                        ${test.classNum}
			                    </td>
			                    <td>
			                        <input type="hidden" name="tests[${status.index}].student.no" value="${test.student.no}" />
			                        ${test.student.no}
			                    </td>
			                    <td>${test.student.name}</td>
			                    <td>
			                        <input type="text" name="tests[${status.index}].point" value="${test.point}" size="4" />
			                        <input type="hidden" name="tests[${status.index}].no" value="${test.no}" />
			                        <input type="hidden" name="tests[${status.index}].subject.cd" value="${test.subject.cd}" />
			                        <!-- エラーメッセージ表示 -->
    								<c:if test="${errors != null && errors[status.index] != null}">
        								<div style="color: red; font-size: small;">
            								${errors[status.index]}
        								</div>
    								</c:if>
			                    </td>
			                </tr>
			            </c:forEach>
			        </table>
			        <input type="submit" value="登録して終了">
			    </form>
			</c:if>


        </section>
    </c:param>
</c:import>