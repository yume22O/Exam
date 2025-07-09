package scoremanager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.ClassNumDAO;
import dao.StudentDAO;
import dao.SubjectDAO;
import dao.TestDAO;
import tool.Action;

public class TestRegistAction extends Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");

        // セッションからログイン中の教師を取得
        HttpSession session = request.getSession();
        Teacher teacher = (Teacher) session.getAttribute("teacher");

        // Teacherから所属学校情報を取得
        School school = teacher.getSchool();
        if (school == null) {
            request.setAttribute("error", "所属学校情報がありません。");
            return "error.jsp";
        }

        // クラス一覧取得
        ClassNumDAO classDao = new ClassNumDAO();
        List<String> classList = classDao.filter(school);

        // 科目一覧取得
        SubjectDAO subjectDao = new SubjectDAO();
        List<Subject> subjectList = subjectDao.filter(school.getCd());

        // 入学年度一覧取得
        StudentDAO studentDao = new StudentDAO();
        List<Student> studentList = studentDao.filter(school, true);
        Set<Integer> entYearSet = new TreeSet<>((a, b) -> b - a);
        for (Student s : studentList) {
            entYearSet.add(s.getEntYear());
        }
        List<Integer> entYearList = new ArrayList<>(entYearSet);

        // 入力パラメータ取得
        String entYearStr = request.getParameter("f1");
        String classNum = request.getParameter("f2");
        String subjectCd = request.getParameter("f3");
        String noStr = request.getParameter("f4");

        // Subject取得
        Subject selectedSubject = null;
        if (subjectCd != null && !subjectCd.isEmpty()) {
            for (Subject sub : subjectList) {
                if (sub.getCd().equals(subjectCd)) {
                    selectedSubject = sub;
                    break;
                }
            }
        }

        String searched = request.getParameter("searched");

        //テスト結果リストを準備
        List<Test> testList = new ArrayList<>();

        // パラメータが揃っている場合に絞り込み検索
        if (entYearStr != null && !entYearStr.isEmpty()
                && classNum != null && !classNum.isEmpty()
                && selectedSubject != null
                && noStr != null && !noStr.isEmpty()) {
            try {
                int entYear = Integer.parseInt(entYearStr);
                int no = Integer.parseInt(noStr);

                TestDAO testDao = new TestDAO();
                testList = testDao.filter(entYear, classNum, selectedSubject, no, school);
            } catch (NumberFormatException e) {
                //空リスト
            }
        } else if ("true".equals(searched)){ //絞り込み条件に未入力があればエラーを返す
            request.setAttribute("error", "入学年度とクラスと科目と回数を選択してください");
        }

        //学校コードに登録されている回数をすべて取得（重複なし）
        TestDAO testDao = new TestDAO();
        List<Integer> noList = testDao.getTestNosBySchool(school.getCd());

        //JSPへセット
        request.setAttribute("classList", classList);
        request.setAttribute("subjectList", subjectList);
        request.setAttribute("entYearList", entYearList);
        request.setAttribute("testList", testList);
        request.setAttribute("noList", noList);
        //選択された科目と回数をJSPに渡す
        request.setAttribute("selectedSubject", selectedSubject);
        request.setAttribute("selectedNo", noStr);


        return "test_regist.jsp";
    }
}
