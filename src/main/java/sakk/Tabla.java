package sakk;

/** A sakktábla: 8×8 mező, mindegyiken egy figura vagy null. */
public class Tabla {
    private final Figura[][] mezok = new Figura[8][8];

    public Figura get(Mezo m) {
        return mezok[m.oszlop()][m.sor()];
    }

    public void tesz(Mezo m, Figura f) {
        mezok[m.oszlop()][m.sor()] = f;
    }

    /** Leveszi és visszaadja a mezőn álló figurát (vagy null-t). */
    public Figura levesz(Mezo m) {
        Figura f = get(m);
        tesz(m, null);
        return f;
    }

    /** Áll-e figura honnan és hova KÖZÖTT (egyenes vagy átlós vonalon). */
    public boolean utbanVan(Mezo honnan, Mezo hova) {
        int dx = Integer.signum(hova.oszlop() - honnan.oszlop());
        int dy = Integer.signum(hova.sor() - honnan.sor());
        int o = honnan.oszlop() + dx;
        int s = honnan.sor() + dy;
        while (o != hova.oszlop() || s != hova.sor()) {
            if (mezok[o][s] != null) return true;
            o += dx;
            s += dy;
        }
        return false;
    }
}
