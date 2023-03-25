import java.lang.reflect.*;

import java.util.*;


public class Main {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        Random generator = new Random(0);
        int n = sc.nextInt();
        int[] lengths = new int[n];
        Trajetoria[] t = new Trajetoria[n];
        for(int i = 0; i<n;i++)
            lengths[i] = sc.nextInt();
        for(int i = 0; i<n;i++) {
            ArrayList<Ponto> pontos = new ArrayList<>();
            for (int j = 0; j < lengths[i]; j++)
                pontos.add(new Ponto(generator.nextInt(100), generator.nextInt(100)));
            t[i] = new Trajetoria(pontos);
        }
        double pm = sc.nextDouble();
        for(Trajetoria traj : t){
            if(traj.getPoints().size()>2)
                if(generator.nextDouble()<pm)traj.mutation(generator);
            System.out.println(traj);
        }

        sc.close();

    }

}