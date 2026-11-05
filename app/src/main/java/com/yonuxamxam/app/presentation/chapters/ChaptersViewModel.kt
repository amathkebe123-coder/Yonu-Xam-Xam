package com.yonuxamxam.app.presentation.chapters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yonuxamxam.app.data.model.Chapitre
import com.yonuxamxam.app.data.repository.FakeHomeRepository
import com.yonuxamxam.app.domain.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel orchestrant la logique et l'état de l'écran des chapitres [ChaptersScreen].
 */
class ChaptersViewModel(
    savedStateHandle: SavedStateHandle? = null,
    private val repository: HomeRepository = FakeHomeRepository()
) : ViewModel() {

    private val classeArg: String = savedStateHandle?.get<String>("classe") ?: "TS"
    private val matiereArg: String = savedStateHandle?.get<String>("matiere") ?: "Maths"

    private val _uiState = MutableStateFlow<ChaptersUiState>(ChaptersUiState.Loading)
    val uiState: StateFlow<ChaptersUiState> = _uiState.asStateFlow()

    init {
        loadChapters(classeArg, matiereArg)
    }

    /**
     * Charge les chapitres pour la classe et la matière spécifiées.
     */
    fun loadChapters(classe: String = classeArg, matiere: String = matiereArg) {
        viewModelScope.launch {
            _uiState.value = ChaptersUiState.Loading
            try {
                val chapitres = repository.getChapitres(classe, matiere)
                _uiState.value = ChaptersUiState.Success(
                    classe = classe,
                    matiere = matiere,
                    chapitres = chapitres
                )
            } catch (e: Exception) {
                _uiState.value = ChaptersUiState.Error(
                    e.localizedMessage ?: "Erreur lors du chargement des chapitres."
                )
            }
        }
    }

    /**
     * Ouvre la ModalBottomSheet du chapitre sélectionné.
     */
    fun onChapterClicked(chapitre: Chapitre) {
        _uiState.update { state ->
            if (state is ChaptersUiState.Success) {
                state.copy(selectedChapterBottomSheet = chapitre)
            } else state
        }
    }

    /**
     * Ferme la ModalBottomSheet du chapitre.
     */
    fun dismissChapterBottomSheet() {
        _uiState.update { state ->
            if (state is ChaptersUiState.Success) {
                state.copy(selectedChapterBottomSheet = null)
            } else state
        }
    }
}

