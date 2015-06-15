package Backend;

import java.util.TreeSet;

/**
 * A class meant to represent a relationship a person has with another person.
 * This more accurately reflects how likely it is that a person infected another person,
 * as one single interaction is likely not enough information to determine if an infection occured.
 */
public class Relationship implements Comparable<Relationship> {

    /**
     * TreeSet of Interactions,
     */
    private TreeSet<Interaction> interactions = new TreeSet<Interaction>();

    private Person otherPerson;

    private int timesInteracted;
    private double averageInteractionLength = 0;

    private double infectionLikelihood = 15; //Start at 15, decrease or increase from there.

    public Relationship(Person other){
        otherPerson = other;
    }

    public void addInteraction(Interaction next){
        interactions.add(next);
    }

    public double getInfectionLikelihood(){return infectionLikelihood;}
    
    @Override
    public int compareTo(Relationship other){
        if(infectionLikelihood > other.getInfectionLikelihood())
            return -1;
        else
            return 1;
    }

    /**
     * Algorithm to compute the likelihood that this relationship caused the infection
     * For now will simply be hard values based on research to indicate the infection likelihood.
     *
     * Goal is to convert this algorithm into a learning algorithm using neural networks.
     * If algorithm can "learn" what factors contribute to higher infection rates, it will become more accurate.
     *
     * TODO: This algorithm needs to constantly be updated to be more accurate.
     *
     * @param maxInteractions The highest number of interactions of highest likelihood relationship seen so far.
     *                          Allows for recomputing once all or some relationships have been analyzed.
     * @param maxAverage The largest average interaction length of highest likelihood relationship seen so far.
     *                   Allows for recomputing once all or some relationships have been analyzed.
     * @return
     */
    public void computeInfectionLikeliHood(int maxInteractions, double maxAverage, int highestLikelihood){
        timesInteracted = interactions.size();

        for(Interaction i: interactions){
            averageInteractionLength += i.getTimePeriod().getDuration();
        }
        averageInteractionLength = (averageInteractionLength / timesInteracted)/60000.0; //Gives us average interaction length in minutes

        /**
         * If this is the first interaction seen, make a baseline to compare other relationships against.
         *
         * Note that increased interaction time should be inversely related to number of interactions.
         *
         * If someone works with someone else who is sick, they may get 2 interactions each day just on the nature
         *  of how these interactions are computed. These values will need to be tweaked, as I believe prolonged
         *  exposure is a better indicator than being close to someone multiple times, but it is a start.
         */
        if(maxInteractions == 0){ //First interaction seen
            if(averageInteractionLength > 30) //Interacting with this person for extended periods of time
                infectionLikelihood *= 1.5;
            else if(averageInteractionLength > 60)
                infectionLikelihood *= 2;
            else if(averageInteractionLength > 90)
                infectionLikelihood *= 2.5;

            if(timesInteracted > 4 && timesInteracted < 10)
                infectionLikelihood *= 1.5;
            else if(timesInteracted > 10 && timesInteracted < 15)
                infectionLikelihood  *= 2;
            else if(timesInteracted > 15)
                infectionLikelihood *= 2.5;
        }

        /**
         * Otherwise calculate values based the best we have so far. Compare the difference in values of number of interactions
         *  and average interaction length to get a relative infection rate. WHICH IS WHAT WE WANT.
         *
         *  As I said above, I think interaction length is more important than interaction times. So I built the computation as follows:
         *      1. Increment by an amount relative to what criteria this relationship beats,
         *              this will give it a slight edge to start.
         *          NOTE: This naturally gives a slight curve, putting higher likelihoods together.
         *              This should result in a few outliers rising to the top.
         *      2. Add/Subtract half the difference between average interaction list.
         *      3. Add/Subtract a third of the difference between number of interactions.
         *
         *  Again this needs to be tweaked, but will at the least give us a ranking of relationships.
         *
         *  If we can change the algorithm to consider all other relationships (or at least the top 4-5)
         *      this would be ideal. However this is something for a more complex computing system (*cough* neural networks *cough*)
         */
        else{
            if(averageInteractionLength > maxAverage){
                if(timesInteracted > maxInteractions){
                    infectionLikelihood = (highestLikelihood + (100 - highestLikelihood) * 0.25)
                            + (averageInteractionLength - maxAverage)/2
                                    + (timesInteracted - maxInteractions) /3;

                }
                else
                    infectionLikelihood = (highestLikelihood + (100 - highestLikelihood) * 0.125)
                            + (averageInteractionLength - maxAverage)/2
                                    - (timesInteracted - maxInteractions) /3;
            }
            else{
                if(timesInteracted > maxInteractions){
                    infectionLikelihood = (highestLikelihood + (100 - highestLikelihood) * 0.05)
                            - (averageInteractionLength - maxAverage)/2
                                    + (timesInteracted - maxInteractions) /3;
                }
                else
                    infectionLikelihood = (highestLikelihood - (100 - highestLikelihood) * 0.3)
                            - (averageInteractionLength - maxAverage)/2
                                    - (timesInteracted - maxInteractions) /3;
            }
        }
    }
}
