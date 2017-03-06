import java.sql.*;
public class ArticleTree {

	public static void main(String[] args) {
		new ArticleTree().show();
	}
	
	public void show(){
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try{
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1/bbs?useSSL=true&user=root&password=19960121");
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from article where pid = 0");
			while(rs.next()){
				System.out.println(rs.getString("cont"));
				tree(conn, rs.getInt("id"), 1);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}finally{
			try{
				if(rs != null){
					rs.close();
					rs = null;
				}
				if(stmt != null){
					stmt.close();
					stmt = null;
				}
				if(conn != null){
					conn.close();
					conn = null;
				}
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
	}
	
	private void tree(Connection conn, int id, int level){
		Statement  stmt = null;
		ResultSet rs = null;

		StringBuffer strPre = new StringBuffer("");
		for(int i = 0; i < level; i++){
			strPre.append("    ");
		}
		
		try{
			stmt = conn.createStatement();
			String sql = "select * from article where pid =" + id;
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				System.out.println(strPre + rs.getString("cont"));
				tree(conn, rs.getInt("id"), level + 1);
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{
				if(rs != null){
					rs.close();
					rs = null;
				}
				if(stmt != null){
					stmt.close();
					stmt = null;
				}
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
	}
}
