package fr.uha.miage.enumerations;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JourTest {

    @Test
    void contientLesSeptJoursDeLaSemaine() {
        assertEquals(7, Jour.values().length);
    }

    @Test
    void samediEtDimancheSontLeWeekend() {
        assertTrue(Jour.SAMEDI.isWeekend());
        assertTrue(Jour.DIMANCHE.isWeekend());
    }

    @Test
    void lundiNestPasLeWeekend() {
        assertFalse(Jour.LUNDI.isWeekend());
    }

    @Test
    void retrouveUnJourAPartirDeSonNom() {
        assertSame(Jour.MERCREDI, Jour.valueOf("MERCREDI"));
    }
}
