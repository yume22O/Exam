<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="../css/studentupdate.css">

<c:import url="base.jsp">
    <c:param name="title">得点管理システム</c:param>
    <c:param name="scripts"></c:param>
    <c:param name="content">
        <section>
            <h2>学生情報変更</h2>

            <!-- <p>student.classNum: <c:out value="${student.classNum}" /></p>
            <p>class_num_setの中身:</p>
			<ul>
			  <c:forEach var="num" items="${class_num_set}">
			    <li>${num}</li>
			  </c:forEach>
			</ul> -->

			<form action="StudentUpdateExecute.action" method="post">
                <input type="hidden" name="no" value="${student.no}" />
				<div>
					<label>入学年度</label><br>
	  				<input type="text" id="entYear" name="ent_year" value="${student.entYear}" readonly>
				</div>

				<div>
                <label>学生番号</label><br>
  				<input type="text" id="no" name="no" value="${student.no}" readonly>
				</div>

				<div>
                <label>氏名</label><br>
                <input type="text" id="name" name="name" value="${student.name}" required/>
                <c:if test="${not empty errorname}">
                	<p style="color: red;">${errorname}</p>
                </c:if>
				</div>

                <div>
                    <label>クラス</label><br>
                    <select id="class_num" name="class_num">
                        <c:forEach var="num" items="${class_num_set}">
                            <option value="${num}" <c:if test="${num == student.classNum}">selected</c:if>>${num}</option>
                        </c:forEach>
                    </select>
                </div>

                <div>
                    <label>
                        在学中<input type="checkbox" name="is_attend" value="true" <c:if test="${student.attend}">checked</c:if> />
                    </label>
                </div>

                <div><button type="submit" name="login">変更</button></div>

                <div><a href="StudentList.action">戻る</a></div>

            </form>
        </section>
    </c:param>
</c:import>