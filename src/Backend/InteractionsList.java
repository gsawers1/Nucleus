package Backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.TreeSet;
public class InteractionsList
{
    private TreeSet<Interaction> interactions = new TreeSet<Interaction>();
    private DatabaseManager dbms = new DatabaseManager();

    public InteractionsList()
    {

    }

    public void populateList() throws SQLException{
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
        boolean connected = dbms.establishConnection();

        if(connected){
            result = dbms.sendSelectQuery(sql);
            boolean areentrys = result.next();
            int i = 0;
            while(areentrys){
                Interaction next = new Interaction(i++,
                        result.getInt("IDA"),
                        result.getInt("IDB"),
                        new Location(result.getInt("LIDA"), result.getInt("LongA"), result.getInt("LatA"),
                                (double) Math.max(result.getInt("LatA") - result.getInt("LatB"),
                                        result.getInt("LongA") - result.getInt("LongB"))),
                        new Range(result.getDate("TimeA"),result.getDate("TimeB")),
                        0
                        );
                interactions.add(next);
                areentrys = result.next();
            }
        }
    }
}
