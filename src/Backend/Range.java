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
}
