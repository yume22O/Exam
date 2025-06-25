package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.ClassNum;
import bean.School;

/**
 * クラス番号（ClassNum）に関するデータベース操作を行うDAOクラスです。
 * クラス番号の取得、一覧取得、登録、更新ができます。
 */
public class ClassNumDAO extends DAO {

    /**
     * 指定されたクラス番号と学校コードでクラス情報を1件取得します。
     * @param class_num 取得したいクラス番号
     * @param school 学校情報（Schoolオブジェクト）
     * @return 対応するClassNumオブジェクト。無い場合はnull
     * @throws Exception データベース例外等
     */
    public ClassNum get(String class_num, School school) throws Exception {
        // 結果を格納するClassNumインスタンスを用意
        ClassNum classNum = new ClassNum();
        // DB接続を取得
        Connection con = getConnection();
        PreparedStatement st = null;

        try {
            // SQL文を準備（クラス番号と学校コードで検索）
            st = con.prepareStatement("select * from class_num where class_num = ? and school_cd = ?");
            st.setString(1, class_num); // 1番目の?にクラス番号をセット
            st.setString(2, school.getCd()); // 2番目の?に学校コードをセット

            ResultSet rs = st.executeQuery();
            SchoolDAO sDao = new SchoolDAO(); // 学校情報を取得するためのDAO

            if (rs.next()) {
                // レコードが取得できた場合、値をセット
                classNum.setClass_num(rs.getString("class_num"));
                classNum.setSchool(sDao.get(rs.getString("school_cd")));
            } else {
                // レコードが無い場合はnullを返す
                classNum = null;
            }
        } catch (Exception e) {
            // 例外が発生したらそのままスロー
            throw e;
        } finally {
            // 必ずDBリソースをクローズする
            if (st != null) {
                try { st.close(); } catch (SQLException sqle) { throw sqle; }
            }
            if (con != null) {
                try { con.close(); } catch (SQLException sqle) { throw sqle; }
            }
        }
        return classNum;
    }

    /**
     * 指定した学校に所属するクラス番号の一覧を取得します。
     * @param school 学校情報
     * @return クラス番号（String型）のリスト
     * @throws Exception
     */
    public List<String> filter(School school) throws Exception {
        List<String> list = new ArrayList<>();
        Connection con = getConnection();
        PreparedStatement st = null;

        try {
            // SQL: 指定した学校コードのクラス番号を取得し、昇順で並べる
            st = con.prepareStatement("select class_num from class_num where school_cd=? order by class_num");
            st.setString(1, school.getCd());

            ResultSet rs = st.executeQuery();

            // 結果セットから全クラス番号をリストに格納
            while (rs.next()) {
                list.add(rs.getString("class_num"));
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
        return list;
    }

    /**
     * クラス番号を新規登録します。
     * @param classNum 登録したいクラス情報
     * @return 成功した場合はtrue、失敗した場合はfalse
     * @throws Exception
     */
    public boolean save(ClassNum classNum) throws Exception {
        Connection con = getConnection();
        PreparedStatement st = null;
        boolean result = false;

        try {
            // SQL: INSERTで新しいクラス番号を追加
            st = con.prepareStatement("insert into class_num (class_num, school_cd) values (?, ?)");
            st.setString(1, classNum.getClass_num());
            st.setString(2, classNum.getSchool().getCd());

            int cnt = st.executeUpdate(); // 影響行数が0より大きい=成功
            result = cnt > 0;
        } catch (SQLException e) {
            throw e;
        } finally {
            if (st != null) {
                try { st.close(); } catch (SQLException sqle) { throw sqle; }
            }
            if (con != null) {
                try { con.close(); } catch (SQLException sqle) { throw sqle; }
            }
        }
        return result;
    }

    /**
     * 既存のクラス番号を新しい番号に更新します。
     * @param classNum 更新したいクラス情報
     * @param newClassNum 新しいクラス番号
     * @return 成功した場合はtrue、失敗した場合はfalse
     * @throws Exception
     */
    public boolean save(ClassNum classNum, String newClassNum) throws Exception {
        Connection con = getConnection();
        PreparedStatement st = null;
        boolean result = false;

        try {
            // SQL: UPDATEでクラス番号を変更
            st = con.prepareStatement("update class_num set class_num=? where class_num=? and school_cd=?");
            st.setString(1, newClassNum);
            st.setString(2, classNum.getClass_num());
            st.setString(3, classNum.getSchool().getCd());

            int cnt = st.executeUpdate();
            result = cnt > 0;
        } catch (SQLException e) {
            throw e;
        } finally {
            if (st != null) {
                try { st.close(); } catch (SQLException sqle) { throw sqle; }
            }
            if (con != null) {
                try { con.close(); } catch (SQLException sqle) { throw sqle; }
            }
        }
        return result;
    }
}