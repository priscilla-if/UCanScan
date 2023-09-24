package nz.ac.uclive.dsi61.ucanscan.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import nz.ac.uclive.dsi61.ucanscan.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun RaceScreen(context: Context,
               navController: NavController) {
    // A surface container using the 'background' color from the theme
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        //TODO: replace the example code
        Greeting("Race")

        // ROUGH BUTTON TO DEMONSTRATE NAVIGATION: REPLACE ME
        Button(
            onClick = {
                navController.navigate(Screens.MainMenu.route)
            }
        ) {
            // empty content
        }
    }
}
