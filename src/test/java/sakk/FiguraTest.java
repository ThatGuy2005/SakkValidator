package sakk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static sakk.Szin.FEHER;
import static sakk.Szin.FEKETE;

/** Az egyes figurák lephet()-je (pszeudo-szabályosság). */
class FiguraTest {

    private Tabla t = new Tabla();

    private boolean lephet(String honnan, String hova) {
        Mezo h = Mezo.of(honnan);
        return t.get(h).lephet(h, Mezo.of(hova), t);
    }

    private void tesz(String m, Figura f) {
        t.tesz(Mezo.of(m), f);
    }

    @Test
    void bastyaEgyenesen() {
        tesz("d4", new Bastya(FEHER));
        assertTrue(lephet("d4", "d8"));
        assertTrue(lephet("d4", "a4"));
        assertFalse(lephet("d4", "e5"));
        assertFalse(lephet("d4", "d4"));
    }

    @Test
    void bastyaUtjaElzarva() {
        tesz("d4", new Bastya(FEHER));
        tesz("d6", new Gyalog(FEKETE));
        assertTrue(lephet("d4", "d6"));
        assertFalse(lephet("d4", "d7"));
    }

    @Test
    void sajatFiguratNemUthet() {
        tesz("d4", new Bastya(FEHER));
        tesz("d6", new Gyalog(FEHER));
        assertFalse(lephet("d4", "d6"));
    }

    @Test
    void futoAtlosan() {
        tesz("c1", new Futo(FEHER));
        assertTrue(lephet("c1", "h6"));
        assertFalse(lephet("c1", "c3"));
        tesz("e3", new Gyalog(FEKETE));
        assertFalse(lephet("c1", "g5"));
    }

    @Test
    void vezerMindenIranyba() {
        tesz("d4", new Vezer(FEHER));
        assertTrue(lephet("d4", "d8"));
        assertTrue(lephet("d4", "h8"));
        assertTrue(lephet("d4", "a1"));
        assertTrue(lephet("d4", "h4"));
        assertFalse(lephet("d4", "e6"));
    }

    @Test
    void huszarLeugorjaAzAkadalyokat() {
        t = Tabla.kezdoAllas();
        assertTrue(lephet("b1", "c3"));
        assertTrue(lephet("b1", "a3"));
        assertFalse(lephet("b1", "d2"));
        assertFalse(lephet("b1", "b3"));
    }

    @Test
    void kiralyEgyet() {
        tesz("e4", new Kiraly(FEHER));
        assertTrue(lephet("e4", "e5"));
        assertTrue(lephet("e4", "f5"));
        assertFalse(lephet("e4", "e6"));
    }

    @Test
    void gyalogElore() {
        t = Tabla.kezdoAllas();
        assertTrue(lephet("e2", "e3"));
        assertTrue(lephet("e2", "e4"));
        assertFalse(lephet("e2", "e5"));
        assertTrue(lephet("e7", "e5"));
        assertFalse(lephet("e2", "e1"));
    }

    @Test
    void gyalogKettosCsakKezdosorbol() {
        tesz("e3", new Gyalog(FEHER));
        assertFalse(lephet("e3", "e5"));
    }

    @Test
    void gyalogUtjaElzarva() {
        tesz("e2", new Gyalog(FEHER));
        tesz("e3", new Huszar(FEKETE));
        assertFalse(lephet("e2", "e3"));
        assertFalse(lephet("e2", "e4"));
    }

    @Test
    void gyalogAtlosanCsakUtve() {
        tesz("e4", new Gyalog(FEHER));
        assertFalse(lephet("e4", "d5"));
        tesz("d5", new Gyalog(FEKETE));
        assertTrue(lephet("e4", "d5"));
        tesz("f5", new Gyalog(FEHER));
        assertFalse(lephet("e4", "f5"));
    }

    @Test
    void lephetNemModositSemmit() {
        t = Tabla.kezdoAllas();
        Figura gyalog = t.get(Mezo.of("e2"));
        lephet("e2", "e4");
        assertSame(gyalog, t.get(Mezo.of("e2")));
        assertNull(t.get(Mezo.of("e4")));
        assertFalse(gyalog.mozdult);
    }
}
