package nz.ac.uclive.dsi61.ucanscan.screens

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.res.Configuration
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import nz.ac.uclive.dsi61.ucanscan.HurryUpAlarmReceiver
import nz.ac.uclive.dsi61.ucanscan.R
import nz.ac.uclive.dsi61.ucanscan.navigation.Screens
import nz.ac.uclive.dsi61.ucanscan.viewmodel.IsRaceStartedModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.PreferencesViewModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.StopwatchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun MainMenuScreen(context: Context, navController: NavController,
    stopwatchViewModel: StopwatchViewModel, isRaceStartedModel: IsRaceStartedModel, preferencesViewModel: PreferencesViewModel
) {
    val configuration = LocalConfiguration.current
    val IS_LANDSCAPE = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var selectedUserName by remember { mutableStateOf("") }
    val userNameState by preferencesViewModel.getUserNameState("userName", initialValue = "")

    LaunchedEffect(userNameState) {
        selectedUserName = userNameState
    }


    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            // TODO placeholder cherry blossom image at the moment, we can change to something else once we decide on a better design?
            painter = painterResource(R.drawable.cherry_blossoms),
            contentDescription = "background_image",
            contentScale = ContentScale.Crop,
            alpha = 0.5F
        )
        Scaffold(
            containerColor = Color.Transparent,
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape)
                            .border(width = 2.dp, color = Color.White, shape = CircleShape),
                        painter = painterResource(id = R.drawable.cat_one),
                        contentDescription = "Placeholder Logo"
                    )
                    Greeting(selectedUserName)

                    if (IS_LANDSCAPE) {
                        Row() {
                            MainButton(stringResource(R.string.start_race), true,
                                navController, stopwatchViewModel, isRaceStartedModel)
                            Spacer(modifier = Modifier.width(16.dp)) // horizontal space between buttons
                            MainButton(stringResource(R.string.my_times), false,
                                navController, stopwatchViewModel, isRaceStartedModel)
                        }
                    }

                    if (!IS_LANDSCAPE) {
                        Column() {
                            MainButton(stringResource(R.string.start_race), true,
                                navController, stopwatchViewModel, isRaceStartedModel)
                            MainButton(stringResource(R.string.my_times), false,
                                navController, stopwatchViewModel, isRaceStartedModel)
                        }
                    }
                }
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = Color.Transparent,
                    contentPadding = PaddingValues(16.dp)
                ) {

                    Button(
                        onClick = {
                            navController.navigate(Screens.Preferences.route)
                        },
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = ""
                            )
                            Spacer(modifier = Modifier.width(width = 8.dp))
                            Text(
                                text = stringResource(R.string.preferences)
                            )
                        }
                    }
                    Spacer( // push the button to the left side of the screen
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        )
    }
    BackHandler {
        // Stop user from going back to Race screen
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    val welcomeText = if (name.isEmpty()) {
        "Welcome!"
    } else {
        "Welcome, $name!"
    }

    Text(
        text = welcomeText,
        modifier = modifier,
        fontSize = 30.sp

    )
}

@Composable
fun MainButton(text: String, isStartButton: Boolean, navController: NavController, stopwatchViewModel: StopwatchViewModel, isRaceStartedModel: IsRaceStartedModel) {
    Spacer(modifier = Modifier.height(16.dp))
    Button(
        onClick = {
            if(isStartButton) {
                navController.navigate(Screens.Race.route)
                stopwatchViewModel.isRunning = true
                isRaceStartedModel.setRaceStarted(true)
                //scheduleHurryUpReminder()
            } else {
                navController.navigate(Screens.Leaderboard.route)
            }
        },
        modifier = Modifier.size(width = 200.dp, height = 130.dp)
    ) {
        Text(
            text = text,
            fontSize = 20.sp
        )
    }
}
