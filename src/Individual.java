public abstract class Individual {
    public abstract void addGene(double pa);
    public abstract void removeGene(double pr);
    public abstract void mutate(double pm);
    public abstract Individual[] crossover(Individual other);
    public abstract double fitness();
}
