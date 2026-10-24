package sakk;

/** Egy végrehajtott lépés: honnan, hova, és melyik figura lépett. */
public record Lepes(Mezo honnan, Mezo hova, Figura f) {
}
