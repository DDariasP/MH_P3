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
public class BusquedaLocal {

    public final int SEED;
    public Random rand;
    public Solucion[] solBL;
    public Lista[] convergencia;

    public BusquedaLocal(int a) {
        SEED = a;
        rand = new Random(SEED);
        solBL = new Solucion[P3.NUMP];
        convergencia = new Lista[P3.NUMP];
        for (int i = 0; i < P3.NUMP; i++) {
            convergencia[i] = new Lista<Integer>();
        }
    }

    public void ejecutarBL() {
        for (int i = 0; i < P3.NUMP; i++) {
            solBL[i] = BL(i);
            System.out.println(solBL[i].coste + "\t" + solBL[i].eval);
            if (i == 2 && SEED == 333) {
                GraficaS g = new GraficaS(convergencia[i], "BL", Color.RED);
                g.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                g.setBounds(200, 350, 800, 400);
                g.setTitle("BL - P" + (i + 1) + " - S" + SEED);
                g.setVisible(true);
            }
        }
    }

    public Solucion BL(int tamP) {
        int[] P = P3.P[tamP];
        int ciu = P[0];
        int cam = P[2];
        int eval = -1;
        int maxeval = P3.MAX2 * ciu;
        Lista listaPal = P3.listaPal.get(tamP);
        Matriz listaDist = P3.listaDist.get(tamP);

        Solucion inicial = Solucion.genRandom(cam, listaPal, rand);
        inicial.coste = Solucion.funCoste(inicial, listaDist);
        eval++;
        inicial.eval = eval;
        convergencia[tamP].add(inicial.coste);

        Solucion actual = inicial;
        while (eval < maxeval) {
            Solucion siguiente = Solucion.gen4opt(cam, actual, rand);
            siguiente.coste = Solucion.funCoste(siguiente, listaDist);
            eval++;
            siguiente.eval = eval;
            if (siguiente.eval % P3.MS2 == 0) {
                convergencia[tamP].add(siguiente.coste);
            }
            if (actual.coste > siguiente.coste) {
                actual = siguiente;
            }
        }

        return actual;
    }

    public static Solucion BL(Random rand, int tamP, int maxiter, Solucion inicial, Lista<Integer> convergencia) {
        int[] P = P3.P[tamP];
        int cam = P[2];
        int iter = 0;
        int eval = inicial.eval;
        Matriz listaDist = P3.listaDist.get(tamP);

        inicial.coste = Solucion.funCoste(inicial, listaDist);
        iter++;
        eval++;
        inicial.eval = eval;
        convergencia.add(inicial.coste);

        Solucion actual = inicial;
        Solucion siguiente = inicial;
        while (iter < maxiter) {
            siguiente = Solucion.gen4opt(cam, actual, rand);
            siguiente.coste = Solucion.funCoste(siguiente, listaDist);
            iter++;
            eval++;
            siguiente.eval = eval;
            if (siguiente.eval % P3.MM2 == 0) {
                convergencia.add(siguiente.coste);
            }
            if (actual.coste > siguiente.coste) {
                actual = siguiente;
            }
        }

        convergencia.add(siguiente.coste);
        actual.lasteval = eval;
        return actual;
    }

}
