package mh.algoritmos;

import java.awt.Color;
import java.util.Random;
import static javax.swing.WindowConstants.*;
import mh.*;
import mh.tipos.*;

/**
 *
 * @author diego
 */
public class GeneticoGeneracional {

    public final int SEED;
    public Random rand;
    public Cromosoma[] cromGG;
    public Lista[] convergencia;
    public final int tipoX, tipoM;
    public int lastGen;

    public GeneticoGeneracional(int a, int b, int c) {
        SEED = a;
        rand = new Random(SEED);
        cromGG = new Cromosoma[P3.NUMP];
        convergencia = new Lista[P3.NUMP];
        for (int i = 0; i < P3.NUMP; i++) {
            convergencia[i] = new Lista<Integer>();
        }
        tipoX = b;
        tipoM = c;
    }

    public void ejecutarGG() {
        for (int i = 0; i < P3.NUMP; i++) {
            cromGG[i] = GG(i);
            System.out.println(cromGG[i].coste + "\t" + cromGG[i].eval);
            if (SEED == 1) {
                GraficaE g = new GraficaE(convergencia[i], "GG", Color.GREEN);
                g.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                g.setBounds(200, 350, 800, 400);
                g.setTitle("GG - P" + (i + 1) + " - S" + SEED);
                g.setVisible(true);
            }
        }
    }

    public Cromosoma GG(int tamP) {
        int[] P = P3.P[tamP];
        int ciu = P[0];
        int cam = P[2];
        int eval = -1;
        int maxeval = P3.MAX * ciu;
        Lista listaGen = P3.listaGen.get(tamP);
        Matriz listaDist = P3.listaDist.get(tamP);
        Cromosoma tmp;

        //INICIALIZACION
        Lista<Cromosoma> inicial = new Lista<>();
        tmp = Cromosoma.genGreedy(cam, listaGen, listaDist, rand);
        tmp.coste = Cromosoma.funCoste(tmp, listaDist);
        eval++;
        tmp.eval = eval;
        inicial.add(tmp);

        lastGen = 0;
        Cromosoma elite = tmp;

        for (int i = 1; i < P3.POBLACION; i++) {
            tmp = Cromosoma.genRandom(cam, listaGen, rand);
            tmp.coste = Cromosoma.funCoste(tmp, listaDist);
            eval++;
            tmp.eval = eval;
            inicial.add(tmp);
        }

        //GENERACIONES
        Lista<Cromosoma> actual = inicial;
        while (eval < maxeval - 1) {
            //SELECCION Y RECOMBINACION
            Lista<Cromosoma> siguiente = new Lista<>();
            int descendientes = 0;
            while (eval < maxeval - 1 && descendientes < P3.POBLACION) {
                Cromosoma padre1 = Cromosoma.torneo(P3.TORNEO, actual, listaDist, rand);
                Cromosoma padre2 = Cromosoma.torneo(P3.TORNEO, actual, listaDist, rand);
                double cruce = rand.nextDouble();
                if (cruce >= 1.0 - P3.CRUCE) {
                    Cromosoma[] hijos = new Cromosoma[2];
                    if (tipoX == 1) {
                        Cromosoma.cruceOX(padre1, padre2, hijos, rand);
                    } else {
                        Cromosoma.cruceAEX(padre1, padre2, hijos, rand);
                    }
                    hijos[0].coste = Cromosoma.funCoste(hijos[0], listaDist);
                    eval++;
                    hijos[0].eval = eval;
                    siguiente.add(hijos[0]);
                    descendientes++;
                    hijos[1].coste = Cromosoma.funCoste(hijos[1], listaDist);
                    eval++;
                    hijos[1].eval = eval;
                    siguiente.add(hijos[1]);
                    descendientes++;
                } else {
                    siguiente.add(padre1);
                    descendientes++;
                    siguiente.add(padre2);
                    descendientes++;
                }
            }
            //MUTACION
            int mutaciones = 0;
            while (eval < maxeval - 1 && mutaciones < P3.POBLACION) {
                double mutacion = rand.nextDouble();
                if (mutacion >= 1.0 - P3.MUTACION) {
                    tmp = siguiente.get(mutaciones);
                    if (tipoM == 1) {
                        tmp.mutacionCM(cam, rand);
                    } else {
                        tmp.mutacionIM(cam, rand);
                    }
                    tmp.coste = Cromosoma.funCoste(tmp, listaDist);
                    eval++;
                    tmp.eval = eval;
                }
                mutaciones++;
            }
            //REEMPLAZAMIENTO
            if (descendientes == P3.POBLACION && mutaciones == P3.POBLACION) {
                Cromosoma.sort(actual);
                Cromosoma.sort(siguiente);
                for (int i = 0; i < P3.ELITISMO; i++) {
                    siguiente.remove(siguiente.size() - 1);
                }
                for (int i = 0; i < P3.ELITISMO; i++) {
                    tmp = actual.get(i);
                    siguiente.add(tmp);
                }
                //RESULTADO
                Cromosoma.sort(siguiente);
                actual = siguiente;
                lastGen++;
                if (elite.coste > actual.get(0).coste) {
                    elite = actual.get(0);
                    elite.lasteval = eval;
                }
            } else {
//                System.out.println("lastGen=" + lastGen);
//                System.out.println("descendientes=" + descendientes);
//                System.out.println("mutaciones=" + mutaciones);
            }
        }

        return elite;
    }
}
