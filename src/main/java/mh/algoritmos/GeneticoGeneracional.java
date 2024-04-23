package mh.algoritmos;

import java.awt.Color;
import java.text.DecimalFormat;
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
    public final int id;
    public final String nombre;
    public final Color color;
    public int gen, eval, maxeval;

    public GeneticoGeneracional(int a, String b, int c) {
        SEED = a;
        rand = new Random(SEED);
        cromGG = new Cromosoma[P3.NUMP];
        convergencia = new Lista[P3.NUMP];
        for (int i = 0; i < P3.NUMP; i++) {
            convergencia[i] = new Lista<Integer>();
        }
        nombre = b;
        id = c;
        switch (id) {
            case 0:
                color = Color.GREEN;
                break;
            case 1:
                color = Color.CYAN;
                break;
            case 2:
                color = Color.MAGENTA;
                break;
            case 3:
                color = Color.YELLOW;
                break;
            default:
                throw new AssertionError();
        }
    }

    public void ejecutarGG() {
        for (int i = 0; i < P3.NUMP; i++) {
            double time = System.currentTimeMillis();
            cromGG[i] = GG(i);
            time = ((System.currentTimeMillis() - time) / 6000);
            String t = new DecimalFormat("#.00").format(time);
            System.out.println(t + " seg");
            System.out.println("lastGen=" + gen);
            System.out.println("lastEval=" + eval);
            System.out.println("coste=" + cromGG[i].coste);
            System.out.println("eval=" + cromGG[i].eval);
            if (i == -1 && SEED == -1) {
                Grafica g = new Grafica(convergencia[i], nombre, color, P3.RATIOGG[id]);
                g.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                g.setBounds(200, 350, 800, 400);
                g.setTitle(nombre + " - P" + (i + 1) + " - S" + SEED);
                g.setVisible(true);
            }
        }
    }

    public Cromosoma GG(int tamP) {
        int[] P = P3.P[tamP];
        int ciu = P[0];
        maxeval = P3.MAXGG[id] * ciu;
        eval = 0;
        gen = 0;
        Lista listaGen = P3.listaGen.get(tamP);
        Matriz listaDist = P3.listaDist.get(tamP);
        Cromosoma tmp;

        //INICIALIZACION
        Lista<Cromosoma> inicial = new Lista<>();
        tmp = Cromosoma.genGreedy(listaGen, listaDist, rand);
        tmp.coste = Cromosoma.funCoste(tmp, listaDist);
        eval++;
        tmp.eval = eval;
        inicial.add(tmp);
        Cromosoma elite = tmp;
        convergencia[tamP].add(elite.coste);

        for (int i = 1; i < P3.POBLACION; i++) {
            tmp = Cromosoma.genRandom(listaGen, rand);
            tmp.coste = Cromosoma.funCoste(tmp, listaDist);
            eval++;
            tmp.eval = eval;
            inicial.add(tmp);
        }

        //GENERACIONES
        Lista<Cromosoma> actual = inicial;
        while (eval <= maxeval - 3) {
            //SELECCION Y RECOMBINACION
            Lista<Cromosoma> siguiente = new Lista<>();
            int descendientes = 0;
            while (eval <= maxeval - 2 && descendientes <= P3.POBLACION - 2) {
                Cromosoma padre1 = Cromosoma.torneo(P3.TORNEO, actual, listaDist, rand);
                Cromosoma padre2 = Cromosoma.torneo(P3.TORNEO, actual, listaDist, rand);
                double cruce = rand.nextDouble();
                if (cruce >= 1.0 - P3.CRUCE) {
                    Cromosoma[] hijos = new Cromosoma[2];
                    if (id == 0 || id == 1) {
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
            if (descendientes == P3.POBLACION) {
                int mutaciones = 0;
                while (eval <= maxeval - 1 && mutaciones <= P3.POBLACION - 1) {
                    double mutaP = rand.nextDouble();
                    if (mutaP >= 1.0 - P3.MUTACION) {
                        tmp = siguiente.get(mutaciones);
                        if (id == 0 || id == 2) {
                            Cromosoma.mutacionCM(tmp, rand);
                        } else {
                            Cromosoma.mutacionIM(tmp, rand);
                        }
                        tmp.coste = Cromosoma.funCoste(tmp, listaDist);
                        eval++;
                        tmp.eval = eval;
                    }
                    mutaciones++;
                }
                //REEMPLAZAMIENTO
                if (mutaciones == P3.POBLACION) {
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
                    gen++;
                    if (gen % P3.RATIOGG[id] == 0) {
                        convergencia[tamP].add(actual.get(0).coste);
                    }
                    if (elite.coste > actual.get(0).coste) {
                        elite = actual.get(0);
                    }
                } else {
                    convergencia[tamP].add(elite.coste);
                }
            }
        }

        return elite;
    }
}
