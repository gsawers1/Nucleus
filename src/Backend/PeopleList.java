package Backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.TreeSet;

public class PeopleList
{
    /**
     * Hashtable of People involved in the Nucleus project, mapped from ID to Person.
     */
    private Hashtable<Integer,Person> people = new Hashtable<Integer, Person>();

    /**
     * Default constructor for Objects of class PeopleList.
     */
    public PeopleList() {}

    /**
     * Populate the list of people.
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
                                     (long)result.getInt("TimeReported"));
            people.put(ID, next);
            areentrys = result.next();
        }
    }

    /**
     * Accessor method for the Hashtable of People composing this PeopleList.
     * @return the Hashtable of People
     */
    public Hashtable<Integer, Person> getPeopleList(){return people;}

    /**
     * Accessor method for the Person associated with the specified ID.
     * @param id the ID of th Person to search for
     * @return the Person associated with the specified ID
     */
    public Person getPerson(int id){
        return people.get(id);
    }

    /**
     * Use to assign the interactionSets for each individual person.
     * @param person The person to assign the set to.
     * @param interactions The set to assign to the person.
     * @throws PersonNotFoundException Throws an exception if the person is not in the PeopleList.
     */
    public void assignInteractions(Person person, TreeSet<Interaction> interactions) throws PersonNotFoundException{
        Person update = people.get(person.getID());
        if(update == null)
            throw new PersonNotFoundException("Person not found in the list");
        else {
            update.setInteractionSet(interactions);
            update.consolidateInteractions();

            /**
             * Run build infection chance twice.
             * First time to get initial comparisons, second time to smooth out any bad outliers.
             */
            update.buildRelationshipInfectionChance();
            update.printRelationships();
            people.put(update.getID(), update);
        }
    }
}
