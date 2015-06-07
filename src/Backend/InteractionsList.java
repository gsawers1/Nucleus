package Backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;


public class InteractionsList
{
    private HashMap<Integer, ArrayList<Interaction>> interactions = new HashMap<Integer, ArrayList<Interaction>>();

    public InteractionsList()
    {

    }

    /**
     * Performs the steps to create the interactions HashMap.
     * First pulls down all interactions from the database using the SQL Query
     * Then creates Interaction objects based on the information retrieved from the database
     * Stores these objects inside of ArrayLists inside the HashMap organized by the ID of the primary person in the interaction.
     *
     * This way of organizing the HashMap gives us O(1) retrieval time from the HashMap and O(n) time to copy into an organized TreeSet.
     *
     * While this makes our space constraint higher, the time it saves on searching the HashMap is potentially worth it.
     *
     * @param dbms
     * @throws SQLException
     */
    public void populateList(DatabaseManager dbms) throws SQLException{
        /***
         * Needs to be checked
         */
        String sql = "SELECT P1.ID AS IDA, P2.ID AS IDB, L1.ID AS LIDA, L1.Latitude AS LatA,\n" +
                "L1.Longitude AS LongA, L2.Latitude AS LatB, L2.Longitude AS LongB, \n" +
                "L1.TimeOfDay AS TimeA, L2.TimeOfDay AS TimeB, L1.Day AS DayA, L2.Day AS DayB \n"+
                "FROM People P1, People P2, Locations L1, Locations L2\n" +
                "WHERE (LongA - LongB < 2 AND LongA - LongB > -2)\n" +
                "AND (LatA - LatB < 2 AND LatA - LatB > -2)\n" +
                "AND L1.Person <> L2.Person\n" +
                "AND DATEDIFF(DayA, DayB) = 0" +
                "AND TIME_TO_SEC(TIMEDIFF(TimeA,TimeB)) < 300 ;\n";

        ResultSet result;
        ArrayList<Interaction> currentSet;

        result = dbms.sendSelectQuery(sql);
        boolean areentrys = result.next();
        int i = 0;
        while(areentrys){
            int personID = result.getInt("IDA");
            Interaction next = new Interaction(i++,
                    personID,
                    result.getInt("IDB"),
                    new Location(result.getInt("LIDA"), result.getInt("LongA"), result.getInt("LatA"),
                            (double) Math.max(result.getInt("LatA") - result.getInt("LatB"),
                                    result.getInt("LongA") - result.getInt("LongB"))),
                    new Range(result.getDate("TimeA"),result.getDate("TimeB")),
                    0
                    );

            currentSet = interactions.get(personID);
            currentSet.add(next);
            interactions.put(personID, currentSet);
            areentrys = result.next();

        }
    }

    /**
     * Retrieves the set of interactions for a specified person.
     * @param target
     * @return
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
}
