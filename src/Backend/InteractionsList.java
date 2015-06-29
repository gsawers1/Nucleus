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
        String sql = "SELECT L1.ID, L2.ID, L1.Latitude,\n"+
                "L1.Longitude, L2.Latitude, L2.Longitude,\n" +
                "L1.TimeAndDate, L2.TimeAndDate, L1.Person, L2.Person \n" +
                "FROM Locations L1, Locations L2\n" +
                "WHERE (L1.Longitude - L2.Longitude < .00002 AND L1.Longitude - L2.Longitude > -.00002)\n" +
                "AND (L1.Latitude - L2.Latitude < .00002 AND L1.Latitude - L2.Latitude > -.00002)\n"+
                "AND L1.Person <> L2.Person\n"+
                "AND (L1.TimeAndDate - L2.TimeAndDate < 360000 AND  L1.TimeAndDate - L2.TimeAndDate > -360000);\n";

        ResultSet result;
        ArrayList<Interaction> currentSet;

        result = dbms.sendSelectQuery(sql);
        boolean areentrys = result.next();
        int i = 0;
        while(areentrys){
            int personIDA= result.getInt("L1.Person");
            int personIDB = result.getInt("L2.Person");
            //System.out.println("Current Person" + personIDA);
            Interaction nextA = new Interaction(i++,
                    personList.getPerson(personIDA),
                    personList.getPerson(personIDB),
                    new Location(result.getInt("L1.ID"), result.getDouble("L1.Longitude"), result.getDouble("L1.Latitude"),
                            Math.max(result.getDouble("L1.Latitude") - result.getDouble("L2.Latitude"),
                                    result.getDouble("L1.Longitude") - result.getDouble("L2.Longitude"))),
                    new Range((long)result.getInt("L1.TimeAndDate"),(long)result.getInt("L2.TimeAndDate")),
                    false
                    );

            Interaction nextB = new Interaction(i++,
                    personList.getPerson(personIDB),
                    personList.getPerson(personIDA),
                    new Location(result.getInt("L2.ID"), result.getDouble("L2.Longitude"), result.getDouble("L2.Latitude"),
                            Math.max(result.getDouble("L2.Latitude") - result.getDouble("L1.Latitude"),
                                    result.getDouble("L2.Longitude") - result.getDouble("L1.Longitude"))),
                    new Range((long)result.getInt("L2.TimeAndDate"),(long)result.getInt("L1.TimeAndDate")),
                    false
            );
            /**
             * Probably need to change this a bit, like change Location distance to the distance formula.
             */
            currentSet = interactions.get(personIDA);
            if(currentSet == null){
                currentSet = new ArrayList<Interaction>();
            }
            currentSet.add(nextA);
            interactions.put(personIDA, currentSet);

            currentSet = interactions.get(personIDB);
            if(currentSet == null){
                currentSet = new ArrayList<Interaction>();
            }
            currentSet.add(nextB);
            interactions.put(personIDB, currentSet);

            areentrys = result.next();

        }
        System.out.println(interactions.size());
    }

    /**
     * Retrieves the set of interactions for a specified person.
     * @param target
     * @return The Set of interactions that the person is involved in.
     */
    public TreeSet<Interaction> getInteractionByPerson(Person target){
        System.out.println("Creating Set for:" + target.getID());
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
