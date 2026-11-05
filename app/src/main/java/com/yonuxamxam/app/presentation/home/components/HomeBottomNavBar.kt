package com.yonuxamxam.app.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yonuxamxam.app.R
import com.yonuxamxam.app.ui.theme.YonuBlue
import com.yonuxamxam.app.ui.theme.yonuColors

private data class NavItemData(
    val titleRes: Int,
    val icon: ImageVector
)

/**
 * Barre de navigation fixe en bas de l'écran.
 *
 * Spécifications :
 * - Hauteur ~72 dp, coins supérieurs arrondis (24 dp).
 * - Cinq onglets : Accueil, Cours, Exercices, Chat, Profil.
 * - Accueil actif en bleu. Autres onglets déclenchent onTabClick (Snackbar "Bientôt disponible").
 */
@Composable
fun HomeBottomNavBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.yonuColors
    val shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)

    val navItems = listOf(
        NavItemData(R.string.nav_home, Icons.Default.Home),
        NavItemData(R.string.nav_courses, Icons.AutoMirrored.Filled.MenuBook),
        NavItemData(R.string.nav_exercises, Icons.AutoMirrored.Filled.Assignment),
        NavItemData(R.string.nav_chat, Icons.Default.ChatBubbleOutline),
        NavItemData(R.string.nav_profile, Icons.Default.Person)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 16.dp, shape = shape, spotColor = colors.textPrimary.copy(alpha = 0.1f))
            .clip(shape)
            .background(colors.surface)
            .border(1.dp, colors.border, shape)
            .navigationBarsPadding()
            .height(72.dp)
            .padding(horizontal = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEachIndexed { index, item ->
                val isSelected = index == selectedTab
                val itemColor = if (isSelected) YonuBlue else colors.textSecondary

                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onTabSelected(index) }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = stringResource(id = item.titleRes),
                        tint = itemColor,
                        modifier = Modifier.size(24.dp)
                    )

                    Text(
                        text = stringResource(id = item.titleRes),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = itemColor,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }
    }
}

