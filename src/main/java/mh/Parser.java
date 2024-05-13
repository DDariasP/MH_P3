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
                listaDist.s[contador] = fila;
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

            writer.write("GG-OX-M1 - n*" + P3.MAXGG[0]);
            writer.write("\n---------------------");
            GeneticoGeneracional[] gg11 = (GeneticoGeneracional[]) lista.get(0);
            for (int i = 0; i < P3.SEED.length; i++) {
                for (int j = 0; j < P3.NUMP; j++) {
                    writer.write("\n" + gg11[i].cromGG[j].coste + "\t" + gg11[i].cromGG[j].eval);
                }
                writer.write("\n---------------------");
            }
            writer.write("\n---------------------");

            writer.write("\nGG-OX-M2 - n*" + P3.MAXGG[1]);
            writer.write("\n---------------------");
            GeneticoGeneracional[] gg12 = (GeneticoGeneracional[]) lista.get(1);
            for (int i = 0; i < P3.SEED.length; i++) {
                for (int j = 0; j < P3.NUMP; j++) {
                    writer.write("\n" + gg12[i].cromGG[j].coste + "\t" + gg12[i].cromGG[j].eval);
                }
                writer.write("\n---------------------");
            }
            writer.write("\n---------------------");

            writer.write("\nGG-AEX-M1 - n*" + P3.MAXGG[2]);
            writer.write("\n---------------------");
            GeneticoGeneracional[] gg21 = (GeneticoGeneracional[]) lista.get(2);
            for (int i = 0; i < P3.SEED.length; i++) {
                for (int j = 0; j < P3.NUMP; j++) {
                    writer.write("\n" + gg21[i].cromGG[j].coste + "\t" + gg21[i].cromGG[j].eval);
                }
                writer.write("\n---------------------");
            }
            writer.write("\n---------------------");

            writer.write("\nGG-AEX-M2 - n*" + P3.MAXGG[3]);
            writer.write("\n---------------------");
            GeneticoGeneracional[] gg22 = (GeneticoGeneracional[]) lista.get(3);
            for (int i = 0; i < P3.SEED.length; i++) {
                for (int j = 0; j < P3.NUMP; j++) {
                    writer.write("\n" + gg22[i].cromGG[j].coste + "\t" + gg22[i].cromGG[j].eval);
                }
                writer.write("\n---------------------");
            }
            writer.write("\n---------------------");

            writer.write("\nMM-OX-AM-1-0.2 - n*" + P3.MAXMM[0]);
            writer.write("\n---------------------");
            Memetico[] mm11 = (Memetico[]) lista.get(4);
            for (int i = 0; i < P3.SEED.length; i++) {
                for (int j = 0; j < P3.NUMP; j++) {
                    writer.write("\n" + mm11[i].cromMM[j].coste + "\t" + mm11[i].cromMM[j].eval);
                }
                writer.write("\n---------------------");
            }
            writer.write("\n---------------------");

            writer.write("\nMM-OX-AM-10-1.0 - n*" + P3.MAXMM[1]);
            writer.write("\n---------------------");
            Memetico[] mm12 = (Memetico[]) lista.get(5);
            for (int i = 0; i < P3.SEED.length; i++) {
                for (int j = 0; j < P3.NUMP; j++) {
                    writer.write("\n" + mm12[i].cromMM[j].coste + "\t" + mm12[i].cromMM[j].eval);
                }
                writer.write("\n---------------------");
            }
            writer.write("\n---------------------");

            writer.write("\nMM-AEX-AM-1-0.2 - n*" + P3.MAXMM[2]);
            writer.write("\n---------------------");
            Memetico[] mm21 = (Memetico[]) lista.get(6);
            for (int i = 0; i < P3.SEED.length; i++) {
                for (int j = 0; j < P3.NUMP; j++) {
                    writer.write("\n" + mm21[i].cromMM[j].coste + "\t" + mm21[i].cromMM[j].eval);
                }
                writer.write("\n---------------------");
            }
            writer.write("\n---------------------");

            writer.write("\nMM-AEX-AM-10-1.0 - n*" + P3.MAXMM[3]);
            writer.write("\n---------------------");
            Memetico[] mm22 = (Memetico[]) lista.get(7);
            for (int i = 0; i < P3.SEED.length; i++) {
                for (int j = 0; j < P3.NUMP; j++) {
                    writer.write("\n" + mm22[i].cromMM[j].coste + "\t" + mm22[i].cromMM[j].eval);
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
