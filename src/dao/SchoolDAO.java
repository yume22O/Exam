package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bean.School;

/**
 * 学校情報（School）に関するデータベース操作を行うDAOクラスです。
 */
public class SchoolDAO extends DAO {
    /**
     * 学校コードで学校情報を1件取得します。
     * @param cd 学校コード
     * @return Schoolオブジェクト。該当なしはnull
     * @throws Exception
     */
    public School get(String cd) throws Exception {
        School school = new School();
        Connection con = getConnection();
        PreparedStatement st = null;

        try {
            // SQL: 学校コードで検索
            st = con.prepareStatement("select * from school where cd = ?");
            st.setString(1, cd);

            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                // レコードがあれば情報をセット
                school.setCd(rs.getString("cd"));
                school.setName(rs.getString("Name")); // DBのカラム名に注意
            } else {
                // 無ければnullを返す
                school = null;
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

        return school;
    }
}