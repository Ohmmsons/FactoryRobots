public interface Individual {
    void addGene(double pa);
    void removeGene(double pr);
    void mutate(double pm);
    Individual[] crossover(Individual other);
    double fitness();
}
