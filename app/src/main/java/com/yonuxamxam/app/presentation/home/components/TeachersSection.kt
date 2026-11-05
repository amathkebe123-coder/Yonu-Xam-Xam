package com.yonuxamxam.app.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yonuxamxam.app.R
import com.yonuxamxam.app.data.model.Professeur
import com.yonuxamxam.app.ui.theme.YonuBlue
import com.yonuxamxam.app.ui.theme.yonuColors

/**
 * Section "Vos professeurs" avec défilement horizontal.
 *
 * Spécifications :
 * - Liste horizontale avec bouton "Voir tout".
 * - Chaque élément : cercle avec initiales, nom et matière.
 * - Exemples : M. Diop • Maths, Mme Sow • PC, M. Fall • Philo, Mme Ba • SVT.
 */
@Composable
fun TeachersSection(
    professeurs: List<Professeur>,
    onTeacherClick: (Professeur) -> Unit,
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.yonuColors

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        // En-tête avec titre et bouton "Voir tout"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.teachers_title),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary
                )
            )

            TextButton(onClick = onSeeAllClick) {
                Text(
                    text = stringResource(id = R.string.see_all),
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = YonuBlue
                    )
                )
            }
        }

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(professeurs, key = { it.id }) { prof ->
                TeacherItem(
                    professeur = prof,
                    onClick = { onTeacherClick(prof) }
                )
            }
        }
    }
}

@Composable
private fun TeacherItem(
    professeur: Professeur,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.yonuColors
    val shape = RoundedCornerShape(14.dp)

    Column(
        modifier = Modifier
            .width(105.dp)
            .clip(shape)
            .background(colors.surface)
            .border(1.dp, colors.border, shape)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Cercle avec initiales
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(colors.surfaceSecondary)
                .border(1.5.dp, colors.yonuBlue.copy(alpha = 0.5f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = professeur.initiales,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = colors.yonuBlueDark,
                    fontSize = 13.sp
                )
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Nom du professeur
        Text(
            text = professeur.nom,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary,
                fontSize = 12.5.sp
            ),
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(2.dp))

        // Matière
        Text(
            text = professeur.matiere,
            style = MaterialTheme.typography.bodySmall.copy(
                color = colors.textSecondary,
                fontSize = 11.sp
            ),
            maxLines = 1
        )
    }
}

