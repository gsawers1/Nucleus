package Backend;

import java.util.*;

/**
 * A class meant to represent a relationship a person has with another person.
 * This more accurately reflects how likely it is that a person infected another person,
 * as one single interaction is likely not enough information to determine if an infection occured.
 */
public class Relationship implements Comparable<Relationship> {

    /**
     * TreeSet of Interactions
     */
    private TreeSet<Interaction> interactions = new TreeSet<Interaction>();

    private Person otherPerson;

    private int timesInteracted;
    private double averageInteractionLengthDistance = 0;
    final private double durationConstant = 100 / 1400;
    final private double radiusConstant = 100 / 49;
    final private double gammaConstant = 0;
    int originalInteractionSetSize;

    private double infectionLikelihood = 15; //Start at 15, decrease or increase from there.

    public Relationship(Person other, int originalInteractionSetSize ) {
        this.originalInteractionSetSize = originalInteractionSetSize;
        otherPerson = other;
    }

    public void addInteraction(Interaction next) {
        interactions.add(next);
    }

    public double getInfectionLikelihood() {
        return infectionLikelihood;
    }

    public Person getOtherPerson(){return otherPerson;}

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
     * <p/>
     * Goal is to convert this algorithm into a learning algorithm using neural networks.
     * If algorithm can "learn" what factors contribute to higher infection rates, it will become more accurate.
     * <p/>
     * TODO: This algorithm needs to constantly be updated to be more accurate.
     * <p/>
     * TODO: Change to the function Julio designed to come up with likelihood.
     *
     * @param maxInteractions The highest number of interactions of highest likelihood relationship seen so far.
     *                        Allows for recomputing once all or some relationships have been analyzed.
     * @param maxAverage      The largest average interaction length of highest likelihood relationship seen so far.
     *                        Allows for recomputing once all or some relationships have been analyzed.
     * @return
     */
    public void computeInfectionLikeliHood() {
        timesInteracted = interactions.size();

        for (Interaction i : interactions) {
            averageInteractionLengthDistance += i.getTimePeriod().getDuration() * (radiusConstant*(1-i.getPlace().getRadius()));
        }
        averageInteractionLengthDistance = (averageInteractionLengthDistance / timesInteracted) / 60000.0; //Gives us average interaction length in minutes

        /**
         * If this is the first interaction seen, make a baseline to compare other relationships against.
         *
         * Note that increased interaction time should be inversely related to number of interactions.
         *
         * If someone works with someone else who is sick, they may get 2 interactions each day just on the nature
         *  of how these interactions are computed. These values will need to be tweaked, as I believe prolonged
         *  exposure is a better indicator than being close to someone multiple times, but it is a start.
         *
         *  Max first relationship score is 93.75.
         */
        infectionLikelihood = averageInteractionLengthDistance + timesInteracted * durationConstant ;

    }

    /**
     * YEAH! MATH BITCH!
     */
    public void calcInfectionLiklihood()
    {
        int numInteractions = interactions.size();

        //Maps to track the mode duration and mode radius
        HashMap<Long, Integer> durationModeMap = new HashMap<Long, Integer>();
        HashMap<Long, Integer> radiusModeMap = new HashMap<Long, Integer>();

        long maxDuration = 0;
        long maxRadius = 0;

        //Count of the appearances of the max duration and radius in the interactions
        int maxDurationCount = 1;
        int maxRadiusCount = 1;

        //Run through the interactions once to find the mode and max values
        //of the duration and radius. The radius is being truncated to a
        //long for ease of comparison
        for (Interaction interact : interactions) {
            long tempDuration = interact.getTimePeriod().getDuration();
            long tempRadius = (long) interact.getPlace().getRadius();

            if (tempDuration > maxDuration)
                maxDuration = tempDuration;
            if (tempRadius < maxRadius)
                maxRadius = tempRadius;

            //Add a count to the current interaction's duration/radius
            try {
                durationModeMap.put(tempDuration, durationModeMap.get(tempDuration) + 1);
                radiusModeMap.put(tempRadius, radiusModeMap.get(tempRadius) + 1);
            }
            catch (NullPointerException except) {
                durationModeMap.put(tempDuration, 1);
                radiusModeMap.put(tempRadius, 1);
            }
        }

        //Extract the actual mode values and sort them to find the highest
        ArrayList<Integer> modeDurationCountList = new ArrayList<Integer>(durationModeMap.values());
        ArrayList<Integer> modeRadiusCountList = new ArrayList<Integer>(radiusModeMap.values());
        Collections.sort(modeDurationCountList);
        Collections.sort(modeRadiusCountList);

        //
        double sumDuration = 0;
        double sumRadius = 0;

        //Run through the interactions a second time to
        for (Interaction interact : interactions) {
            long tempDuration = interact.getTimePeriod().getDuration();
            long tempRadius = (long) interact.getPlace().getRadius();

            if (tempDuration == maxDuration)
                ++maxDurationCount;
            if (tempRadius == maxRadius)
                ++maxRadiusCount;

            sumDuration += tempDuration * durationModeMap.get(tempDuration);
            sumRadius += tempRadius * radiusModeMap.get(tempRadius);
        }

        System.out.println("Max Duration Count: "+ numInteractions);


        double alpha = (maxDurationCount / numInteractions) *
                (modeDurationCountList.get(modeDurationCountList.size() - 1) / (double) numInteractions);
        double beta = (maxRadiusCount / numInteractions) *
                (modeRadiusCountList.get(modeRadiusCountList.size() - 1) / (double) numInteractions);
        double gamma = (double) numInteractions / originalInteractionSetSize;

        System.out.println("Alpha: " + alpha + ", Beta: " + beta + ", Gamma:" + gamma);


        infectionLikelihood =  alpha * durationConstant * (sumDuration / numInteractions) +
                beta  * (100 + radiusConstant * (sumRadius / numInteractions)) +
                gamma * numInteractions;
    }

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

        gamma -= gamma * error;


        return  actualOutput;
    }


}
