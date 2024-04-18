package mh;

import mh.algoritmos.*;
import mh.tipos.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author diego
 */
public class Parser {

    public static Matriz leerDist(int ciu, String filename) {
        Matriz listaDist = new Matriz(ciu, ciu, -1);
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            String line;
            String[] tokens;
            int contador = 0;
            while (contador < ciu) {
                line = scanner.nextLine();
                tokens = line.split("\\s+");
                int[] fila = new int[ciu];
                for (int i = 0; i < ciu; i++) {
                    fila[i] = Integer.parseInt(tokens[i]);
                }
                listaDist.m[contador] = fila;
                contador++;
            }
            scanner.close();
        } catch (IOException ex) {
            System.out.println("Error en File.");
        }
        return listaDist;
    }

    public static Lista<Integer> leerPal(String filename) {
        Lista<Integer> listaPal = new Lista<>();
        try {
            File file = new File(filename);
            Scanner scanner = new Scanner(file);
            String line;
            while (scanner.hasNextLine()) {
                line = scanner.nextLine();
                listaPal.add(Integer.valueOf(line));
            }
            scanner.close();
        } catch (IOException ex) {
            System.out.println("Error en File.");
        }
        return listaPal;
    }

    public static void escribir(String filename, ArrayList<Object> lista) {
        try {
            File resultados = new File(filename);
            if (resultados.exists()) {
                resultados.delete();
                System.out.println("\nArchivo " + resultados.getName() + " sobreescrito.\n");
            } else {
                System.out.println("\nArchivo " + resultados.getName() + " creado.\n");
            }
            resultados.createNewFile();
            FileWriter writer = new FileWriter(filename);

            writer.write("GRASP-BL - n*" + P3.MAX);
            writer.write("\n---------------------");
            GRASP[] gbl = (GRASP[]) lista.get(5);
            for (int i = 0; i < P3.SEED.length; i++) {
                for (int j = 0; j < P3.NUMP; j++) {
                    writer.write("\n" + gbl[i].solGP[j].coste + "\t" + gbl[i].solGP[j].eval);
                }
                writer.write("\n---------------------");
            }
            writer.write("\n---------------------");

            writer.write("\nGRASP-ES - n*" + P3.MAX);
            writer.write("\n---------------------");
            GRASP[] ges = (GRASP[]) lista.get(6);
            for (int i = 0; i < P3.SEED.length; i++) {
                for (int j = 0; j < P3.NUMP; j++) {
                    writer.write("\n" + ges[i].solGP[j].coste + "\t" + ges[i].solGP[j].eval);
                }
                writer.write("\n---------------------");
            }
            writer.write("\n---------------------");

            writer.close();
        } catch (IOException e) {
            System.out.println("Error en File.");
        }
    }

}
