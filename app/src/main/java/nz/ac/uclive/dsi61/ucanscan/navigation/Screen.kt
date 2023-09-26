package nz.ac.uclive.dsi61.ucanscan.navigation

sealed class Screens(val route: String) {
    object MainMenu: Screens("main_menu")
    object Preferences: Screens("preferences")
    object Race: Screens("race")
    object Map: Screens("map")

}