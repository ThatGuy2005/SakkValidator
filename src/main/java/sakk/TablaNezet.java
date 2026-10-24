package sakk;

/** A tábla szöveges megjelenítése a konzolon. */
public class TablaNezet {

    private static final String OSZLOPOK = "  a b c d e f g h";

    public static String rajzol(Tabla t) {
        StringBuilder sb = new StringBuilder(OSZLOPOK).append('\n');
        for (int sor = 7; sor >= 0; sor--) {
            sb.append(sor + 1).append(' ');
            for (int o = 0; o < 8; o++) {
                Figura f = t.get(new Mezo(o, sor));
                sb.append(f == null ? '.' : f.jel()).append(' ');
            }
            sb.append(sor + 1).append('\n');
        }
        return sb.append(OSZLOPOK).toString();
    }
}
