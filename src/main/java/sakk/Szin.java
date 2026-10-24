package sakk;

public enum Szin {
    FEHER, FEKETE;

    public Szin ellenfel() {
        return this == FEHER ? FEKETE : FEHER;
    }
}
