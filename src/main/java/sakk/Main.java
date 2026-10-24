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

            String hiba = tabla.hibaOka(honnan, hova, kiLep);
            if (hiba != null) {
                System.out.println("szabálytalan: " + hiba);
                continue;
            }
            char valasztas = 'V';
            if (tabla.atvaltozas(honnan, hova)) {
                System.out.print("Átváltozás (V/B/F/H, alap: V): ");
                String v = in.hasNextLine() ? in.nextLine().trim() : "";
                if (!v.isEmpty()) valasztas = v.charAt(0);
            }
            tabla.vegrehajt(honnan, hova, valasztas);
            System.out.println("szabályos");
            System.out.println(TablaNezet.rajzol(tabla));
            kiLep = kiLep.ellenfel();
        }
    }
}
