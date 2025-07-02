package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Test;

public class TestDAO extends DAO {

    public List<Test> filter(String schoolCd, int entYear, String classNum, String subjectCd, int no) {
        List<Test> list = new ArrayList<>();

        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            con = getConnection();

            String sql =
            	    "SELECT t.student_no, t.class_num, t.subject_cd, t.school_cd, t.no, t.point, " +
            	    "s.name AS student_name, sub.name AS subject_name, sch.name AS school_name " +
            	    "FROM test t " +
            	    "JOIN student s ON t.student_no = s.no " +
            	    "JOIN subject sub ON t.subject_cd = sub.cd " +
            	    "JOIN school sch ON t.school_cd = sch.cd " +
            	    "WHERE t.school_cd = ? AND s.ent_year = ? AND t.class_num = ? AND t.subject_cd = ? AND t.no = ?";


            st = con.prepareStatement(sql);
            st.setString(1, schoolCd);
            st.setInt(2, entYear);
            st.setString(3, classNum);
            st.setString(4, subjectCd);
            st.setInt(5, no);

            rs = st.executeQuery();

            while (rs.next()) {
                Test test = new Test();

                // 生徒情報
                Student stu = new Student();
                stu.setNo(rs.getString("student_no"));
                stu.setName(rs.getString("student_name"));
                test.setStudent(stu);

                // 科目
                Subject sub = new Subject();
                sub.setCd(rs.getString("subject_cd"));
                sub.setName(rs.getString("subject_name"));
                test.setSubject(sub);

                // 学校
                School sch = new School();
                sch.setCd(rs.getString("school_cd"));
                sch.setName(rs.getString("school_name"));
                test.setSchool(sch);

                // その他
                test.setClassNum(rs.getString("class_num"));
                test.setNo(rs.getInt("no"));
                test.setPoint(rs.getInt("point"));

                list.add(test);
            }

        } catch (Exception e) {
            System.err.println("TestDAO.filter() エラー: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (Exception e) {
                System.err.println("リソース解放エラー: " + e.getMessage());
            }
        }

        return list;
    }
}
