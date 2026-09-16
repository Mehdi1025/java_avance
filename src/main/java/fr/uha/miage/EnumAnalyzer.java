package fr.uha.miage;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Field;

public class EnumAnalyzer {
    
    private final Class<?> enumClass;
    private TypeInfo typeInfo; 
    // La structure pour un attribut
public record FieldInfo(
    String nom, 
    String type, 
    String modificateurs, 
    boolean estConstanteEnum, 
    boolean estSynthetique
) {}

// La liste de tous les attributs trouvés
private List<FieldInfo> fieldsInfo = new ArrayList<>();
    // La structure pour stocker les infos d'une seule constante
public record ConstantInfo(
    String nom, 
    int ordinal, 
    String representation, 
    boolean aCorpsDeClassePropre
) {}

// La liste qui contiendra toutes les constantes analysées
private List<ConstantInfo> constantsInfo = new ArrayList<>();// On va stocker les résultats ici

    // Un "record" (nouveauté récente de Java) est parfait pour stocker des données immuables
    public record TypeInfo(
        String nomSimple, 
        String nomQualifie, 
        String paquetage, 
        String modificateurs, 
        String superClasse,
        String interfaces
    ) {}

    public EnumAnalyzer(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("La classe à analyser ne peut pas être null.");
        }
        if (!clazz.isEnum()) {
            throw new NotAnEnumException("Le type fourni (" + clazz.getName() + ") n'est pas une énumération.");
        }
        this.enumClass = clazz;
    }

    // L'étape 1 du projet : L'analyse du Type
    public void analyzeType() {
        // L'introspection en action !
        String nom = enumClass.getSimpleName();
        String nomComplet = enumClass.getName();
        String paquetage = enumClass.getPackageName();
        
        // Modifier.toString permet de traduire le code interne de Java (ex: 17) en mots ("public final")
        String modifs = Modifier.toString(enumClass.getModifiers());
        
        // Une énumération hérite toujours de java.lang.Enum
        String superCl = enumClass.getSuperclass() != null ? enumClass.getSuperclass().getName() : "Aucune";
        
        // On récupère les interfaces sous forme de tableau, qu'on transforme en chaîne de caractères
        String interfs = Arrays.toString(enumClass.getInterfaces());

        // On sauvegarde tout ça dans notre record
        this.typeInfo = new TypeInfo(nom, nomComplet, paquetage, modifs, superCl, interfs);
    }

    // Pour que nos tests (ou le rapport final) puissent lire les résultats
    public TypeInfo getTypeInfo() {
        return typeInfo;
    }
    public void analyzeConstants() {
    // 1. On demande à Java de nous donner toutes les valeurs de l'énumération
    Object[] constantes = enumClass.getEnumConstants();
    
    if (constantes != null) {
        for (Object obj : constantes) {
            // On force (cast) l'objet en type Enum pour avoir accès à name() et ordinal()
            Enum<?> constanteEnum = (Enum<?>) obj;
            
            String nom = constanteEnum.name();
            int ordinal = constanteEnum.ordinal();
            String representation = constanteEnum.toString();
            
            // 2. L'astuce pro : Si le développeur a créé un comportement spécifique 
            // pour cette constante (ex: redéfinir une méthode juste pour SAMEDI), 
            // Java a secrètement créé une "sous-classe". On le détecte en comparant les classes !
            boolean aCorpsDeClasse = (constanteEnum.getClass() != this.enumClass);
            
            // 3. On sauvegarde tout dans notre liste
            constantsInfo.add(new ConstantInfo(nom, ordinal, representation, aCorpsDeClasse));
        }
    }
}

// Le getter pour pouvoir lire les résultats (utile pour les tests et le rapport)
public List<ConstantInfo> getConstantsInfo() {
    return constantsInfo;
}
public void analyzeFields() {
    // On récupère tous les attributs de la classe
    Field[] champs = enumClass.getDeclaredFields();
    
    for (Field champ : champs) {
        String nom = champ.getName();
        String type = champ.getType().getSimpleName();
        String modifs = Modifier.toString(champ.getModifiers());
        
        // Est-ce une de nos valeurs d'énumération (LUNDI, MARDI...) ?
        boolean estConstante = champ.isEnumConstant();
        
        // Est-ce un attribut technique généré secrètement par le compilateur ?
        boolean estSynth = champ.isSynthetic();
        
        fieldsInfo.add(new FieldInfo(nom, type, modifs, estConstante, estSynth));
    }
}

public List<FieldInfo> getFieldsInfo() {
    return fieldsInfo;
}
}