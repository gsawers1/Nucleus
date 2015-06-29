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
    /**
     * A Hashtable to store the list of Interactions associated with a specific Person's ID.
     */
    private Hashtable<Integer, ArrayList<Interaction>> interactions = new Hashtable<Integer, ArrayList<Interaction>>();

    /**
     * The list of People involved in the Nucleus project.
     */
    private PeopleList personList;

    /**
     * Default constructor for objects in class InteractionsList.
     */
    public InteractionsList() {}

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
     * @param dbms the DatabaseManager corresponding to the database for the Nucleus project
     * @throws SQLException
     */
    public void populateList(DatabaseManager dbms) throws SQLException{
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
                            getDistanceFromLatLonInKm(result.getDouble("L1.Latitude"), result.getDouble("L1.Longitude"),
                                    result.getDouble("L2.Latitude"), result.getDouble("L2.Longitude"))
                    ),
                    new Range((long)result.getInt("L1.TimeAndDate"),(long)result.getInt("L2.TimeAndDate")),
                    false
                    );

                    Interaction nextB = new Interaction(i++,
                    personList.getPerson(personIDB),
                    personList.getPerson(personIDA),
                    new Location(result.getInt("L1.ID"), result.getDouble("L1.Longitude"), result.getDouble("L1.Latitude"),
                            getDistanceFromLatLonInKm(result.getDouble("L1.Latitude"), result.getDouble("L1.Longitude"),
                                    result.getDouble("L2.Latitude"), result.getDouble("L2.Longitude"))
                    ),
                    new Range((long)result.getInt("L2.TimeAndDate"),(long)result.getInt("L1.TimeAndDate")),
                    false
            );

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
     * Retrieves the set of interactions for a specified Person.
     * @param target the Person from which to get the set of interactions.
     * @return The set of interactions that the person is involved in.
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

    /**
     * Setter method for the list of People.
     * @param people the list of People to set.
     */
    public void setPeopleList(PeopleList people){personList = people;}

    /**
     * Sets the list of People to null.
     */
    public void clearPeopleList(){personList = null;}

    /**
     * Get distance in meters between two pairs of latitude and longitude points.
     * @param lat1 latitude of the first point
     * @param lon1 longitude of the first point
     * @param lat2 latitude of the second point
     * @param lon2 longitude of the second point
     * @return the distance in meters between the two pairs of latitude and longitude points
     */
    private double getDistanceFromLatLonInKm(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371; // Radius of the earth in km
        double dLat = deg2rad(lat2-lat1);  // deg2rad below
        double dLon = deg2rad(lon2-lon1);
        double a =
                Math.sin(dLat/2) * Math.sin(dLat/2) +
                        Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) *
                                Math.sin(dLon/2) * Math.sin(dLon/2)
                ;
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c; // Distance in km
        return d;
    }

    /**
     * Convert from degrees to radians.
     * @param deg the degrees to be converted
     * @return the radians equivalent to the specified degrees
     */
    private double deg2rad(double deg) {
        return deg * (Math.PI/180);
    }

    /**
     * Normalize the data value to be between 0-1 using a special constant.
     * @param e data value to be normalized
     * @return the normalized data value
     */
    private double normalize(double e){
        return e/0.02840;
    }
}
