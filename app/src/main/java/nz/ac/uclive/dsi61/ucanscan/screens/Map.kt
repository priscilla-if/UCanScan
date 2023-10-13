package nz.ac.uclive.dsi61.ucanscan.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import nz.ac.uclive.dsi61.ucanscan.R
import nz.ac.uclive.dsi61.ucanscan.UCanScanApplication
import nz.ac.uclive.dsi61.ucanscan.viewmodel.LandmarkViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun MapScreen(context: Context,
               navController: NavController, landmarkViewModel: LandmarkViewModel) {

    val foundLandmarks = landmarkViewModel.foundLandmarks

    var cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 16f)
    }


    // only one landmark at a time isn't found. Set the camera on this landmark.
    if(!landmarkViewModel.currentLandmark?.isFound!!) {
        var landmark = landmarkViewModel.currentLandmark
        cameraPositionState = rememberCameraPositionState {
            if (landmark != null) {
                position = CameraPosition.fromLatLngZoom(LatLng(landmark.latitude, landmark.longitude), 16f)
            }
        }
    }


    /* Apply custom styling to the map.
     * We use the official Google Maps JSON Styling Wizard at https://mapstyle.withgoogle.com/ to
     * set a custom style, to for example remove the pointers for the UC buildings and change the
     * colours of the map to match the green & purple scheme UC uses on their official maps.
     *
     * We get the JSON from this wizard, load it here, then apply it via the properties variable in
     * the GoogleMap component.
     *
     * This code from: https://www.kodeco.com/34720426-maps-compose-library-tutorial-for-android-getting-started?page=2
     */
    val mapProperties by remember {
        mutableStateOf(
            MapProperties(
                mapStyleOptions = MapStyleOptions.loadRawResourceStyle(context, R.raw.custom_map_style)
            )
        )
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = mapProperties
    ) {
        for(landmark in foundLandmarks) {
            if (landmark.isFound) {
                Marker( // green markers for visited landmarks
                    state = MarkerState(position = LatLng(landmark.latitude, landmark.longitude)),
                    title = landmark.name,
                    snippet = landmark.description,
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                )
            }
        }

        var landmark = landmarkViewModel.currentLandmark
        if (landmark != null) {
            Marker( // red marker for the one upcoming landmark
                state = MarkerState(position = LatLng(landmark.latitude, landmark.longitude)),
                title = landmark.name,
                snippet = landmark.description,
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
            )
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

            BackToRaceButton(navController)
        }
    }


}
