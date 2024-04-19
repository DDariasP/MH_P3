package mh;

import java.util.Random;
import mh.tipos.*;

/**
 *
 * @author diego
 */
public final class CromosomaTest {

    public static void test() {
        Random r1 = new Random(333);
        Random r2 = new Random(333);

        Solucion s = Solucion.genRandom(12, P3.listaPal.get(2), r1);
        System.out.println(Solucion.funCoste(s, P3.listaDist.get(2)));
        System.out.println(s);

        Cromosoma c = Cromosoma.genRandom(12, P3.listaGen.get(2), r2);
        System.out.println(Cromosoma.funCoste(c, P3.listaDist.get(2)));
        System.out.println(c);
    }

}
