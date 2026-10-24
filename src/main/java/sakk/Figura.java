package sakk;

import java.util.List;

/**
 * Egy sakkfigura. Minden figura maga tudja, hogyan léphet.
 *
 * <p>Szerződés a {@link #lephet} metódusra (minden alosztályra kötelező):
 * <ul>
 *   <li>TISZTA: nincs mellékhatása, nem módosítja sem a táblát, sem a figurát.</li>
 *   <li>Csak pszeudo-szabályosságot vizsgál: a lépés geometriáját, hogy szabad-e az út,
 *       és hogy a célmezőn nem saját figura áll.</li>
 *   <li>Azt NEM vizsgálja, hogy a lépés után sakkban marad-e a saját király;
 *       az a {@link Tabla} dolga.</li>
 * </ul>
 */
public abstract class Figura {
    final Szin szin;
    boolean mozdult;

    protected Figura(Szin szin) {
        this.szin = szin;
    }

    /** Pszeudo-szabályos-e a lépés honnan → hova. Tiszta függvény. */
    public abstract boolean lephet(Mezo honnan, Mezo hova, Tabla t);

    /** A figura Unicode jele (szín szerint). */
    public abstract char jel();

    /** A célmező üres, vagy ellenfél áll rajta. */
    protected boolean celSzabad(Mezo hova, Tabla t) {
        Figura cel = t.get(hova);
        return cel == null || cel.szin != szin;
    }

    /** Csúszó lépés a megadott irányok egyikén, akadály nélkül. */
    protected boolean csuszik(Mezo honnan, Mezo hova, Tabla t, List<Irany> iranyok) {
        int dx = hova.oszlop() - honnan.oszlop();
        int dy = hova.sor() - honnan.sor();
        if (dx == 0 && dy == 0) return false;
        if (dx != 0 && dy != 0 && Math.abs(dx) != Math.abs(dy)) return false;
        boolean joIrany = iranyok.stream()
                .anyMatch(i -> i.dx == Integer.signum(dx) && i.dy == Integer.signum(dy));
        return joIrany && !t.utbanVan(honnan, hova) && celSzabad(hova, t);
    }
}
