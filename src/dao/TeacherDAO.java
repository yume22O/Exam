package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import bean.School;
import bean.Teacher;

/**
 * 教員情報（Teacher）に関するデータベース操作を行うDAOクラスです。
 */
public class TeacherDAO extends DAO {

    /**
     * 教員IDとパスワードを指定して、その教員情報を1件検索します。
     * @param id 教員ID
     * @param password パスワード
     * @return Teacherオブジェクト。該当しなければnull
     * @throws Exception
     */
    public Teacher search(String id, String password) throws Exception {
        Teacher teacher = null;

        Connection con = getConnection();

        // SQL: 教員IDとパスワードで検索
        PreparedStatement st = con.prepareStatement("select * from teacher where id=? and password=?");
        st.setString(1, id);
        st.setString(2, password);
        ResultSet rs = st.executeQuery();

        // 検索結果があればTeacherインスタンス生成し、値をセット
        while (rs.next()) {
            teacher = new Teacher();
            teacher.setId(rs.getString("id"));
            teacher.setName(rs.getString("name"));
            teacher.setPassword(rs.getString("password"));

         // School情報をセット（追加部分）
            String schoolCd = rs.getString("school_cd");
            SchoolDAO sDao = new SchoolDAO();
            School school = sDao.get(schoolCd);     // school_cdからSchoolを取得
            teacher.setSchool(school);
        }

        // 使用したリソースをクローズ
        st.close();
        con.close();
        return teacher;
    }
}