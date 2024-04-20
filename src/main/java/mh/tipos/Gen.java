package mh.tipos;

/**
 *
 * @author diego
 */
public class Gen {

    public final int id;
    public final int destino;
    public int coste;
    public static final Gen NULO = new Gen(-1, -1);

    public Gen(int n, int m) {
        id = n;
        destino = m;
        coste = Integer.MAX_VALUE;
    }

    public static Lista<Gen> invert(Lista<Gen> lista) {
        int tam = lista.size();
        Gen[] array = new Gen[tam];
        for (int i = 0; i < tam; i++) {
            array[i] = lista.get(i);
        }

        int inicio = 0;
        int fin = tam - 1;
        while (inicio < fin) {
            Gen tmp = array[inicio];
            array[inicio] = array[fin];
            array[fin] = tmp;
            inicio++;
            fin--;
        }

        Lista<Gen> inv = new Lista<>();
        for (int i = 0; i < tam; i++) {
            inv.add(array[i]);
        }
        return inv;
    }

    public static Lista<Gen> friendSort(Lista<Gen> lista, Cromosoma c) {
        Lista<Gen> f = new Lista<>();
        for (int i = 0; i < c.t.filas; i++) {
            for (int j = 0; j < c.t.columnas; j++) {
                f.add(c.t.t[i][j]);
            }
        }

        Lista<Gen> friend = new Lista<>();
        for (int i = 0; i < f.size(); i++) {
            if (lista.contains(f.get(i))) {
                friend.add(f.get(i));
            }
        }

        return friend;
    }

    public static void sort(Lista<Gen> lista) {
        if (lista == null || lista.isEmpty()) {
            return;
        }
        quickSort(lista, 0, lista.size() - 1);
    }

    private static void quickSort(Lista<Gen> lista, int menor, int mayor) {
        if (menor < mayor) {
            int indexPivote = partition(lista, menor, mayor);
            quickSort(lista, menor, indexPivote - 1);
            quickSort(lista, indexPivote + 1, mayor);
        }
    }

    private static int partition(Lista<Gen> lista, int menor, int mayor) {
        Gen pivote = lista.get(mayor);
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

    private static void swap(Lista<Gen> lista, int i, int j) {
        Gen temp = lista.get(i);
        lista.set(i, lista.get(j));
        lista.set(j, temp);
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }

        if (!(o instanceof Gen)) {
            return false;
        }

        Gen obj = (Gen) o;

        return (id == obj.id && destino == obj.destino);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + this.id;
        hash = 43 * hash + this.destino;
        return hash;
    }

    @Override
    public String toString() {
        String output = "[" + id + "," + destino + "]";
        return output;
    }

}
