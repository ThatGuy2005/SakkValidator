package sakk;

public class Huszar extends Figura {

    public Huszar(Szin szin) {
        super(szin);
    }

    @Override
    public boolean lephet(Mezo honnan, Mezo hova, Tabla t) {
        int dx = Math.abs(hova.oszlop() - honnan.oszlop());
        int dy = Math.abs(hova.sor() - honnan.sor());
        return dx * dy == 2 && celSzabad(hova, t);
    }

    @Override
    public char jel() {
        return szin == Szin.FEHER ? '♘' : '♞';
    }
}
