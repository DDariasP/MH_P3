package mh.algoritmos;

import mh.*;
import mh.tipos.*;
import java.util.Random;
import static javax.swing.WindowConstants.*;

/**
 *
 * @author diego
 */
public class GRASP {

    public final int SEED;
    public Random rand;
    public Solucion[] solGP;
    public Lista[][] convergencia;
    public Lista<Solucion> listaElite;

    public GRASP(int a) {
        SEED = a;
        rand = new Random(SEED);
        solGP = new Solucion[P3.NUMP];
        convergencia = new Lista[P3.NUMP][P3.RESTART];
        for (int i = 0; i < P3.NUMP; i++) {
            for (int j = 0; j < P3.RESTART; j++) {
                convergencia[i][j] = new Lista<Integer>();
            }
        }
    }

    public void ejecutarBL() {
        for (int i = 0; i < P3.NUMP; i++) {
            solGP[i] = BL(i);
            System.out.println(solGP[i].coste + "\t" + solGP[i].eval + "\tn=" + listaElite.count(solGP[i]));
            if (i == 2 && SEED == 333) {
                GraficaM g = new GraficaM(convergencia[i], "GRASP-BL");
                g.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                g.setBounds(200, 350, 800, 500);
                g.setTitle("GRASP-BL - P" + (i + 1) + " - S" + SEED);
                g.setVisible(true);
            }
        }
    }

    public Solucion BL(int tamP) {
        int[] P = P3.P[tamP];
        int ciu = P[0];
        int maxiter = P3.MAX * ciu;

        int lasteval = -1;
        Solucion elite = new Solucion(new Matriz(1, 1, 0));
        listaElite = new Lista<>();
        for (int i = 0; i < P3.RESTART; i++) {
//            Solucion inicial = LRCCamiones(tamP);
            Solucion inicial = LRCPalets(tamP);
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
            solGP[i] = ES(i);
            System.out.println(solGP[i].coste + "\t" + solGP[i].eval + "\tn=" + listaElite.count(solGP[i]));
            if (i == 2 && SEED == 333) {
                GraficaM g = new GraficaM(convergencia[i], "GRASP-ES");
                g.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                g.setBounds(200, 350, 800, 500);
                g.setTitle("GRASP-ES - P" + (i + 1) + " - S" + SEED);
                g.setVisible(true);
            }
        }
    }

    public Solucion ES(int tamP) {
        int[] P = P3.P[tamP];
        int ciu = P[0];
        int maxiter = P3.MAX * ciu;

        int lasteval = -1;
        Solucion elite = new Solucion(new Matriz(1, 1, 0));
        listaElite = new Lista<>();
        for (int i = 0; i < P3.RESTART; i++) {
//            Solucion inicial = LRCCamiones(tamP);
            Solucion inicial = LRCPalets(tamP);
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
            solGP[i] = BT(i);
            System.out.println(solGP[i].coste + "\t" + solGP[i].eval + "\tn=" + listaElite.count(solGP[i]));
            if (i == 2 && SEED == 333) {
                GraficaM g = new GraficaM(convergencia[i], "GRASP-BT");
                g.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                g.setBounds(200, 350, 800, 500);
                g.setTitle("GRASP-BT - P" + (i + 1) + " - S" + SEED);
                g.setVisible(true);
            }
        }
    }

    public Solucion BT(int tamP) {
        int[] P = P3.P[tamP];
        int ciu = P[0];
        int maxiter = P3.MAX * ciu;

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
//            Solucion inicial = LRCCamiones(tamP);
            Solucion inicial = LRCPalets(tamP);
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

    public Solucion LRCCamiones(int tamP) {
        int[] P = P3.P[tamP];
        int cam = P[2];
        Lista<Integer> listaPal = P3.listaPal.get(tamP);
        Matriz listaDist = P3.listaDist.get(tamP);

        int[] ultimopal = new int[cam];
        int[] palxcam = new int[cam];
        for (int i = 0; i < cam; i++) {
            ultimopal[i] = 1;
            palxcam[i] = 0;
        }

        Matriz matriz = new Matriz(cam, P3.MAXPAL, -1);

        for (int i = 0; i < listaPal.size(); i++) {
            int palet = listaPal.get(i);
            int ciupal = palet - 1;
            int[] distcalc = new int[cam];
            for (int j = 0; j < cam; j++) {
                int ciucam = ultimopal[j] - 1;
                distcalc[j] = listaDist.m[ciucam][ciupal];
            }

            Lista<Candidato> LRC = new Lista();
            for (int j = 0; j < cam; j++) {
                if (palxcam[j] < P3.MAXPAL) {
                    LRC.add(new Candidato(j, distcalc[j]));
                }
            }
            Candidato.sort(LRC);

            int limite = (int) (0.5 * cam);
            Candidato elegido = null;
            while (elegido == null) {
                int pos = rand.nextInt(limite);
                if (pos < LRC.size()) {
                    elegido = LRC.get(pos);
                }
            }

            matriz.m[elegido.id][palxcam[elegido.id]] = palet;
            ultimopal[elegido.id] = palet;
            palxcam[elegido.id]++;
//            System.out.println("elegido=" + elegido.id);
//            System.out.println(matriz);
        }

        Solucion s = new Solucion(matriz);
        return s;
    }

    public Solucion LRCPalets(int tamP) {
        int[] P = P3.P[tamP];
        int cam = P[2];
        Lista<Integer> listaP = P3.listaPal.get(tamP);
        Lista<Candidato> listaPal = new Lista<>();
        for (int i = 0; i < listaP.size(); i++) {
            listaPal.add(new Candidato(listaP.get(i), -1));
        }
        Matriz listaDist = P3.listaDist.get(tamP);

        int[] ultimopal = new int[cam];
        for (int i = 0; i < cam; i++) {
            ultimopal[i] = 1;
        }

        Matriz matriz = new Matriz(cam, P3.MAXPAL, -1);

        for (int i = 0; i < P3.MAXPAL; i++) {
            for (int j = 0; j < cam; j++) {
                int ciucam = ultimopal[j] - 1;
                Lista<Candidato> LRC = new Lista<>();
                for (int k = 0; k < listaPal.size(); k++) {
                    int id = listaPal.get(k).id;
                    int ciupal = id - 1;
                    int coste = listaDist.m[ciucam][ciupal];
                    Candidato tmp = new Candidato(id, coste);
                    LRC.add(tmp);
                }
                Candidato.sort(LRC);

                int limite = (int) (0.1 * listaPal.size());
                Candidato elegido = null;
                while (elegido == null) {
                    int pos;
                    if (limite <= 0) {
                        pos = 0;
                    } else {
                        pos = rand.nextInt(limite);
                    }
                    if (pos < LRC.size()) {
                        elegido = LRC.get(pos);
                    }
                }
                listaPal.remove(elegido);

                matriz.m[j][i] = elegido.id;
                ultimopal[j] = elegido.id;
//                System.out.println("elegido=" + elegido.id);
//                System.out.println(matriz);
            }
        }

        Solucion s = new Solucion(matriz);
        return s;
    }
}
