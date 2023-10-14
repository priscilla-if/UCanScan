package nz.ac.uclive.dsi61.ucanscan.screens


import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import nz.ac.uclive.dsi61.ucanscan.Constants
import nz.ac.uclive.dsi61.ucanscan.R
import nz.ac.uclive.dsi61.ucanscan.navigation.BottomNavigationBar
import nz.ac.uclive.dsi61.ucanscan.navigation.Screens
import nz.ac.uclive.dsi61.ucanscan.navigation.TopNavigationBar
import nz.ac.uclive.dsi61.ucanscan.viewmodel.IsRaceStartedModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.LandmarkViewModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.StopwatchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun RaceScreen(context: Context, navController: NavController,
               stopwatchViewModel : StopwatchViewModel, isRaceStartedModel : IsRaceStartedModel,
               landmarkViewModel: LandmarkViewModel) {
    val configuration = LocalConfiguration.current
    val IS_LANDSCAPE = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    landmarkViewModel.updateLandmarks()
    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }, content = {
            val openDialog = remember { mutableStateOf(false)  }

            TopNavigationBar(
                navController = navController,
                stopwatchViewModel = stopwatchViewModel,
                onGiveUpClick = {
                    openDialog.value = true
                },
                isRaceStartedModel = isRaceStartedModel,
                landmarkViewModel = landmarkViewModel
            )

            if(IS_LANDSCAPE) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = Constants.TOP_NAVBAR_HEIGHT)
                        .padding(bottom = Constants.BOTTOM_NAVBAR_HEIGHT),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    StopwatchIncrementFunctionality(stopwatchViewModel)

                    Column(
                        modifier = Modifier
                            .padding(32.dp)
                            .weight(0.33f)
                    ) {
                        RaceTitle()
                    }

                    Column(
                        modifier = Modifier
                            .weight(0.33f)
                    ) {
                        RaceCircle()
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(0.33f),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        RaceSquareButton(navController, Screens.Camera.route, R.drawable.camera)
                        RaceSquareButton(navController, Screens.Map.route, R.drawable.map)
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = Constants.TOP_NAVBAR_HEIGHT)
                        .padding(bottom = Constants.BOTTOM_NAVBAR_HEIGHT),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    RaceTitle()

                    RaceCircle()

                    StopwatchIncrementFunctionality(stopwatchViewModel)

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        RaceSquareButton(navController, Screens.Camera.route, R.drawable.camera)
                        RaceSquareButton(navController, Screens.Map.route, R.drawable.map)
                        //TODO REMOVE THIS I AM JUST USING THIS TO ACCESS THE FINISHED RACE SCREEN!
                        Button(
                            modifier = Modifier
                                .size(100.dp),
                            shape = RoundedCornerShape(16.dp),
                            onClick = {
                                navController.navigate(Screens.FinishedRace.route)
                            },
                        ) {}
                    }
                }
            }

            BackHandler {
                //TODO once everything is linked up don't allow user to go back
            }

        }
    )}



@Composable
fun RaceTitle() {
    Text(
        modifier = Modifier.padding(bottom = 30.dp),
        text = stringResource(id = R.string.next_landmark),
        style = TextStyle(
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )
    )
}


@Composable
fun RaceCircle() {
    Box(
        modifier = Modifier
            .size(300.dp)
            .background(colorResource(R.color.light_grey), shape = CircleShape)
    ) {
        Text(
            text = landmarkViewModel.currentLandmark?.name.toString(),
            color = Color.Black,
            fontSize = 28.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 130.dp)
                .align(Alignment.Center),
            style = TextStyle(
                fontWeight = FontWeight.Bold
            )
        )
    }
}


@Composable
fun RaceSquareButton(navController: NavController, route: String, iconId: Int) {
    Button(
        modifier = Modifier
            .size(Constants.MEDIUM_BTN_HEIGHT),
        shape = RoundedCornerShape(16.dp),
        onClick = {
            navController.navigate(route)
        },
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
        )
    }
}




/**
 * Create a button at the bottom of a screen that has a bottom navbar.
 */
@Composable
fun BackToRaceOrHomeButtonContainer(navController: NavController, innerPadding: PaddingValues,
                                    isRaceStarted: State<Boolean>, landmarkViewModel: LandmarkViewModel,
                                    isLandscape: Boolean) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth() // helps center the button horizontally
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .padding(
                    horizontal = if (isLandscape) {
                        16.dp // add padding to the right of the btn if landscape
                    } else {
                        0.dp
                    }
                ),
            horizontalAlignment = if (isLandscape) {
                Alignment.End // push btn to the right of the screen if landscape
            } else {
                Alignment.CenterHorizontally
            }
        ) {

            Button(
                onClick = {

                    // If the race has ended
                    if (landmarkViewModel.currentLandmark == null && isRaceStarted.value) {
                        navController.navigate(Screens.FinishedRace.route)
                        landmarkViewModel.resetLandmarks()
                    }
                    else if (isRaceStarted.value && landmarkViewModel.currentLandmark != null) {
                        navController.navigate(Screens.Race.route)
                    } else {
                        navController.navigate(Screens.MainMenu.route)
                    }
                },
                modifier = Modifier
                    .padding(bottom = innerPadding.calculateBottomPadding())
                    .size(width = 200.dp, height = Constants.MEDIUM_BTN_HEIGHT)
            ) {
                Text(
                    text = stringResource(if (landmarkViewModel.currentLandmark == null && isRaceStarted.value) R.string.finish_race else if (isRaceStarted.value) R.string.back_to_race else R.string.back_to_home),
                    fontSize = 20.sp
                )
            }
        }
    }
}

/**
 * Create a button at the bottom of a screen that doesn't have a bottom navbar.
 */
@Composable
fun BackToRaceButton(navController: NavController) {
    Button(
        onClick = {
            navController.navigate(Screens.Race.route)
        },
        modifier = Modifier.size(width = 200.dp, height = Constants.MEDIUM_BTN_HEIGHT)
    ) {
        Text(
            text = stringResource(R.string.back_to_race),
            fontSize = 20.sp //TODO: replace with MaterialTheme styling
        )
    }
}




/**
 * Increments the stopwatch if it is running.
 */
@Composable
fun StopwatchIncrementFunctionality(stopwatchViewModel: StopwatchViewModel) {
    LaunchedEffect(stopwatchViewModel.isRunning) {
        while (stopwatchViewModel.isRunning) {
            withFrameMillis { frameTime ->
                if (stopwatchViewModel.startTime == 0L) {
                    stopwatchViewModel.startTime = frameTime
                }
                stopwatchViewModel.time = frameTime - stopwatchViewModel.startTime
            }
        }
    }
}