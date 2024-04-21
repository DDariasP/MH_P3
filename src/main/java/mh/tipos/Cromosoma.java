package mh.tipos;

import java.util.Objects;
import java.util.Random;
import mh.*;

/**
 *
 * @author diego
 */
public class Cromosoma {

    public int eval;
    public int lasteval;
    public Solucion m;
    public int coste;

    public Cromosoma(Solucion n) {
        eval = -1;
        lasteval = -1;
        m = n;
        coste = Integer.MAX_VALUE;
    }

    public static Cromosoma genRandom(Lista<Gen> listaGen, Random rand) {
        int cam = listaGen.size() / P3.MAXPAL;
        Solucion matriz = new Solucion(cam, P3.MAXPAL);

        int[] palxcam = new int[cam];
        for (int i = 0; i < cam; i++) {
            palxcam[i] = 0;
        }

        for (int i = 0; i < listaGen.size(); i++) {
            Gen palet = listaGen.get(i);
            int x = rand.nextInt(cam);
            int y = 0;
            while (palxcam[x] == P3.MAXPAL) {
                x = (x + 1) % cam;
            }
            while (matriz.s[x][y] != Gen.NULO) {
                y++;
            }
            matriz.s[x][y] = palet;
            palxcam[x]++;
        }

        return (new Cromosoma(matriz));
    }

    public static Cromosoma genGreedy(Lista<Gen> listaGen, Matriz listaDist, Random rand) {
        int cam = listaGen.size() / P3.MAXPAL;
        Lista<Gen> listaC = new Lista<>(listaGen);

        int[] ultimopal = new int[cam];
        for (int i = 0; i < cam; i++) {
            ultimopal[i] = 1;
        }

        Solucion matriz = new Solucion(cam, P3.MAXPAL);

        for (int i = 0; i < P3.MAXPAL; i++) {
            for (int j = 0; j < cam; j++) {
                int ciucam = ultimopal[j] - 1;
                Lista<Gen> LRC = new Lista<>();
                for (int k = 0; k < listaC.size(); k++) {
                    int id = listaC.get(k).id;
                    int destino = listaC.get(k).destino;
                    int ciupal = destino - 1;
                    int coste = listaDist.m[ciucam][ciupal];
                    Gen tmp = new Gen(id, destino);
                    tmp.coste = coste;
                    LRC.add(tmp);
                }
                Gen.sort(LRC);

                int limite = (int) (0.1 * listaC.size());
                Gen elegido = null;
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
                listaC.remove(elegido);

                matriz.s[j][i] = elegido;
                ultimopal[j] = elegido.destino;
            }
        }

        Cromosoma c = new Cromosoma(matriz);
        return c;
    }

    public static Cromosoma gen4opt(Cromosoma c, Random rand) {
        int cam = c.m.filas;
        Solucion matriz = new Solucion(c.m);
        int x1, x2, x3, x4, y1, y2, y3, y4;

        x1 = rand.nextInt(cam);
        x2 = x1;
        x3 = rand.nextInt(cam);
        x4 = rand.nextInt(cam);
        while (x4 == x3 || x4 == x2) {
            x4 = rand.nextInt(cam);
        }

        y1 = rand.nextInt(P3.MAXPAL);
        y2 = rand.nextInt(P3.MAXPAL);
        y3 = rand.nextInt(P3.MAXPAL);
        y4 = rand.nextInt(P3.MAXPAL);

        Gen tmp;
        tmp = matriz.s[x1][y1];
        matriz.s[x1][y1] = matriz.s[x2][y2];
        matriz.s[x2][y2] = tmp;
        tmp = matriz.s[x3][y3];
        matriz.s[x3][y3] = matriz.s[x4][y4];
        matriz.s[x4][y4] = tmp;

        return (new Cromosoma(matriz));
    }

    public static int funCoste(Cromosoma c, Matriz listaDist) {
        int coste = 0;
        for (int i = 0; i < c.m.filas; i++) {
            Lista<Integer> visitadas = new Lista<>();
            int actual = 0;
            visitadas.add(actual);
            Gen[] camion = c.m.s[i];
            for (int j = 0; j < camion.length; j++) {
                int siguiente = camion[j].destino - 1;
                if (!visitadas.contains(siguiente) && siguiente != actual) {
                    coste = coste + listaDist.m[actual][siguiente];
                    actual = siguiente;
                    visitadas.add(actual);
                }
            }
            coste = coste + listaDist.m[actual][0];
        }
        return coste;
    }

    public static Cromosoma torneo(int K, Lista<Cromosoma> poblacion, Matriz listaDist, Random rand) {
        Lista<Cromosoma> torneo = new Lista<>();
        Lista<Integer> elegidos = new Lista<>();
        for (int i = 0; i < K; i++) {
            int pos = -1;
            while (pos == -1 || elegidos.contains(pos)) {
                pos = rand.nextInt(poblacion.size());
            }
            elegidos.add(pos);
            torneo.add(poblacion.get(pos));
        }
        Cromosoma.sort(torneo);
        Cromosoma ganador = torneo.get(0);
        return ganador;
    }

    public static void cruceOX(Cromosoma P0, Cromosoma P1, Cromosoma[] H, Random rand) {
        int cam = P0.m.filas;
        int x1, x2, y1, y2;
        x1 = rand.nextInt(cam);
        x2 = rand.nextInt(cam);
        while (x2 == x1) {
            x2 = rand.nextInt(cam);
        }
        if (x1 > x2) {
            int tmp = x1;
            x1 = x2;
            x2 = tmp;
        }
        y1 = rand.nextInt(P3.MAXPAL);
        y2 = rand.nextInt(P3.MAXPAL);

        Lista<Gen> restoP0 = new Lista<>();
        Lista<Gen> restoP1 = new Lista<>();
        Lista<Gen> seccionP0 = new Lista<>();
        Lista<Gen> seccionP1 = new Lista<>();

        //DE CAM_0 A CAM_X1-1
        for (int i = 0; i < x1; i++) {
            for (int j = 0; j < P3.MAXPAL; j++) {
                restoP0.add(P0.m.s[i][j]);
                restoP1.add(P1.m.s[i][j]);
            }
        }
        //DE CAM_X1_0 A CAM_X1_Y1-1
        for (int j = 0; j < y1; j++) {
            restoP0.add(P0.m.s[x1][j]);
            restoP1.add(P1.m.s[x1][j]);
        }
        //DE CAM_X1_Y1 A CAM_X1+1
        for (int j = y1; j < P3.MAXPAL; j++) {
            seccionP0.add(P0.m.s[x1][j]);
            seccionP1.add(P1.m.s[x1][j]);
        }
        //DE CAM_X1+1 A CAM_X2-1
        for (int i = x1 + 1; i < x2; i++) {
            for (int j = 0; j < P3.MAXPAL; j++) {
                seccionP0.add(P0.m.s[i][j]);
                seccionP1.add(P1.m.s[i][j]);
            }
        }
        //DE CAM_X2_0 A CAM_X2_Y2
        for (int j = 0; j <= y2; j++) {
            seccionP0.add(P0.m.s[x2][j]);
            seccionP1.add(P1.m.s[x2][j]);
        }
        //DE CAM_X2_Y2+1 A CAM_X2+1
        for (int j = y2 + 1; j < P3.MAXPAL; j++) {
            restoP0.add(P0.m.s[x2][j]);
            restoP1.add(P1.m.s[x2][j]);
        }
        //DE CAM_X2+1 A CAM_Z
        for (int i = x2 + 1; i < cam; i++) {
            for (int j = 0; j < P3.MAXPAL; j++) {
                restoP0.add(P0.m.s[i][j]);
                restoP1.add(P1.m.s[i][j]);
            }
        }

        Lista<Gen> friendP0 = Gen.friendSort(restoP0, P1);
        Lista<Gen> friendP1 = Gen.friendSort(restoP1, P0);
        H[0] = new Cromosoma(new Solucion(cam, P3.MAXPAL));
        H[1] = new Cromosoma(new Solucion(cam, P3.MAXPAL));

        //DE CAM_0 A CAM_X1-1
        for (int i = 0; i < x1; i++) {
            for (int j = 0; j < P3.MAXPAL; j++) {
                H[0].m.s[i][j] = friendP0.get(0);
                friendP0.remove(0);
                H[1].m.s[i][j] = friendP1.get(0);
                friendP1.remove(0);
            }
        }
        //DE CAM_X1_0 A CAM_X1_Y1-1
        for (int j = 0; j < y1; j++) {
            H[0].m.s[x1][j] = friendP0.get(0);
            friendP0.remove(0);
            H[1].m.s[x1][j] = friendP1.get(0);
            friendP1.remove(0);
        }
        //DE CAM_X1_Y1 A CAM_X1+1
        for (int j = y1; j < P3.MAXPAL; j++) {
            H[0].m.s[x1][j] = seccionP0.get(0);
            seccionP0.remove(0);
            H[1].m.s[x1][j] = seccionP1.get(0);
            seccionP1.remove(0);

        }
        //DE CAM_X1+1 A CAM_X2-1
        for (int i = x1 + 1; i < x2; i++) {
            for (int j = 0; j < P3.MAXPAL; j++) {
                H[0].m.s[i][j] = seccionP0.get(0);
                seccionP0.remove(0);
                H[1].m.s[i][j] = seccionP1.get(0);
                seccionP1.remove(0);
            }
        }
        //DE CAM_X2_0 A CAM_X2_Y2
        for (int j = 0; j <= y2; j++) {
            H[0].m.s[x2][j] = seccionP0.get(0);
            seccionP0.remove(0);
            H[1].m.s[x2][j] = seccionP1.get(0);
            seccionP1.remove(0);
        }
        //DE CAM_X2_Y2+1 A CAM_X2+1
        for (int j = y2 + 1; j < P3.MAXPAL; j++) {
            H[0].m.s[x2][j] = friendP0.get(0);
            friendP0.remove(0);
            H[1].m.s[x2][j] = friendP1.get(0);
            friendP1.remove(0);
        }
        //DE CAM_X2+1 A CAM_Z
        for (int i = x2 + 1; i < cam; i++) {
            for (int j = 0; j < P3.MAXPAL; j++) {
                H[0].m.s[i][j] = friendP0.get(0);
                friendP0.remove(0);
                H[1].m.s[i][j] = friendP1.get(0);
                friendP1.remove(0);
            }
        }
    }

    public static void cruceAEX(Cromosoma P0, Cromosoma P1, Cromosoma[] H, Random rand) {
        int cam = P0.m.filas;
        Lista<Gen>[] genesP = new Lista[2];
        genesP[0] = new Lista<>();
        genesP[1] = new Lista<>();
        for (int i = 0; i < cam; i++) {
            for (int j = 0; j < P3.MAXPAL; j++) {
                genesP[0].add(P0.m.s[i][j]);
                genesP[1].add(P1.m.s[i][j]);
            }
        }

        Lista<Gen>[] genesH = new Lista[2];
        genesH[0] = new Lista<>();
        genesH[0].add(genesP[0].get(0));
        genesH[0].add(genesP[0].get(1));
        genesH[1] = new Lista<>();
        genesH[1].add(genesP[1].get(0));
        genesH[1].add(genesP[1].get(1));

        int tam = genesP[0].size();
        int limite = tam - 2;
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < limite; j++) {
                Gen actual = genesH[i].get(genesH[i].size() - 1);
                Gen siguiente = actual;
                int tipo = (j % 2 - i + 1) % 2;
                int pos = genesP[tipo].position(actual);
                if (pos + 1 < tam) {
                    siguiente = genesP[tipo].get(pos + 1);
                }
                while (genesH[i].contains(siguiente)) {
                    pos = rand.nextInt(tam);
                    siguiente = genesP[tipo].get(pos);
                }
                genesH[i].add(siguiente);
            }
        }

        H[0] = new Cromosoma(new Solucion(cam, P3.MAXPAL));
        H[1] = new Cromosoma(new Solucion(cam, P3.MAXPAL));
        for (int i = 0; i < cam; i++) {
            for (int j = 0; j < P3.MAXPAL; j++) {
                H[0].m.s[i][j] = genesH[0].get(0);
                genesH[0].remove(0);
                H[1].m.s[i][j] = genesH[1].get(0);
                genesH[1].remove(0);
            }
        }
    }

    public static void mutacionCM(Cromosoma c, Random rand) {
        int cam = c.m.filas;
        int x1, x2, y1, y2;
        x1 = rand.nextInt(cam);
        x2 = rand.nextInt(cam);
        while (x2 == x1) {
            x2 = rand.nextInt(cam);
        }
        y1 = rand.nextInt(P3.MAXPAL);
        y2 = rand.nextInt(P3.MAXPAL);

        Gen tmp;
        tmp = c.m.s[x1][y1];
        c.m.s[x1][y1] = c.m.s[x2][y2];
        c.m.s[x2][y2] = tmp;
    }

    public static void mutacionIM(Cromosoma c, Random rand) {
        int cam = c.m.filas;
        int x1, x2, y1, y2;
        x1 = rand.nextInt(cam);
        x2 = rand.nextInt(cam);
        while (x2 == x1) {
            x2 = rand.nextInt(cam);
        }
        if (x1 > x2) {
            int tmp = x1;
            x1 = x2;
            x2 = tmp;
        }
        y1 = rand.nextInt(P3.MAXPAL);
        y2 = rand.nextInt(P3.MAXPAL);

        Lista<Gen> seccion = new Lista<>();
        for (int j = y1; j < P3.MAXPAL; j++) {
            seccion.add(c.m.s[x1][j]);
        }
        for (int i = x1 + 1; i < x2; i++) {
            for (int j = 0; j < P3.MAXPAL; j++) {
                seccion.add(c.m.s[i][j]);
            }
        }
        for (int j = 0; j <= y2; j++) {
            seccion.add(c.m.s[x2][j]);
        }

        seccion = Gen.invert(seccion);
        for (int j = y1; j < P3.MAXPAL; j++) {
            c.m.s[x1][j] = seccion.get(0);
            seccion.remove(0);
        }
        for (int i = x1 + 1; i < x2; i++) {
            for (int j = 0; j < P3.MAXPAL; j++) {
                c.m.s[i][j] = seccion.get(0);
                seccion.remove(0);
            }
        }
        for (int j = 0; j <= y2; j++) {
            c.m.s[x2][j] = seccion.get(0);
            seccion.remove(0);
        }
    }

    public static void sort(Lista<Cromosoma> lista) {
        if (lista == null || lista.isEmpty()) {
            return;
        }
        quickSort(lista, 0, lista.size() - 1);
    }

    private static void quickSort(Lista<Cromosoma> lista, int menor, int mayor) {
        if (menor < mayor) {
            int indexPivote = partition(lista, menor, mayor);
            quickSort(lista, menor, indexPivote - 1);
            quickSort(lista, indexPivote + 1, mayor);
        }
    }

    private static int partition(Lista<Cromosoma> lista, int menor, int mayor) {
        Cromosoma pivote = lista.get(mayor);
        int i = menor - 1;
        for (int j = menor; j < mayor; j++) {
            if (lista.get(j).coste - pivote.coste <= 0) {
                i++;
                swap(lista, i, j);
            }
        }
        swap(lista, i + 1, mayor);
        return (i + 1);
    }

    private static void swap(Lista<Cromosoma> lista, int i, int j) {
        Cromosoma temp = lista.get(i);
        lista.replace(i, lista.get(j));
        lista.replace(j, temp);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Cromosoma)) {
            return false;
        }

        Cromosoma obj = (Cromosoma) o;

        return (m.equals(obj.m));
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.m);
        return hash;
    }

    @Override
    public String toString() {
        String output = m.toString();
        return output;
    }

}
