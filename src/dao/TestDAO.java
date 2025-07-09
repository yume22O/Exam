package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Test;

public class TestDAO extends DAO {

	public List<Test> filter(int entYear, String classNum, Subject subject, int num, School school) {
	    List<Test> list = new ArrayList<>();

	    String schoolCd = school.getCd();
	    String subjectCd = subject.getCd();

	    try (Connection con = getConnection()) {

	        String sql =
	            "SELECT t.student_no, t.class_num, t.subject_cd, t.school_cd, t.no, t.point, " +
	            "s.name AS student_name, s.ent_year, " +
	            "sub.name AS subject_name, sch.name AS school_name " +
	            "FROM test t " +
	            "JOIN student s ON t.student_no = s.no " +
	            "JOIN subject sub ON t.subject_cd = sub.cd " +
	            "JOIN school sch ON t.school_cd = sch.cd " +
	            "WHERE t.school_cd = ? AND s.ent_year = ? AND t.class_num = ? AND t.subject_cd = ? AND t.no = ?";

	        PreparedStatement st = con.prepareStatement(sql);
	        st.setString(1, schoolCd);
	        st.setInt(2, entYear);
	        st.setString(3, classNum);
	        st.setString(4, subjectCd);
	        st.setInt(5, num);

	        ResultSet rs = st.executeQuery();

	        while (rs.next()) {
	            Test test = new Test();

	            //生徒情報
	            Student student = new Student();
	            student.setNo(rs.getString("student_no"));
	            student.setName(rs.getString("student_name"));
	            student.setEntYear(rs.getInt("ent_year"));
	            test.setStudent(student);

	            //科目
	            Subject sub = new Subject();
	            sub.setCd(rs.getString("subject_cd"));
	            sub.setName(rs.getString("subject_name"));
	            test.setSubject(sub);

	            //学校
	            School sch = new School();
	            sch.setCd(rs.getString("school_cd"));
	            sch.setName(rs.getString("school_name"));
	            test.setSchool(sch);

	            test.setClassNum(rs.getString("class_num"));
	            test.setNo(rs.getInt("no"));
	            test.setPoint(rs.getInt("point"));

	            list.add(test);
	        }

	        rs.close();
	        st.close();

	    } catch (Exception e) {
	        System.err.println("TestDAO.filter() エラー: " + e.getMessage());
	        e.printStackTrace();
	    }

	    return list;
	}





    public List<Test> postFilter(ResultSet rSet, School school) throws SQLException {
        List<Test> list = new ArrayList<>();
        while (rSet.next()) {
            Test test = new Test();

            //生徒
            Student student = new Student();
            student.setNo(rSet.getString("student_no"));
            student.setName(rSet.getString("student_name"));
            test.setStudent(student);

            //科目
            Subject subject = new Subject();
            subject.setCd(rSet.getString("subject_cd"));
            subject.setName(rSet.getString("subject_name"));
            test.setSubject(subject);

            //学校
            test.setSchool(school);

            test.setClassNum(rSet.getString("class_num"));
            test.setNo(rSet.getInt("no"));
            test.setPoint(rSet.getInt("point"));

            list.add(test);
        }
        return list;
    }

    //回数noを重複なしで取得
    public List<Integer> getTestNosBySchool(String schoolCd) {
        List<Integer> noList = new ArrayList<>();

        String sql = "SELECT DISTINCT no FROM test WHERE school_cd = ? ORDER BY no";

        try (Connection con = getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            st.setString(1, schoolCd);

            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    noList.add(rs.getInt("no"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return noList;
    }

    //
    public boolean save(List<Test> list) {
        boolean success = true;

        String sql = "MERGE INTO test (student_no, class_num, subject_cd, school_cd, no, point) " +
                "KEY(student_no, class_num, subject_cd, school_cd, no) " +
                "VALUES (?, ?, ?, ?, ?, ?)";


        try (Connection con = getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            for (Test test : list) {
                if (test.getStudent() == null || test.getSubject() == null || test.getSchool() == null) {
                    continue; //必須情報がない場合はスキップ
                }

                System.out.println("save() : studentNo=" + test.getStudent().getNo() + ", classNum=" + test.getClassNum()
                    + ", subjectCd=" + test.getSubject().getCd() + ", schoolCd=" + test.getSchool().getCd()
                    + ", no=" + test.getNo() + ", point=" + test.getPoint());

                st.setString(1, test.getStudent().getNo());
                st.setString(2, test.getClassNum());
                st.setString(3, test.getSubject().getCd());
                st.setString(4, test.getSchool().getCd());
                st.setInt(5, test.getNo());
                st.setInt(6, test.getPoint());

                st.addBatch(); //バッチ登録
            }

            st.executeBatch(); //一括実行

        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }

        return success;
    }


}
