package sakk;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static sakk.Szin.FEHER;
import static sakk.Szin.FEKETE;

/** A Tabla szabályai: saját sakk tilalma, sáncolás, en passant, átváltozás. */
class TablaTest {

    private static final String SAKKBA = "a saját király sakkba kerülne";

    private Tabla t = new Tabla();

    private void tesz(String m, Figura f) {
        t.tesz(Mezo.of(m), f);
    }

    private String hiba(String honnan, String hova, Szin kiLep) {
        return t.hibaOka(Mezo.of(honnan), Mezo.of(hova), kiLep);
    }

    private void lep(String honnan, String hova) {
        t.vegrehajt(Mezo.of(honnan), Mezo.of(hova));
    }

    // --- alapellenőrzések ---

    @Test
    void uresMezorolNemLehet() {
        t = Tabla.kezdoAllas();
        assertNotNull(hiba("e4", "e5", FEHER));
    }

    @Test
    void ellenfelFigurajavalNemLehet() {
        t = Tabla.kezdoAllas();
        assertNotNull(hiba("e7", "e5", FEHER));
    }

    // --- saját sakk tilalma ---

    @Test
    void kotottFiguraNemLephet() {
        tesz("e1", new Kiraly(FEHER));
        tesz("e2", new Futo(FEHER));
        tesz("e8", new Bastya(FEKETE));
        assertEquals(SAKKBA, hiba("e2", "d3", FEHER));
    }

    @Test
    void kiralyNemLephetTamadottMezore() {
        tesz("e1", new Kiraly(FEHER));
        tesz("d8", new Bastya(FEKETE));
        assertEquals(SAKKBA, hiba("e1", "d1", FEHER));
        assertNull(hiba("e1", "f1", FEHER));
    }

    @Test
    void sakkotHaritaniKell() {
        tesz("e1", new Kiraly(FEHER));
        tesz("e8", new Bastya(FEKETE));
        tesz("b1", new Huszar(FEHER));
        tesz("g1", new Huszar(FEHER));
        assertTrue(t.sakkbanVan(FEHER));
        assertEquals(SAKKBA, hiba("b1", "c3", FEHER));
        assertNull(hiba("g1", "e2", FEHER));
    }

    // --- sáncolás ---

    private void sancAlap() {
        tesz("e1", new Kiraly(FEHER));
        tesz("a1", new Bastya(FEHER));
        tesz("h1", new Bastya(FEHER));
        tesz("e8", new Kiraly(FEKETE));
    }

    @Test
    void rovidSancolasAthelyeziABastyat() {
        sancAlap();
        assertNull(hiba("e1", "g1", FEHER));
        lep("e1", "g1");
        assertInstanceOf(Kiraly.class, t.get(Mezo.of("g1")));
        assertInstanceOf(Bastya.class, t.get(Mezo.of("f1")));
        assertNull(t.get(Mezo.of("h1")));
    }

    @Test
    void hosszuSancolas() {
        sancAlap();
        lep("e1", "c1");
        assertInstanceOf(Kiraly.class, t.get(Mezo.of("c1")));
        assertInstanceOf(Bastya.class, t.get(Mezo.of("d1")));
        assertNull(t.get(Mezo.of("a1")));
    }

    @Test
    void sancolasTiltvaHaAKiralyMozdult() {
        sancAlap();
        t.get(Mezo.of("e1")).mozdult = true;
        assertNotNull(hiba("e1", "g1", FEHER));
    }

    @Test
    void sancolasTiltvaHaABastyaMozdult() {
        sancAlap();
        t.get(Mezo.of("h1")).mozdult = true;
        assertNotNull(hiba("e1", "g1", FEHER));
    }

    @Test
    void sancolasTiltvaHaUtbanVan() {
        sancAlap();
        tesz("b1", new Huszar(FEHER));
        assertNotNull(hiba("e1", "c1", FEHER));
    }

    @Test
    void sancolasTiltvaSakkban() {
        sancAlap();
        tesz("e5", new Bastya(FEKETE));
        assertEquals("sakkban nem lehet sáncolni", hiba("e1", "g1", FEHER));
    }

    @Test
    void sancolasTiltvaTamadottMezonAt() {
        sancAlap();
        tesz("f5", new Bastya(FEKETE));
        assertEquals("a király támadott mezőn haladna át", hiba("e1", "g1", FEHER));
    }

    @Test
    void sancolasTiltvaTamadottCelmezore() {
        sancAlap();
        tesz("g5", new Bastya(FEKETE));
        assertEquals(SAKKBA, hiba("e1", "g1", FEHER));
    }

    // --- en passant ---

    @Test
    void enPassantSzabalyosEsLeveszi() {
        t = Tabla.kezdoAllas();
        lep("e2", "e4");
        lep("a7", "a6");
        lep("e4", "e5");
        lep("d7", "d5");
        assertNull(hiba("e5", "d6", FEHER));
        lep("e5", "d6");
        assertNull(t.get(Mezo.of("d5")));
        assertInstanceOf(Gyalog.class, t.get(Mezo.of("d6")));
    }

    @Test
    void enPassantCsakAzonnal() {
        t = Tabla.kezdoAllas();
        lep("e2", "e4");
        lep("a7", "a6");
        lep("e4", "e5");
        lep("d7", "d5");
        lep("h2", "h3");
        lep("h7", "h6");
        assertNotNull(hiba("e5", "d6", FEHER));
    }

    @Test
    void enPassantNemFedhetiFelASajatKiralyt() {
        tesz("a5", new Kiraly(FEHER));
        tesz("b5", new Gyalog(FEHER));
        tesz("c7", new Gyalog(FEKETE));
        tesz("h5", new Bastya(FEKETE));
        lep("c7", "c5");
        assertEquals(SAKKBA, hiba("b5", "c6", FEHER));
    }

    // --- átváltozás ---

    @Test
    void atvaltozasAlapbolVezer() {
        tesz("e7", new Gyalog(FEHER));
        assertTrue(t.atvaltozas(Mezo.of("e7"), Mezo.of("e8")));
        lep("e7", "e8");
        Figura uj = t.get(Mezo.of("e8"));
        assertInstanceOf(Vezer.class, uj);
        assertEquals(FEHER, uj.szin);
    }

    @Test
    void atvaltozasValasztottFigurara() {
        tesz("d2", new Gyalog(FEKETE));
        t.vegrehajt(Mezo.of("d2"), Mezo.of("d1"), 'H');
        assertInstanceOf(Huszar.class, t.get(Mezo.of("d1")));
    }

    @Test
    void nemUtolsoSorNemValtozik() {
        tesz("e6", new Gyalog(FEHER));
        assertFalse(t.atvaltozas(Mezo.of("e6"), Mezo.of("e7")));
    }
}
