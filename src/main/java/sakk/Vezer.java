package sakk;

public class Vezer extends Figura {

    public Vezer(Szin szin) {
        super(szin);
    }

    @Override
    public boolean lephet(Mezo honnan, Mezo hova, Tabla t) {
        return csuszik(honnan, hova, t, Irany.MIND);
    }

    @Override
    public char jel() {
        return szin == Szin.FEHER ? '♕' : '♛';
    }
}
