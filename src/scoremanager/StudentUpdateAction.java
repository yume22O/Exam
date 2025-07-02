package scoremanager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.School;
import bean.Student;
import dao.ClassNumDAO;
import dao.StudentDAO;
import tool.Action;

public class StudentUpdateAction extends Action {
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//リクエストパラメータから学生番号取得
		String no = request.getParameter("no");

		//学生情報を取得
		StudentDAO dao = new StudentDAO();
		Student student = dao.get(no);

		//学校情報からクラス一覧を取得
		School school = student.getSchool();
		ClassNumDAO classnumdao = new ClassNumDAO();
		List<String> classnumlist = classnumdao.filter(school);

		/*学校情報がnullかチェック＆ログ出力
	    if (school == null) {
	        System.out.println("Student no=" + no + " の学校情報がnullです");
	    } else {
	        System.out.println("Student no=" + no + " の学校コード：" + school.getCd());
	    }*/

		request.setAttribute("student", student);
		request.setAttribute("class_num_set", classnumlist);

		return "student_update.jsp";
	}
}