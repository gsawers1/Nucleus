package Backend;

import java.sql.SQLException;
import java.util.*;

public class Runner{

    private static InteractionsList interactionList = new InteractionsList();
    private static PeopleList peopleList = new PeopleList();
    private static DatabaseManager dbms = new DatabaseManager();

    public static void main(String args[]){

        try{
            dbms.establishConnection();
            peopleList.populateList(dbms);
            interactionList.populateList(dbms);
        }
        catch(SQLException ex){
            System.out.println("SQL Exception occurred when populating list: " + ex.getErrorCode());
        }

        Hashtable<Integer, Person> initialList = peopleList.getPeopleList();
        initializeListWithInteractions(initialList);
        initialList = null; //Just clearing this up

    }

    /**
     * Get the infection likelihood of a specific interaction
     * NOTE: Must be handled out here after consolodateInteractions has been run for each person.
     */
    public static void getInfectionLikeliHood(){}

    /**
     * Puts all the found interactions inside each person object in the peopleList.
     * @param list The list of all people returned from the database
     */
    public static void initializeListWithInteractions(Hashtable<Integer, Person> list){
        Iterator it = list.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            TreeSet<Interaction> next = interactionList.getInteractionByPerson((Person) pair.getValue());
            try {
                peopleList.assignInteractions((Person) pair.getValue(), next);
                ((Person) pair.getValue()).consolodateInteractions();
            } catch (PersonNotFoundException e) {
                System.out.println(e.getMessage() + "with personID" + pair.getKey());
            }
        }
    }
}
