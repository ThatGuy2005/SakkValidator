package sakk;

public class Bastya extends Figura {

    public Bastya(Szin szin) {
        super(szin);
    }

    @Override
    public boolean lephet(Mezo honnan, Mezo hova, Tabla t) {
        return csuszik(honnan, hova, t, Irany.EGYENES);
    }

    @Override
    public char jel() {
        return szin == Szin.FEHER ? '♖' : '♜';
    }
}
