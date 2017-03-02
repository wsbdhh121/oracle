# JDBC
----

- 直接下载eclipse java ee版，lomboz_eclipse插件已淘汰。

## JDBC编程步骤

### 1-Load the Driver
```java
Class.forName() ;
Class.forName().newInstance();
new DriverName();
```
- **实例化时自动向DriverManager注册，不需显式调用DriverManager.registerDriver方法**

- 在Build Path里add External Archiyes,找到oracle对应的jar包classes12.jar，引入，位于C:\app\licheng\product\11.2.0\dbhome_1\oui\jlib
- 上一步操作相当于把classes12.jar放到了该项目的classpath里去了

### 2-Connect to the DataBase
```java
DriverManager.getConnection();
```

### 3-Execute the SQL
- Connection.CreateStatement();
- Statement.executeQuery();——select语句
- Statement.executeUpdate();——insert、update、delete等语句

### 4-Retrieve the result data
- 循环取得结果```while(rs.next());```

### 5-Show the result data
- 将数据库中的各种类型转换为Java中的类型(getXXX)方法

### 6-Close
- close the resultset;
- close the statement;
- close the connection;


### 示例
```java
import java.sql.*;public class TestJDBC {	public static void main(String[] args) throws Exception {		Class.forName("oracle.jdbc.driver.OracleDriver");		//new oracle.jdbc.driver.OracleDriver();		Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:orcl", "scott", "19960121");		Statement stmt = conn.createStatement();		ResultSet rs = stmt.executeQuery("select * from dept");		while(rs.next()){			System.out.println(rs.getString("deptno"));			System.out.println(rs.getInt("deptno"));		}		rs.close();		stmt.close();		conn.close();	}}
```

改进版：

```java
import java.sql.*;public class TestJDBC {	public static void main(String[] args) {		Connection conn = null;		Statement stmt = null;		ResultSet rs = null;		try {			Class.forName("oracle.jdbc.driver.OracleDriver");			// new oracle.jdbc.driver.OracleDriver();			conn = DriverManager.getConnection("jdbc:oracle:thin:@127.0.0.1:1521:orcl", "scott", "19960121");			stmt = conn.createStatement();			rs = stmt.executeQuery("select * from dept");			while (rs.next()) {				System.out.println(rs.getString("deptno"));				System.out.println(rs.getInt("deptno"));			}		} catch (ClassNotFoundException e) {			e.printStackTrace();		} catch (SQLException e) {			e.printStackTrace();		} finally {			try {				if (rs != null) {					rs.close();					rs = null;				}				if (stmt != null) {					stmt.close();					stmt = null;				}				if (conn != null) {					conn.close();					conn = null;				}			} catch (SQLException e) {				e.printStackTrace();			}		}	}}
```

