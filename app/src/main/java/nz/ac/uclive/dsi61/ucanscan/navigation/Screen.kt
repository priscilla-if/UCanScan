package nz.ac.uclive.dsi61.ucanscan.navigation

sealed class Screens(val route: String) {
    object MainMenu: Screens("main_menu")

    object Race: Screens("race")


    object Camera: Screens("camera")

    object Preferences: Screens("preferences")



}