package mh.algoritmos;

import java.awt.Color;
import java.util.Random;
import mh.*;
import mh.tipos.*;
import static javax.swing.WindowConstants.*;

/**
 *
 * @author diego
 */
public class BusquedaTaboo {

    public final int SEED;
    public Random rand;
    public Solucion[] solBT;
    public Lista[] convergencia;

    public static final double KRAND = 0.25;
    public static final double KGREED = 0.50;
    public static final double KREST = 0.25;
    public static final double KSIZE = 0.50;
    public double tenencia;
    public Lista<Movimiento> listaTaboo;
    public Solucion elite;
    public int[][][] memoriaM;
    public int[][] memoriaC;

    public BusquedaTaboo(int a) {
        SEED = a;
        rand = new Random(SEED);
        solBT = new Solucion[P3.NUMP];
        convergencia = new Lista[P3.NUMP];
        for (int i = 0; i < P3.NUMP; i++) {
            convergencia[i] = new Lista<Integer>();
        }
    }

    public void ejecutarBT() {
        for (int i = 0; i < P3.NUMP; i++) {
            solBT[i] = BT(i);
            System.out.println(solBT[i].coste + "\t" + solBT[i].eval);
            if (i == 2 && SEED == 333) {
                GraficaS g = new GraficaS(convergencia[i], "BT", Color.MAGENTA);
                g.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                g.setBounds(200, 350, 800, 400);
                g.setTitle("BT - P" + (i + 1) + " - S" + SEED);
                g.setVisible(true);
            }
        }
    }

    public Solucion BT(int tamP) {
        int[] P = P3.P[tamP];
        int ciu = P[0];
        int cam = P[2];
        int eval = -1;
        int maxeval = P3.MAX2 * ciu;
        int iter, vecindario;
        int maxiter = maxeval / P3.RESTART;
        int reini = 0;
        Lista listaPal = P3.listaPal.get(tamP);
        Matriz listaDist = P3.listaDist.get(tamP);

        tenencia = 4.0;
        memoriaM = new int[cam][P3.MAXPAL][ciu];
        for (int i = 0; i < cam; i++) {
            for (int j = 0; j < P3.MAXPAL; j++) {
                for (int k = 0; k < ciu; k++) {
                    memoriaM[i][j][k] = 0;
                }
            }
        }
        memoriaC = new int[cam][ciu];
        for (int i = 0; i < cam; i++) {
            for (int j = 0; j < ciu; j++) {
                memoriaC[i][j] = 0;
            }
        }

        Solucion inicial = Solucion.genRandom(cam, listaPal, rand);
        inicial.coste = Solucion.funCoste(inicial, listaDist);
        eval++;
        inicial.eval = eval;
        Solucion actual, siguiente, mejor;
        elite = new Solucion(new Matriz(1, 1, 0));
        double[] probAcum = {KRAND, KRAND + KGREED, KRAND + KGREED + KREST};
        convergencia[tamP].add(inicial.coste);

        while (eval < maxeval && reini < P3.RESTART) {
            listaTaboo = new Lista<>();
            if (reini > 0) {
                if (rand.nextBoolean()) {
                    tenencia = Math.round(tenencia + tenencia * KSIZE);
                } else {
                    tenencia = Math.max(Math.round(tenencia - tenencia * KSIZE), 2.0);
                }

                double selector = rand.nextDouble();
                int indice = 0;
                while (indice < probAcum.length && selector >= probAcum[indice]) {
                    indice++;
                }
                switch (indice) {
                    case 0:
                        inicial = Solucion.genRandom(cam, listaPal, rand);
                        break;
                    case 1:
//                        inicial = Solucion.genMemoriaM(cam, listaPal, memoriaM);
                        inicial = Solucion.genMemoriaC(cam, listaPal, memoriaC);
                        break;
                    case 2:
                        inicial = elite;
                        break;
                    default:
                        throw new AssertionError();
                }
                inicial.coste = Solucion.funCoste(inicial, listaDist);
                eval++;
                inicial.eval = eval;
                convergencia[tamP].add(inicial.coste);
                if (elite.coste > inicial.coste) {
                    elite = inicial;
                }
            }

            actual = inicial;
            mejor = new Solucion(new Matriz(1, 1, 0));
            iter = 0;
            while (iter < maxiter) {
                vecindario = 0;
                while (vecindario < P3.VECIN) {
                    siguiente = Solucion.genTaboo(cam, actual, rand, listaTaboo);
                    siguiente.coste = Solucion.funCoste(siguiente, listaDist);
                    eval++;
                    siguiente.eval = eval;
                    if (siguiente.eval % P3.MS2 == 0) {
                        convergencia[tamP].add(siguiente.coste);
                    }
                    iter++;
                    vecindario++;
                    if (mejor.coste > siguiente.coste) {
                        mejor = siguiente;
                    }
                }
                actual = mejor;
                actualizarTaboo(actual);
                actualizarMemoria(actual.m, cam);
                if (elite.coste > actual.coste) {
                    elite = actual;
                }
            }
            reini++;
        }

        return elite;
    }

    public static Solucion BT(Random rand, int tamP, int maxiter, Solucion inicial, Solucion elite, Lista<Integer> convergencia, double tenencia) {
        int[] P = P3.P[tamP];
        int cam = P[2];
        int iter = 0;
        int eval = inicial.eval;
        int vecindario;
        Matriz listaDist = P3.listaDist.get(tamP);

        inicial.coste = Solucion.funCoste(inicial, listaDist);
        iter++;
        eval++;
        inicial.eval = eval;
        convergencia.add(inicial.coste);
        Solucion actual, siguiente, mejor;
        if (elite.coste > inicial.coste) {
            elite = inicial;
        }

        Lista<Movimiento> listaTaboo = new Lista<>();

        while (iter < maxiter) {
            actual = inicial;
            mejor = new Solucion(new Matriz(1, 1, 0));
            vecindario = 0;
            while (vecindario < P3.VECIN) {
                siguiente = Solucion.genTaboo(cam, actual, rand, listaTaboo);
                siguiente.coste = Solucion.funCoste(siguiente, listaDist);
                iter++;
                eval++;
                siguiente.eval = eval;
                if (siguiente.eval % P3.MM2 == 0) {
                    convergencia.add(siguiente.coste);
                }
                vecindario++;
                if (mejor.coste > siguiente.coste) {
                    mejor = siguiente;
                }
            }
            actual = mejor;
            BusquedaTaboo.actualizarTaboo(actual, listaTaboo, tenencia);
            if (elite.coste > actual.coste) {
                elite = actual;
            }
        }

        elite.lasteval = eval;
        return elite;
    }

    private void actualizarTaboo(Solucion s) {
        if (!listaTaboo.contains(s.mov)) {
            if (listaTaboo.size() == tenencia) {
                listaTaboo.remove(0);
            }
            listaTaboo.add(s.mov);
        }
    }

    private static void actualizarTaboo(Solucion s, Lista<Movimiento> listaTaboo, double tenencia) {
        if (!listaTaboo.contains(s.mov)) {
            if (listaTaboo.size() == tenencia) {
                listaTaboo.remove(0);
            }
            listaTaboo.add(s.mov);
        }
    }

    private void actualizarMemoria(Matriz m, int cam) {
        for (int i = 0; i < cam; i++) {
            for (int j = 0; j < P3.MAXPAL; j++) {
                int ciu = m.m[i][j] - 1;
//                memoriaM[i][j][ciu]++;
                memoriaC[i][ciu]++;
            }
        }
    }

}
