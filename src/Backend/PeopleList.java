package Backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

public class PeopleList
{
    private HashMap<Integer,Person> people = new HashMap<Integer, Person>(); //TODO: Change this data structure to something more iterable


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
            int ID = result.getInt("ID");
            Person next = new Person(result.getInt("ID"),
                                     result.getString("FirstName"),
                                     result.getString("LastName"),
                                     result.getBoolean("Infected"),
                                     result.getDate("TimeReported"));
            people.put(ID, next);
            areentrys = result.next();
        }
    }

    public HashMap<Integer, Person> getPeopleList(){return people;}

    public Person getPerson(int id){
        return people.get(id);
    }

    public void assignInteractions(Person person, TreeSet<Interaction> interactions) throws PersonNotFoundException{
        Person update = people.get(person.getID());
        if(update == null)
            throw new PersonNotFoundException("Person not found in the list");
        else {
            update.setInteractionSet(interactions);
            people.put(update.getID(), update);
        }
    }
}
