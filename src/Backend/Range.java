package Backend;


/**
 * IMPORTANT!!: I CHANGED THESE TO LONGS BECAUSE THE JAVA DATE CLASS IS DEPRECATED AND IS BASICALLY A LONG WRAPPER
 *  WE WILL HOLD TIME AS MILLISECONDS FROM THE EPOCH (JANUARY 1ST 1970 00:00:00) INSTEAD
 */

/**
 * Class that determines a time range. Can be used to determine the length of an interaction or length of infection of a person.
 */
public class Range
{
    private long lowerBound;

    private long upperBound;

    public Range(long lowerBound, long upperBound)
    {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public long getLowerBound(){return lowerBound;}
    public long getUpperBound(){return upperBound;}

    public long getDuration(){ return upperBound - lowerBound;}

//    public boolean equals(Object o){
//        if(o == null || !(o instanceof Location)) return false;
//
//        Range r = (Range) o;
//
//        if(lowerBound.equals(r.getLowerBound()) && upperBound.equals(r.getUpperBound()))
//            return true;
//        else
//            return false;
//    }
}
