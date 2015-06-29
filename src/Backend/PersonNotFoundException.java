package Backend;

/**
 * PersonNotFoundException specifies the error that the Person
 * being sought either does not exist or is not contained in the
 * list being searched.
 */
public class PersonNotFoundException extends Exception{

    /**
     * Constructor for Objects of class PersonNotFoundException.
     * @param message the message to be associated with the throwing of this Exception
     */
    public PersonNotFoundException(String message) {
        super(message);
    }
}
