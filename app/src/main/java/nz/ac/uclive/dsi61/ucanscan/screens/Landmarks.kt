package nz.ac.uclive.dsi61.ucanscan.screens

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import nz.ac.uclive.dsi61.ucanscan.UCanScanApplication
import nz.ac.uclive.dsi61.ucanscan.entities.Landmark
import nz.ac.uclive.dsi61.ucanscan.viewmodel.LandmarkViewModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.LandmarkViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun LandmarksScreen(context: Context,
                    navController: NavController
) {
//    private val viewModel: LandmarkViewModel by activityViewModels() {
//        LandmarkViewModelFactory((activity?.application as UCanScanApplication).repository)
//    }
    val context = LocalContext.current
    val application = context.applicationContext as UCanScanApplication
    val viewModel: LandmarkViewModel = viewModel(factory = LandmarkViewModelFactory(application.repository))
    val landmarks by viewModel.getLandmarks().collectAsState(initial= emptyList<Landmark>())
    println("is this working")
    println(landmarks)

//    Log.d("ASAAAAA", viewModel.getLandmarks().collect())
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // Display the pre-filled landmarks here
//        println(viewModel.getLandmarks())


        Button(
            onClick = {
                // Verify the pre-filled data when the button is clicked
                // You can add your verification logic here
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = viewModel.getLandmarks().toString()
            )
        }
    }

    // Handle back button press
    BackHandler {
        // Handle back button press if needed
    }
}
