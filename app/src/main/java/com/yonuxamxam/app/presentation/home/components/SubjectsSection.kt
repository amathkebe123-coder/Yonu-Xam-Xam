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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yonuxamxam.app.R
import com.yonuxamxam.app.data.model.Matiere
import com.yonuxamxam.app.ui.theme.YonuBlueDark
import com.yonuxamxam.app.ui.theme.yonuColors

/**
 * Section "Matières" du dashboard [SubjectsSection].
 *
 * Spécifications :
 * - Carte globale avec titre "Matières" et badge indiquant la classe active.
 * - Grille de 2 colonnes et 4 lignes dans l'ordre strict :
 *   1. Maths et PC
 *   2. SVT et Anglais
 *   3. Français et Espagnol
 *   4. Arabe et Philo
 * - Bouton de 52 dp de haut, rayon de 12 dp, nom à gauche et 1ère lettre dans un cercle à droite.
 * - Matière sélectionnée : fond #1E3A8A et texte blanc.
 * - Un clic sur une matière met à jour la sélection et navigue vers la page Chapitres dédiée.
 */
@Composable
fun SubjectsSection(
    matieres: List<Matiere>,
    selectedMatiereNom: String,
    onMatiereSelected: (String) -> Unit,
    classeActive: String,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.yonuColors

    // Ordre imposé par le prompt : 4 paires (2 colonnes x 4 lignes)
    val pairs = listOf(
        Pair("Maths", "PC"),
        Pair("SVT", "Anglais"),
        Pair("Français", "Espagnol"),
        Pair("Arabe", "Philo")
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(colors.surface)
            .border(1.dp, colors.border, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        // En-tête de la carte : Titre "Matières" + Badge de classe
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.matieres_title),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
            )

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
                    text = classeActive,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = colors.yonuBlueDark
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Grille de 2 colonnes et 4 lignes de matières
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            pairs.forEach { (itemLeft, itemRight) ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SubjectButton(
                        name = itemLeft,
                        isSelected = selectedMatiereNom == itemLeft,
                        onClick = { onMatiereSelected(itemLeft) },
                        modifier = Modifier.weight(1f)
                    )
                    SubjectButton(
                        name = itemRight,
                        isSelected = selectedMatiereNom == itemRight,
                        onClick = { onMatiereSelected(itemRight) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

/**
 * Bouton unitaire de matière (52 dp de haut, rayon 12 dp).
 */
@Composable
private fun SubjectButton(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.yonuColors
    val shape = RoundedCornerShape(12.dp)

    val backgroundModifier = if (isSelected) {
        Modifier
            .shadow(4.dp, shape)
            .background(YonuBlueDark, shape)
    } else {
        Modifier
            .background(colors.surfaceSecondary, shape)
            .border(1.dp, colors.border, shape)
    }

    Row(
        modifier = modifier
            .height(52.dp)
            .clip(shape)
            .then(backgroundModifier)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Nom de la matière à gauche
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = if (isSelected) Color.White else colors.textPrimary,
                fontSize = 13.sp
            ),
            maxLines = 1
        )

        // Première lettre dans un cercle à droite
        Box(
            modifier = Modifier
                .size(26.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected) Color.White.copy(alpha = 0.20f)
                    else colors.surface
                )
                .border(
                    width = if (isSelected) 0.dp else 1.dp,
                    color = if (isSelected) Color.Transparent else colors.border,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name.firstOrNull()?.toString() ?: "",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    color = if (isSelected) Color.White else colors.yonuBlue
                )
            )
        }
    }
}

