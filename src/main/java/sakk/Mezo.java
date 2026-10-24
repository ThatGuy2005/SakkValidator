package sakk;

import java.util.List;
import java.util.stream.IntStream;

/** Egy mező a táblán: oszlop 0..7 (a..h), sor 0..7 (1..8). */
public record Mezo(int oszlop, int sor) {

    /** A tábla mind a 64 mezője. */
    public static final List<Mezo> OSSZES = IntStream.range(0, 64)
            .mapToObj(i -> new Mezo(i % 8, i / 8))
            .toList();

    public Mezo {
        if (oszlop < 0 || oszlop > 7 || sor < 0 || sor > 7) {
            throw new IllegalArgumentException("Érvénytelen mező: " + oszlop + "," + sor);
        }
    }

    /** Algebrai jelölésből, pl. "e4". */
    public static Mezo of(String s) {
        if (s == null || s.length() != 2) {
            throw new IllegalArgumentException("Érvénytelen mező: " + s);
        }
        char o = Character.toLowerCase(s.charAt(0));
        char r = s.charAt(1);
        if (o < 'a' || o > 'h' || r < '1' || r > '8') {
            throw new IllegalArgumentException("Érvénytelen mező: " + s);
        }
        return new Mezo(o - 'a', r - '1');
    }

    @Override
    public String toString() {
        return "" + (char) ('a' + oszlop) + (char) ('1' + sor);
    }
}
