package Backend;

/**
 * A class to represent a Location of where a person has been.
 * Abstracted so that it does not belong to one person as it does in the database.
 */
public class Location
{
    /**
     * The ID of this Location.
     */
    private int ID;

    /**
     * The longitude specifying this Location.
     */
    private double longitude;

    /**
     * The latitude specifying this Location.
     */
    private double latitude;

    /**
     * The radius that defines a circular area around this Location.
     *
     * Can have a value of 0 to define a point on the map.
     */
    private double radius;

    /**
     * Constructor for Objects of class Location.
     * @param ID the ID of this Location.
     * @param longitude the longitude specifying this Location.
     * @param latitude the latitude specifying this Location.
     * @param radius the radius that defines a circular area around this Location.
     */
    public Location(int ID, double longitude, double latitude, double radius)
    {
        this.ID = ID;
        this.longitude = longitude;
        this.latitude = latitude;
        this.radius = radius;
    }

    /**
     * Accessor method for the ID of this Location.
     * @return the ID of this Location.
     */
    public int getID(){return ID;}

    /**
     * Accessor method for the longitude that specifies this Location.
     * @return the longitude that specifies this Location
     */
    public double getLongitude(){return longitude;}

    /**
     * Accessor method for the latitude that specifies this Location.
     * @return the latitude that specifies this Location
     */
    public double getLatitude(){return latitude;}

    /**
     * Accessor method for the radius of this Location.
     * @return the radius of this Location
     */
    public double getRadius(){return radius;}

    /**
     * Compares this Location to the specified one for equality.
     * @param o the Location to be compared to this one
     * @return true if the two Locations are considered equal, false otherwise
     */
    @Override
    public boolean equals(Object o){
        if(o == null || !(o instanceof Location)) return false;

        Location l = (Location) o;
        if(latitude == l.getLatitude() && longitude == l.getLongitude())
            return true;
        else
            return false;
    }
}
