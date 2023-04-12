import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;


public class Main {


    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);
        Random generator = new Random(0);
        int g = sc.nextInt();
        double pm = sc.nextDouble();
        double pa = sc.nextDouble();
        double pr = sc.nextDouble();
        int x1 = sc.nextInt();
        int y1 = sc.nextInt();
        int x2 = sc.nextInt();
        int y2 = sc.nextInt();
        int[] lengths = new int[sc.nextInt()];
        for (int i = 0; i < lengths.length; i++) {
            lengths[i] = sc.nextInt();
        }
        FiguraGeometrica f;
        DecimalFormatSymbols unusualSymbols = new DecimalFormatSymbols();
        unusualSymbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("0.00", unusualSymbols);
        String s;

        String[] aos;

        sc.nextLine();
        ArrayList<FiguraGeometrica> figuras = new ArrayList<>();

        while (sc.hasNextLine()) {
            s = sc.nextLine();

            aos = s.split(" ");


            switch (aos[0]) {
                case "Circunferencia" -> {
                    figuras.add(new Circunferencia(new Ponto[]{new Ponto(Integer.parseInt(aos[1]), Integer.parseInt(aos[2]))}, Double.parseDouble(aos[3])));
                }
                case "Triangulo" -> {
                    figuras.add(new Triangulo(new Ponto[]{new Ponto(Integer.parseInt(aos[1]), Integer.parseInt(aos[2])), new Ponto(Integer.parseInt(aos[3]), Integer.parseInt(aos[4])), new Ponto(Integer.parseInt(aos[5]), Integer.parseInt(aos[6]))}));

                }
                case "Retangulo" -> {
                    figuras.add(new Retangulo(new Ponto[]{new Ponto(Integer.parseInt(aos[1]), Integer.parseInt(aos[2])), new Ponto(Integer.parseInt(aos[3]), Integer.parseInt(aos[4])), new Ponto(Integer.parseInt(aos[5]), Integer.parseInt(aos[6])), new Ponto(Integer.parseInt(aos[7]), Integer.parseInt(aos[8]))}));

                }
                default -> {
                    System.out.println("Tipo de obstaculo desconhecido");
                    System.exit(0);
                }
            }
        }
        SGA sga = new SGA(pm, pa, pr, x1, y1, x2, y2, lengths.length, lengths, generator, figuras);
        TrajectoryPopulation pop = sga.sga(g);
        sc.close();
    }

}