package sakk;

public class Kiraly extends Figura {

    public Kiraly(Szin szin) {
        super(szin);
    }

    @Override
    public boolean lephet(Mezo honnan, Mezo hova, Tabla t) {
        int dx = Math.abs(hova.oszlop() - honnan.oszlop());
        int dy = Math.abs(hova.sor() - honnan.sor());
        return Math.max(dx, dy) == 1 && celSzabad(hova, t);
    }

    @Override
    public char jel() {
        return szin == Szin.FEHER ? '♔' : '♚';
    }
}
