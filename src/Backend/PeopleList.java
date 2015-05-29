package Backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class PeopleList
{
    private HashSet<Person> people;

    private DatabaseManager dbms = new DatabaseManager();

    public PeopleList()
    {

    }

    public void populateList() throws SQLException {
        String sql = "SELECT * FROM People;";
        ResultSet result;
        boolean connected = dbms.establishConnection();

        if(connected){
            result = dbms.sendSelectQuery(sql);
            while(!result.isLast()){
                Person next = new Person(result.getInt("ID"),
                                         result.getString("FirstName"),
                                         result.getString("LastName"),
                                         result.getBoolean("Infected"),
                                         result.getDate("TimeReported"));
                people.add(next);

            }
        }


    }
}
