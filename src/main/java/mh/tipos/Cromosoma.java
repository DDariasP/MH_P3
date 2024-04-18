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
    public Matriz m;
    public int coste;

    public Cromosoma(Matriz n) {
        eval = -1;
        lasteval = -1;
        m = new Matriz(n);
        coste = Integer.MAX_VALUE;
    }

    public static Cromosoma genRandom(int cam, Lista<Gen> listaGen, Random rand) {
        Matriz matriz = new Matriz(cam, P3.MAXPAL, -1);

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
            while (matriz.m[x][y] != -1) {
                y++;
            }
            matriz.m[x][y] = palet;
            palxcam[x]++;
        }

        return (new Cromosoma(matriz));
    }

    public static Cromosoma genMutacion(int cam, Cromosoma s, Random rand) {
        Matriz matriz = new Matriz(s.m);

        int x1, x2, y1, y2, y3, y4;

        x1 = rand.nextInt(cam);
        x2 = rand.nextInt(cam);
        while (x2 == x1) {
            x2 = rand.nextInt(cam);
        }

        y1 = rand.nextInt(P3.MAXPAL);
        y2 = rand.nextInt(P3.MAXPAL);
        while (y2 == y1 || Math.abs(y2 - y1) > 4) {
            y2 = rand.nextInt(P3.MAXPAL);
        }
        y3 = rand.nextInt(P3.MAXPAL);
        y4 = rand.nextInt(P3.MAXPAL);
        while (y4 == y3 || Math.abs(y4 - y3) > 4) {
            y4 = rand.nextInt(P3.MAXPAL);
        }

        Movimiento nuevo;
        nuevo = new Movimiento(x1, y1, x2, y3);
        Movimiento.aplicar(nuevo, matriz);
        nuevo = new Movimiento(x1, y2, x2, y4);
        Movimiento.aplicar(nuevo, matriz);

        return (new Cromosoma(matriz));
    }

    public static int funCoste(Cromosoma s, Matriz listaDist) {
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

        if (!(o instanceof Cromosoma)) {
            return false;
        }

        Cromosoma obj = (Cromosoma) o;

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
