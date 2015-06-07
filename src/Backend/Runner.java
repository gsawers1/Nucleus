package Backend;

import java.sql.SQLException;

public class Runner {

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

        for(Person, )


    }
}
