package com.yonuxamxam.app.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yonuxamxam.app.R
import com.yonuxamxam.app.data.model.Chapitre
import com.yonuxamxam.app.ui.theme.YonuBlue
import com.yonuxamxam.app.ui.theme.yonuColors

/**
 * ModalBottomSheet affiché lorsqu'un chapitre est sélectionné.
 *
 * Spécifications :
 * - Titre du chapitre
 * - Quatre choix de contenu : Cours écrit, Vidéos, Exercices, Quiz.
 * - Le clic déclenche le callback [onChoiceClick] (affichant "Bientôt disponible").
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChapterBottomSheet(
    chapitre: Chapitre,
    onDismissRequest: () -> Unit,
    onChoiceClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.yonuColors
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = colors.surface,
        scrimColor = Color.Black.copy(alpha = 0.5f),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // En-tête du modal avec badge de matière et titre du chapitre
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${chapitre.matiereNom} • Chapitre ${chapitre.numero}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = colors.yonuBlue,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = chapitre.titre,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = colors.textPrimary,
                        fontSize = 18.sp
                    )
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 4 Choix : Cours écrit, Vidéos, Exercices, Quiz
            ChapterOptionItem(
                title = stringResource(id = R.string.choice_written_course),
                subtitle = "Fiches de synthèse et démonstrations",
                icon = Icons.AutoMirrored.Filled.MenuBook,
                onClick = { onChoiceClick("Cours écrit") }
            )

            ChapterOptionItem(
                title = stringResource(id = R.string.choice_videos),
                subtitle = "Explications pas-à-pas par vos professeurs",
                icon = Icons.Default.PlayCircle,
                onClick = { onChoiceClick("Vidéos") }
            )

            ChapterOptionItem(
                title = stringResource(id = R.string.choice_exercises),
                subtitle = "Exercices d'entraînement et sujets BAC",
                icon = Icons.AutoMirrored.Filled.Assignment,
                onClick = { onChoiceClick("Exercices") }
            )

            ChapterOptionItem(
                title = stringResource(id = R.string.choice_quiz),
                subtitle = "Testez vos connaissances en 10 questions",
                icon = Icons.Default.Quiz,
                onClick = { onChoiceClick("Quiz") }
            )
        }
    }
}

@Composable
private fun ChapterOptionItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.yonuColors
    val shape = RoundedCornerShape(14.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.surfaceSecondary)
            .border(1.dp, colors.border, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(YonuBlue.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = YonuBlue,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    fontSize = 14.sp
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = colors.textSecondary,
                    fontSize = 11.5.sp
                )
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = colors.textSecondary.copy(alpha = 0.6f),
            modifier = Modifier.size(14.dp)
        )
    }
}

