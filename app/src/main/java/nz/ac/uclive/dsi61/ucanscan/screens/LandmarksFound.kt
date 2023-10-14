package nz.ac.uclive.dsi61.ucanscan.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import nz.ac.uclive.dsi61.ucanscan.Constants
import nz.ac.uclive.dsi61.ucanscan.R
import nz.ac.uclive.dsi61.ucanscan.entity.Landmark
import nz.ac.uclive.dsi61.ucanscan.navigation.BottomNavigationBar
import nz.ac.uclive.dsi61.ucanscan.navigation.TopNavigationBar
import nz.ac.uclive.dsi61.ucanscan.viewmodel.IsRaceStartedModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.LandmarkViewModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.StopwatchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun LandmarksFoundScreen(context: Context, navController: NavController,
                         stopwatchViewModel : StopwatchViewModel, isRaceStartedModel : IsRaceStartedModel,
                         landmarkViewModel: LandmarkViewModel
) {
    val configuration = LocalConfiguration.current
    val IS_LANDSCAPE = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val landmarks by landmarkViewModel.landmarks.collectAsState(emptyList<Landmark>())

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        },
        content = { innerPadding ->

            val openDialog = remember { mutableStateOf(false) }

            TopNavigationBar(
                navController = navController,
                stopwatchViewModel = stopwatchViewModel,
                onGiveUpClick = {
                    openDialog.value = true
                },
                isRaceStartedModel = isRaceStartedModel,
                landmarkViewModel = landmarkViewModel
            )

            StopwatchIncrementFunctionality(stopwatchViewModel)


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = Constants.TOP_NAVBAR_HEIGHT) // provide space for the top app
                    .padding(bottom = Constants.BOTTOM_NAVBAR_HEIGHT)
                    .padding(horizontal = 16.dp),   // provide padding all around the content: left & right
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.landmarks_found_screen),
                    fontSize = 24.sp
                )

                Spacer( // spacing between "Found Landmarks" title and the lazycolumn
                    modifier = Modifier.height(16.dp)
                )

                FoundLandmarksList(context, landmarkViewModel.foundLandmarks, IS_LANDSCAPE)
            }

            BackToRaceOrHomeButtonContainer(navController, innerPadding, isRaceStartedModel.isRaceStarted, landmarkViewModel, IS_LANDSCAPE)

            BackHandler {
                // user has a back to race button so doesn't need going back with system
            }
        })
}



@SuppressLint("DiscouragedApi") // getIdentifier(): getting a resource ID given a string
@Composable
fun FoundLandmarksList(context: Context, landmarks: List<Landmark>, isLandscape: Boolean) {
    val PADDING_BETWEEN_ROWS = if(isLandscape) {0.dp} else {16.dp} // if landscape, thr btn is on the left so don't need padding above the btn
    val BACK_TO_RACE_BTN_HEIGHT = if(isLandscape) {0.dp} else {Constants.MEDIUM_BTN_HEIGHT} // if landscape, the btn is on the left: don't have space given for it

    // The lazycolumn is scrollable & allows the "landmarks found" title text to stick to the screen
    LazyColumn(
        modifier = Modifier
            // Reserve space for the Back To Race button, in its own section below the lazycolumn,
            // so that the button doesn't overlap the last image.
            .padding(bottom = PADDING_BETWEEN_ROWS + BACK_TO_RACE_BTN_HEIGHT)
    ) {
        items(landmarks) { landmark ->
            Row(
                modifier = Modifier
                    .padding(top = 16.dp)                       // padding between entries
                    .fillMaxWidth()
            ) {
                Column(    // column 1: landmark image
                    modifier = Modifier.padding(end = 16.dp),   // padding on right of image
                ) {
                    Row(
                    ) {
                        // The image filename is derived from the landmark's name:
                        // eg "Puaka-James Hight" becomes "landmark_puaka_james_hight.jpg"
                        val fileNameParts = landmark.name.split(" ", "-")
                        val fileName = fileNameParts.joinToString("_").lowercase()
                        // Create a resource ID for a named image in the "drawable" directory
                        val resourceId = context.resources.getIdentifier(
                            "landmark_$fileName",
                            "drawable",
                            context.packageName
                        )
                        // Use a default image if the image wasn't found (has invalid resource ID)
                        val drawableId = if (resourceId != 0) {
                            resourceId
                        } else {
                            R.drawable.landmark_no_image
                        }

                        Image(
                            painter = painterResource(id = drawableId),
                            contentDescription = null,
                            modifier = Modifier.size(if(isLandscape) {96.dp} else {128.dp})
                        )
                    }
                }

                Column(    // column 2: landmark text
                ) {
                    Row(
                    ) {
                        Text(
                            text = landmark.name,
                            fontSize = 24.sp,            //TODO: replace with MaterialTheme styling
                            fontWeight = FontWeight.Bold // ^
                        )
                    }
                    Row(
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
    }
}
