package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Student;

/**
 * 生徒情報（Student）に関するデータベース操作を担当するDAOクラスです。
 */
public class StudentDAO extends DAO {
    // 基本的な検索SQL。学校コードで絞り込む
    private String baseSql = "select * from student where school_cd=?";

    /**
     * 生徒番号で生徒情報を1件取得します。
     * @param no 生徒番号
     * @return 生徒のStudentオブジェクト。該当なければnull
     * @throws Exception
     */
    public Student get(String no) throws Exception {
        Student student = new Student();
        Connection con = getConnection();
        PreparedStatement st = null;

        try {
            // SQL: 生徒番号で検索
            st = con.prepareStatement("select * from student where no=?");
            st.setString(1, no);
            ResultSet rs = st.executeQuery();

            SchoolDAO schoolDao = new SchoolDAO(); // 学校情報の取得用

            if (rs.next()) {
                // レコードが取得できた場合、各情報をセット
                student.setNo(rs.getString("no"));
                student.setName(rs.getString("name"));
                student.setEntYear(rs.getInt("ent_year"));
                student.setClassNum(rs.getString("class_num"));
                student.setIsAttend(rs.getBoolean("is_attend"));
                // 学校情報をセット
                student.setSchool(schoolDao.get(rs.getString("school_cd")));
            } else {
                // レコードがなければnull
                student = null;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (st != null) {
                try { st.close(); } catch (SQLException sqle) { throw sqle; }
            }
            if (con != null) {
                try { con.close(); } catch (SQLException sqle) { throw sqle; }
            }
        }
        return student;
    }

    /**
     * ResultSetから生徒リストを作成する補助メソッド（内部用）
     * @param rSet DB取得結果
     * @param school 学校情報
     * @return Studentのリスト
     */
    private List<Student> postFilter(ResultSet rSet, School school) throws Exception {
        List<Student> list = new ArrayList<>();
        try {
            // 1行ずつStudentに変換してリスト化
            while (rSet.next()) {
                Student student = new Student();
                student.setNo(rSet.getString("no"));
                student.setName(rSet.getString("name"));
                student.setEntYear(rSet.getInt("ent_year"));
                student.setClassNum(rSet.getString("class_num"));
                student.setIsAttend(rSet.getBoolean("is_attend"));
                student.setSchool(school);
                list.add(student);
            }
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 指定条件で生徒のリストを取得（入学年、クラス、在籍状態）
     */
    public List<Student> filter(School school, int entYear, String classNum, boolean isAttend) throws Exception {
        List<Student> list = new ArrayList<>();
        Connection con = getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;

        // 絞り込み条件をSQLに追加
        String condition = " and ent_year=? and class_num=?";
        String order = " order by no asc";
        String conditionIsAttend = "";

        if (isAttend) {
            conditionIsAttend = " and is_attend=true";
        }
        try {
            // 条件を組み合わせてSQL実行
            st = con.prepareStatement(baseSql + condition + conditionIsAttend + order);
            st.setString(1, school.getCd());
            st.setInt(2, entYear);
            st.setString(3, classNum);
            rs = st.executeQuery();
            list = postFilter(rs, school);
        } catch (Exception e) {
            throw e;
        } finally {
            if (st != null) {
                try { st.close(); } catch (SQLException sqle) { throw sqle; }
            }
            if (con != null) {
                try { con.close(); } catch (SQLException sqle) { throw sqle; }
            }
        }

        return list;
    }

    /**
     * 指定条件で生徒のリストを取得（入学年、在籍状態）
     */
    public List<Student> filter(School school, int entYear, boolean isAttend) throws Exception {
        List<Student> list = new ArrayList<>();
        Connection con = getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        String condition = " and ent_year=?";
        String order = " order by no asc";
        String conditionIsAttend = "";

        if (isAttend) {
            conditionIsAttend = " and is_attend=true";
        }
        try {
            st = con.prepareStatement(baseSql + condition + conditionIsAttend + order);
            st.setString(1, school.getCd());
            st.setInt(2, entYear);
            rs = st.executeQuery();
            list = postFilter(rs, school);
        } catch (Exception e) {
            throw e;
        } finally {
            if (st != null) {
                try { st.close(); } catch (SQLException sqle) { throw sqle; }
            }
            if (con != null) {
                try { con.close(); } catch (SQLException sqle) { throw sqle; }
            }
        }

        return list;
    }

    /**
     * 指定条件で生徒のリストを取得（在籍状態のみ）
     */
    public List<Student> filter(School school, boolean isAttend) throws Exception {
        List<Student> list = new ArrayList<>();
        Connection con = getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        String order = " order by no asc";
        String conditionIsAttend = "";

        if (isAttend) {
            conditionIsAttend = " and is_attend=true";
        }
        try {
            st = con.prepareStatement(baseSql + conditionIsAttend + order);
            st.setString(1, school.getCd());
            rs = st.executeQuery();
            list = postFilter(rs, school);
        } catch (Exception e) {
            throw e;
        } finally {
            if (st != null) {
                try { st.close(); } catch (SQLException sqle) { throw sqle; }
            }
            if (con != null) {
                try { con.close(); } catch (SQLException sqle) { throw sqle; }
            }
        }
        return list;
    }

    /**
     * 生徒情報を新規登録または更新します。
     * @param student 対象の生徒情報
     * @return 成功時true、失敗時false
     * @throws Exception
     */
    public boolean save(Student student) throws Exception {
        Connection con = getConnection();
        PreparedStatement st = null;

        int count = 0;

        try {
            // 既存データがあるかチェック
            Student old = get(student.getNo());
            if (old == null) {
                // 新規登録
                st = con.prepareStatement("insert into student(no, name, ent_year, class_num, is_attend, school_cd) values(?, ?, ? ,? ,?, ?)");
                st.setString(1, student.getNo());
                st.setString(2, student.getName());
                st.setInt(3, student.getEntYear());
                st.setString(4, student.getClassNum());
                st.setBoolean(5, student.isAttend());
                st.setString(6, student.getSchool().getCd());
            } else {
                // 更新
                st = con.prepareStatement("update student set name=?, ent_year=?, class_num=?, is_attend=? where no=?");
                st.setString(1, student.getName());
                st.setInt(2, student.getEntYear());
                st.setString(3, student.getClassNum());
                st.setBoolean(4, student.isAttend());
                st.setString(5, student.getNo());
            }
            count = st.executeUpdate();

        } catch (Exception e) {
            throw e;
        } finally {
            if (st != null) {
                try { st.close(); } catch (SQLException sqle) { throw sqle; }
            }
            if (con != null) {
                try { con.close(); } catch (SQLException sqle) { throw sqle; }
            }
        }

        return count > 0;
    }
}