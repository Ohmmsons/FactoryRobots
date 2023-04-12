import java.util.ArrayList;
import java.util.List;

public interface Population {

    ArrayList<Individual> getIndividuals();
    void setIndividuals(ArrayList<Individual> individuals);
    String populationInfo();
    void sortByFitness();
    Population tournament();
}