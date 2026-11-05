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
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.School
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
import com.yonuxamxam.app.data.model.ClasseStat
import com.yonuxamxam.app.ui.theme.YonuDiagonalGradient
import com.yonuxamxam.app.ui.theme.yonuColors

/**
 * Section des cartes de classes (TS et TL par défaut, côte à côte).
 *
 * Spécifications :
 * - Deux cartes côte à côte : TS et TL.
 * - Chaque carte : icône, nom de classe, "X matières • Y chapitres" (calculé dynamiquement).
 * - Sélectionnée : dégradé bleu #3B82F6 vers #1E3A8A avec texte blanc.
 * - Non sélectionnée : surface normale + bordure.
 */
@Composable
fun ClassCardsSection(
    classes: List<ClasseStat>,
    selectedClasseCode: String,
    onClasseSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.yonuColors

    // Extraire les deux classes principales TS et TL demandées
    val tsStat = classes.find { it.code == "TS" } ?: ClasseStat("TS", 8, 32)
    val tlStat = classes.find { it.code == "TL" } ?: ClasseStat("TL", 8, 30)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        ClassCardItem(
            stat = tsStat,
            isSelected = selectedClasseCode == tsStat.code,
            onClick = { onClasseSelected(tsStat.code) },
            modifier = Modifier.weight(1f)
        )

        ClassCardItem(
            stat = tlStat,
            isSelected = selectedClasseCode == tlStat.code,
            onClick = { onClasseSelected(tlStat.code) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun ClassCardItem(
    stat: ClasseStat,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.yonuColors

    val cardShape = RoundedCornerShape(18.dp)
    val backgroundModifier = if (isSelected) {
        Modifier
            .shadow(elevation = 6.dp, shape = cardShape, spotColor = colors.yonuBlue.copy(alpha = 0.4f))
            .background(brush = YonuDiagonalGradient, shape = cardShape)
    } else {
        Modifier
            .background(color = colors.surface, shape = cardShape)
            .border(width = 1.dp, color = colors.border, shape = cardShape)
    }

    Box(
        modifier = modifier
            .clip(cardShape)
            .then(backgroundModifier)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // Icône dans son conteneur circulaire
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) Color.White.copy(alpha = 0.22f)
                        else colors.surfaceSecondary
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Bookmark,
                    contentDescription = null,
                    tint = if (isSelected) Color.White else colors.yonuBlue,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Nom de la classe (ex: TS ou TL)
            Text(
                text = stat.code,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else colors.textPrimary
                )
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Nombre calculé de matières et chapitres
            Text(
                text = "${stat.nbMatieres} matières • ${stat.nbChapitres} chapitres",
                style = MaterialTheme.typography.bodySmall.copy(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (isSelected) Color.White.copy(alpha = 0.85f) else colors.textSecondary
                )
            )
        }
    }
}

