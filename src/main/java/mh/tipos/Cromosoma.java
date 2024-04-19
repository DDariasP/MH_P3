package mh.tipos;

import java.util.ArrayList;
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
    public Tabla t;
    public int coste;

    public Cromosoma(Tabla n) {
        eval = -1;
        lasteval = -1;
        t = new Tabla(n);
        coste = Integer.MAX_VALUE;
    }

    public static Cromosoma genRandom(int cam, Lista<Gen> listaGen, Random rand) {
        Tabla tabla = new Tabla(cam, P3.MAXPAL);

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
            while (tabla.t[x][y] != Gen.NULO) {
                y++;
            }
            tabla.t[x][y] = palet;
            palxcam[x]++;
        }

        return (new Cromosoma(tabla));
    }

    public static Cromosoma genGreedy(int cam, Lista<Gen> listaGen, Matriz listaDist, Random rand) {
        Lista<Gen> listaC = new Lista<>();
        for (int i = 0; i < listaGen.size(); i++) {
            listaC.add(listaGen.get(i));
        }

        int[] ultimopal = new int[cam];
        for (int i = 0; i < cam; i++) {
            ultimopal[i] = 1;
        }

        Tabla tabla = new Tabla(cam, P3.MAXPAL);

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

                tabla.t[j][i] = elegido;
                ultimopal[j] = elegido.destino;
//                System.out.println("elegido=" + elegido);
//                System.out.println(tabla);
            }
        }

        Cromosoma c = new Cromosoma(tabla);
        return c;
    }

    public static int funCoste(Cromosoma c, Matriz listaDist) {
        int coste = 0;
        for (int i = 0; i < c.t.filas; i++) {
            Lista<Integer> visitadas = new Lista<>();
            int actual = 0;
            visitadas.add(actual);
            Gen[] camion = c.t.t[i];
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

    public static Cromosoma torneo(int tam, Lista<Cromosoma> poblacion, Matriz listaDist, Random rand) {
        Lista<Cromosoma> torneo = new Lista<>();
        Lista<Integer> elegidos = new Lista<>();
        for (int i = 0; i < tam; i++) {
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

    public static void cruceOX(Cromosoma p1, Cromosoma p2, Cromosoma[] h, Random rand) {
        h[0] = p1;
        h[1] = p2;
    }

    public static void cruceAEX(Cromosoma p1, Cromosoma p2, Cromosoma[] h, Random rand) {
        h[0] = p1;
        h[1] = p2;
    }

    public void mutacionCM(int cam, Random rand) {
        int x1, x2, y1, y2;
        x1 = rand.nextInt(cam);
        x2 = rand.nextInt(cam);
        while (x2 == x1) {
            x2 = rand.nextInt(cam);
        }
        y1 = rand.nextInt(P3.MAXPAL);
        y2 = rand.nextInt(P3.MAXPAL);

        Gen tmp;
        tmp = this.t.t[x1][y1];
        this.t.t[x1][y1] = this.t.t[x2][y2];
        this.t.t[x2][y2] = tmp;
    }

    public void mutacionIM(int cam, Random rand) {
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
            seccion.add(this.t.t[x1][j]);
        }
        for (int i = x1 + 1; i < x2; i++) {
            for (int j = 0; j < P3.MAXPAL; j++) {
                seccion.add(this.t.t[i][j]);
            }
        }
        for (int j = 0; j <= y2; j++) {
            seccion.add(this.t.t[x2][j]);
        }

        seccion = Gen.invert(seccion);
        for (int j = y1; j < P3.MAXPAL; j++) {
            this.t.t[x1][j] = seccion.get(0);
            seccion.remove(0);
        }
        for (int i = x1 + 1; i < x2; i++) {
            for (int j = 0; j < P3.MAXPAL; j++) {
                this.t.t[i][j] = seccion.get(0);
                seccion.remove(0);
            }
        }
        for (int j = 0; j <= y2; j++) {
            this.t.t[x2][j] = seccion.get(0);
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
        lista.set(i, lista.get(j));
        lista.set(j, temp);
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

        return (t.equals(obj.t));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + this.eval;
        hash = 23 * hash + this.lasteval;
        hash = 23 * hash + Objects.hashCode(this.t);
        hash = 23 * hash + this.coste;
        return hash;
    }

    @Override
    public String toString() {
        String output = t.toString();
        return output;
    }

}
