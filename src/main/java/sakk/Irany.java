package sakk;

import java.util.List;

/** A 8 lépésirány (dx = oszlop, dy = sor változása). */
public enum Irany {
    FEL(0, 1), LE(0, -1), JOBBRA(1, 0), BALRA(-1, 0),
    FEL_JOBBRA(1, 1), FEL_BALRA(-1, 1), LE_JOBBRA(1, -1), LE_BALRA(-1, -1);

    public static final List<Irany> EGYENES = List.of(FEL, LE, JOBBRA, BALRA);
    public static final List<Irany> ATLOS = List.of(FEL_JOBBRA, FEL_BALRA, LE_JOBBRA, LE_BALRA);
    public static final List<Irany> MIND = List.of(values());

    public final int dx;
    public final int dy;

    Irany(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }
}
