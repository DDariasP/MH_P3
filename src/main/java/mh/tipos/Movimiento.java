package mh.tipos;

import java.util.Random;
import mh.*;

/**
 *
 * @author diego
 */
public class Movimiento {

    public int oriX, oriY, destX, destY;

    public Movimiento() {
        oriX = -1;
        oriY = -1;
        destX = -1;
        destY = -1;
    }

    public Movimiento(int x1, int y1, int x2, int y2) {
        oriX = x1;
        oriY = y1;
        destX = x2;
        destY = y2;
    }

    Movimiento(Random rand, int cam) {
        oriX = rand.nextInt(cam);
        destX = rand.nextInt(cam);
        oriY = rand.nextInt(P3.MAXPAL);
        destY = rand.nextInt(P3.MAXPAL);
    }

    public static void aplicar(Movimiento mov, Matriz matriz) {
        int tmp;
        tmp = matriz.m[mov.oriX][mov.oriY];
        matriz.m[mov.oriX][mov.oriY] = matriz.m[mov.destX][mov.destY];
        matriz.m[mov.destX][mov.destY] = tmp;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }

        if (!(o instanceof Movimiento)) {
            return false;
        }

        Movimiento obj = (Movimiento) o;

        return (oriX == obj.oriX && oriY == obj.oriY
                && destX == obj.destX && destY == obj.destY);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + this.oriX;
        hash = 79 * hash + this.oriY;
        hash = 79 * hash + this.destX;
        hash = 79 * hash + this.destY;
        return hash;
    }

    @Override
    public String toString() {
        String output = "[" + oriX + "][" + oriY + "] -> [" + destX + "][" + destY + "]";
        return output;
    }

}
