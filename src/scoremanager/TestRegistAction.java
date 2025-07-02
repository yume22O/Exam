package scoremanager;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.ClassNumDAO;
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


        System.out.println("セッションのuser = " + teacher);



        // Teacherから所属学校情報を取得
        School school = teacher.getSchool();
        if (school == null) {
            // 所属学校情報がない場合の処理（必要なら）
            request.setAttribute("error", "所属学校情報がありません。");
            return "error.jsp";
        }


        // クラス一覧を取得
        ClassNumDAO classDao = new ClassNumDAO();
        List<String> classList = classDao.filter(school);

        // 科目一覧を取得
        SubjectDAO subjectDao = new SubjectDAO();
        List<Subject> subjectList = subjectDao.filter(school.getCd());

        System.out.println("subjectList size = " + subjectList.size());
        for(Subject s : subjectList) {
            System.out.println(s.getCd() + " : " + s.getName());
        }


        //入力パラメータ取得
        String entYearStr = request.getParameter("entYear");
        String classNum = request.getParameter("classNum");
        String subjectCd = request.getParameter("subjectCd");
        String noStr = request.getParameter("no");

        // JSPへセット
        request.setAttribute("classList", classList);
        request.setAttribute("subjectList", subjectList);

        //絞り込み結果
        List<Test> testList = null;

        //パラメータがすべてそろっていたら絞り込み検索を実行
        if (entYearStr != null && !entYearStr.isEmpty() &&
                classNum != null && !classNum.isEmpty() &&
                subjectCd != null && !subjectCd.isEmpty() &&
                noStr != null && !noStr.isEmpty()) {

                int entYear = Integer.parseInt(entYearStr);
                int no = Integer.parseInt(noStr);

                TestDAO testDao = new TestDAO();
                testList = testDao.filter(school.getCd(), entYear, classNum, subjectCd, no);
                request.setAttribute("testList", testList);
            }

        return "test_regist.jsp";

    }
}
