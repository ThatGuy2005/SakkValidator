package sakk;

import java.util.Scanner;

/** Konzolos bíró: lépéseket olvas be, és kiírja, szabályosak-e. */
public class Main {

    public static void main(String[] args) {
        Tabla tabla = Tabla.kezdoAllas();
        Szin kiLep = Szin.FEHER;
        Scanner in = new Scanner(System.in);
        System.out.println(TablaNezet.rajzol(tabla));

        while (true) {
            System.out.print(kiLep + " lép (pl. e2 e4, kilépés: q): ");
            if (!in.hasNextLine()) break;
            String sor = in.nextLine().trim();
            if (sor.equalsIgnoreCase("q")) break;

            String[] reszek = sor.split("\\s+");
            if (reszek.length != 2) {
                System.out.println("szabálytalan: hibás bemenet, a formátum pl. e2 e4");
                continue;
            }
            Mezo honnan, hova;
            try {
                honnan = Mezo.of(reszek[0]);
                hova = Mezo.of(reszek[1]);
            } catch (IllegalArgumentException e) {
                System.out.println("szabálytalan: " + e.getMessage());
                continue;
            }

            String hiba = hiba(tabla, honnan, hova, kiLep);
            if (hiba != null) {
                System.out.println("szabálytalan: " + hiba);
                continue;
            }
            tabla.vegrehajt(honnan, hova);
            System.out.println("szabályos");
            System.out.println(TablaNezet.rajzol(tabla));
            kiLep = kiLep.ellenfel();
        }
    }

    /** Ideiglenes ellenőrzés; a 8. lépésben a Tabla.szabalyos() veszi át. */
    private static String hiba(Tabla t, Mezo honnan, Mezo hova, Szin kiLep) {
        Figura f = t.get(honnan);
        if (f == null) return "nincs figura a(z) " + honnan + " mezőn";
        if (f.szin != kiLep) return "az nem a te figurád";
        if (!f.lephet(honnan, hova, t)) return "a figura nem léphet így";
        return null;
    }
}
