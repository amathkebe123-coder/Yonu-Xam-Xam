package com.yonuxamxam.app.presentation.home

import com.yonuxamxam.app.data.model.BacBlanc
import com.yonuxamxam.app.data.model.Chapitre
import com.yonuxamxam.app.data.model.ClasseStat
import com.yonuxamxam.app.data.model.CoursEnCours
import com.yonuxamxam.app.data.model.Eleve
import com.yonuxamxam.app.data.model.Matiere
import com.yonuxamxam.app.data.model.Professeur
import com.yonuxamxam.app.data.model.SearchResultItem
import com.yonuxamxam.app.data.model.SuggestionCours

/**
 * État global de l'écran Dashboard [HomeEleveScreen].
 */
sealed interface HomeUiState {
    /**
     * État de chargement initial affichant les skeletons/shimmers.
     */
    data object Loading : HomeUiState

    /**
     * État de succès lorsque toutes les données du dashboard sont prêtes.
     */
    data class Success(
        val eleve: Eleve,
        val classes: List<ClasseStat>,
        val classeActive: String = "TS",
        val matieres: List<Matiere>,
        val matiereActive: String = "Maths",
        val chapitres: List<Chapitre>,
        val coursEnCours: List<CoursEnCours>,
        val professeurs: List<Professeur>,
        val suggestionsCours: List<SuggestionCours>,
        val bacBlancs: List<BacBlanc>,
        val searchQuery: String = "",
        val searchResults: List<SearchResultItem> = emptyList(),
        val isSearchActive: Boolean = false,
        val selectedChapterBottomSheet: Chapitre? = null,
        val selectedNavTab: Int = 0
    ) : HomeUiState

    /**
     * État d'erreur avec message descriptif et possibilité de réessayer.
     */
    data class Error(val message: String) : HomeUiState
}

