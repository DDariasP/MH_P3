package mh.tipos;

import java.util.Arrays;

/**
 *
 * @author diego
 */
public class Matriz {

    public final int filas, columnas;
    public int[][] s;

    public Matriz(int a, int b, int c) {
        filas = a;
        columnas = b;
        s = new int[a][b];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                s[i][j] = c;
            }
        }
    }

    public Matriz(Matriz copia) {
        filas = copia.filas;
        columnas = copia.columnas;
        s = new int[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                s[i][j] = copia.s[i][j];
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
                if (s[i][j] != obj.s[i][j]) {
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
        hash = 37 * hash + Arrays.deepHashCode(this.s);
        return hash;
    }

    @Override
    public String toString() {
        String output = "";
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                output = output + s[i][j] + " ";
            }
            output = output + "\n";
        }
        return output;
    }

}
