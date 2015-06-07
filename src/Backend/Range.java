package Backend;

import java.sql.Date;

public class Range
{
    private Date lowerBound;

    private Date upperBound;

    public Range(Date lowerBound, Date upperBound)
    {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public Date getLowerBound(){return lowerBound;}
    public Date getUpperBound(){return upperBound;}

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
