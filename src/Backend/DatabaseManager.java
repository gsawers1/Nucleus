package Backend;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager
{

	Connection conn = null;

	public DatabaseManager() {

	}

	public boolean establishConnection() {

		Properties connectionProps = new Properties();
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		final String url = "jdbc:mysql://localhost:3306/Nucleus";
		final String user = "root";
		final String password = "zq873T!o";

		try {
			conn = DriverManager.getConnection(url+"?user="+user+"&password="+password);
			System.out.println("Connection established");
		}
		catch(SQLException ex) {
			System.out.println("Could not establish connection"  );
			ex.printStackTrace();
			return false;
		}

		return true;
	}

	public ResultSet sendSelectQuery(String query) {
		ResultSet result;
		Statement statement;

		try {
			 statement = conn.createStatement();
			 result = statement.executeQuery(query);
		}
		catch(SQLException ex){
			System.out.println("Server returned an error: ");
			ex.printStackTrace();
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