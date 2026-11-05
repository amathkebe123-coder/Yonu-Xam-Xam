package com.yonuxamxam.app.data.model

/**
 * Modèle de données pour l'élève connecté.
 */
data class Eleve(
    val id: String = "eleve_1",
    val nom: String = "Ndiaye",
    val prenom: String = "Fatou",
    val classe: String = "TS",
    val aDesNotificationsNonLues: Boolean = true
) {
    val initiales: String
        get() = "${prenom.firstOrNull() ?: 'F'}${nom.firstOrNull() ?: 'N'}".uppercase()
}

/**
 * Classes prises en charge par Yonu Xam Xam.
 */
enum class ClasseEnum(val code: String, val label: String) {
    SIXIEME("6e", "Sixième"),
    CINQUIEME("5e", "Cinquième"),
    QUATRIEME("4e", "Quatrième"),
    TROISIEME("3e", "Troisième"),
    SECONDE_S("2nde S", "Seconde Scientifique"),
    SECONDE_L("2nde L", "Seconde Littéraire"),
    PREMIERE_S("1ère S", "Première Scientifique"),
    PREMIERE_L("1ère L", "Première Littéraire"),
    TERMINALE_S("TS", "Terminale S"),
    TERMINALE_L("TL", "Terminale L");

    companion object {
        fun fromCode(code: String): ClasseEnum = entries.find { it.code == code } ?: TERMINALE_S
    }
}

/**
 * Résumé de classe pour les cartes de sélection.
 */
data class ClasseStat(
    val code: String,
    val nbMatieres: Int,
    val nbChapitres: Int
)

/**
 * Matière scolaire.
 */
data class Matiere(
    val id: String,
    val nom: String,
    val codeCourt: String
)

/**
 * Chapitre d'une matière avec ses types de contenus.
 */
data class Chapitre(
    val id: String,
    val numero: Int,
    val titre: String,
    val matiereNom: String,
    val aCoursEcrit: Boolean = true,
    val aVideos: Boolean = true,
    val aExercices: Boolean = true,
    val aQuiz: Boolean = true
)

/**
 * Cours entamé par l'élève dans la section "Continuer".
 */
data class CoursEnCours(
    val id: String,
    val matiere: String,
    val titre: String,
    val progression: Int // En pourcentage (0-100)
)

/**
 * Professeur intervenant sur la plateforme.
 */
data class Professeur(
    val id: String,
    val nom: String,
    val matiere: String,
    val initiales: String
)

/**
 * Suggestion de cours recommandée pour l'élève.
 */
data class SuggestionCours(
    val id: String,
    val titre: String,
    val professeur: String,
    val note: Double,
    val vues: Int
)

/**
 * Épreuve d'entraînement Bac Blanc.
 */
data class BacBlanc(
    val id: String,
    val matiere: String,
    val classe: String,
    val duree: String
)

/**
 * Type d'élément trouvé dans la recherche.
 */
enum class SearchResultType(val label: String) {
    COURS("Cours"),
    LECON("Leçons"),
    EXERCICE("Exercices")
}

/**
 * Résultat de recherche unitaire.
 */
data class SearchResultItem(
    val id: String,
    val titre: String,
    val sousTitre: String,
    val type: SearchResultType
)

