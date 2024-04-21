package mh.tipos;

import java.util.Arrays;

/**
 *
 * @author diego
 */
public class Tabla {

    public final int filas, columnas;
    public Gen[][] s;

    public Tabla(int a, int b) {
        filas = a;
        columnas = b;
        s = new Gen[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                s[i][j] = Gen.NULO;
            }
        }
    }

    public Tabla(Tabla copia) {
        filas = copia.filas;
        columnas = copia.columnas;
        s = new Gen[filas][columnas];
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
        int hash = 3;
        hash = 11 * hash + this.filas;
        hash = 11 * hash + this.columnas;
        hash = 11 * hash + Arrays.deepHashCode(this.s);
        return hash;
    }

    @Override
    public String toString() {
        String output = "\n";
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                output = output + s[i][j] + " ";
            }
            output = output + "\n";
        }
        return output;
    }

}
