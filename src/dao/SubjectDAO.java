
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Subject;



public class SubjectDAO extends DAO {

	//科目コードと学校コードを引数で渡して、科目情報を返す
	public Subject get(String cd, School school) {
		Subject subject = null;
    	String sql = "SELECT cd, name, school_cd FROM subject WHERE cd = ? AND school_cd = ?";

    	try (Connection con = getConnection();
    			PreparedStatement ps = con.prepareStatement(sql)) {

    				ps.setString(1, cd);
    				ps.setString(2, school.getCd());

    				try (ResultSet rs = ps.executeQuery()) {
    					if (rs.next()) {
    						subject = new Subject();
    						subject.setCd(rs.getString("cd"));
    						subject.setName(rs.getString("name"));
    						subject.setSchool(rs.getString("school_cd"));
    					}
    				}

    	} catch (SQLException e) {
    		e.printStackTrace(); // 必要に応じてログ記録やカスタム例外化へ変更可能
    	} catch (Exception e1) {
    		// TODO 自動生成された catch ブロック
    		e1.printStackTrace();
    	}
    	return subject;
	}




	//学校コードから科目listを取得
    public List<Subject> filter(String school){
        List<Subject> list = new ArrayList<>();

        Connection con = null;
        PreparedStatement st = null;
        ResultSet rs = null;


        try {
        	con = getConnection();
        	st = con.prepareStatement(
        			"select * from subject where school_cd = ?"
        			);
        	st.setString(1, school);
        	rs = st.executeQuery();

        	while (rs.next()) {
                Subject s = new Subject();
                s.setCd(rs.getString("cd"));
                s.setName(rs.getString("name"));
                s.setSchool(rs.getString("school_cd"));
                list.add(s);
            }
        }catch (Exception e) {
        	System.err.println("SubjectDAOエラー:" + e.getMessage());
        	e.printStackTrace();
        }finally{
        	try{
        		if (rs != null) rs.close();
        		if (st != null) st.close();
        		if (con != null) con.close();
        	}catch (Exception e){
        		System.err.println("リソースの解放エラー:" + e.getMessage());
        	}
        }

        return list;
    }


    public boolean save(Subject subject) throws SQLException {
        String sql = "INSERT INTO grade (school_cd, subject_cd, student_cd, score) VALUES (?, ?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, subject.getSchool());     // 学校コード
            ps.setString(2, subject.getCd());            // 科目コード
            ps.setString(3, subject.getName());    // 学生コード（Subject ではなく Grade DTO を使う想定）

            int cnt = ps.executeUpdate();
            return cnt > 0;
        } catch (Exception e) {
        	// TODO 自動生成された catch ブロック
        	e.printStackTrace();
        }
        return false;
    }


    public boolean delete(Subject subject) throws SQLException {
        String sql = "DELETE FROM grade WHERE school_cd = ? AND subject_cd = ? AND student_cd = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, subject.getSchool());    // 学校コード
            ps.setString(2, subject.getCd());           // 科目コード
            ps.setString(3, subject.getName());   // 学生コード

            int count = ps.executeUpdate();
            return count > 0;  // 1行以上削除されたら true を返す
        } catch (Exception e) {
        	// TODO 自動生成された catch ブロック
        	e.printStackTrace();
        }
        return false;
    }


}
