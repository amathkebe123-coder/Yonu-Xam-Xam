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
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.PlayLesson
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yonuxamxam.app.R
import com.yonuxamxam.app.data.model.SearchResultItem
import com.yonuxamxam.app.data.model.SearchResultType
import com.yonuxamxam.app.ui.theme.YonuBlue
import com.yonuxamxam.app.ui.theme.YonuBlueDark
import com.yonuxamxam.app.ui.theme.yonuColors

/**
 * Vue des résultats de recherche regroupés par type : Cours, Leçons, Exercices.
 */
@Composable
fun SearchResultsSection(
    results: List<SearchResultItem>,
    onItemClick: (SearchResultItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.yonuColors

    if (results.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(colors.surfaceSecondary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.SearchOff,
                        contentDescription = null,
                        tint = colors.textSecondary,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Text(
                    text = stringResource(id = R.string.no_search_results),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = colors.textSecondary,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
        return
    }

    val cours = results.filter { it.type == SearchResultType.COURS }
    val lecons = results.filter { it.type == SearchResultType.LECON }
    val exercices = results.filter { it.type == SearchResultType.EXERCICE }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (cours.isNotEmpty()) {
            SearchGroup(
                title = SearchResultType.COURS.label,
                icon = Icons.AutoMirrored.Filled.MenuBook,
                items = cours,
                onItemClick = onItemClick
            )
        }

        if (lecons.isNotEmpty()) {
            SearchGroup(
                title = SearchResultType.LECON.label,
                icon = Icons.Default.PlayLesson,
                items = lecons,
                onItemClick = onItemClick
            )
        }

        if (exercices.isNotEmpty()) {
            SearchGroup(
                title = SearchResultType.EXERCICE.label,
                icon = Icons.AutoMirrored.Filled.Assignment,
                items = exercices,
                onItemClick = onItemClick
            )
        }
    }
}

@Composable
private fun SearchGroup(
    title: String,
    icon: ImageVector,
    items: List<SearchResultItem>,
    onItemClick: (SearchResultItem) -> Unit
) {
    val colors = MaterialTheme.yonuColors

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = YonuBlue,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = colors.textPrimary,
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = "(${items.size})",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = colors.textSecondary
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(colors.surface)
                .border(1.dp, colors.border, RoundedCornerShape(16.dp))
        ) {
            items.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onItemClick(item) }
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.titre,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = colors.textPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.sousTitre,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = colors.textSecondary
                            )
                        )
                    }
                }

                if (index < items.size - 1) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(colors.border.copy(alpha = 0.5f))
                    )
                }
            }
        }
    }
}

