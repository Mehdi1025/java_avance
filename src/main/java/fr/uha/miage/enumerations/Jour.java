package fr.uha.miage.enumerations;

public enum Jour {
    LUNDI,
    MARDI,
    MERCREDI,
    JEUDI,
    VENDREDI,
    SAMEDI,
    DIMANCHE;

    public boolean isWeekend() {
        return this == SAMEDI || this == DIMANCHE;
    }
}
