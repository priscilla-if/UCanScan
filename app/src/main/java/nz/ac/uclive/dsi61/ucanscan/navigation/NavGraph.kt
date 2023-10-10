package nz.ac.uclive.dsi61.ucanscan.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import nz.ac.uclive.dsi61.ucanscan.screens.CameraScreen
import nz.ac.uclive.dsi61.ucanscan.screens.FinishedRaceScreen
import nz.ac.uclive.dsi61.ucanscan.screens.LandmarksScreen
import nz.ac.uclive.dsi61.ucanscan.screens.LeaderboardScreen
import nz.ac.uclive.dsi61.ucanscan.screens.LandmarksFoundScreen
import nz.ac.uclive.dsi61.ucanscan.screens.MainMenuScreen
import nz.ac.uclive.dsi61.ucanscan.screens.MapScreen
import nz.ac.uclive.dsi61.ucanscan.screens.PreferencesScreen
import nz.ac.uclive.dsi61.ucanscan.screens.RaceScreen
import nz.ac.uclive.dsi61.ucanscan.viewmodel.IsRaceStartedModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.PreferencesViewModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.StopwatchViewModel

// https://dev.to/jbc7ag/jetpack-compose-navigation-tutorial-9en
@Composable
fun NavGraph (navController: NavHostController,  stopwatchViewModel: StopwatchViewModel, isRaceStartedModel : IsRaceStartedModel, preferencesViewModel: PreferencesViewModel) {

    NavHost(
        navController = navController,
        startDestination = Screens.MainMenu.route
    ) {
        composable(
            route = Screens.MainMenu.route
        ) { backStackEntry ->
            MainMenuScreen(LocalContext.current, navController, stopwatchViewModel, isRaceStartedModel)
        }

        composable(
            route = Screens.Preferences.route
        ) {backStackEntry ->
            PreferencesScreen(LocalContext.current, navController, stopwatchViewModel, isRaceStartedModel, preferencesViewModel)
        }

        composable(
            route = Screens.Race.route
        ) { backStackEntry ->
            RaceScreen(LocalContext.current, navController, stopwatchViewModel, isRaceStartedModel)
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
            LeaderboardScreen(LocalContext.current, navController, stopwatchViewModel, isRaceStartedModel)
        }

        composable(
            route = Screens.LandmarksFound.route
        ) { backStackEntry ->
            LandmarksFoundScreen(LocalContext.current, navController, stopwatchViewModel, isRaceStartedModel)
        }

        composable(
            route = Screens.FinishedRace.route
        ) {backStackEntry ->
            FinishedRaceScreen(LocalContext.current, navController, stopwatchViewModel, isRaceStartedModel)
        }
    }
}