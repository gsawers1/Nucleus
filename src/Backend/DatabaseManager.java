package Backend;

import java.sql.*;
import java.util.Properties;

public class DatabaseManager
{
	/**
	 * The Connection to the specific database for the Nucleus project.
	 */
	Connection conn = null;

    /**
     * Default constructor for Objects of class DatabaseManager.
     */
	public DatabaseManager() {}

    /**
     * Establishes a connection with the database.
     * @return true if the connection was successful, false for a failure
     */
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

    /**
     * Sends a Select Query to the database.
     * @param query the Query to be sent
     * @return the ResultSet from this Query
     */
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

    /**
     * Populate the tables of the database according the specified Query.
     * @param query the query to populate the tables
     */
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