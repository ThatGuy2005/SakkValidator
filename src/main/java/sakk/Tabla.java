package sakk;

/** A sakktábla. (Egyelőre csak annyi, amennyi a Figura fordításához kell; a 3. lépés bővíti.) */
public class Tabla {
    private final Figura[][] mezok = new Figura[8][8];

    public Figura get(Mezo m) {
        return mezok[m.oszlop()][m.sor()];
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
