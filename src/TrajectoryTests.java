//import org.junit.Test;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.Random;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class TrajectoryTests {
//    @Test
//    public void testNCollisions(){
//        Ponto[] pontos = new Ponto[]{new Ponto(1.0,1.0),new Ponto(2.0,2.0),new Ponto(3.0,3.0)};
//        ArrayList<Ponto> pontosAL = new ArrayList<>();
//        Collections.addAll(pontosAL, pontos);
//        Trajetoria trajectory = new Trajetoria(pontosAL);
//        ArrayList<FiguraGeometrica> figuras = new ArrayList<>();
//        figuras.add(new Circunferencia(new Ponto[]{new Ponto(1.0,2.0)},1));
//        assertEquals(trajectory.nCollisions(figuras),1);
//    }
//    @Test
//    public void testFitness(){
//        Ponto[] pontos = new Ponto[]{new Ponto(1.0,1.0),new Ponto(2.0,2.0),new Ponto(3.0,3.0)};
//        ArrayList<Ponto> pontosAL = new ArrayList<>();
//        Collections.addAll(pontosAL, pontos);
//        Trajetoria trajectory = new Trajetoria(pontosAL);
//        ArrayList<FiguraGeometrica> figuras = new ArrayList<>();
//        figuras.add(new Circunferencia(new Ponto[]{new Ponto(1.0,2.0)},1));
//        assertEquals(trajectory.getFitness(figuras),0.1802870870702775);
//    }
//}
