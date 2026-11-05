package com.yonuxamxam.app.presentation.chapters.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yonuxamxam.app.data.model.Chapitre
import com.yonuxamxam.app.ui.theme.YonuBlue
import com.yonuxamxam.app.ui.theme.yonuColors

/**
 * Carte verticale d'un chapitre dans [ChaptersScreen].
 *
 * Spécifications :
 * - Numéro dans un cercle bleu distinctif.
 * - Titre complet du chapitre au format officiel (ex: "Chapitre 1 - Bases").
 * - Badges légers des 4 contenus inclus (Cours écrit, Vidéos, Exercices, Quiz).
 * - Clic ouvrant la ModalBottomSheet des options.
 */
@Composable
fun ChapterCard(
    chapitre: Chapitre,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.yonuColors
    val shape = RoundedCornerShape(16.dp)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = shape,
                spotColor = colors.textPrimary.copy(alpha = 0.05f)
            )
            .clip(shape)
            .background(colors.surface)
            .border(1.dp, colors.border, shape)
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Cercle bleu avec numéro du chapitre
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(YonuBlue),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = chapitre.numero.toString(),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )
        }

        // Colonne centrale : Titre et indicateurs
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = chapitre.titre,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = colors.textPrimary,
                    fontSize = 14.5.sp,
                    lineHeight = 19.sp
                )
            )

            // Puces de contenu pédagogique disponibles
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (chapitre.aCoursEcrit) {
                    ContentBadge(icon = Icons.AutoMirrored.Filled.MenuBook, label = "Cours")
                }
                if (chapitre.aVideos) {
                    ContentBadge(icon = Icons.Default.PlayCircle, label = "Vidéo")
                }
                if (chapitre.aExercices) {
                    ContentBadge(icon = Icons.AutoMirrored.Filled.Assignment, label = "Exos")
                }
                if (chapitre.aQuiz) {
                    ContentBadge(icon = Icons.Default.Quiz, label = "Quiz")
                }
            }
        }

        // Chevron droit
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = "Ouvrir",
            tint = colors.textSecondary.copy(alpha = 0.6f),
            modifier = Modifier.size(14.dp)
        )
    }
}

@Composable
private fun ContentBadge(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.yonuColors
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(colors.surfaceSecondary)
            .border(0.5.dp, colors.border, RoundedCornerShape(6.dp))
            .padding(horizontal = 5.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = colors.yonuBlue,
            modifier = Modifier.size(10.dp)
        )
        Spacer(modifier = Modifier.width(3.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 9.5.sp,
                color = colors.textSecondary,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

