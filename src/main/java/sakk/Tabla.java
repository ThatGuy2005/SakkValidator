package sakk;

/** A sakktábla: 8×8 mező, mindegyiken egy figura vagy null. */
public class Tabla {
    private final Figura[][] mezok = new Figura[8][8];
    private Lepes utolsoLepes;

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

    /**
     * Miért szabálytalan a lépés? null, ha szabályos.
     * Sorrend: van-e saját figura, léphet-e így (lephet), és nem kerül-e sakkba a saját király.
     */
    public String hibaOka(Mezo honnan, Mezo hova, Szin kiLep) {
        Figura f = get(honnan);
        if (f == null) return "nincs figura a(z) " + honnan + " mezőn";
        if (f.szin != kiLep) return "az nem a te figurád";
        if (!f.lephet(honnan, hova, this)) return "a figura nem léphet így";

        if (sancolas(f, honnan, hova)) {
            if (sakkbanVan(kiLep)) return "sakkban nem lehet sáncolni";
            Mezo koztes = new Mezo((honnan.oszlop() + hova.oszlop()) / 2, honnan.sor());
            Tabla atkeles = masolat();
            atkeles.athelyez(honnan, koztes);
            if (atkeles.sakkbanVan(kiLep)) return "a király támadott mezőn haladna át";
        }

        Tabla proba = masolat();
        proba.athelyez(honnan, hova);
        if (proba.sakkbanVan(kiLep)) return "a saját király sakkba kerülne";
        return null;
    }

    public boolean szabalyos(Mezo honnan, Mezo hova, Szin kiLep) {
        return hibaOka(honnan, hova, kiLep) == null;
    }

    /** Végrehajtja a lépést (ellenőrzés nélkül): ütés esetén a célmezőn álló figura lekerül. */
    public void vegrehajt(Mezo honnan, Mezo hova) {
        vegrehajt(honnan, hova, 'V');
    }

    /** Mint fent; ha a gyalog beér, a választott figurává változik (V/B/F/H, egyébként vezér). */
    public void vegrehajt(Mezo honnan, Mezo hova, char atvaltozas) {
        Figura f = athelyez(honnan, hova);
        f.mozdult = true;
        utolsoLepes = new Lepes(honnan, hova, f);
        if (f.atvaltozik(hova)) {
            Figura uj = ujFigura(atvaltozas, f.szin);
            uj.mozdult = true;
            tesz(hova, uj);
        }
    }

    /** Átváltozással járna-e a lépés? (A játékmenet ebből tudja, hogy kérdeznie kell.) */
    public boolean atvaltozas(Mezo honnan, Mezo hova) {
        Figura f = get(honnan);
        return f != null && f.atvaltozik(hova);
    }

    /** Figura létrehozása a választott betűből; ez létrehozás, nem lépéslogika. */
    private static Figura ujFigura(char betu, Szin szin) {
        return switch (Character.toUpperCase(betu)) {
            case 'B' -> new Bastya(szin);
            case 'F' -> new Futo(szin);
            case 'H' -> new Huszar(szin);
            default -> new Vezer(szin);
        };
    }

    /**
     * Az a mező, amelyre most en passant ütéssel lépni lehet (amelyen az ellenfél gyalogja
     * az előző lépésben átugrott), vagy null.
     */
    public Mezo enPassantCel() {
        // instanceof csak a gyalog kettős lépésének felismerésére
        if (utolsoLepes == null || !(utolsoLepes.f() instanceof Gyalog)) return null;
        Mezo h = utolsoLepes.honnan();
        int dy = utolsoLepes.hova().sor() - h.sor();
        if (Math.abs(dy) != 2) return null;
        return new Mezo(h.oszlop(), h.sor() + dy / 2);
    }

    /** Csak a figurákat mozgatja; a figurák állapotát nem módosítja, így másolaton is biztonságos. */
    private Figura athelyez(Mezo honnan, Mezo hova) {
        Figura f = levesz(honnan);
        if (sancolas(f, honnan, hova)) {
            boolean rovid = hova.oszlop() > honnan.oszlop();
            Mezo bastyaHonnan = new Mezo(rovid ? 7 : 0, honnan.sor());
            Mezo bastyaHova = new Mezo(rovid ? 5 : 3, honnan.sor());
            tesz(bastyaHova, levesz(bastyaHonnan));
        }
        // en passant: az ütött gyalog a célmező mögött, a kiinduló sorban áll
        if (f instanceof Gyalog && hova.equals(enPassantCel())) {
            levesz(new Mezo(hova.oszlop(), honnan.sor()));
        }
        tesz(hova, f);
        return f;
    }

    /** Sáncolás-e: a király két oszlopot lép. (instanceof csak a sáncolás azonosítására.) */
    private static boolean sancolas(Figura f, Mezo honnan, Mezo hova) {
        return f instanceof Kiraly && Math.abs(hova.oszlop() - honnan.oszlop()) == 2;
    }

    /** Támadja-e a megadott színű fél az m mezőt? Minden figurát a saját lephet()-jével kérdez meg. */
    public boolean tamadott(Mezo m, Szin tamado) {
        for (Mezo honnan : Mezo.OSSZES) {
            Figura f = get(honnan);
            if (f != null && f.szin == tamado && f.lephet(honnan, m, this)) return true;
        }
        return false;
    }

    /** Sakkban van-e a megadott színű király? (Király nélküli állásban: nem.) */
    public boolean sakkbanVan(Szin szin) {
        Mezo k = kiralyMezo(szin);
        return k != null && tamadott(k, szin.ellenfel());
    }

    /** Van-e a megadott színnek legalább egy szabályos lépése? */
    public boolean vanSzabalyosLepese(Szin szin) {
        for (Mezo honnan : Mezo.OSSZES) {
            Figura f = get(honnan);
            if (f == null || f.szin != szin) continue;
            for (Mezo hova : Mezo.OSSZES) {
                if (szabalyos(honnan, hova, szin)) return true;
            }
        }
        return false;
    }

    /** A lépésre következő fél helyzete: "matt", "patt", "sakk", vagy null, ha egyik sem. */
    public String allapot(Szin kiLep) {
        boolean sakk = sakkbanVan(kiLep);
        if (!vanSzabalyosLepese(kiLep)) return sakk ? "matt" : "patt";
        return sakk ? "sakk" : null;
    }

    private Mezo kiralyMezo(Szin szin) {
        for (Mezo m : Mezo.OSSZES) {
            // instanceof csak a király megkeresésére, nem lépéslogikára
            if (get(m) instanceof Kiraly k && k.szin == szin) return m;
        }
        return null;
    }

    /** Sekély másolat: ugyanazok a figura-objektumok, de saját mezőtömb. */
    private Tabla masolat() {
        Tabla t = new Tabla();
        t.utolsoLepes = utolsoLepes;
        for (int o = 0; o < 8; o++) {
            t.mezok[o] = mezok[o].clone();
        }
        return t;
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
