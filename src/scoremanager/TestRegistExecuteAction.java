package scoremanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.StudentDAO;
import dao.TestDAO;
import tool.Action;


public class TestRegistExecuteAction extends Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");

        Map<Integer, String> errors = new HashMap<>();

        //セッションから教師を取得して学校コードを取得
        HttpSession session = request.getSession();
        Teacher teacher = (Teacher) session.getAttribute("teacher");
        School school = teacher.getSchool();

        if (school == null) {
            request.setAttribute("error", "所属学校情報がありません。");
            return "error.jsp";
        }

        // 入力されたテストデータをリストに格納
        List<Test> testList = new ArrayList<>();

        int i = 0;
        while (true) {
            String studentNo = request.getParameter("tests[" + i + "].student.no");
            String classNum = request.getParameter("tests[" + i + "].classNum");
            String subjectCd = request.getParameter("tests[" + i + "].subject.cd");
            String pointStr = request.getParameter("tests[" + i + "].point");
            String noStr = request.getParameter("tests[" + i + "].no");

            if (studentNo == null || classNum == null || subjectCd == null || pointStr == null || noStr == null) {
                break;
            }

            //デバッグログ
            //System.out.println("i=" + i + " studentNo=" + studentNo + " classNum=" + classNum + " subjectCd=" + subjectCd);

            try {
                int point = Integer.parseInt(pointStr);

                if (point < 0 || point > 100) {
                    errors.put(i, "0～100までの数値で入力してください");
                }

                int no = Integer.parseInt(noStr);

                Test test = new Test();

                test.setClassNum(classNum);
                test.setNo(no);
                test.setPoint(point);

                Student student = new Student();
                student.setNo(studentNo);
                test.setStudent(student);

                Subject subject = new Subject();
                subject.setCd(subjectCd);
                test.setSubject(subject);

                test.setSchool(school); //学校情報をセット

                testList.add(test);
            } catch (NumberFormatException e) {
            	errors.put(i, "数値を入力してください");
            }

            i++;
        }

        StudentDAO studentDao = new StudentDAO();
        for (Test test : testList) {
            String studentNo = test.getStudent().getNo();
            Student student = studentDao.get(studentNo);
            if (student != null) {
                test.setStudent(student);
            }
        }

        //エラーがあった場合は登録せずに入力画面へ戻す
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.setAttribute("testList", testList);
            return "test_regist.jsp";  // エラー時は入力画面へ戻す
        }

        // データベースへ保存
        TestDAO dao = new TestDAO();
        boolean result = dao.save(testList);

        // メッセージを設定して完了ページへ
        if (result) {
            request.setAttribute("message", "登録が完了しました。");
        } else {
            request.setAttribute("message", "登録中にエラーが発生しました。");
        }

        return "test_regist_done.jsp";
    }
}
