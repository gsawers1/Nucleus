package Backend;

public class Location
{
    private int ID;

    private int longitude;

    private int latitude;

    private double radius;

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

    public double radius(){return radius;}
}
