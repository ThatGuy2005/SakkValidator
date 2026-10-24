package sakk;

public class Kiraly extends Figura {

    public Kiraly(Szin szin) {
        super(szin);
    }

    @Override
    public boolean lephet(Mezo honnan, Mezo hova, Tabla t) {
        int dx = hova.oszlop() - honnan.oszlop();
        int dy = hova.sor() - honnan.sor();
        if (Math.max(Math.abs(dx), Math.abs(dy)) == 1) {
            return celSzabad(hova, t);
        }
        return dy == 0 && Math.abs(dx) == 2 && sancolhat(honnan, dx > 0, t);
    }

    /**
     * A sáncolás pszeudo-szabályos része: a király és a bástya nem mozdult, köztük üres a sor.
     * Hogy a király sakkban van-e, vagy támadott mezőn haladna át, azt a Tabla vizsgálja.
     */
    private boolean sancolhat(Mezo honnan, boolean rovid, Tabla t) {
        int hatsoSor = szin == Szin.FEHER ? 0 : 7;
        if (mozdult || !honnan.equals(new Mezo(4, hatsoSor))) return false;
        Mezo bastyaMezo = new Mezo(rovid ? 7 : 0, hatsoSor);
        // instanceof csak a sáncoló bástya azonosítására
        return t.get(bastyaMezo) instanceof Bastya b && b.szin == szin && !b.mozdult
                && !t.utbanVan(honnan, bastyaMezo);
    }

    @Override
    public char jel() {
        return szin == Szin.FEHER ? '♔' : '♚';
    }
}
