package sakk;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class MezoTest {

    @Test
    void algebraiJelolesbol() {
        assertEquals(new Mezo(0, 0), Mezo.of("a1"));
        assertEquals(new Mezo(4, 3), Mezo.of("e4"));
        assertEquals(new Mezo(7, 7), Mezo.of("h8"));
    }

    @Test
    void toStringAlgebrai() {
        assertEquals("e4", Mezo.of("e4").toString());
        assertEquals("h8", new Mezo(7, 7).toString());
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"z9", "e", "e44", "", "i1", "a0", "a9"})
    void hibasBemenet(String s) {
        assertThrows(IllegalArgumentException.class, () -> Mezo.of(s));
    }

    @Test
    void tablanKivuliKoordinata() {
        assertThrows(IllegalArgumentException.class, () -> new Mezo(8, 0));
        assertThrows(IllegalArgumentException.class, () -> new Mezo(0, -1));
    }
}
