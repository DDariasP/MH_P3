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
public class EnfriamientoSimulado {

    public final int SEED;
    public Random rand;
    public Solucion[] solES;
    public Lista[] convergencia;

    public static final double KA = 0.9;
    public static final int KI = 50;
    public double T, delta, aceptacion, T0, sigma, lnCiu;

    public EnfriamientoSimulado(int a) {
        SEED = a;
        rand = new Random(SEED);
        solES = new Solucion[P3.NUMP];
        convergencia = new Lista[P3.NUMP];
        for (int i = 0; i < P3.NUMP; i++) {
            convergencia[i] = new Lista<Integer>();
        }
    }

    public void ejecutarES() {
        for (int i = 0; i < P3.NUMP; i++) {
            solES[i] = ES(i);
            System.out.println(solES[i].coste + "\t" + solES[i].eval);
            if (i == 2 && SEED == 333) {
                GraficaS g = new GraficaS(convergencia[i], "ES", Color.GREEN);
                g.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                g.setBounds(200, 350, 800, 400);
                g.setTitle("ES - P" + (i + 1) + " - S" + SEED);
                g.setVisible(true);
            }
        }
    }

    public Solucion ES(int tamP) {
        int[] P = P3.P[tamP];
        int ciu = P[0];
        int cam = P[2];
        Lista listaPal = P3.listaPal.get(tamP);
        Matriz listaDist = P3.listaDist.get(tamP);
        int filas = listaDist.filas;

        double dividendo = 0.0;
        double divisor = 0.0;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j <= i; j++) {
                int d = listaDist.m[i][j];
                dividendo = dividendo + d;
                divisor++;

            }
        }
        double media = dividendo / divisor;

        double sumatorio = 0.0;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j <= i; j++) {
                int d = listaDist.m[i][j];
                double resta = d - media;
                sumatorio = sumatorio + Math.pow(resta, 2);

            }
        }
        sigma = Math.sqrt(sumatorio / divisor);
        lnCiu = Math.log(ciu);
        T0 = sigma / lnCiu;

        T = T0;
        int eval = -1;
        int maxeval = P3.MAX2 * ciu;
        int vecindario = 1;
        int enfr = 0;
        int maxenfr = KI * ciu;

        Solucion inicial = Solucion.genRandom(cam, listaPal, rand);
        inicial.coste = Solucion.funCoste(inicial, listaDist);
        eval++;
        inicial.eval = eval;
        inicial.T0 = T0;
        inicial.TF = T0;
        inicial.enfr = enfr;
        convergencia[tamP].add(inicial.coste);

        Solucion actual = inicial;
        Solucion siguiente = inicial;
        while (enfr < maxenfr && eval < maxeval) {
            vecindario = 1;
            while (vecindario < P3.VECIN && enfr < maxenfr && eval < maxeval) {
                siguiente = Solucion.gen2optAlt(cam, actual, rand);
                siguiente.coste = Solucion.funCoste(siguiente, listaDist);
                eval++;
                siguiente.eval = eval;
                if (siguiente.eval % P3.MS2 == 0) {
                    convergencia[tamP].add(siguiente.coste);
                }
                siguiente.T0 = T0;
                siguiente.TF = T;
                siguiente.enfr = enfr;
                vecindario++;
                delta = siguiente.coste - actual.coste;
                aceptacion = rand.nextDouble();
                if (delta < 0 || aceptacion < Math.exp(-delta / T)) {
                    actual = siguiente;
                }
                if (vecindario == P3.VECIN) {
                    T = KA * T;
                    enfr++;
                }
            }
        }

        convergencia[tamP].add(siguiente.coste);
        return actual;
    }

    public static Solucion ES(Random rand, int tamP, int maxiter, Solucion inicial, Lista<Integer> convergencia) {
        int[] P = P3.P[tamP];
        int ciu = P[0];
        int cam = P[2];
        Matriz listaDist = P3.listaDist.get(tamP);
        int filas = listaDist.filas;

        double dividendo = 0.0;
        double divisor = 0.0;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j <= i; j++) {
                int d = listaDist.m[i][j];
                dividendo = dividendo + d;
                divisor++;
            }
        }
        double media = dividendo / divisor;

        double sumatorio = 0.0;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j <= i; j++) {
                int d = listaDist.m[i][j];
                double resta = d - media;
                sumatorio = sumatorio + Math.pow(resta, 2);
            }
        }
        double sigma = Math.sqrt(sumatorio / divisor);
        double lnCiu = Math.log(ciu);
        double T0 = sigma / lnCiu;

        double T = T0;
        int iter = 0;
        int eval = inicial.eval;
        int vecindario = 1;
        int enfr = 0;
        int maxenfr = KI * ciu;

        inicial.coste = Solucion.funCoste(inicial, listaDist);
        iter++;
        eval++;
        inicial.eval = eval;
        inicial.T0 = T0;
        inicial.TF = T0;
        inicial.enfr = enfr;
        convergencia.add(inicial.coste);

        Solucion actual = inicial;
        Solucion siguiente = inicial;
        while (enfr < maxenfr && iter < maxiter) {
            vecindario = 1;
            while (vecindario < P3.VECIN && enfr < maxenfr && iter < maxiter) {
                siguiente = Solucion.gen2optAlt(cam, actual, rand);
                siguiente.coste = Solucion.funCoste(siguiente, listaDist);
                iter++;
                eval++;
                siguiente.eval = eval;
                if (siguiente.eval % P3.MM2 == 0) {
                    convergencia.add(siguiente.coste);
                }
                siguiente.T0 = T0;
                siguiente.TF = T;
                siguiente.enfr = enfr;
                vecindario++;
                double delta = siguiente.coste - actual.coste;
                double aceptacion = rand.nextDouble();
                if (delta < 0 || aceptacion < Math.exp(-delta / T)) {
                    actual = siguiente;
                }
                if (vecindario == P3.VECIN) {
                    T = KA * T;
                    enfr++;
                }
            }
        }

        if (inicial.eval == 0) {
            convergencia.add(siguiente.coste);
        }
        actual.lasteval = eval;
        return actual;
    }
}
