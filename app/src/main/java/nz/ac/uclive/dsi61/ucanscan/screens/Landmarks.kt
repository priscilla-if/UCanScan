package nz.ac.uclive.dsi61.ucanscan.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import nz.ac.uclive.dsi61.ucanscan.UCanScanApplication
import nz.ac.uclive.dsi61.ucanscan.entity.Landmark
import nz.ac.uclive.dsi61.ucanscan.viewmodel.LandmarkViewModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.LandmarkViewModelFactory

// This is a temporary Screen, for showcasing how we can query the DB to display things.
// Could potentially be used in the future for the FoundLandmarksScreen? Otherwise it does not do
// much at the moment
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun LandmarksScreen(context: Context,
                    navController: NavController
) {

    val application = context.applicationContext as UCanScanApplication
    val viewModel: LandmarkViewModel = viewModel(factory = LandmarkViewModelFactory(application.repository))
    val landmarks by viewModel.getLandmarks().collectAsState(initial= emptyList<Landmark>())
    // Here is how we can get stuff from our DB to display on a screen - ideally we have
    // different viewModels depending on the logic we are working with. I made the LandmarkViewModel for now
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Button(
            onClick = {
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = viewModel.getLandmarks().toString()
            )
        }

        for (landmark in landmarks) {
            Text(text = landmark.latitude.toString())
        }

    }

    BackHandler {
    }
}
