package sakk;

/** A sakktábla: 8×8 mező, mindegyiken egy figura vagy null. */
public class Tabla {
    private final Figura[][] mezok = new Figura[8][8];

    /** A szabványos kezdőállás. */
    public static Tabla kezdoAllas() {
        Tabla t = new Tabla();
        for (Szin sz : Szin.values()) {
            int hatso = sz == Szin.FEHER ? 0 : 7;
            int gyalogSor = sz == Szin.FEHER ? 1 : 6;
            Figura[] tisztek = {
                    new Bastya(sz), new Huszar(sz), new Futo(sz), new Vezer(sz),
                    new Kiraly(sz), new Futo(sz), new Huszar(sz), new Bastya(sz)
            };
            for (int o = 0; o < 8; o++) {
                t.tesz(new Mezo(o, hatso), tisztek[o]);
                t.tesz(new Mezo(o, gyalogSor), new Gyalog(sz));
            }
        }
        return t;
    }

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

    /** Végrehajtja a lépést (ellenőrzés nélkül): ütés esetén a célmezőn álló figura lekerül. */
    public void vegrehajt(Mezo honnan, Mezo hova) {
        Figura f = levesz(honnan);
        tesz(hova, f);
        f.mozdult = true;
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
