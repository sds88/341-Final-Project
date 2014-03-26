package employee_Interface;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class MySQLConnection {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://129.22.171.242:3306/project";
	private final String USER;
	private final String PASS;
	
	public Connection conn = null;
	public Statement stmt = null;
	public ResultSet rs = null;
	
	public MySQLConnection(String username, String password)
	{
		USER = username;
		PASS = password;
		try {
			Class.forName(JDBC_DRIVER);
		} catch (ClassNotFoundException e) {
			System.err.println("Driver not found.");
			e.printStackTrace();
			return;
		}
		
		try {
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch (SQLException e) {
			System.err.println("Connection failed.");
			e.printStackTrace();
			return;
		}
	}

	public void ExecuteQuery(String sqlString)
	{
		if(conn == null)
		{
			System.err.println("Connection not yet established.");
			return;
		}
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sqlString);
		} catch(SQLException e) {
			System.err.println("SQLException: " + e.getMessage());
		}
		PrintResultSet(rs);
		return;
	}
	
	public void ExecuteNonQuery(String sqlString)
	{
		if(conn == null)
		{
			System.err.println("Connection not yet established.");
			return;
		}
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(sqlString);
		} catch(SQLException e) {
			System.out.println("SQLException: " + e.getMessage());
		} finally {
			if(stmt != null)
			{
				try {
					stmt.close();
				} catch (SQLException e) {}
				stmt = null;
			}
		}
	}
	
	private void PrintResultSet(ResultSet rs)
	{
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int numberOfColumns = rsmd.getColumnCount();
			for (int i = 1; i <= numberOfColumns; i++) 
			{
				if (i > 1) 
					System.out.print(",  ");
	        	String columnName = rsmd.getColumnName(i);
	        	System.out.print(columnName);
			}
			System.out.println("");
	  
			while (rs.next()) {
		        for (int i = 1; i <= numberOfColumns; i++) 
		        {
		        	if (i > 1) 
		        		System.out.print(",  ");
		        	String columnValue = rs.getString(i);
		        	System.out.print(columnValue);
		        }
		        System.out.println("");  
			}
	    } catch(SQLException e) {
	    	System.err.print("SQLException: " + e.getMessage());
	    }  
	}
	
	public void CloseConnection()
	{
		if(conn != null)
		{
			try {
				conn.close();
			} catch (SQLException e) {}
			conn = null;
		}
		if(stmt != null)
		{
			try {
				stmt.close();
			} catch (SQLException e) {}
			stmt = null;
		}
		if(rs != null)
		{
			try {
				rs.close();
			} catch (SQLException e) {}
			rs = null;
		}
	}

}
