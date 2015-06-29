package Backend;

import java.sql.SQLException;
import java.util.*;

/**
 * "Main" class that launches the Nucleus application.
 *
 * Pulls information down from the database, then performs the steps necessary to organize this data and
 * analyze it so that we can determine how likely infection was between two people.
 *
 * GLOBAL NOTE: As it is currently implemented this iteration has bad space issues. We store interactions multiple times across
 * different people, and each interaction is stored twice before consolidation. I can see this un-connectiveness between two people
 * becoming a problem in a full scale implementation.
 */
public class Runner{

    /**
     * The list of all Interactions stored in the database to be compared.
     */
    private static InteractionsList interactionList = new InteractionsList();

    /**
     * The list of People involved in the Nucleus project.
     */
    private static PeopleList peopleList = new PeopleList();

    /**
     * The DatabaseManager for the database specific to the Nucleus project.
     */
    private static DatabaseManager dbms = new DatabaseManager();

    /**
     * Main method to control the application logic.
     * @param args arguements to the application
     */
    public static void main(String args[]) {

        try{
            dbms.establishConnection();
            peopleList.populateList(dbms);
            interactionList.setPeopleList(peopleList);
            interactionList.populateList(dbms);
        }
        catch(SQLException ex){
            System.out.println("SQL Exception occurred when populating list: " );
            ex.printStackTrace();
        }

        Hashtable<Integer, Person> initialList = peopleList.getPeopleList();
        initializeListWithInteractions(initialList); //When we run this interactionList will not be accurate but peopleList will.
        initialList = null; //Just clearing this up

    }

    /**
     * Get the infection likelihood of a specific interaction
     * NOTE: Must be handled out here after consolidateInteractions has been run for each person.
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

            } catch (PersonNotFoundException e) {
                System.out.println(e.getMessage() + "with personID" + pair.getKey());
            }
        }
    }
}
