package sakk;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

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

    /** Figura a FEN-szerű betűjeléből: nagybetű = fehér, kisbetű = fekete. */
    private static Figura figura(char betu) {
        Szin szin = Character.isUpperCase(betu) ? FEHER : FEKETE;
        return switch (Character.toUpperCase(betu)) {
            case 'K' -> new Kiraly(szin);
            case 'V' -> new Vezer(szin);
            case 'B' -> new Bastya(szin);
            case 'F' -> new Futo(szin);
            case 'H' -> new Huszar(szin);
            case 'G' -> new Gyalog(szin);
            default -> throw new IllegalArgumentException("ismeretlen figura: " + betu);
        };
    }

    /** Minden figura egyedül az üres táblán: legalább egy szabályos és egy szabálytalan lépés. */
    @ParameterizedTest(name = "{0} {1}-{2} -> {3}")
    @CsvSource({
            "B, d4, d8, true",   "B, d4, a4, true",   "B, d4, e5, false",  "B, d4, d4, false",
            "F, c1, h6, true",   "F, c1, a3, true",   "F, c1, c3, false",
            "V, d4, h8, true",   "V, d4, d1, true",   "V, d4, e6, false",
            "H, d4, e6, true",   "H, d4, b3, true",   "H, d4, d6, false",  "H, d4, f6, false",
            "K, e4, f5, true",   "K, e4, e3, true",   "K, e4, e6, false",
            "G, e2, e3, true",   "G, e2, e4, true",   "G, e2, e5, false",  "G, e2, d3, false",
            "G, e3, e5, false",  "G, e2, e1, false",
            "g, e7, e5, true",   "g, e7, e6, true",   "g, e7, e8, false",
    })
    void uresTablan(char betu, String honnan, String hova, boolean varhato) {
        tesz(honnan, figura(betu));
        assertEquals(varhato, lephet(honnan, hova));
    }

    @Test
    void bastyaUtjaElzarva() {
        tesz("d4", new Bastya(FEHER));
        tesz("d6", new Gyalog(FEKETE));
        assertTrue(lephet("d4", "d6"));
        assertFalse(lephet("d4", "d7"));
    }

    @Test
    void futoUtjaElzarva() {
        tesz("c1", new Futo(FEHER));
        tesz("e3", new Gyalog(FEKETE));
        assertFalse(lephet("c1", "g5"));
    }

    @Test
    void sajatFiguratNemUthet() {
        tesz("d4", new Bastya(FEHER));
        tesz("d6", new Gyalog(FEHER));
        assertFalse(lephet("d4", "d6"));
    }

    @Test
    void huszarLeugorjaAzAkadalyokat() {
        t = Tabla.kezdoAllas();
        assertTrue(lephet("b1", "c3"));
        assertTrue(lephet("b1", "a3"));
        assertFalse(lephet("b1", "d2"));
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
