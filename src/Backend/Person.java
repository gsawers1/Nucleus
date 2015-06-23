package Backend;

import java.util.*;

/**
 * Represents one person from the database.
 * Each person object is unique, as based on the ID field.
 */
public class Person
{
    private int ID;

    private String firstName;

    private String lastName;

    private boolean infected;

    private Range infectionRange;

    private final int DAYINMILLIS = 86400000;

    private int maxInteractions = 0;

    private double maxAverage = 0;

    private double highestLikelihood = 0;

    /**
     * Contains all the relationships a person has. This is a more complete picture of a persons interactions with one
     *  other individual.
     */
    private TreeMap<Integer,Relationship> relationships = new TreeMap<Integer, Relationship>();

    /**
     * Contains all the interactions the person is involved in. Generated by the InteractionsList.
     */
    private TreeSet<Interaction> personalInteractionSet = new TreeSet<Interaction>();

    public Person(int ID, String first, String last, boolean infected, long timeReported){
        this.ID = ID;
        firstName = first;
        lastName = last;
        this.infected = infected;
        infectionRange = new Range(timeReported - DAYINMILLIS, timeReported + 7*DAYINMILLIS );
    }
    public int getID(){return ID;}

    public void setInteractionSet(TreeSet<Interaction> set){
        personalInteractionSet = set;
    }

    public Range getInfectionRange(){return infectionRange;}

    public TreeSet<Interaction> getInteractionSet(){return personalInteractionSet;}

    /**
     * Goal of this method is to remove Interactions that would not infect a person
     * as well as combine interactions that flow into each other (e.g. last longer than the location update interval)
     * into one interaction.
     *
     * As it is written, it will make personalInteractionSet inaccurate, as such, after it is run we should focus on relationships.
     */
    public void consolidateInteractions(){
        Iterator itr = personalInteractionSet.iterator();
        if(itr.hasNext()){
            Interaction current = (Interaction) itr.next();
            while(itr.hasNext()) {
                Interaction next = (Interaction) itr.next();
            //Check if the interaction occurred before or after one of the two people was infected, if it was ignore it.
                if (current.getTimePeriod().getUpperBound() < current.getPersonB().getInfectionRange().getLowerBound()
                        || current.getTimePeriod().getUpperBound() < infectionRange.getLowerBound()
                        || current.getTimePeriod().getLowerBound() > current.getPersonB().getInfectionRange().getUpperBound()
                        || current.getTimePeriod().getLowerBound() > infectionRange.getUpperBound())
                    current = next;
                else {
                    if (current.shallowEquals(next)) {
                        current = current.combineInteractions(next);
                    } else {
                        current.setChecked();
                        if (relationships.containsKey(current.getPersonBID())) {
                            Relationship update = relationships.get(current.getPersonBID());
                            update.addInteraction(current);
                            relationships.put(current.getPersonBID(), update);
                        } else {
                            relationships.put(current.getPersonBID(), new Relationship(current.getPersonB()));
                        }
                        current = next;
                    }
                }
            }
        }
    }

    /**
     * Compute the infection likelihood of each relationship
     *
     * Not the nicest statement in the for loop but whatever it works.
     */
    public void buildRelationshipInfectionChance(){
        ArrayList<Double> maxValues = new ArrayList<Double>();
        for(Map.Entry<Integer, Relationship> entry: relationships.entrySet()){
            maxValues = entry.getValue().computeInfectionLikeliHood(maxInteractions, maxAverage, highestLikelihood);
            maxInteractions = (int)(double) maxValues.get(0);
            maxAverage = maxValues.get(1);
            highestLikelihood = maxValues.get(2);
        }
    }
}
