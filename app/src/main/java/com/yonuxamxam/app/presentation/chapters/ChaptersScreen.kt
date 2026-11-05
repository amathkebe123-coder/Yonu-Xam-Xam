package com.yonuxamxam.app.presentation.chapters

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yonuxamxam.app.R
import com.yonuxamxam.app.data.model.Chapitre
import com.yonuxamxam.app.presentation.chapters.components.ChapterCard
import com.yonuxamxam.app.presentation.home.components.ChapterBottomSheet
import com.yonuxamxam.app.ui.theme.YonuBlue
import com.yonuxamxam.app.ui.theme.YonuXamXamTheme
import com.yonuxamxam.app.ui.theme.yonuColors
import kotlinx.coroutines.launch

/**
 * Écran dédié aux chapitres d'une matière [ChaptersScreen].
 *
 * Spécifications (Prompt 1.B) :
 * - En-tête : Bouton retour, nom de la matière et classe (ex: "Maths • TS").
 * - Liste complète des chapitres disponibles sous forme de cartes verticales.
 * - Numéro et titre de chaque chapitre.
 * - État vide : "Aucun chapitre disponible" si la liste est vide.
 * - Pas de barre de navigation inférieure.
 * - Un clic sur un chapitre ouvre la ModalBottomSheet existante (Cours écrit, Vidéos, Exercices, Quiz).
 * - Le retour Android ou le bouton retour revient au dashboard sans perte d'état.
 * - Responsive mobile (360-430 dp) et tablette (colonne centrée max 480 dp).
 */
@Composable
fun ChaptersScreen(
    classe: String,
    matiere: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChaptersViewModel = viewModel()
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

    // Synchronisation classe & matière si nécessaire
    LaunchedEffect(classe, matiere) {
        viewModel.loadChapters(classe, matiere)
    }

    val systemInDark = isSystemInDarkTheme()

    YonuXamXamTheme(darkTheme = systemInDark) {
        val colors = MaterialTheme.yonuColors

        Scaffold(
            modifier = modifier.fillMaxSize(),
            containerColor = colors.background,
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .statusBarsPadding(),
                contentAlignment = Alignment.TopCenter
            ) {
                // Conteneur responsive max 480 dp pour tablette
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .widthIn(max = 480.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    when (val state = uiState) {
                        is ChaptersUiState.Loading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = YonuBlue)
                            }
                        }

                        is ChaptersUiState.Error -> {
                            ChaptersErrorContent(
                                message = state.message,
                                onRetry = { viewModel.loadChapters(classe, matiere) },
                                onBackClick = onBackClick
                            )
                        }

                        is ChaptersUiState.Success -> {
                            ChaptersContent(
                                classe = state.classe,
                                matiere = state.matiere,
                                chapitres = state.chapitres,
                                onBackClick = onBackClick,
                                onChapterClick = { viewModel.onChapterClicked(it) }
                            )

                            // ModalBottomSheet existante identique au dashboard
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
 * Contenu principal de la page Chapitres.
 */
@Composable
private fun ChaptersContent(
    classe: String,
    matiere: String,
    chapitres: List<Chapitre>,
    onBackClick: () -> Unit,
    onChapterClick: (Chapitre) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.yonuColors

    Column(modifier = modifier.fillMaxSize()) {
        // En-tête fixe avec bouton retour et titre
        ChaptersHeader(
            classe = classe,
            matiere = matiere,
            nbChapitres = chapitres.size,
            onBackClick = onBackClick
        )

        if (chapitres.isEmpty()) {
            // État vide
            EmptyChaptersState(modifier = Modifier.weight(1f))
        } else {
            // Liste verticale des cartes de chapitres
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item(key = "header_summary") {
                    Text(
                        text = stringResource(id = R.string.chapters_list_header),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = colors.textSecondary,
                            letterSpacing = 0.5.sp
                        ),
                        modifier = Modifier.padding(bottom = 4.dp, start = 2.dp)
                    )
                }

                items(
                    items = chapitres,
                    key = { it.id }
                ) { chapitre ->
                    ChapterCard(
                        chapitre = chapitre,
                        onClick = { onChapterClick(chapitre) }
                    )
                }

                item(key = "bottom_spacer") {
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}

/**
 * En-tête fixe de la page Chapitres avec bouton retour.
 */
@Composable
private fun ChaptersHeader(
    classe: String,
    matiere: String,
    nbChapitres: Int,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.yonuColors

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.surface)
            .border(
                width = 1.dp,
                color = colors.border
            )
            .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Bouton retour confortable
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(colors.surfaceSecondary)
                .border(1.dp, colors.border, CircleShape)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(id = R.string.back),
                tint = colors.textPrimary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Nom de la matière et classe (ex: "Maths • TS")
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "$matiere • $classe",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    fontSize = 17.sp
                )
            )
            Text(
                text = "$nbChapitres chapitres disponibles",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = colors.textSecondary,
                    fontSize = 12.sp
                )
            )
        }

        // Badge de classe
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(colors.surfaceSecondary)
                .border(1.dp, colors.border, RoundedCornerShape(8.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = classe,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = colors.yonuBlue
                )
            )
        }
    }
}

/**
 * Affichage lorsque aucun chapitre n'est disponible.
 */
@Composable
private fun EmptyChaptersState(modifier: Modifier = Modifier) {
    val colors = MaterialTheme.yonuColors

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(colors.surfaceSecondary),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                    contentDescription = null,
                    tint = colors.textSecondary,
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.no_chapters_available),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Contenu en cas d'erreur avec bouton réessayer et retour.
 */
@Composable
private fun ChaptersErrorContent(
    message: String,
    onRetry: () -> Unit,
    onBackClick: () -> Unit,
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
            text = message,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = colors.textSecondary,
                textAlign = TextAlign.Center
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Button(
                onClick = onBackClick,
                colors = ButtonDefaults.buttonColors(containerColor = colors.surfaceSecondary)
            ) {
                Text(text = stringResource(id = R.string.back), color = colors.textPrimary)
            }
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(containerColor = YonuBlue)
            ) {
                Text(text = stringResource(id = R.string.retry), color = Color.White)
            }
        }
    }
}

@Preview(name = "ChaptersScreen Light", showBackground = true)
@Composable
private fun ChaptersScreenPreview() {
    YonuXamXamTheme(darkTheme = false) {
        ChaptersContent(
            classe = "TS",
            matiere = "Maths",
            chapitres = listOf(
                Chapitre("1", 1, "Chapitre 1 - Bases", "Maths"),
                Chapitre("2", 2, "Chapitre 2 - Approfondissement", "Maths"),
                Chapitre("3", 3, "Chapitre 3 - Exercices types BAC", "Maths"),
                Chapitre("4", 4, "Chapitre 4 - Annales", "Maths")
            ),
            onBackClick = {},
            onChapterClick = {}
        )
    }
}

@Preview(name = "ChaptersScreen Dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ChaptersScreenDarkPreview() {
    YonuXamXamTheme(darkTheme = true) {
        ChaptersContent(
            classe = "TS",
            matiere = "Maths",
            chapitres = listOf(
                Chapitre("1", 1, "Chapitre 1 - Bases", "Maths"),
                Chapitre("2", 2, "Chapitre 2 - Approfondissement", "Maths"),
                Chapitre("3", 3, "Chapitre 3 - Exercices types BAC", "Maths"),
                Chapitre("4", 4, "Chapitre 4 - Annales", "Maths")
            ),
            onBackClick = {},
            onChapterClick = {}
        )
    }
}

