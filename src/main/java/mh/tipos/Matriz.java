package mh.tipos;

import java.util.Arrays;

/**
 *
 * @author diego
 */
public class Matriz {

    public final int filas, columnas;
    public int[][] m;

    public Matriz(int a, int b, int c) {
        filas = a;
        columnas = b;
        m = new int[a][b];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                m[i][j] = c;
            }
        }
    }

    public Matriz(Matriz copia) {
        filas = copia.filas;
        columnas = copia.columnas;
        m = new int[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                m[i][j] = copia.m[i][j];
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Matriz)) {
            return false;
        }

        Matriz obj = (Matriz) o;

        boolean iguales = true;
        if (filas != obj.filas || columnas != obj.columnas) {
            iguales = false;
        }
        int i = 0;
        while (i < filas && iguales) {
            int j = 0;
            while (j < columnas && iguales) {
                if (m[i][j] != obj.m[i][j]) {
                    iguales = false;
                }
                j++;
            }
            i++;
        }

        return iguales;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + this.filas;
        hash = 37 * hash + this.columnas;
        hash = 37 * hash + Arrays.deepHashCode(this.m);
        return hash;
    }

    @Override
    public String toString() {
        String output = "";
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                output = output + m[i][j] + " ";
            }
            output = output + "\n";
        }
        return output;
    }

}
