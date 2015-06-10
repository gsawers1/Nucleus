package Backend;

public class Interaction implements Comparable<Interaction> {

    private final Integer ID;

    private int personIDA;
    private int personIDB;

    private Location place;

    private Range timePeriod;
    private double infectionLikelihood;

    private final int LOCATION_UPDATE_TIME = 3600000;

    private int repeats = 0;

    public Interaction(int ID, int personA, int personB, Location place, Range time, double infection){
        this.ID = ID;
        this.personIDA = personA;
        this.personIDB = personB;
        this.place = place;
        this.timePeriod = time;
        this.infectionLikelihood = infection;
    }

    public double getInfectionLikelihood(){
        return infectionLikelihood;
    }
    public Location getPlace(){return place;}
    public Range getTimePeriod(){return timePeriod;}

    /**
     * Checks to see if this interaction involves the given person, determined by ID
     * @param targetId The ID of the person to check for
     * @return True if the person is in the interaction, otherwise False
     */
    public boolean contains(int targetId){
        if(targetId == personIDA || targetId == personIDB)
            return true;
        else
            return false;
    }

    /**
     * Compare to another Interaction based on infection likelihood
     * @param other The other interaction to compare to
     * @return -1 if Likelihood of the other interaction is higher, otherwise 1. No tie-breaker has yet been decided.
     */
    public int compareTo(Interaction other){
        if(infectionLikelihood < other.getInfectionLikelihood())
            return -1;
        else if(infectionLikelihood > other.getInfectionLikelihood())
            return 1;
        else
            return 1;
    }

    public void incrementRepeats(){repeats++;}

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
        if(other.contains(personIDA) && other.contains(personIDB) && place.equals(other.getPlace())
                && ((timePeriod.getLowerBound()-otherPeriod.getUpperBound() < LOCATION_UPDATE_TIME
                        && timePeriod.getLowerBound()-otherPeriod.getUpperBound() > (-1 * LOCATION_UPDATE_TIME))
                    || (otherPeriod.getLowerBound()- timePeriod.getUpperBound() < LOCATION_UPDATE_TIME)
                        && otherPeriod.getLowerBound()- timePeriod.getUpperBound() < (-1 * LOCATION_UPDATE_TIME))){
            return true;
        }
        else
            return false;
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
