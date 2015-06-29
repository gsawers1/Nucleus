package Backend;

public class Interaction implements Comparable<Interaction> {

    /**
     * The ID of this Interaction.
     */
    private final Integer ID;

    /**
     * The first Person involved in this Interaction.
     */
    private Person personA;

    /**
     * the second Person involved in this Interaction.
     */
    private Person personB;

    /**
     * The place where this Interaction took place.
     */
    private Location place;

    /**
     * The time range in which this Interaction took place.
     */
    private Range timePeriod;

    /**
     * Boolean representing whether or not this Interaction has already been sorted when being compared
     */
    private boolean checked; //Boolean check to see if interaction has been sorted yet. Used for comparing.

    /**
     *  Conversion factor of milliseconds in an hour.
     */
    private final int LOCATION_UPDATE_TIME = 3600000;

    /**
     * Constructor for Objects of class Interaction.
     * @param ID the ID of this Interaction
     * @param personA the first Person involved in this Interaction
     * @param personB the second Person involved in this Interaction
     * @param place the place where this Interaction took place
     * @param time the time range in which this Interaction took place
     * @param checked boolean representing whether or not this Interaction has already been sorted when being compared
     */
    public Interaction(int ID, Person personA, Person personB, Location place, Range time, boolean checked){
        this.ID = ID;
        this.personA = personA;
        this.personB = personB;
        this.place = place;
        this.timePeriod = time;
        this.checked = checked;
    }

    /**
     * Accessor method for the Location.
     * @return the Location of this Interaction
     */
    public Location getPlace(){return place;}

    /**
     * Accessor method for the Range.
     * @return the Range of this Interaction
     */
    public Range getTimePeriod(){return timePeriod;}

    /**
     * Accessor method for ID of Person B.
     * @return the integer ID of Person B of this Interaction
     */
    public int getPersonBID(){return personB.getID();}

    /**
     * Accessor method for Person B.
     * @return the Person B of this Interaction
     */
    public Person getPersonB(){return personB;}

    /**
     * Set the boolean 'checked' to true.
     */
    public void setChecked(){
        checked = true;
    }


    /**
     * Checks to see if this interaction involves the given person, determined by ID.
     * @param targetId The ID of the person to check for
     * @return True if the person is in the interaction, otherwise False
     */
    public boolean contains(int targetId){
        if(targetId == personA.getID() || targetId == personB.getID())
            return true;

        return false;
    }

    /**
     * Compares this Interaction to the specified one based on infection likelihood.
     * @param other the other interaction to compare to
     * @return -1 if Likelihood of the other interaction is higher, otherwise 1. No tie-breaker has yet been decided.
     */
    @Override
    public int compareTo(Interaction other){
        Range otherTime = other.getTimePeriod();

        if(checked) {
            if (timePeriod.getDuration() > otherTime.getDuration())
                return -1;
            else
                return 1;
        }
        else {
            if (timePeriod.getLowerBound() < otherTime.getLowerBound())
                return -1;
            else
                return 1;
        }
    }


    /**
     * Use to compare two objects for equality when combining interactions.
     * @param other The other interaction
     * @return true if the two Interactions are considered equal, false otherwise
     */
    public boolean shallowEquals(Interaction other){
        Range otherPeriod = other.getTimePeriod();

        /**
         * Fair warning this if is ugly...
         *
         * What it does though is compare people and place for strict equality, then compares if the time ranges
         * are close to each other.
         */
        if(other.contains(personA.getID()) && other.contains(personB.getID()) && place.equals(other.getPlace())
                && ((timePeriod.getLowerBound()-otherPeriod.getUpperBound() < LOCATION_UPDATE_TIME
                        && timePeriod.getLowerBound()-otherPeriod.getUpperBound() > (-1 * LOCATION_UPDATE_TIME))
                    || (otherPeriod.getLowerBound()- timePeriod.getUpperBound() < LOCATION_UPDATE_TIME)
                        && otherPeriod.getLowerBound()- timePeriod.getUpperBound() < (-1 * LOCATION_UPDATE_TIME)))
            return true;
        else
            return false;
    }

    /**
     * Combine this Interaction with the specified one to count as one Interaction with a longer duration.
     * @param later the Interaction to be combined
     * @return the new, combined Interaction
     */
    public Interaction combineInteractions(Interaction later){
        Range next = new Range(timePeriod.getLowerBound(), later.getTimePeriod().getUpperBound());
        return new Interaction(ID, personA, personB, place, next, true);
    }
}
