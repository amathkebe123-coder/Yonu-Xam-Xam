package com.yonuxamxam.app.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yonuxamxam.app.data.model.Chapitre
import com.yonuxamxam.app.data.repository.FakeHomeRepository
import com.yonuxamxam.app.domain.repository.HomeRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel orchestrant la logique et l'état de l'écran [HomeEleveScreen].
 */
class HomeViewModel(
    private val repository: HomeRepository = FakeHomeRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadDashboardData()
    }

    /**
     * Charge l'ensemble des données du dashboard avec une brève simulation
     * de chargement pour afficher les skeletons shimmer.
     */
    fun loadDashboardData(classeInitiale: String = "TS", matiereInitiale: String = "Maths") {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            try {
                // Simulation réaliste d'un court temps de chargement
                delay(650)

                val eleve = repository.getEleve().copy(classe = classeInitiale)
                val classes = repository.getClasses()
                val matieres = repository.getMatieres(classeInitiale)
                val chapitres = repository.getChapitres(classeInitiale, matiereInitiale)
                val coursEnCours = repository.getCoursEnCours()
                val professeurs = repository.getProfesseurs()
                val suggestions = repository.getSuggestionsCours()
                val bacBlancs = repository.getBacBlancs(classeInitiale)

                _uiState.value = HomeUiState.Success(
                    eleve = eleve,
                    classes = classes,
                    classeActive = classeInitiale,
                    matieres = matieres,
                    matiereActive = matiereInitiale,
                    chapitres = chapitres,
                    coursEnCours = coursEnCours,
                    professeurs = professeurs,
                    suggestionsCours = suggestions,
                    bacBlancs = bacBlancs
                )
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error(
                    e.localizedMessage ?: "Erreur inconnue lors du chargement."
                )
            }
        }
    }

    /**
     * Changement de classe sélectionnée (ex: TS <-> TL ou autre).
     * Met à jour dynamiquement l'en-tête, les matières, les chapitres, le récapitulatif et les bacs blancs.
     */
    fun selectClasse(nouvelleClasse: String) {
        val currentState = _uiState.value as? HomeUiState.Success ?: return
        if (currentState.classeActive == nouvelleClasse) return

        viewModelScope.launch {
            try {
                val nouveauxChapitres = repository.getChapitres(nouvelleClasse, currentState.matiereActive)
                val nouveauxBacBlancs = repository.getBacBlancs(nouvelleClasse)

                _uiState.update { state ->
                    if (state is HomeUiState.Success) {
                        state.copy(
                            classeActive = nouvelleClasse,
                            eleve = state.eleve.copy(classe = nouvelleClasse),
                            chapitres = nouveauxChapitres,
                            bacBlancs = nouveauxBacBlancs
                        )
                    } else state
                }
            } catch (e: Exception) {
                // Maintenir l'état en cas d'erreur de mise à jour
            }
        }
    }

    /**
     * Changement de matière sélectionnée (Maths, PC, SVT, etc.).
     */
    fun selectMatiere(matiereNom: String) {
        val currentState = _uiState.value as? HomeUiState.Success ?: return
        if (currentState.matiereActive == matiereNom) return

        viewModelScope.launch {
            try {
                val nouveauxChapitres = repository.getChapitres(currentState.classeActive, matiereNom)

                _uiState.update { state ->
                    if (state is HomeUiState.Success) {
                        state.copy(
                            matiereActive = matiereNom,
                            chapitres = nouveauxChapitres
                        )
                    } else state
                }
            } catch (e: Exception) {
                // Ignorer
            }
        }
    }

    /**
     * Gestion de la recherche avec debounce de 300 ms.
     */
    fun onSearchQueryChanged(query: String) {
        val currentState = _uiState.value as? HomeUiState.Success ?: return

        _uiState.update { state ->
            if (state is HomeUiState.Success) {
                state.copy(
                    searchQuery = query,
                    isSearchActive = query.isNotBlank()
                )
            } else state
        }

        searchJob?.cancel()

        if (query.isBlank()) {
            _uiState.update { state ->
                if (state is HomeUiState.Success) {
                    state.copy(searchResults = emptyList(), isSearchActive = false)
                } else state
            }
            return
        }

        searchJob = viewModelScope.launch {
            delay(300) // Délai de 300 ms exigé
            val results = repository.search(query, currentState.classeActive)
            _uiState.update { state ->
                if (state is HomeUiState.Success) {
                    state.copy(searchResults = results, isSearchActive = true)
                } else state
            }
        }
    }

    /**
     * Efface la saisie de recherche et masque les résultats temporaires.
     */
    fun clearSearch() {
        searchJob?.cancel()
        _uiState.update { state ->
            if (state is HomeUiState.Success) {
                state.copy(
                    searchQuery = "",
                    searchResults = emptyList(),
                    isSearchActive = false
                )
            } else state
        }
    }

    /**
     * Ouvre le ModalBottomSheet d'un chapitre sélectionné.
     */
    fun onChapterClicked(chapitre: Chapitre) {
        _uiState.update { state ->
            if (state is HomeUiState.Success) {
                state.copy(selectedChapterBottomSheet = chapitre)
            } else state
        }
    }

    /**
     * Ferme le ModalBottomSheet du chapitre.
     */
    fun dismissChapterBottomSheet() {
        _uiState.update { state ->
            if (state is HomeUiState.Success) {
                state.copy(selectedChapterBottomSheet = null)
            } else state
        }
    }

    /**
     * Sélection d'un onglet de navigation basse (0 = Accueil).
     */
    fun selectNavTab(index: Int) {
        _uiState.update { state ->
            if (state is HomeUiState.Success) {
                state.copy(selectedNavTab = index)
            } else state
        }
    }
}

