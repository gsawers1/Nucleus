package Backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class PeopleList
{
    private HashSet<Person> people;


    public PeopleList()
    {

    }

    /**
     * Populate the list of people
     * @throws SQLException
     */
    public void populateList(DatabaseManager dbms) throws SQLException {
        String sql = "SELECT * FROM People;";
        ResultSet result;

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
