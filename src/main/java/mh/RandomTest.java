package mh;

import java.util.Random;

/**
 *
 * @author diego
 */
public class RandomTest {

    public final int SEED;
    public Random rand;

    public RandomTest(int a) {
        SEED = a;
        rand = new Random(SEED);
    }

    public static void randomTest() {
        RandomTest t1 = new RandomTest(000);
        test1(t1);
        test2(t1);
        RandomTest t2 = new RandomTest(000);
        test1(t2);
        test2(t2);
    }

    public static void test1(RandomTest t) {
        String s = "";
        for (int i = 0; i < 10; i++) {
            s = s + t.rand.nextInt() % 10 + " ";
        }
        System.out.println(s);
    }

    public static void test2(RandomTest t) {
        String s = "";
        for (int i = 0; i < 5; i++) {
            s = s + t.rand.nextInt() % 5 + " ";
        }
        System.out.println(s);
    }

}
