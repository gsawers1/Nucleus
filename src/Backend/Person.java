package Backend;

import java.sql.Date;
import java.util.TreeSet;

public class Person
{
    private int ID;

    private String firstName;

    private String lastName;

    private boolean infected;

    private Date timeReported;

    private TreeSet<Interaction> personalInteractionSet = new TreeSet<Interaction>();

    public Person(int ID, String first, String last, boolean infected, Date time){
        this.ID = ID;
        firstName = first;
        lastName = last;
        this.infected = infected;
        timeReported = time;
    }
    public int getID(){return ID;}

    public void setInteractionSet(TreeSet<Interaction> set){
        personalInteractionSet = set;
    }
}
