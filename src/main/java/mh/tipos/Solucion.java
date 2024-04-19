package mh.tipos;

import java.util.Objects;
import java.util.Random;
import mh.*;

/**
 *
 * @author diego
 */
public class Solucion {

    public int eval;
    public int lasteval;
    public Matriz m;
    public int coste;

    public Solucion(Matriz n) {
        eval = -1;
        lasteval = -1;
        m = new Matriz(n);
        coste = Integer.MAX_VALUE;
    }

    public static Solucion genRandom(int cam, Lista<Integer> listaPal, Random rand) {
        Matriz matriz = new Matriz(cam, P3.MAXPAL, -1);

        int[] palxcam = new int[cam];
        for (int i = 0; i < cam; i++) {
            palxcam[i] = 0;
        }

        for (int i = 0; i < listaPal.size(); i++) {
            int palet = listaPal.get(i);
            int x = rand.nextInt(cam);
            int y = 0;
            while (palxcam[x] == P3.MAXPAL) {
                x = (x + 1) % cam;
            }
            while (matriz.m[x][y] != -1) {
                y++;
            }
            matriz.m[x][y] = palet;
            palxcam[x]++;
        }

        return (new Solucion(matriz));
    }

    public static Solucion gen4opt(int cam, Solucion s, Random rand) {
        Matriz matriz = new Matriz(s.m);

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

        int tmp;
        tmp = matriz.m[x1][y1];
        matriz.m[x1][y1] = matriz.m[x2][y2];
        matriz.m[x2][y2] = tmp;
        tmp = matriz.m[x3][y3];
        matriz.m[x3][y3] = matriz.m[x4][y4];
        matriz.m[x4][y4] = tmp;

        return (new Solucion(matriz));
    }

    public static int funCoste(Solucion s, Matriz listaDist) {
        int coste = 0;
        for (int i = 0; i < s.m.filas; i++) {
            Lista<Integer> visitadas = new Lista<>();
            int actual = 0;
            visitadas.add(actual);
            int[] camion = s.m.m[i];
            for (int j = 0; j < camion.length; j++) {
                int siguiente = camion[j] - 1;
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

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }

        if (!(o instanceof Solucion)) {
            return false;
        }

        Solucion obj = (Solucion) o;

        return (m.equals(obj.m));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + this.eval;
        hash = 23 * hash + this.lasteval;
        hash = 23 * hash + Objects.hashCode(this.m);
        hash = 23 * hash + this.coste;
        return hash;
    }

    @Override
    public String toString() {
        String output = m.toString();
        return output;
    }

}
