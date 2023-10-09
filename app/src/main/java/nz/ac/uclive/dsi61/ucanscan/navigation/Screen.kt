package nz.ac.uclive.dsi61.ucanscan.navigation
import androidx.annotation.StringRes
import nz.ac.uclive.dsi61.ucanscan.R

sealed class Screens(val route: String, @StringRes val resourceId: Int) {
    object MainMenu: Screens("main_menu", R.string.main_screen)

    object Race: Screens("race", R.string.race_screen)

    object LandmarksFound: Screens("landmarks_found", R.string.landmarks_found_screen)

    object Preferences: Screens("preferences", R.string.preferences)

    object Leaderboard: Screens("leaderboard", R.string.leaderboard_screen)

    object Map: Screens("map", R.string.map_screen)

    object Camera: Screens("camera", R.string.camera_screen)

    object FinishedRace: Screens("finished_race", R.string.finished_race_screen)


}