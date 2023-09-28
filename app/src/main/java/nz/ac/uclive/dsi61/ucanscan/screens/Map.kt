package nz.ac.uclive.dsi61.ucanscan.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import nz.ac.uclive.dsi61.ucanscan.R
import nz.ac.uclive.dsi61.ucanscan.UCanScanApplication
import nz.ac.uclive.dsi61.ucanscan.navigation.Screens
import nz.ac.uclive.dsi61.ucanscan.viewmodel.LandmarkViewModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.LandmarkViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun MapScreen(context: Context,
               navController: NavController) {

    val application = context.applicationContext as UCanScanApplication
    val viewModel: LandmarkViewModel = viewModel(factory = LandmarkViewModelFactory(application.repository))
    val landmarks by viewModel.getLandmarks().collectAsState(initial= emptyList<nz.ac.uclive.dsi61.ucanscan.entity.Landmark>())


    var cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 16f)
    }

    for(landmark in landmarks) {
        // only one landmark at a time isn't found. Set the camera on this landmark.
        if(!landmark.isFound) {
            cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(LatLng(landmark.latitude, landmark.longitude), 16f)
            }
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        for(landmark in landmarks) {
            if(landmark.isFound) {
                Marker( // green markers for visited landmarks
                    state = MarkerState(position = LatLng(landmark.latitude, landmark.longitude)),
                    title = landmark.name,
                    snippet = landmark.description,
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                )
            } else {
                Marker( // red marker for the one upcoming landmark
                    state = MarkerState(position = LatLng(landmark.latitude, landmark.longitude)),
                    title = landmark.name,
                    snippet = landmark.description,
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                )
            }
        }

    }


    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth() // helps centre the button horizontally
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Button(
                onClick = {
                    navController.navigate(Screens.Race.route)
                },
                modifier = Modifier.size(width = 200.dp, height = 90.dp)
            ) {
                Text(
                    text = stringResource(R.string.back_to_race),
                    fontSize = 20.sp
                )
            }
        }
    }


}
