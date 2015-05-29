package Backend;

import java.sql.*;
public class DatabaseManager{

	Connection conn;
	public DatabaseManager()
	{

	}

	public void establishConnection(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch (ClassNotFoundException ex)
		{
			System.out.println("Driver not found");
		}


		String url = "jdbc:mysql@csc-db0.csc.calpoly.edu/";
		String user = "gsawers";
		String password = "zq873T!o";

		try {
			conn = DriverManager.getConnection(url+user+"?user="+user+"&password="+password);
		}
		catch(SQLException ex){
			System.out.println("Could not establish connection");
			return;
		}
		System.out.println("Connection established");
	}

	public ResultSet sendSelectQuery(String query){
		ResultSet result;
		Statement statement;
		try {
			 statement = conn.createStatement();
			result = statement.executeQuery(query);
		}
		catch(SQLException ex){
			System.out.println("Server returned an error: " + ex.getErrorCode());
			return null;
		}

		return result;
	}

	public void populateTables(String query){
		try {
			PreparedStatement statement = conn.prepareStatement(query);
			statement.executeUpdate();
		}
		catch(SQLException ex){
			System.out.println("Server returned an error with code: " + ex.getErrorCode());
		}
	}
}