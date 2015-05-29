import java.sql.*;
public class DatabaseManager{
	
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
		};


		Connection conn = null;
		String url = "jdbc:mysql@csc-db0.csc.calpoly.edu/";
		String user = "gsawers"
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
}