package sakk;

public class Gyalog extends Figura {

    public Gyalog(Szin szin) {
        super(szin);
    }

    /** Előre: fehérnek +1, feketének -1 sor. */
    private int elore() {
        return szin == Szin.FEHER ? 1 : -1;
    }

    private int kezdoSor() {
        return szin == Szin.FEHER ? 1 : 6;
    }

    @Override
    public boolean lephet(Mezo honnan, Mezo hova, Tabla t) {
        int dx = hova.oszlop() - honnan.oszlop();
        int dy = hova.sor() - honnan.sor();
        Figura cel = t.get(hova);

        if (dx == 0 && dy == elore()) {
            return cel == null;
        }
        if (dx == 0 && dy == 2 * elore() && honnan.sor() == kezdoSor()) {
            return cel == null && !t.utbanVan(honnan, hova);
        }
        if (Math.abs(dx) == 1 && dy == elore()) {
            return (cel != null && cel.szin != szin) || enPassant(honnan, hova, t);
        }
        return false;
    }

    /** Az ellenfél gyalogja az előző lépésben épp a hova mezőn ugrott át. */
    private boolean enPassant(Mezo honnan, Mezo hova, Tabla t) {
        Figura atugro = t.get(new Mezo(hova.oszlop(), honnan.sor()));
        return hova.equals(t.enPassantCel()) && atugro != null && atugro.szin != szin;
    }

    @Override
    public char jel() {
        return szin == Szin.FEHER ? '♙' : '♟';
    }
}
