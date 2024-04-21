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
public class Memetico {

    public final int SEED;
    public Random rand;
    public Cromosoma[] cromMM;
    public Lista[] convergencia;
    public final String tipoX;
    public final int optG;
    public final double optP;
    public final String nombre;
    public final Color color;
    public int lastGen;
    public Lista<Cromosoma> cacheOpt;

    public Memetico(int a, String b, int c, double d) {
        SEED = a;
        rand = new Random(SEED);
        cromMM = new Cromosoma[P3.NUMP];
        convergencia = new Lista[P3.NUMP];
        for (int i = 0; i < P3.NUMP; i++) {
            convergencia[i] = new Lista<Integer>();
        }
        tipoX = b;
        optG = c;
        optP = d;
        nombre = tipoX + "-AM-" + optG + "-" + optP;
        switch (nombre) {
            case "OX-AM-1-0.2":
                color = Color.GREEN;
                break;
            case "OX-AM-10-1.0":
                color = Color.CYAN;
                break;
            case "AEX-AM-1-0.2":
                color = Color.MAGENTA;
                break;
            case "AEX-AM-10-1.0":
                color = Color.YELLOW;
                break;
            default:
                throw new AssertionError();
        }
    }

    public void ejecutarMM() {
        for (int i = 0; i < P3.NUMP; i++) {
            cromMM[i] = MM(i);
            System.out.println(cromMM[i].coste + "\t" + cromMM[i].eval);
            if (i == 2 && SEED == 333) {
                Grafica g = new Grafica(convergencia[i], "MM-" + nombre, color);
                g.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                g.setBounds(200, 350, 800, 400);
                g.setTitle("MM-" + nombre + " - P" + (i + 1) + " - S" + SEED);
                g.setVisible(true);
            }
        }
    }

    public Cromosoma MM(int tamP) {
        int[] P = P3.P[tamP];
        int ciu = P[0];
        int eval = -1;
        int maxeval = P3.MAX * ciu;
        Lista listaGen = P3.listaGen.get(tamP);
        Matriz listaDist = P3.listaDist.get(tamP);
        Cromosoma tmp;
        cacheOpt = new Lista<>();

        //INICIALIZACION
        Lista<Cromosoma> inicial = new Lista<>();
        tmp = Cromosoma.genGreedy(listaGen, listaDist, rand);
        tmp.coste = Cromosoma.funCoste(tmp, listaDist);
        eval++;
        tmp.eval = eval;
        inicial.add(tmp);

        lastGen = 0;
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
                    if (tipoX.equals("OX")) {
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
            //REEMPLAZAMIENTO
            if (descendientes == P3.POBLACION) {
                Cromosoma.sort(actual);
                Cromosoma.sort(siguiente);
                for (int i = 0; i < P3.ELITISMO; i++) {
                    siguiente.remove(siguiente.size() - 1);
                }
                for (int i = 0; i < P3.ELITISMO; i++) {
                    tmp = actual.get(i);
                    siguiente.add(tmp);
                }
                Cromosoma.sort(siguiente);
                actual = siguiente;
                lastGen++;
                //OPTIMIZACION
                if (lastGen % optG == 0) {
                    Cromosoma.optimizacionAM(actual, listaDist, optG, optP, cacheOpt, rand);
                    Cromosoma.sort(actual);
                }
                //RESULTADO
                if (lastGen % P3.MG == 0) {
                    convergencia[tamP].add(actual.get(0).coste);
                }
                if (elite.coste > actual.get(0).coste) {
                    elite = actual.get(0);
                    elite.lasteval = eval;
                }
            } else {
                System.out.println("lastGen=" + lastGen);
                convergencia[tamP].add(elite.coste);
            }
        }

        return elite;
    }
}
