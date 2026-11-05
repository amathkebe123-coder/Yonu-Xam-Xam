package com.yonuxamxam.app.presentation.navigation

/**
 * Définition centralisée des routes de navigation de Yonu Xam Xam.
 */
object NavRoutes {
    /** Route du Dashboard Élève */
    const val HOME = "home"

    /** Route de la page des chapitres d'une matière avec arguments {classe} et {matiere} */
    const val CHAPTERS = "chapters/{classe}/{matiere}"

    /**
     * Construit l'URL de navigation vers la page Chapitres.
     */
    fun chapters(classe: String, matiere: String): String {
        return "chapters/$classe/$matiere"
    }
}

