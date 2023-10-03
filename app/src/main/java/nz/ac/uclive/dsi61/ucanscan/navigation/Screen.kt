package nz.ac.uclive.dsi61.ucanscan.navigation
import androidx.annotation.StringRes
import nz.ac.uclive.dsi61.ucanscan.R

sealed class Screens(val route: String, @StringRes val resourceId: Int) {
    object MainMenu: Screens("main_menu", R.string.main_screen)

    object Race: Screens("race", R.string.race_screen)

    object Landmarks: Screens("landmarks", R.string.landmarks_screen)

    object Preferences: Screens("preferences", R.string.preferences_screen)

    object Leaderboard: Screens("leaderboard", R.string.leaderboard_screen)

    object Map: Screens("map")

    object Camera: Screens("camera")

}