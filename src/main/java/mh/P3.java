package mh;

import mh.tipos.*;
import mh.algoritmos.*;
import java.util.ArrayList;

/**
 *
 * @author diego
 */
public class P3 {

    public static final int MAX = 5000;
    public static final int MS = MAX;
    public static final int MM = MAX * 2;
    public static final int RESTART = 10;
    public static final int VECIN = 100;
    public static final int MAXPAL = 14;
    public static final int NUMP = 3;
    public static final int[][] P = {{25, 84, 6}, {38, 126, 9}, {50, 168, 12}};
    public static final int[] SEED = {111, 222, 333, 123, 321};
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

//        System.out.println("\nGRASP-BL");
//        GRASP[] gbl = new GRASP[SEED.length];
//        System.out.println("---------------------");
//        for (int i = 0; i < SEED.length; i++) {
//            gbl[i] = new GRASP(SEED[i]);
//            gbl[i].ejecutarBL();
//            System.out.println("---------------------");
//        }
//
//        System.out.println("\nGRASP-ES");
//        GRASP[] ges = new GRASP[SEED.length];
//        System.out.println("---------------------");
//        for (int i = 0; i < SEED.length; i++) {
//            ges[i] = new GRASP(SEED[i]);
//            ges[i].ejecutarES();
//            System.out.println("---------------------");
//        }
//
//        ArrayList<Object> resultados = new ArrayList<>();
//        resultados.add(gbl);
//        resultados.add(ges);
//
//        Parser.escribir("RESULTADOS-P3.txt", resultados);
    }
}
