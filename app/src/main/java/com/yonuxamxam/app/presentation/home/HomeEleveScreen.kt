package com.yonuxamxam.app.presentation.home

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yonuxamxam.app.R
import com.yonuxamxam.app.data.model.BacBlanc
import com.yonuxamxam.app.data.model.Chapitre
import com.yonuxamxam.app.data.model.ClasseStat
import com.yonuxamxam.app.data.model.CoursEnCours
import com.yonuxamxam.app.data.model.Eleve
import com.yonuxamxam.app.data.model.Matiere
import com.yonuxamxam.app.data.model.Professeur
import com.yonuxamxam.app.data.model.SuggestionCours
import com.yonuxamxam.app.presentation.home.components.BacBlancSection
import com.yonuxamxam.app.presentation.home.components.ChapterBottomSheet
import com.yonuxamxam.app.presentation.home.components.ClassCardsSection
import com.yonuxamxam.app.presentation.home.components.ContinueSection
import com.yonuxamxam.app.presentation.home.components.HeaderSection
import com.yonuxamxam.app.presentation.home.components.HomeBottomNavBar
import com.yonuxamxam.app.presentation.home.components.HomeSkeletonLoading
import com.yonuxamxam.app.presentation.home.components.SearchBarSection
import com.yonuxamxam.app.presentation.home.components.SearchResultsSection
import com.yonuxamxam.app.presentation.home.components.SubjectsSection
import com.yonuxamxam.app.presentation.home.components.SuggestedCoursesSection
import com.yonuxamxam.app.presentation.home.components.TeachersSection
import com.yonuxamxam.app.ui.theme.YonuBlue
import com.yonuxamxam.app.ui.theme.YonuXamXamTheme
import com.yonuxamxam.app.ui.theme.yonuColors
import kotlinx.coroutines.launch

/**
 * Écran principal du Dashboard Élève [HomeEleveScreen].
 *
 * Conforme à toutes les spécifications du Prompt 1.A :
 * - MVVM avec [HomeViewModel] et [StateFlow].
 * - En-tête et navigation fixes en bas, seul le contenu central défile.
 * - Gestion du thème clair et sombre lisible avec contraste adapté.
 * - Recherche réactive avec debounce 300 ms.
 * - ModalBottomSheet sur clic chapitre avec options (Cours, Vidéos, Exercices, Quiz).
 * - Snackbar "Bientôt disponible" pour toute action non encore implémentée.
 * - Responsive mobile (360 - 430 dp) et tablette (centré à max 480 dp).
 */
@Composable
fun HomeEleveScreen(
    onNavigateToChapters: (classe: String, matiere: String) -> Unit = { _, _ -> },
    viewModel: HomeViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val soonAvailableText = stringResource(id = R.string.soon_available)

    val showSnackbar: (String) -> Unit = { message ->
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
            snackbarHostState.showSnackbar(message)
        }
    }

    // Le thème suit automatiquement le mode clair ou sombre du système
    val isDark = isSystemInDarkTheme()

    YonuXamXamTheme(darkTheme = isDark) {
        val colors = MaterialTheme.yonuColors

        Scaffold(
            modifier = modifier.fillMaxSize(),
            containerColor = colors.background,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            bottomBar = {
                if (uiState is HomeUiState.Success) {
                    val state = uiState as HomeUiState.Success
                    HomeBottomNavBar(
                        selectedTab = state.selectedNavTab,
                        onTabSelected = { index ->
                            if (index == 0) {
                                viewModel.selectNavTab(0)
                            } else {
                                showSnackbar(soonAvailableText)
                            }
                        }
                    )
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = paddingValues.calculateBottomPadding())
                    .statusBarsPadding(),
                contentAlignment = Alignment.TopCenter
            ) {
                // Conteneur centré pour garantir une largeur harmonieuse sur tablette (max 480 dp)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .widthIn(max = 480.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    when (val state = uiState) {
                        is HomeUiState.Loading -> {
                            HomeSkeletonLoading(modifier = Modifier.fillMaxSize())
                        }

                        is HomeUiState.Error -> {
                            HomeErrorContent(
                                message = state.message,
                                onRetry = { viewModel.loadDashboardData() },
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        is HomeUiState.Success -> {
                            HomeSuccessContent(
                                state = state,
                                onQueryChange = { viewModel.onSearchQueryChanged(it) },
                                onClearQuery = { viewModel.clearSearch() },
                                onClasseSelected = { viewModel.selectClasse(it) },
                                onMatiereSelected = { matiere ->
                                    viewModel.selectMatiere(matiere)
                                    onNavigateToChapters(state.classeActive, matiere)
                                },
                                onChapterClick = { viewModel.onChapterClicked(it) },
                                onCourseClick = { showSnackbar(soonAvailableText) },
                                onTeacherClick = { showSnackbar(soonAvailableText) },
                                onSeeAllTeachers = { showSnackbar(soonAvailableText) },
                                onSuggestionClick = { showSnackbar(soonAvailableText) },
                                onStartBacBlanc = { showSnackbar(soonAvailableText) },
                                onNotificationClick = { showSnackbar("Aucune nouvelle notification.") },
                                onProfileClick = { showSnackbar(soonAvailableText) },
                                onSearchResultClick = { showSnackbar(soonAvailableText) },
                                modifier = Modifier.fillMaxSize()
                            )

                            // ModalBottomSheet pour le chapitre sélectionné
                            state.selectedChapterBottomSheet?.let { chapitre ->
                                ChapterBottomSheet(
                                    chapitre = chapitre,
                                    onDismissRequest = { viewModel.dismissChapterBottomSheet() },
                                    onChoiceClick = {
                                        viewModel.dismissChapterBottomSheet()
                                        showSnackbar(soonAvailableText)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * Contenu principal déroulant lorsque les données sont chargées avec succès.
 */
@Composable
private fun HomeSuccessContent(
    state: HomeUiState.Success,
    onQueryChange: (String) -> Unit,
    onClearQuery: () -> Unit,
    onClasseSelected: (String) -> Unit,
    onMatiereSelected: (String) -> Unit,
    onChapterClick: (Chapitre) -> Unit,
    onCourseClick: (CoursEnCours) -> Unit,
    onTeacherClick: (Professeur) -> Unit,
    onSeeAllTeachers: () -> Unit,
    onSuggestionClick: (SuggestionCours) -> Unit,
    onStartBacBlanc: (BacBlanc) -> Unit,
    onNotificationClick: () -> Unit,
    onProfileClick: () -> Unit,
    onSearchResultClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    Column(modifier = modifier) {
        // En-tête fixe en haut
        HeaderSection(
            eleve = state.eleve,
            classeActive = state.classeActive,
            onNotificationClick = onNotificationClick,
            onProfileClick = onProfileClick
        )

        // Barre de recherche fixe sous l'en-tête
        SearchBarSection(
            query = state.searchQuery,
            onQueryChange = onQueryChange,
            onClearQuery = onClearQuery
        )

        // Contenu central scrollable
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (state.isSearchActive) {
                // Affichage temporaire des résultats de recherche
                item(key = "search_results") {
                    SearchResultsSection(
                        results = state.searchResults,
                        onItemClick = { onSearchResultClick() }
                    )
                }
            } else {
                // 1. Cartes de classes (TS et TL)
                item(key = "class_cards") {
                    ClassCardsSection(
                        classes = state.classes,
                        selectedClasseCode = state.classeActive,
                        onClasseSelected = onClasseSelected
                    )
                }

                // 2. Matières
                item(key = "subjects_section") {
                    SubjectsSection(
                        matieres = state.matieres,
                        selectedMatiereNom = state.matiereActive,
                        onMatiereSelected = onMatiereSelected,
                        classeActive = state.classeActive
                    )
                }

                // 3. Section Continuer
                item(key = "continue_section") {
                    ContinueSection(
                        coursList = state.coursEnCours,
                        onCourseClick = onCourseClick
                    )
                }

                // 4. Section Vos professeurs
                item(key = "teachers_section") {
                    TeachersSection(
                        professeurs = state.professeurs,
                        onTeacherClick = onTeacherClick,
                        onSeeAllClick = onSeeAllTeachers
                    )
                }

                // 5. Section Suggestions de cours
                item(key = "suggestions_section") {
                    SuggestedCoursesSection(
                        suggestions = state.suggestionsCours,
                        onSuggestionClick = onSuggestionClick
                    )
                }

                // 6. Section Bac Blancs
                item(key = "bac_blancs_section") {
                    BacBlancSection(
                        bacBlancs = state.bacBlancs,
                        onStartClick = onStartBacBlanc
                    )
                }

                // Espace tampon pour respirer au-dessus de la barre de navigation
                item(key = "bottom_spacer") {
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}

/**
 * Vue d'erreur avec bouton Réessayer.
 */
@Composable
private fun HomeErrorContent(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.yonuColors

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.error_loading_data),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary,
                textAlign = TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = colors.textSecondary,
                textAlign = TextAlign.Center
            )
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = YonuBlue
            )
        ) {
            Text(
                text = stringResource(id = R.string.retry),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ====================================================================
// APERÇUS COMPOSE (PREVIEWS) EN THÈME CLAIR ET THÈME SOMBRE
// ====================================================================

@Preview(
    name = "Dashboard Élève - Thème Clair",
    showBackground = true,
    widthDp = 390,
    heightDp = 800,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun HomeEleveScreenLightPreview() {
    YonuXamXamTheme(darkTheme = false) {
        val sampleState = getSamplePreviewState()
        HomeSuccessContent(
            state = sampleState,
            onQueryChange = {},
            onClearQuery = {},
            onClasseSelected = {},
            onMatiereSelected = {},
            onChapterClick = {},
            onCourseClick = {},
            onTeacherClick = {},
            onSeeAllTeachers = {},
            onSuggestionClick = {},
            onStartBacBlanc = {},
            onNotificationClick = {},
            onProfileClick = {},
            onSearchResultClick = {}
        )
    }
}

@Preview(
    name = "Dashboard Élève - Thème Sombre",
    showBackground = true,
    widthDp = 390,
    heightDp = 800,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun HomeEleveScreenDarkPreview() {
    YonuXamXamTheme(darkTheme = true) {
        val sampleState = getSamplePreviewState()
        HomeSuccessContent(
            state = sampleState,
            onQueryChange = {},
            onClearQuery = {},
            onClasseSelected = {},
            onMatiereSelected = {},
            onChapterClick = {},
            onCourseClick = {},
            onTeacherClick = {},
            onSeeAllTeachers = {},
            onSuggestionClick = {},
            onStartBacBlanc = {},
            onNotificationClick = {},
            onProfileClick = {},
            onSearchResultClick = {}
        )
    }
}

private fun getSamplePreviewState(): HomeUiState.Success {
    return HomeUiState.Success(
        eleve = Eleve(nom = "Ndiaye", prenom = "Fatou", classe = "TS"),
        classes = listOf(
            ClasseStat("TS", 8, 32),
            ClasseStat("TL", 8, 30)
        ),
        classeActive = "TS",
        matieres = listOf(
            Matiere("m1", "Maths", "M"),
            Matiere("m2", "PC", "P"),
            Matiere("m3", "SVT", "S"),
            Matiere("m4", "Anglais", "A"),
            Matiere("m5", "Français", "F"),
            Matiere("m6", "Espagnol", "E"),
            Matiere("m7", "Arabe", "A"),
            Matiere("m8", "Philo", "P")
        ),
        matiereActive = "Maths",
        chapitres = listOf(
            Chapitre("c1", 1, "Chapitre 1 - Bases", "Maths"),
            Chapitre("c2", 2, "Chapitre 2 - Approfondissement", "Maths"),
            Chapitre("c3", 3, "Chapitre 3 - Exercices types BAC", "Maths"),
            Chapitre("c4", 4, "Chapitre 4 - Annales", "Maths")
        ),
        coursEnCours = listOf(
            CoursEnCours("p1", "Maths", "Suites numériques", 65),
            CoursEnCours("p2", "PC", "Mécanique", 40)
        ),
        professeurs = listOf(
            Professeur("pr1", "M. Diop", "Maths", "MD"),
            Professeur("pr2", "Mme Sow", "PC", "MS"),
            Professeur("pr3", "M. Fall", "Philo", "MF"),
            Professeur("pr4", "Mme Ba", "SVT", "MB")
        ),
        suggestionsCours = listOf(
            SuggestionCours("s1", "Ondes mécaniques", "Mme Sow", 4.9, 1420),
            SuggestionCours("s2", "Limites & Dérivées", "M. Diop", 4.8, 2100)
        ),
        bacBlancs = listOf(
            BacBlanc("b1", "Maths", "TS", "4h00"),
            BacBlanc("b2", "PC", "TS", "3h30")
        )
    )
}

