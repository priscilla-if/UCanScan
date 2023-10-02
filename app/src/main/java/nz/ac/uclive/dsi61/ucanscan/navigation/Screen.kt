package nz.ac.uclive.dsi61.ucanscan.navigation

import androidx.annotation.StringRes
import nz.ac.uclive.dsi61.ucanscan.R

sealed class Screens(val route: String, @StringRes val resourceId: Int) {
    object MainMenu: Screens("main_menu", R.string.main)

    object Race: Screens("race")

    object Landmarks: Screens("landmarks")

    object Preferences: Screens("preferences")



}