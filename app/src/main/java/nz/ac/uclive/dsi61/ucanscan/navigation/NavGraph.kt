package nz.ac.uclive.dsi61.ucanscan.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import nz.ac.uclive.dsi61.ucanscan.screens.CameraScreen
import nz.ac.uclive.dsi61.ucanscan.screens.LandmarksScreen
import nz.ac.uclive.dsi61.ucanscan.screens.LeaderboardScreen
import nz.ac.uclive.dsi61.ucanscan.screens.MainMenuScreen
import nz.ac.uclive.dsi61.ucanscan.screens.MapScreen
import nz.ac.uclive.dsi61.ucanscan.screens.PreferencesScreen
import nz.ac.uclive.dsi61.ucanscan.screens.RaceScreen

// https://dev.to/jbc7ag/jetpack-compose-navigation-tutorial-9en
@Composable
fun NavGraph (navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.MainMenu.route
    ) {
        composable(
            route = Screens.MainMenu.route
        ) { backStackEntry ->
            MainMenuScreen(LocalContext.current, navController)
        }

        composable(
            route = Screens.Preferences.route
        ) {backStackEntry ->
            PreferencesScreen(LocalContext.current, navController)
        }

        composable(
            route = Screens.Race.route
        ) { backStackEntry ->
            RaceScreen(LocalContext.current, navController)
        }

        composable(
            route = Screens.Map.route
        ) { backStackEntry ->
            MapScreen(LocalContext.current, navController)
        }

        composable(
            route = Screens.Camera.route
        ) { backStackEntry ->
            CameraScreen(LocalContext.current, navController)
        }

        composable(
            route = Screens.Leaderboard.route
        ) {backStackEntry ->
            LeaderboardScreen(LocalContext.current, navController)
        }



        composable(
            route = Screens.Landmarks.route
        ) { backStackEntry ->
            LandmarksScreen(LocalContext.current, navController)
        }
    }
}