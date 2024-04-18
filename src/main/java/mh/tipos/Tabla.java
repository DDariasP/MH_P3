package mh.tipos;

import java.util.Objects;

/**
 *
 * @author diego
 */
public class Tabla {

    public final int filas, columnas;
    public Lista<Gen[]> t;

    public Tabla(int a, int b) {
        filas = a;
        columnas = b;
        t = new Lista<>();
        for (int i = 0; i < filas; i++) {
            Gen[] camion = new Gen[columnas];
            for (int j = 0; j < columnas; j++) {
                camion[j] = Gen.NULO;
            }
            t.add(camion);
        }
    }

    public Tabla(Tabla copia) {
        filas = copia.filas;
        columnas = copia.columnas;
        t = new Lista<>();
        for (int i = 0; i < filas; i++) {
            Gen[] camion = new Gen[columnas];
            for (int j = 0; j < columnas; j++) {
                camion[j] = copia.t.get(i)[j];
            }
            t.add(camion);
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
            Gen[] camion = t.get(i);
            Gen[] otro = obj.t.get(i);
            while (j < columnas && iguales) {
                if (camion[j] != otro[j]) {
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
        hash = 53 * hash + this.filas;
        hash = 53 * hash + this.columnas;
        hash = 53 * hash + Objects.hashCode(this.t);
        return hash;
    }

    @Override
    public String toString() {
        String output = "";
        for (int i = 0; i < filas; i++) {
            Gen[] camion = t.get(i);
            for (int j = 0; j < columnas; j++) {
                output = output + camion[j] + " ";
            }
            output = output + "\n";
        }
        return output;
    }

}
