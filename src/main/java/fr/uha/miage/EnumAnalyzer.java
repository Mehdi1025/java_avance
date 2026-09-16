package fr.uha.miage;

public class EnumAnalyzer {
    
    private final Class<?> enumClass; // On stocke la cible à analyser

    // Le constructeur est notre "videur"
    public EnumAnalyzer(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("La classe à analyser ne peut pas être null.");
        }
        
        // Introspection : On demande à Java si la classe est bien une énumération
        if (!clazz.isEnum()) {
            throw new NotAnEnumException("Le type fourni (" + clazz.getName() + ") n'est pas une énumération.");
        }
        
        this.enumClass = clazz;
    }

    public Class<?> getEnumClass() {
        return enumClass;
    }
}git add .