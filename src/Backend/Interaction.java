package Backend;

public class Interaction implements Comparable<Interaction> {

    private final Integer ID;

    private Person personA;
    private Person personB;

    private Location place;

    private Range timePeriod;

    private boolean checked; //Boolean check to see if interaction has been sorted yet. Used for comparing.

    private final int LOCATION_UPDATE_TIME = 3600000;

    public Interaction(int ID, Person personA, Person personB, Location place, Range time, boolean checked){
        this.ID = ID;
        this.personA = personA;
        this.personB = personB;
        this.place = place;
        this.timePeriod = time;
        this.checked = checked;
    }

//    public double getInfectionLikelihood(){ //MOVED TO RELATIONSHIP CLASS
//        return infectionLikelihood;
//    }
    public Location getPlace(){return place;}
    public Range getTimePeriod(){return timePeriod;}
    public int getPersonBID(){return personB.getID();}
    public Person getPersonB(){return personB;}

    public void setChecked(){
        checked = true;
    }


    /**
     * Checks to see if this interaction involves the given person, determined by ID
     * @param targetId The ID of the person to check for
     * @return True if the person is in the interaction, otherwise False
     */
    public boolean contains(int targetId){
        if(targetId == personA.getID() || targetId == personB.getID())
            return true;

        return false;
    }

    /**
     * Compare to another Interaction based on infection likelihood
     * @param other The other interaction to compare to
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
     * Use to compare two objects for equality when combining interactions
     * @param other The other interaction
     * @return
     */
    public boolean shallowEquals(Interaction other){
        Range otherPeriod = other.getTimePeriod();

        /**
         * Fair warning this if is ugly
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

    public Interaction combineInteractions(Interaction later){

        Range next = new Range(timePeriod.getLowerBound(), later.getTimePeriod().getUpperBound());
        return new Interaction(ID, personA, personB, place, next, true);
    }
    /**
     * Added this in to do equals comparison checks but not needed as of yet
     */
//    @Override
//    public boolean equals(Object other){
//        if(!(other instanceof Interaction) || other == null) return false;
//        if(this == other) return true;
//
//        Interaction interaction = (Interaction) other;
//        if(interaction.contains(personIDA) && interaction.contains(personIDB)
//                && place.equals(interaction.getPlace()) && timePeriod.equals(interaction.getTimePeriod())) {
//            return true;
//        }
//        else
//            return false;
//    }
//
//    @Override
//    public int hashCode() {
//        int result = ID.hashCode();
//        result = 31 * result;
//        return result;
//    }

}
