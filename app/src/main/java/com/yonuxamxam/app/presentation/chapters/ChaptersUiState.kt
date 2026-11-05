package com.yonuxamxam.app.presentation.chapters

import com.yonuxamxam.app.data.model.Chapitre

/**
 * État de l'écran des chapitres d'une matière [ChaptersScreen].
 */
sealed interface ChaptersUiState {
    /** Chargement des chapitres */
    data object Loading : ChaptersUiState

    /** Chapitres chargés avec succès */
    data class Success(
        val classe: String,
        val matiere: String,
        val chapitres: List<Chapitre>,
        val selectedChapterBottomSheet: Chapitre? = null
    ) : ChaptersUiState

    /** Erreur de chargement */
    data class Error(val message: String) : ChaptersUiState
}

