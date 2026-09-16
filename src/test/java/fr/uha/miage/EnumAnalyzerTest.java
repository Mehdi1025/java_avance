package fr.uha.miage;

import org.junit.jupiter.api.Test;
import java.util.List; // L'import qui manquait pour résoudre l'erreur !
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

    // Une nouvelle énumération pour tester les constantes avec un comportement propre
    enum FausseMonnaie {
        PIECE_NORMALE,
        PIECE_TRUQUEE {
            @Override
            public String toString() {
                return "Je suis truquée !";
            }
        }
    }

    @Test
    void testAnalyzeConstants() {
        // 1. On donne notre nouvelle énumération au videur
        EnumAnalyzer analyzer = new EnumAnalyzer(FausseMonnaie.class);
        
        // 2. On lance l'analyse des constantes
        analyzer.analyzeConstants();
        List<EnumAnalyzer.ConstantInfo> constantes = analyzer.getConstantsInfo();
        
        // 3. On vérifie qu'il a bien trouvé 2 pièces
        assertEquals(2, constantes.size(), "Il devrait y avoir 2 constantes");
        
        // 4. On vérifie la pièce normale
        EnumAnalyzer.ConstantInfo piece1 = constantes.get(0);
        assertEquals("PIECE_NORMALE", piece1.nom());
        assertEquals(0, piece1.ordinal());
        assertEquals("PIECE_NORMALE", piece1.representation()); // Par défaut, toString() renvoie le nom
        assertFalse(piece1.aCorpsDeClassePropre(), "La pièce normale n'a pas de corps propre");
        
        // 5. On vérifie la pièce truquée (qui a son propre corps de classe)
        EnumAnalyzer.ConstantInfo piece2 = constantes.get(1);
        assertEquals("PIECE_TRUQUEE", piece2.nom());
        assertEquals(1, piece2.ordinal());
        assertEquals("Je suis truquée !", piece2.representation()); // toString() a été modifié !
        assertTrue(piece2.aCorpsDeClassePropre(), "La pièce truquée DOIT avoir son propre corps de classe");
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