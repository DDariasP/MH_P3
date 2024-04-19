package mh.tipos;

import java.util.Arrays;

/**
 *
 * @author diego
 */
public class Tabla {

    public final int filas, columnas;
    public Gen[][] t;

    public Tabla(int a, int b) {
        filas = a;
        columnas = b;
        t = new Gen[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                t[i][j] = Gen.NULO;
            }
        }
    }

    public Tabla(Tabla copia) {
        filas = copia.filas;
        columnas = copia.columnas;
        t = new Gen[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                t[i][j] = copia.t[i][j];
            }
        }
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }

        if (!(o instanceof Tabla)) {
            return false;
        }

        Tabla obj = (Tabla) o;

        boolean iguales = true;
        if (filas != obj.filas || columnas != obj.columnas) {
            iguales = false;
        }
        int i = 0;
        while (i < filas && iguales) {
            int j = 0;
            while (j < columnas && iguales) {
                if (t[i][j] != obj.t[i][j]) {
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
        int hash = 3;
        hash = 11 * hash + this.filas;
        hash = 11 * hash + this.columnas;
        hash = 11 * hash + Arrays.deepHashCode(this.t);
        return hash;
    }

    @Override
    public String toString() {
        String output = "";
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                output = output + t[i][j] + " ";
            }
            output = output + "\n";
        }
        return output;
    }

}
