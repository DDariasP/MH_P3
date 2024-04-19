package mh;

import mh.tipos.*;
import mh.algoritmos.*;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author diego
 */
public class P3 {

    public static final int MAXPAL = 4;
    public static final int NUMP = 3;
    public static final int[][] P = {{25, 84, 6}, {38, 126, 9}, {50, 168, 12}};
    public static final int[] SEED = {111, 222, 333, 123, 321};
    public static final int MAX = 1000;
    public static final int ME = 1000;
    public static final int MG = 1000;
    public static final int POBLACION = 50;
    public static final int TORNEO = 2;
    public static final double CRUCE = 0.85;
    public static final double MUTACION = 0.05;
    public static final int ELITISMO = 5;
    public static ArrayList<Matriz> listaDist;
    public static ArrayList<Lista> listaPal;
    public static ArrayList<Lista> listaGen;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        listaDist = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int ciu = P[i][0];
            listaDist.add(Parser.leerDist(ciu, "matriz_distancias_" + ciu + ".txt"));
        }

        listaPal = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            int pal = P[i][1];
            listaPal.add(Parser.leerPal("destinos_palets_" + pal + ".txt"));
        }

        listaGen = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Lista<Integer> listaP = listaPal.get(i);
            Lista<Gen> listaG = new Lista<>();
            for (int j = 0; j < listaP.size(); j++) {
                int destino = listaP.get(j);
                Gen gen = new Gen(j, destino);
                listaG.add(gen);
            }
            listaGen.add(listaG);
        }

//        Cromosoma c0 = new Cromosoma(new Tabla(3, 4));
//        Cromosoma c1 = new Cromosoma(new Tabla(3, 4));
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 4; j++) {
//                c0.t.t[i][j] = new Gen(i, j);
//                c1.t.t[i][j] = new Gen(i+5, j+5);
//            }
//        }
//        System.out.println(c0);
//        System.out.println(c1);
//        System.out.println("----------------\n");
//        
//        Cromosoma[] h = new Cromosoma[2];
//        Cromosoma.cruceOX(c0, c1, h, new Random(38533));
//        System.out.println(h[0]);
//                System.out.println(h[1]);

        

//        CromosomaTest.test();
//
//        System.out.println("\nGG-11");
//        GeneticoGeneracional[] gg11 = new GeneticoGeneracional[SEED.length];
//        System.out.println("---------------------");
//        for (int i = 0; i < SEED.length; i++) {
//            gg11[i] = new GeneticoGeneracional(SEED[i], 1, 1);
//            gg11[i].ejecutarGG();
//            System.out.println("---------------------");
//        }
//
//        System.out.println("\nGG-12");
//        GeneticoGeneracional[] gg12 = new GeneticoGeneracional[SEED.length];
//        System.out.println("---------------------");
//        for (int i = 0; i < SEED.length; i++) {
//            gg12[i] = new GeneticoGeneracional(SEED[i], 1, 2);
//            gg12[i].ejecutarGG();
//            System.out.println("---------------------");
//        }
//
//        System.out.println("\nGG-21");
//        GeneticoGeneracional[] gg21 = new GeneticoGeneracional[SEED.length];
//        System.out.println("---------------------");
//        for (int i = 0; i < SEED.length; i++) {
//            gg21[i] = new GeneticoGeneracional(SEED[i], 2, 1);
//            gg21[i].ejecutarGG();
//            System.out.println("---------------------");
//        }
//
//        System.out.println("\nGG-22");
//        GeneticoGeneracional[] gg22 = new GeneticoGeneracional[SEED.length];
//        System.out.println("---------------------");
//        for (int i = 0; i < SEED.length; i++) {
//            gg22[i] = new GeneticoGeneracional(SEED[i], 2, 2);
//            gg22[i].ejecutarGG();
//            System.out.println("---------------------");
//        }
//
//        ArrayList<Object> resultados = new ArrayList<>();
//        resultados.add(gg11);
//        resultados.add(gg12);
//
//        Parser.escribir("RESULTADOS-P3.txt", resultados);
    }
}
