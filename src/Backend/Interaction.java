package Backend;

public class Interaction implements Comparable<Interaction> {

    private int ID;

    private int personIDA;
    private int personIDB;

    private Location place;

    private Range timePeriod;
    private double infectionLikelihood;

    public Interaction(int ID, int personA, int personB, Location place, Range time, double infection){
        this.ID = ID;
        this.personIDA = personA;
        this.personIDB = personB;
        this.place = place;
        this.timePeriod = time;
        this.infectionLikelihood = infection;
    }

    public double getInfectionLikelihood(){
        return infectionLikelihood;
    }

    public int compareTo(Interaction other){
        if(infectionLikelihood < other.getInfectionLikelihood())
            return -1;
        else if(infectionLikelihood > other.getInfectionLikelihood())
            return 1;
        else
            return 1;
    }
}
