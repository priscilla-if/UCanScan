package nz.ac.uclive.dsi61.ucanscan.screens


import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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
fun RaceScreen(context: Context,
               navController: NavController, stopwatchViewModel : StopwatchViewModel, isRaceStartedModel : IsRaceStartedModel,
landmarkViewModel: LandmarkViewModel) {

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

            Column(
                modifier = Modifier.fillMaxSize().padding(top = 90.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    modifier = Modifier.padding(bottom = 30.dp),
                    text = stringResource(id = R.string.next_landmark),
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

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

                //increments stopwatch if it is running
                StopwatchIncrementFunctionality(stopwatchViewModel)


                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Button(
                        modifier = Modifier
                            .size(100.dp),
                        shape = RoundedCornerShape(16.dp),
                        onClick = {
                            navController.navigate(Screens.Camera.route)

                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.camera),
                            contentDescription = "Camera",
                            modifier = Modifier
                                .size(100.dp)
                        )
                    }

                    Button(
                        modifier = Modifier
                            .size(100.dp),
                        shape = RoundedCornerShape(16.dp),
                        onClick = {
                            navController.navigate(Screens.Map.route)
                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.map),
                            contentDescription = "Map",
                            modifier = Modifier
                                .size(100.dp)
                        )
                    }


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

            BackHandler {
                //TODO once everything is linked up don't allow user to go back
            }

        }
    )}



/**
 * Create a button at the bottom of a screen that has a bottom navbar.
 */
@Composable
fun BackToRaceOrHomeButtonContainer(navController: NavController, innerPadding: PaddingValues, isRaceStarted: State<Boolean>, landmarkViewModel: LandmarkViewModel) {
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

                    // If the race has ended
                    if (landmarkViewModel.currentLandmark == null) {
                        navController.navigate(Screens.FinishedRace.route)
                    }
                    else if (isRaceStarted.value) {
                        navController.navigate(Screens.Race.route)
                    } else {
                        navController.navigate(Screens.MainMenu.route)
                    }
                },
                modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
                    .size(width = 200.dp, height = 90.dp)

            ) {
                Text(
                    text = stringResource(if (landmarkViewModel.currentLandmark == null) R.string.finish_race else if (isRaceStarted.value) R.string.back_to_race else R.string.back_to_home),
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
        modifier = Modifier.size(width = 200.dp, height = 90.dp)
    ) {
        Text(
            text = stringResource(R.string.back_to_race),
            fontSize = 20.sp //TODO: replace with MaterialTheme styling
        )
    }
}



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