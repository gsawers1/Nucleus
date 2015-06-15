package Backend;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * A class meant to represent a relationship a person has with another person.
 * This more accurately reflects how likely it is that a person infected another person,
 * as one single interaction is likely not enough information to determine if an infection occured.
 */
public class Relationship implements Comparable<Relationship> {

    private TreeSet<Interaction> interactions = new TreeSet<Interaction>();

    private Person otherPerson;

    private int timesInteracted;

    private double infectionLikelihood;

    public Relationship(Person other){
        otherPerson = other;
    }

    public void addInteraction(Interaction next){
        interactions.add(next);
    }

    @Override
    public int compareTo(Relationship other){
        return 1;
    }

    public double computeInfectionLikeliHood(){
        timesInteracted = interactions.size();

        return 0;
    }
}
