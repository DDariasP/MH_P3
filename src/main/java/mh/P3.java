package mh;

import mh.tipos.*;
import mh.algoritmos.*;
import java.util.ArrayList;

/**
 *
 * @author diego
 */
public class P3 {

    public static final int MAXPAL = 14;
    public static final int NUMP = 3;
    public static final int[][] P = {{25, 84, 6}, {38, 126, 9}, {50, 168, 12}};
    public static final int[] SEED = {111, 222, 333, 123, 321};
    public static final int RATIO = 1;
    public static final int MAX = RATIO * 1000;
    public static final int MG = MAX / (RATIO * 10);
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

        System.out.println("\nGG-OX-M1");
        GeneticoGeneracional[] gg11 = new GeneticoGeneracional[SEED.length];
        System.out.println("---------------------");
        for (int i = 0; i < SEED.length; i++) {
            gg11[i] = new GeneticoGeneracional(SEED[i], "OX", "M1");
            gg11[i].ejecutarGG();
            System.out.println("---------------------");
        }

        System.out.println("\nGG-OX-M2");
        GeneticoGeneracional[] gg12 = new GeneticoGeneracional[SEED.length];
        System.out.println("---------------------");
        for (int i = 0; i < SEED.length; i++) {
            gg12[i] = new GeneticoGeneracional(SEED[i], "OX", "M2");
            gg12[i].ejecutarGG();
            System.out.println("---------------------");
        }

        System.out.println("\nGG-AEX-M1");
        GeneticoGeneracional[] gg21 = new GeneticoGeneracional[SEED.length];
        System.out.println("---------------------");
        for (int i = 0; i < SEED.length; i++) {
            gg21[i] = new GeneticoGeneracional(SEED[i], "AEX", "M1");
            gg21[i].ejecutarGG();
            System.out.println("---------------------");
        }

        System.out.println("\nGG-AEX-M2");
        GeneticoGeneracional[] gg22 = new GeneticoGeneracional[SEED.length];
        System.out.println("---------------------");
        for (int i = 0; i < SEED.length; i++) {
            gg22[i] = new GeneticoGeneracional(SEED[i], "AEX", "M2");
            gg22[i].ejecutarGG();
            System.out.println("---------------------");
        }

//        ArrayList<Object> resultados = new ArrayList<>();
//        resultados.add(gg11);
//        resultados.add(gg12);
//        resultados.add(gg21);
//        resultados.add(gg22);
//
//        Parser.escribir("RESULTADOS-P3.txt", resultados);
    }
}
