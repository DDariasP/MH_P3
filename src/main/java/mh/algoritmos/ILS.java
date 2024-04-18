package mh.algoritmos;

import mh.*;
import mh.tipos.*;
import java.util.Random;
import static javax.swing.WindowConstants.*;

/**
 *
 * @author diego
 */
public class ILS {

    public final int SEED;
    public Random rand;
    public Solucion[] solILS;
    public Lista[][] convergencia;
    public Lista<Solucion> listaElite;

    public ILS(int a) {
        SEED = a;
        rand = new Random(SEED);
        solILS = new Solucion[P3.NUMP];
        convergencia = new Lista[P3.NUMP][P3.RESTART];
        for (int i = 0; i < P3.NUMP; i++) {
            for (int j = 0; j < P3.RESTART; j++) {
                convergencia[i][j] = new Lista<Integer>();
            }
        }
    }

    public void ejecutarBL() {
        for (int i = 0; i < P3.NUMP; i++) {
            solILS[i] = BL(i);
            System.out.println(solILS[i].coste + "\t" + solILS[i].eval + "\tn=" + listaElite.count(solILS[i]));
            if (i == 2 && SEED == 333) {
                GraficaM g = new GraficaM(convergencia[i], "ILS-BL");
                g.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                g.setBounds(200, 350, 800, 500);
                g.setTitle("ILS-BL - P" + (i + 1) + " - S" + SEED);
                g.setVisible(true);
            }
        }
    }

    public Solucion BL(int tamP) {
        int[] P = P3.P[tamP];
        int ciu = P[0];
        int cam = P[2];
        int maxiter = P3.MAX2 * ciu;
        Lista listaPal = P3.listaPal.get(tamP);

        int lasteval = -1;
        Solucion elite = new Solucion(new Matriz(1, 1, 0));
        listaElite = new Lista<>();
        for (int i = 0; i < P3.RESTART; i++) {
            Solucion inicial;
            if (i == 0) {
                inicial = Solucion.genRandom(cam, listaPal, rand);
            } else {
                inicial = Solucion.genMutacion(cam, elite, rand);
            }
            inicial.eval = lasteval;
            Solucion tmp = BusquedaLocal.BL(rand, tamP, maxiter, inicial, convergencia[tamP][i]);
            if (elite.coste > tmp.coste) {
                elite = tmp;
            }
            lasteval = tmp.lasteval;
            listaElite.add(tmp);
        }

        return elite;
    }

    public void ejecutarES() {
        for (int i = 0; i < P3.NUMP; i++) {
            solILS[i] = ES(i);
            System.out.println(solILS[i].coste + "\t" + solILS[i].eval + "\tn=" + listaElite.count(solILS[i]));
            if (i == 2 && SEED == 333) {
                GraficaM g = new GraficaM(convergencia[i], "ILS-ES");
                g.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                g.setBounds(200, 350, 800, 500);
                g.setTitle("ILS-ES - P" + (i + 1) + " - S" + SEED);
                g.setVisible(true);
            }
        }
    }

    public Solucion ES(int tamP) {
        int[] P = P3.P[tamP];
        int ciu = P[0];
        int cam = P[2];
        int maxiter = P3.MAX2 * ciu;
        Lista listaPal = P3.listaPal.get(tamP);

        int lasteval = -1;
        Solucion elite = new Solucion(new Matriz(1, 1, 0));
        listaElite = new Lista<>();
        for (int i = 0; i < P3.RESTART; i++) {
            Solucion inicial;
            if (i == 0) {
                inicial = Solucion.genRandom(cam, listaPal, rand);
            } else {
                inicial = Solucion.genMutacion(cam, elite, rand);
            }
            inicial.eval = lasteval;
            Solucion tmp = EnfriamientoSimulado.ES(rand, tamP, maxiter, inicial, convergencia[tamP][i]);
            if (elite.coste > tmp.coste) {
                elite = tmp;
            }
            lasteval = tmp.lasteval;
            listaElite.add(tmp);
        }

        return elite;
    }

    public void ejecutarBT() {
        for (int i = 0; i < P3.NUMP; i++) {
            solILS[i] = BT(i);
            System.out.println(solILS[i].coste + "\t" + solILS[i].eval + "\tn=" + listaElite.count(solILS[i]));
            if (i == 2 && SEED == 333) {
                GraficaM g = new GraficaM(convergencia[i], "ILS-BT");
                g.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                g.setBounds(200, 350, 800, 500);
                g.setTitle("ILS-BT - P" + (i + 1) + " - S" + SEED);
                g.setVisible(true);
            }
        }
    }

    public Solucion BT(int tamP) {
        int[] P = P3.P[tamP];
        int ciu = P[0];
        int cam = P[2];
        int maxiter = P3.MAX2 * ciu;
        Lista listaPal = P3.listaPal.get(tamP);

        int lasteval = -1;
        double tenencia = 4.0;
        Solucion elite = new Solucion(new Matriz(1, 1, 0));
        listaElite = new Lista<>();
        for (int i = 0; i < P3.RESTART; i++) {
            if (i > 0) {
                if (rand.nextBoolean()) {
                    tenencia = Math.round(tenencia + tenencia * BusquedaTaboo.KSIZE);
                } else {
                    tenencia = Math.max(Math.round(tenencia - tenencia * BusquedaTaboo.KSIZE), 2.0);
                }
            }
            Solucion inicial;
            if (i == 0) {
                inicial = Solucion.genRandom(cam, listaPal, rand);
            } else {
                inicial = Solucion.genMutacion(cam, elite, rand);
            }
            inicial.eval = lasteval;
            Solucion tmp = BusquedaTaboo.BT(rand, tamP, maxiter, inicial, inicial, convergencia[tamP][i], tenencia);
            if (elite.coste > tmp.coste) {
                elite = tmp;
            }
            lasteval = tmp.lasteval;
            listaElite.add(tmp);
        }

        return elite;
    }

}
