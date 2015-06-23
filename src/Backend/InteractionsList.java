package Backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.TreeSet;

/**
 * Class to hold all of the interactions found in the database.
 *
 * Uses a big SQL select statement to find interactions in the database and from there populates them into a Hashtable.
 *
 * Can then be used to sort the interactions based on who was involved in them.
 */
public class InteractionsList
{
    private Hashtable<Integer, ArrayList<Interaction>> interactions = new Hashtable<Integer, ArrayList<Interaction>>();
    private PeopleList personList;

    public InteractionsList()
    {

    }

    /**
     * Performs the steps to create the interactions Hashtable.
     * First pulls down all interactions from the database using the SQL Query
     * Then creates Interaction objects based on the information retrieved from the database
     * Stores these objects inside of ArrayLists inside the HashMap organized by the ID of the primary person in the interaction.
     *
     * This way of organizing the Hashtable gives us O(1) retrieval time from the HashMap and O(n) time to copy into an organized TreeSet.
     *
     * While this makes our space constraint higher, the time it saves on searching the Hashtable is potentially worth it.
     *
     * @param dbms
     * @throws SQLException
     */
    public void populateList(DatabaseManager dbms) throws SQLException{
        /***
         * Needs to be checked
         * TODO: Need to modify Database schema so TimeOfDay is an INT type not a TIME type
         */
        String sql = "SELECT P1.ID AS IDA, P2.ID AS IDB, L1.ID AS LIDA, L1.Latitude AS LatA,\n" +
                "L1.Longitude AS LongA, L2.Latitude AS LatB, L2.Longitude AS LongB, \n" +
                "L1.TimeAndDate AS TimeA, L2.TimeAndDate AS TimeB\n"+
                "FROM People P1, People P2, Locations L1, Locations L2\n" +
                "WHERE (LongA - LongB < .00002 AND LongA - LongB > .00002)\n" +
                "AND (LatA - LatB < .00002 AND LatA - LatB > .00002)\n" +
                "AND L1.Person <> L2.Person\n" +
                "AND TimeA-TimeB BETWEEN 300000 AND -300000 \n" +
                "AND L1.Person = P1.ID \n" +
                "AND L2.Person = P2.ID;";

        ResultSet result;
        ArrayList<Interaction> currentSet;

        result = dbms.sendSelectQuery(sql);
        boolean areentrys = result.next();
        int i = 0;
        while(areentrys){
            int personID = result.getInt("IDA");
            Interaction next = new Interaction(i++,
                    personList.getPerson(result.getInt("IDA")),
                    personList.getPerson(result.getInt("IDB")),
                    new Location(result.getInt("LIDA"), result.getInt("LongA"), result.getInt("LatA"),
                            (double) Math.max(result.getInt("LatA") - result.getInt("LatB"),
                                    result.getInt("LongA") - result.getInt("LongB"))),
                    new Range((long)result.getInt("TimeA"),(long)result.getInt("TimeB")),
                    false
                    );
            /**
             * Probably need to change this a bit, like change Location distance to the distance formula.
             */

            currentSet = interactions.get(personID);
            currentSet.add(next);
            interactions.put(personID, currentSet);
            areentrys = result.next();

        }
    }

    /**
     * Retrieves the set of interactions for a specified person.
     * @param target
     * @return The Set of interactions that the person is involved in.
     */
    public TreeSet<Interaction> getInteractionByPerson(Person target){
        TreeSet<Interaction> returnSet = new TreeSet<Interaction>();
        ArrayList<Interaction> targetSet;
        int id = target.getID();

        targetSet = interactions.get(id);
        for(Interaction i : targetSet)
            returnSet.add(i);

        return returnSet;
    }

    public void setPeopleList(PeopleList people){personList = people;}
    public void clearPeopleList(){personList = null;}
}
