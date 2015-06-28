package Backend;

/**
 * A class to represent a Location of where a person has been.
 * Abstracted so that it does not belong to one person as it does in the database.
 */
public class Location
{
    private int ID;

    private int longitude;

    private int latitude;

    private double radius;

    final static double DEFUALT_RADIUS = 0.0; //Need to determine a default radius to surround a persons location

    public Location(int ID, int longitude, int latitude, double radius)
    {
        this.ID = ID;
        this.longitude = longitude;
        this.latitude = latitude;
        this.radius = radius;
    }

    public int getID(){return ID;}

    public int getLongitude(){return longitude;}

    public int getLatitude(){return latitude;}

    public double getRadius(){return radius;}

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
