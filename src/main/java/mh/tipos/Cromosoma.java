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
            Gen[] camion = tabla.t.get(x);
            while (camion[y] != Gen.NULO) {
                y++;
            }
            camion[y] = palet;
            palxcam[x]++;
        }

        return (new Cromosoma(tabla));
    }

    public static int funCoste(Cromosoma c, Matriz listaDist) {
        int coste = 0;
        for (int i = 0; i < c.t.filas; i++) {
            Lista<Integer> visitadas = new Lista<>();
            int actual = 0;
            visitadas.add(actual);
            Gen[] camion = c.t.t.get(i);
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
