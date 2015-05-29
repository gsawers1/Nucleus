package Backend;

import java.sql.Date;

public class Person
{
    private int ID;

    private String firstName;

    private String lastName;

    private boolean infected;

    private Date timeReported;

    public Person(int ID, String first, String last, boolean infected, Date time){
        this.ID = ID;
        firstName = first;
        lastName = last;
        this.infected = infected;
        timeReported = time;
    }

}
