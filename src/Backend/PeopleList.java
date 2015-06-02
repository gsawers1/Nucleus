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

    /**
     * Populate the list of people
     * @throws SQLException
     */
    public void populateList() throws SQLException {
        String sql = "SELECT * FROM People;";
        ResultSet result;
        boolean connected = dbms.establishConnection();

        if(connected){
            result = dbms.sendSelectQuery(sql);
            boolean areentrys = result.next();
            while(areentrys){
                Person next = new Person(result.getInt("ID"),
                                         result.getString("FirstName"),
                                         result.getString("LastName"),
                                         result.getBoolean("Infected"),
                                         result.getDate("TimeReported"));
                people.add(next);
                areentrys = result.next();

            }
        }


    }
}
