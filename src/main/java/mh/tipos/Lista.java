package mh.tipos;

import java.util.ArrayList;

/**
 *
 * @author diego
 * @param <T>
 */
public class Lista<T> {

    public ArrayList<T> lista;

    public Lista() {
        lista = new ArrayList<>();
    }

    public void add(T obj) {
        lista.add(obj);
    }

    public void remove(int index) {
        lista.remove(index);
    }

    public void remove(T obj) {
        lista.remove(obj);
    }

    public T get(int index) {
        return lista.get(index);
    }

    public void set(int i, T obj) {
        lista.set(i, obj);
    }

    public boolean contains(T obj) {
        return lista.contains(obj);
    }

    public int count(T obj) {
        int n = 0;
        for (int i = 0; i < lista.size(); i++) {
            T tmp = lista.get(i);
            if (obj.equals(tmp)) {
                n++;
            }
        }
        return n;
    }

    public int position(T obj) {
        int pos = -1;
        if (lista.contains(obj)) {
            int i = 0;
            boolean encontrado = false;
            while (!encontrado) {
                if (lista.get(i).equals(obj)) {
                    encontrado = true;
                    pos = i;
                }
                i++;
            }
        }
        return pos;
    }

    public int size() {
        return lista.size();
    }

    public boolean isEmpty() {
        return lista.isEmpty();
    }

    @Override
    public String toString() {
        String output = "";
        for (int i = 0; i < lista.size(); i++) {
            output = output + lista.get(i).toString() + " ";
        }
        return output;
    }

}
