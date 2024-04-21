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
    public static final int MAXGG = 5000;
    public static final int RATIOGG = MAXGG / 20;
    public static final int[] MAXMM = {40, 50, 25, 100};
    public static final int[] RATIOMM = {MAXMM[0] / 20, MAXMM[1] / 20, MAXMM[2] / 20, MAXMM[3] / 20};
    public static final int[] BL = {100, 600, 150, 200};
    public static final int POBLACION = 50;
    public static final int TORNEO = 2;
    public static final double CRUCE = 0.85;
    public static final double MUTACION = 0.05;
    public static final int ELITISMO = 5;
    public static final int CACHE = 50;
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

        int i = 2;

        System.out.println("\nGG-OX-M1");
        GeneticoGeneracional[] gg11 = new GeneticoGeneracional[SEED.length];
        System.out.println("---------------------");
//        for (int i = 0; i < SEED.length; i++) {
        gg11[i] = new GeneticoGeneracional(SEED[i], "GG-OX-M1", 0);
        gg11[i].ejecutarGG();
        System.out.println("---------------------");
//        }

        System.out.println("\nGG-OX-M2");
        GeneticoGeneracional[] gg12 = new GeneticoGeneracional[SEED.length];
        System.out.println("---------------------");
//        for (int i = 0; i < SEED.length; i++) {
        gg12[i] = new GeneticoGeneracional(SEED[i], "GG-OX-M2", 1);
        gg12[i].ejecutarGG();
        System.out.println("---------------------");
//        }

        System.out.println("\nGG-AEX-M1");
        GeneticoGeneracional[] gg21 = new GeneticoGeneracional[SEED.length];
        System.out.println("---------------------");
//        for (int i = 0; i < SEED.length; i++) {
        gg21[i] = new GeneticoGeneracional(SEED[i], "GG-AEX-M1", 2);
        gg21[i].ejecutarGG();
        System.out.println("---------------------");
//        }

        System.out.println("\nGG-AEX-M2");
        GeneticoGeneracional[] gg22 = new GeneticoGeneracional[SEED.length];
        System.out.println("---------------------");
//        for (int i = 0; i < SEED.length; i++) {
        gg22[i] = new GeneticoGeneracional(SEED[i], "GG-AEX-M2", 3);
        gg22[i].ejecutarGG();
        System.out.println("---------------------");
//        }

        System.out.println("\nMM-OX-AM-1-0.2");
        Memetico[] mm11 = new Memetico[SEED.length];
        System.out.println("---------------------");
//        for (int i = 0; i < SEED.length; i++) {
        mm11[i] = new Memetico(SEED[i], "MM-OX-AM-", 1, 0.2, 0);
        mm11[i].ejecutarMM();
        System.out.println("---------------------");
//        }

        System.out.println("\nMM-OX-AM-10-1.0");
        Memetico[] mm12 = new Memetico[SEED.length];
        System.out.println("---------------------");
//        for (int i = 0; i < SEED.length; i++) {
        mm12[i] = new Memetico(SEED[i], "MM-OX-AM-", 10, 1.0, 1);
        mm12[i].ejecutarMM();
        System.out.println("---------------------");
//        }

        System.out.println("\nMM-AEX-AM-1-0.2");
        Memetico[] mm21 = new Memetico[SEED.length];
        System.out.println("---------------------");
//        for (int i = 0; i < SEED.length; i++) {
        mm21[i] = new Memetico(SEED[i], "MM-AEX-AM-", 1, 0.2, 2);
        mm21[i].ejecutarMM();
        System.out.println("---------------------");
//        }

        System.out.println("\nMM-AEX-AM-10-1.0");
        Memetico[] mm22 = new Memetico[SEED.length];
        System.out.println("---------------------");
//        for (int i = 0; i < SEED.length; i++) {
        mm22[i] = new Memetico(SEED[i], "MM-AEX-AM-", 10, 1.0, 3);
        mm22[i].ejecutarMM();
        System.out.println("---------------------");
//        }
//
//        ArrayList<Object> resultados = new ArrayList<>();
//        resultados.add(gg11);
//        resultados.add(gg12);
//        resultados.add(gg21);
//        resultados.add(gg22);
//
//        Parser.escribir("RESULTADOS-P3.txt", resultados);
    }
}
