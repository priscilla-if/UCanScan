package nz.ac.uclive.dsi61.ucanscan

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import nz.ac.uclive.dsi61.ucanscan.navigation.NavGraph
import nz.ac.uclive.dsi61.ucanscan.ui.theme.UCanScanTheme
import nz.ac.uclive.dsi61.ucanscan.viewmodel.StopwatchViewModel

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UCanScanTheme(content = {
                Scaffold(
                ) {
                    val navController = rememberNavController()
                    val stopwatchViewModel = remember { StopwatchViewModel() }

                    NavGraph(navController = navController, stopwatchViewModel = stopwatchViewModel)
                }
            })
        }
    }
}
