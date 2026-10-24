package sakk;

public class Futo extends Figura {

    public Futo(Szin szin) {
        super(szin);
    }

    @Override
    public boolean lephet(Mezo honnan, Mezo hova, Tabla t) {
        return csuszik(honnan, hova, t, Irany.ATLOS);
    }

    @Override
    public char jel() {
        return szin == Szin.FEHER ? '♗' : '♝';
    }
}
