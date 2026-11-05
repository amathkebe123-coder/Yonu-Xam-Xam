package com.yonuxamxam.app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yonuxamxam.app.presentation.chapters.ChaptersScreen
import com.yonuxamxam.app.presentation.home.HomeEleveScreen

/**
 * Graphe de navigation principal de l'application Yonu Xam Xam.
 * S'ouvre directement sur le Dashboard Élève [HomeEleveScreen].
 */
@Composable
fun YonuNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.HOME,
        modifier = modifier
    ) {
        // Écran 1 : Dashboard élève
        composable(route = NavRoutes.HOME) {
            HomeEleveScreen(
                onNavigateToChapters = { classe, matiere ->
                    navController.navigate(NavRoutes.chapters(classe, matiere))
                }
            )
        }

        // Écran 2 : Page des chapitres d'une matière
        composable(
            route = NavRoutes.CHAPTERS,
            arguments = listOf(
                navArgument("classe") { type = NavType.StringType },
                navArgument("matiere") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val classe = backStackEntry.arguments?.getString("classe") ?: "TS"
            val matiere = backStackEntry.arguments?.getString("matiere") ?: "Maths"

            ChaptersScreen(
                classe = classe,
                matiere = matiere,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}

