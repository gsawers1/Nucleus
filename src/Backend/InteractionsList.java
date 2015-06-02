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
         * This needs to be changed! DATEDIFF will not detect changes in DATETIME, need to do it a bit differently
         */
        String sql = "SELECT P1.ID AS IDA, P2.ID AS IDB, L1.ID AS LIDA, L1.Latitude AS LatA,\n" +
                "L1.Longitude AS LongA, L2.Latitude AS LatB, L2.Longitude AS LongB, \n" +
                "L1.Time AS TimeA, L2.Time AS TimeB \n"+
                "FROM People P1, People P2, Locations L1, Locations L2\n" +
                "WHERE (L1.Longitude - L2.Longitude < 2 AND L1.Longitude - L2.Longitude > -2)\n" +
                "AND (L1.Latitude - L2.Latitude < 2 AND L1.Latitude - L2.Latitude > -2)\n" +
                "AND L1.Person <> L2.Person\n" +
                "AND DATEDIFF(L1.Time, L2.Time) = 0;\n";
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
