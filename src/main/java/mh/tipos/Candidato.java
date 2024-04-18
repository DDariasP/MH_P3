package mh.tipos;

/**
 *
 * @author diego
 */
public class Candidato {

    public final int id;
    public int coste;

    public Candidato(int n, int m) {
        id = n;
        coste = m;
    }

    public static void sort(Lista<Candidato> lista) {
        if (lista == null || lista.isEmpty()) {
            return;
        }
        quickSort(lista, 0, lista.size() - 1);
    }

    private static void quickSort(Lista<Candidato> lista, int menor, int mayor) {
        if (menor < mayor) {
            int indexPivote = partition(lista, menor, mayor);
            quickSort(lista, menor, indexPivote - 1);
            quickSort(lista, indexPivote + 1, mayor);
        }
    }

    private static int partition(Lista<Candidato> lista, int menor, int mayor) {
        Candidato pivote = lista.get(mayor);
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

    private static void swap(Lista<Candidato> lista, int i, int j) {
        Candidato temp = lista.get(i);
        lista.set(i, lista.get(j));
        lista.set(j, temp);
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }

        if (!(o instanceof Candidato)) {
            return false;
        }

        Candidato obj = (Candidato) o;

        return (id == obj.id);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 43 * hash + this.id;
        hash = 43 * hash + this.coste;
        return hash;
    }

    @Override
    public String toString() {
        String output = "[" + id + "," + coste + "]";
        return output;
    }

}
