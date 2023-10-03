package nz.ac.uclive.dsi61.ucanscan.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import nz.ac.uclive.dsi61.ucanscan.R
import nz.ac.uclive.dsi61.ucanscan.UCanScanApplication
import nz.ac.uclive.dsi61.ucanscan.entity.Landmark
import nz.ac.uclive.dsi61.ucanscan.navigation.Screens
import nz.ac.uclive.dsi61.ucanscan.viewmodel.LandmarkViewModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.LandmarkViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun LandmarksFoundScreen(context: Context,
                         navController: NavController
) {
    // Here is how we can get stuff from our DB to display on a screen - ideally we have
    // different viewModels depending on the logic we are working with. I made the LandmarkViewModel for now
    val application = context.applicationContext as UCanScanApplication
    val viewModel: LandmarkViewModel = viewModel(factory = LandmarkViewModelFactory(application.repository))
    val landmarks by viewModel.getLandmarks().collectAsState(initial= emptyList<Landmark>())


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //TODO: sticky the title
        Text(
            text = stringResource(R.string.landmarks_found),
            fontSize = 24.sp
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )


        //TODO: get images from roomdb
        for (landmark in landmarks) {
            Row(
                modifier = Modifier.padding(top = 16.dp)      // padding between entries
                    .fillMaxWidth()
            ) {
                Column(     // column 1: image
                    modifier = Modifier.padding(end = 16.dp),   // padding on right of image
                ) {
                    Row(
                        //
                    ) {
                        //                Image(bitmap = landmark.image, contentDescription = "landmark photo")
                        Image(
                            painter = painterResource(id = R.drawable.landmark_test_image),
                            contentDescription = "landmark photo",
                            modifier = Modifier.size(128.dp)
                        )
                    }
                }

                Column(     // column 2: text
                    //
                ) {
                    Row(
                        //
                    ) {
                        Text(
//                    modifier = Modifier.padding(top = 16.dp),
                            text = landmark.name,
                            fontSize = 24.sp,            //TODO: replace with MaterialTheme styling
                            fontWeight = FontWeight.Bold // ^
                        )
                    }
                    Row(
                        //
                    ) {
                        Text(
                            text = landmark.description,
                            fontSize = 20.sp,             //TODO: replace with MaterialTheme styling
                            fontWeight = FontWeight.Light // ^
                        )
                    }
                }

            }
        }



        // Add 90dp of padding below the landmarks column
        Spacer(
            modifier = Modifier
                .height(90.dp + 16.dp) // height of button + padding amount
                .fillMaxWidth()
        )



    }




    //TODO: put into function, for map, camera, landscape
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
                    fontSize = 20.sp //TODO: replace with MaterialTheme styling
                )
            }
        }
    }




//    BackHandler { // what to do when phone's back button is clicked
//    }
}