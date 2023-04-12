import java.util.ArrayList;
import java.util.Random;

public abstract class Population{

    protected ArrayList<Individual> individuals;
    protected Random generator;
    public abstract String populationInfo();
    public abstract void sortByFitness();
    public abstract Population tournament();

    public ArrayList<Individual> getIndividuals(){return this.individuals;}

    public void setIndividuals(ArrayList<Individual> individuals) { this.individuals = individuals;}
}
