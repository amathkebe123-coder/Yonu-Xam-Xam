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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yonuxamxam.app.R
import com.yonuxamxam.app.data.model.SuggestionCours
import com.yonuxamxam.app.ui.theme.yonuColors

/**
 * Section "Suggestions de cours" avec cartes verticales.
 *
 * Spécifications :
 * - Liste de cartes avec titre, professeur, note (étoile) et nombre de vues.
 */
@Composable
fun SuggestedCoursesSection(
    suggestions: List<SuggestionCours>,
    onSuggestionClick: (SuggestionCours) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.yonuColors

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = stringResource(id = R.string.suggested_courses_title),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = colors.textPrimary
            )
        )

        suggestions.forEach { item ->
            SuggestionCard(
                suggestion = item,
                onClick = { onSuggestionClick(item) }
            )
        }
    }
}

@Composable
private fun SuggestionCard(
    suggestion: SuggestionCours,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.yonuColors
    val shape = RoundedCornerShape(16.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(colors.surface)
            .border(1.dp, colors.border, shape)
            .clickable(onClick = onClick)
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = suggestion.titre,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = colors.textPrimary,
                    fontSize = 14.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Enseignant
                Text(
                    text = suggestion.professeur,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = colors.textSecondary,
                        fontSize = 12.sp
                    )
                )

                // Stats : Note + Vues
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Note
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFFBBF24), // Jaune ambre
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            text = suggestion.note.toString(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = colors.textPrimary,
                                fontSize = 11.sp
                            )
                        )
                    }

                    // Vues
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Visibility,
                            contentDescription = null,
                            tint = colors.textSecondary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "${suggestion.vues} vues",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = colors.textSecondary,
                                fontSize = 11.sp
                            )
                        )
                    }
                }
            }
        }
    }
}

