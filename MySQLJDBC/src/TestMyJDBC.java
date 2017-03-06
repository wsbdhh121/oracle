import java.sql.*;

public class TestMyJDBC {

	public static void main(String[] args) {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		try {
			new com.mysql.jdbc.Driver();
			conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1/mydata?useSSL=false&" + "user=root&password=19960121");
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from article");
			while(rs.next()){
				System.out.println(rs.getString("id"));
			}
		}catch (SQLException e) {
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

}
