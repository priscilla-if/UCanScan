package nz.ac.uclive.dsi61.ucanscan.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import nz.ac.uclive.dsi61.ucanscan.Landmark
import nz.ac.uclive.dsi61.ucanscan.R
import nz.ac.uclive.dsi61.ucanscan.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun MapScreen(context: Context,
               navController: NavController) {

    var cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 16f)
    }

    //TODO: replace with RoomDB landmarks
    val sampleLandmarks = mutableListOf<Landmark>(
        Landmark(-43.52256, 172.58119, true),
        Landmark(-43.52257, 172.58268, true),
        Landmark(-43.52456, 172.58351, true),
        Landmark(-43.52698, 172.58451, true),
        Landmark(-43.52435, 172.58079, false)
    )

    for(landmark in sampleLandmarks) {
        // only one landmark at a time isn't found. Set the camera on this landmark.
        if(!landmark.isFound) {
            cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(LatLng(landmark.lat, landmark.lng), 16f)
            }
        }
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        for(landmark in sampleLandmarks) {
            if(landmark.isFound) {
                Marker( // green markers for visited landmarks
                    state = MarkerState(position = LatLng(landmark.lat, landmark.lng)),
                    title = "Jack Erskine",
                    snippet = "This is a marker",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                )
            } else {
                Marker( // red marker for the one upcoming landmark
                    state = MarkerState(position = LatLng(landmark.lat, landmark.lng)),
                    title = "Jack Erskine",
                    snippet = "This is a marker",
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                )
            }
        }

    }

    Row(
        horizontalArrangement = Arrangement.Center
    ) {
        Button(
            onClick = {
                navController.navigate(Screens.MainMenu.route)
            },
            modifier = Modifier.size(width = 200.dp, height = 130.dp)
        ) {
            Text(
                text = stringResource(R.string.back_to_race),
                fontSize = 20.sp
            )
        }
    }


}
