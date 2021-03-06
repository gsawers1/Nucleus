package Backend;

import java.util.*;

/**
 * A class meant to represent a relationship a person has with another person.
 * This more accurately reflects how likely it is that a person infected another person,
 * as one single interaction is likely not enough information to determine if an infection occurred.
 */
public class Relationship implements Comparable<Relationship> {

    /**
     * The set of Interactions that define this Relationship.
     */
    private TreeSet<Interaction> interactions = new TreeSet<Interaction>();

    /**
     * The second Person involved in this Relationship.
     */
    private Person otherPerson;

    /**
     * The total number of times the two Persons interacted with a possibility of infection.
     */
    private int timesInteracted;

    /**
     * A variable to hold the average interaction length scaled by how close the two people were during the interaction.
     */
    private double averageInteractionLengthDistance = 0;

    /**
     * Constant to scale number of interactions used in the likelihood formula.
     */
    final private double interactionTimesConstant = 125 / 1400;

    /**
     * Constant for duration used in the likelihood formula.
     */
    final private double durationConstant = 100 / 1440;

    /**
     * Constant for distance used in the likelihood formula.
     */
    final private double radiusConstant = 75 / 49;

    /**
     * Constant for infection number used in the likelihood formula.
     */
    final private double gammaConstant = 0;

    /**
     * The total number of Interactions that the first Person in this Relationship had with anyone.
     */
    int originalInteractionSetSize;

    /**
     * Initial value for the infection likelihood.
     */
    private double infectionLikelihood = 15; //Start at 15, decrease or increase from there.

    /**
     * Constructor for Objects of class Relationship.
     * @param other the second Person involved in this Relationship.
     * @param originalInteractionSetSize the total number of Interactions that the first Person in this Relationship had with anyone.
     */
    public Relationship(Person other, int originalInteractionSetSize ) {
        this.originalInteractionSetSize = originalInteractionSetSize;
        otherPerson = other;
    }

    /**
     * Add the specified Interaction to this Relationship.
     * @param next the Interaction to be added
     */
    public void addInteraction(Interaction next) {
        interactions.add(next);
    }

    /**
     * Accessor method for the computed infection likelihood.
     * @return the computed infection likelihood
     */
    public double getInfectionLikelihood() {
        return infectionLikelihood;
    }

    /**
     * Accessor method for the number of times the two people interacted.
     * @return The number of interactions.
     */
    public int getTimesInteracted(){return interactions.size();}

    /**
     * Acessor method for the average interaction duration scaled by how far apart the two people were during the interaction.
     * @return The average interaction duration.
     */
    public double getAverageInteractionLengthDistance(){return averageInteractionLengthDistance;}

    /**
     * Accessor method for the second Person in this Relationship.
     * @return the second Person in this Relationship
     */
    public Person getOtherPerson(){return otherPerson;}

    /**
     * Compares this Relationship to the specified Relationship based on infection likelihood.
     * @param other the other Relationship to compare
     * @return a negative value if this Relationship is considered less than the specified,
     * positive if the specified is considered less than this
     */
    @Override
    public int compareTo(Relationship other) {
        if (infectionLikelihood > other.getInfectionLikelihood())
            return -1;
        else
            return 1;
    }

    /**
     * Algorithm to compute the likelihood that this relationship caused the infection
     * For now will simply be hard values based on research to indicate the infection likelihood.
     */
    public void computeInfectionLikeliHood(int numTotalInteractions) {
        timesInteracted = interactions.size();

        for (Interaction i : interactions) {
            averageInteractionLengthDistance += i.getTimePeriod().getDuration() * (radiusConstant*(1-i.getPlace().getRadius()));
        }
        averageInteractionLengthDistance = (averageInteractionLengthDistance / timesInteracted) / 60000.0; //Gives us average interaction length in minutes

        /**
         * Combine these scaled factors to determine infection likelihood
         */
        infectionLikelihood = (averageInteractionLengthDistance + (timesInteracted/numTotalInteractions) * interactionTimesConstant)*5 ;
        if(infectionLikelihood > 100.0)
            infectionLikelihood = 100.0;
    }

    /**
     * Calculates the infection likelihood of this relationship.
     *
     * The attempts to get the mathematical model behind this method to produce valid
     * results have failed, but have contributed heavily to an alternate version of computing
     * the likelihood.
     */
    public void calcInfectionLiklihood(int numTotalInteractions)
    {

        double averageInteractionLength = 0;
        double averageRadius = 0;
        int numInteractions = interactions.size();

        //Maps to track the mode duration and mode radius
        HashMap<Long, Integer> durationModeMap = new HashMap<Long, Integer>();
        HashMap<Double, Integer> radiusModeMap = new HashMap<Double, Integer>();

        long maxDuration = 0;
        double maxRadius = 1.0;

        //Run through the interactions once to find the mode and max values
        //of the duration and radius. The radius is being truncated to a
        //long for ease of comparison
        for (Interaction interact : interactions) {
            long tempDuration = interact.getTimePeriod().getDuration();
            double tempRadius = interact.getPlace().getRadius();
            //System.out.println("Temp Radius: " + tempRadius);
            //tempRadius = Math.floor(tempRadius * 100) / 100;

            if (tempDuration > maxDuration)
                maxDuration = tempDuration;
            if (tempRadius < maxRadius)
                maxRadius = tempRadius;

            if(durationModeMap.containsKey(tempDuration))
                durationModeMap.put(tempDuration, durationModeMap.get(tempDuration) + 1);
            else
                durationModeMap.put(tempDuration, 1);

            if(radiusModeMap.containsKey(tempRadius))
                radiusModeMap.put(tempRadius, radiusModeMap.get(tempRadius) + 1);
            else
                radiusModeMap.put(tempRadius, 1);

            averageInteractionLength += tempDuration;
            averageRadius += tempRadius;
        }


        //Averages for duration/radius based on appearance of each interaction value
        double sumDuration = 0;
        double sumRadius = 0;

        int frequentRadiusCount = 1;
        int frequentDurationCount = 1;
        for(Map.Entry<Long, Integer> entry : durationModeMap.entrySet()){
             if(entry.getValue() > frequentDurationCount)
                 frequentDurationCount = entry.getValue();
        }

        for(Map.Entry<Double, Integer> entry : radiusModeMap.entrySet()){
            if(entry.getValue() > frequentRadiusCount)
                frequentRadiusCount = entry.getValue();
        }

        //Regression coefficients for the likelihood formula
        double alpha = (durationModeMap.get(maxDuration)/numInteractions + frequentDurationCount/numInteractions)*100;
        double beta = radiusModeMap.get(maxRadius)/numInteractions * frequentRadiusCount/numInteractions;
        double gamma = numInteractions/numTotalInteractions;

        double alphaComputation = alpha * durationConstant * averageInteractionLength;
        double betaComputation = beta * (100 + radiusConstant * (1+averageRadius));
        double gammaComputation = gamma * 0.5;

        System.out.println("Alpha: " + alpha + ", Beta: " + beta + ", Gamma:" + gamma);

        //Infection likelihood formula
        infectionLikelihood =  alphaComputation + betaComputation + gammaComputation;

    }

    /**
     * Corrects the infection likelihood percentage based on an actual and desired output.
     */
    private double correctLiklihood(double alpha, double beta, double gamma, double sumDuration, double sumRadius, int numInteractions) {
        double error;
        double newAlpha = alpha;
        double newBeta = beta;
        double newGamma = gamma;
        double actualOutput;

        do {
            actualOutput = newAlpha * durationConstant * (sumDuration / numInteractions) +
                    newBeta  * (100 + radiusConstant * (sumRadius / numInteractions)) +
                    newGamma * gammaConstant * numInteractions;
            double desiredOutput = newAlpha * durationConstant * (24 / numInteractions) +
                    newBeta  * (100 + radiusConstant * (0 / numInteractions)) +
                    newGamma * gammaConstant * numInteractions;
            error = 1 - Math.abs(actualOutput - desiredOutput) / desiredOutput;

            Double max = new Double(Math.max(newAlpha, Math.max(newBeta , gamma)));

            if (max.equals(newAlpha))
                newAlpha -= newAlpha * error;
            else if (max.equals(newBeta))
                newBeta -= newBeta * error;
            else
                newGamma -= newGamma * error;
        } while (error > .1);

        return  actualOutput;
    }
}
