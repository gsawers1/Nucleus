package Backend;

/**
 * Class that determines a time range. Can be used to determine the length of an interaction or length of infection of a person.
 * The lower and upper bound of this Range of time are measure in milliseconds measured from 1970.
 */
public class Range
{
    /**
     * The lower bound of this Range of time.
     */
    private long lowerBound;

    /**
     * The upper bound of this Range of time.
     */
    private long upperBound;

    /**
     * Constructor for Objects of class Range.
     * @param lowerBound the lower bound of this Range of time.
     * @param upperBound the upper bound of this Range of time.
     */
    public Range(long lowerBound, long upperBound)
    {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    /**
     * Accessor method for the lower bound of this Range.
     * @return
     */
    public long getLowerBound(){return lowerBound;}

    /**
     * Accessor method for the upper bound of this Range.
     * @return
     */
    public long getUpperBound(){return upperBound;}

    /**
     * Accessor method for the duration of time defined by this Range.
     * @return
     */
    public long getDuration(){ return upperBound - lowerBound;}
}
