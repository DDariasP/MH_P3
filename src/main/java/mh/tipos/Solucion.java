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
    public double T0;
    public double TF;
    public int enfr;
    public Movimiento mov;

    public Solucion(Matriz n) {
        eval = -1;
        lasteval = -1;
        m = new Matriz(n);
        coste = Integer.MAX_VALUE;
        mov = new Movimiento();
    }

    public Solucion(Matriz n, Movimiento a) {
        eval = -1;
        lasteval = -1;
        m = new Matriz(n);
        coste = Integer.MAX_VALUE;
        mov = a;
    }

    public static Solucion genRandom(int cam, Lista<Integer> listaPal, Random rand) {
        Matriz matriz = new Matriz(cam, P3.MAXPAL, -1);

        int[] palxcam = new int[cam];
        for (int i = 0; i < cam; i++) {
            palxcam[i] = 0;
        }

        int contador = 0;
        while (contador < listaPal.size()) {
            int palet = listaPal.get(contador);
            int x = rand.nextInt(cam);
            int y = 0;
            while (palxcam[x] == P3.MAXPAL) {
                x = (x + 1) % cam;
            }
            while (matriz.m[x][y] != -1) {
                y++;
            }
            matriz.m[x][y] = palet;
            contador++;
            palxcam[x]++;
        }

        return (new Solucion(matriz));
    }

    public static Solucion gen2optAlt(int cam, Solucion s, Random rand) {
        Matriz matriz = new Matriz(s.m);

        int x1, x2, y1, y2;

        if (s.eval % 2 == 0) {
            x1 = rand.nextInt(cam);
            x2 = x1;
        } else {
            x1 = rand.nextInt(cam);
            x2 = rand.nextInt(cam);
            while (x2 == x1) {
                x2 = rand.nextInt(cam);
            }
        }
        y1 = rand.nextInt(P3.MAXPAL);
        y2 = rand.nextInt(P3.MAXPAL);

        Movimiento nuevo = new Movimiento(x1, y1, x2, y2);
        Movimiento.aplicar(nuevo, matriz);

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

        Movimiento nuevo;
        nuevo = new Movimiento(x1, y1, x2, y2);
        Movimiento.aplicar(nuevo, matriz);
        nuevo = new Movimiento(x3, y3, x4, y4);
        Movimiento.aplicar(nuevo, matriz);

        return (new Solucion(matriz));
    }

    public static Solucion gen4optAlt(int cam, Solucion s, Random rand) {
        Matriz matriz = new Matriz(s.m);

        int x1, x2, x3, x4, y1, y2, y3, y4;

        if (s.eval % 2 == 0) {
            x1 = rand.nextInt(cam);
            x2 = x1;
        } else {
            x1 = rand.nextInt(cam);
            x2 = rand.nextInt(cam);
            while (x2 == x1) {
                x2 = rand.nextInt(cam);
            }
        }
        x3 = rand.nextInt(cam);
        x4 = rand.nextInt(cam);
        while (x4 == x3 || x4 == x2) {
            x4 = rand.nextInt(cam);
        }

        y1 = rand.nextInt(P3.MAXPAL);
        y2 = rand.nextInt(P3.MAXPAL);
        y3 = rand.nextInt(P3.MAXPAL);
        y4 = rand.nextInt(P3.MAXPAL);

        Movimiento nuevo;
        nuevo = new Movimiento(x1, y1, x2, y2);
        Movimiento.aplicar(nuevo, matriz);
        nuevo = new Movimiento(x3, y3, x4, y4);
        Movimiento.aplicar(nuevo, matriz);

        return (new Solucion(matriz));
    }

    public static Solucion genTaboo(int cam, Solucion s, Random rand, Lista<Movimiento> listaTaboo) {

        Movimiento nuevo = new Movimiento(rand, cam);
        while (listaTaboo.contains(nuevo)) {
            nuevo = new Movimiento(rand, cam);
        }

        Matriz matriz = new Matriz(s.m);
        Movimiento.aplicar(nuevo, matriz);

        return (new Solucion(matriz, nuevo));
    }

    public static Solucion genMemoriaM(int cam, Lista<Integer> listaPal, int[][][] memoriaM) {
        Matriz matriz = new Matriz(cam, P3.MAXPAL, -1);

        for (int ite = 0; ite < listaPal.size(); ite++) {
            int palet = listaPal.get(ite);
            int x = -1;
            int y = -1;
            double min = 0;
            boolean encontrado = false;
            while (!encontrado) {
                int i = 0;
                while (!encontrado && i < cam) {
                    int j = 0;
                    while (!encontrado && j < P3.MAXPAL) {
                        if (memoriaM[i][j][palet - 1] == min && matriz.m[i][j] == -1) {
                            encontrado = true;
                            x = i;
                            y = j;
                        }
                        j++;
                    }
                    i++;
                }
                min++;
            }
            matriz.m[x][y] = palet;
        }

        return (new Solucion(matriz));
    }

    public static Solucion genMemoriaC(int cam, Lista<Integer> listaPal, int[][] memoriaC) {
        Matriz matriz = new Matriz(cam, P3.MAXPAL, -1);

        for (int ite = 0; ite < listaPal.size(); ite++) {
            int palet = listaPal.get(ite);
            int x = -1;
            int y = -1;
            double min = 0;
            boolean encontrado = false;
            while (!encontrado) {
                int i = 0;
                while (!encontrado && i < cam) {
                    int j = 0;
                    while (!encontrado && j < P3.MAXPAL) {
                        if (memoriaC[i][palet - 1] == min && matriz.m[i][j] == -1) {
                            encontrado = true;
                            x = i;
                            y = j;
                        }
                        j++;
                    }
                    i++;
                }
                min++;
            }
            matriz.m[x][y] = palet;
        }

        return (new Solucion(matriz));
    }

    public static Solucion genMutacion(int cam, Solucion s, Random rand) {
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
        hash = 23 * hash + Objects.hashCode(this.mov);
        return hash;
    }

    @Override
    public String toString() {
        String output = m.toString();
        return output;
    }

}
