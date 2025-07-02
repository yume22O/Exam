package scoremanager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.School;
import bean.Student;
import dao.StudentDAO;
import tool.Action;

public class StudentUpdateExecuteAction extends Action {
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");

		//フォームから値を取得
		String no = request.getParameter("no");
		String name = request.getParameter("name");
		String classNum = request.getParameter("class_num");
		int entYear = Integer.parseInt(request.getParameter("ent_year"));
		//チェックボックス　チェックされていなければnull
		String isAttendParam = request.getParameter("is_attend");
		boolean isAttend = isAttendParam != null;


		//セッションから学校情報を取得
		School school = (School) request.getSession().getAttribute("school");

		//studentオブジェクトに設定
		Student student = new Student();
		student.setNo(no);
		student.setName(name);
		student.setEntYear(entYear);
		student.setClassNum(classNum);
		student.setIsAttend(isAttend);
		student.setSchool(school);

		//DBに保存
		StudentDAO dao = new StudentDAO();
		dao.save(student);

		return "student_update_done.jsp";


	}
}