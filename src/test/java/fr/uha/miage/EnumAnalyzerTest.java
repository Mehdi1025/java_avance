package fr.uha.miage;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EnumAnalyzerTest {

    // Notre fausse énumération pour les tests
    enum FaussePizza { PETITE, GRANDE }

    @Test
    void testAvecVraieEnumeration() {
        assertDoesNotThrow(() -> {
            new EnumAnalyzer(FaussePizza.class);
        });
    }

    @Test
    void testAvecClasseNormale() {
        assertThrows(NotAnEnumException.class, () -> {
            new EnumAnalyzer(String.class);
        });
    }

    @Test
    void testAnalyzeType() {
        EnumAnalyzer analyzer = new EnumAnalyzer(FaussePizza.class);
        analyzer.analyzeType();
        EnumAnalyzer.TypeInfo info = analyzer.getTypeInfo();
        
        assertNotNull(info, "Le TypeInfo ne devrait pas être null après l'analyse");
        
        assertEquals("FaussePizza", info.nomSimple());
        assertEquals("fr.uha.miage", info.paquetage());
        assertEquals("java.lang.Enum", info.superClasse());
        assertEquals("fr.uha.miage.EnumAnalyzerTest$FaussePizza", info.nomQualifie());
    }
}