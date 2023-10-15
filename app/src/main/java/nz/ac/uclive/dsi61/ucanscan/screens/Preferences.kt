package nz.ac.uclive.dsi61.ucanscan.screens

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import nz.ac.uclive.dsi61.ucanscan.Constants
import nz.ac.uclive.dsi61.ucanscan.R
import nz.ac.uclive.dsi61.ucanscan.navigation.BottomNavigationBar
import nz.ac.uclive.dsi61.ucanscan.navigation.TopNavigationBar
import nz.ac.uclive.dsi61.ucanscan.viewmodel.IsRaceStartedModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.LandmarkViewModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.PreferencesViewModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.StopwatchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun PreferencesScreen(context: Context,
               navController: NavController, stopwatchViewModel : StopwatchViewModel, isRaceStartedModel : IsRaceStartedModel,
                      preferencesViewModel: PreferencesViewModel, landmarkViewModel: LandmarkViewModel
) {
    val notificationOption1State by preferencesViewModel.getPreferenceState("notificationOption1")
    val notificationOption2State by preferencesViewModel.getPreferenceState("notificationOption2")
    val notificationOption3State by preferencesViewModel.getPreferenceState("notificationOption3")
    val themeOption1State by preferencesViewModel.getPreferenceState("themeOption1")
    val animationOption1State by preferencesViewModel.getPreferenceState("animationOption1")
    val animationOption2State by preferencesViewModel.getPreferenceState("animationOption2")

    var selectedUserName by remember { mutableStateOf("") }
    val userNameState by preferencesViewModel.getUserNameState("userName", initialValue = "")

    LaunchedEffect(userNameState) {
        selectedUserName = userNameState
    }
    Scaffold( bottomBar={ BottomNavigationBar(navController) },
        content={ innerPadding ->
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

            StopwatchIncrementFunctionality(stopwatchViewModel)


                Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    // provide space for bottom navbar so settings don't get hidden behind it
                    .padding(bottom = Constants.BOTTOM_NAVBAR_HEIGHT - 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                    if (isRaceStartedModel.isRaceStarted.value) {
                        Spacer(modifier = Modifier.height(50.dp))
                    }

                Text(
                    text = stringResource(R.string.preferences),
                    fontSize = 24.sp
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                ) {
                    // NOTIFICATIONS SECTION
                    Row(
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = stringResource(R.string.prefs_notifications).uppercase(),
                        )
                    }

                    // option 1
                    Row() {
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = stringResource(R.string.prefs_notifications_opt1)
                            )
                        }
                        Spacer( // push the button to the left side of the screen
                            modifier = Modifier.weight(1f)
                        )
                        // https://betterprogramming.pub/create-an-android-switch-using-jetpack-compose-351eddbf5828


                        Switch(
                            checked = notificationOption1State,
                            onCheckedChange = { saveSetting(context, "","notificationOption1", it, preferencesViewModel) }
                        )
                    }

                    // option 2
                    Row() {
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = stringResource(R.string.prefs_notifications_opt2)
                            )
                        }
                        Spacer(
                            modifier = Modifier.weight(1f)
                        )


                        Switch(
                            checked = notificationOption2State,
                            onCheckedChange = { saveSetting(context, "", "notificationOption2",  it, preferencesViewModel) }
                        )
                    }

                    // option 3
                    Row() {
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = stringResource(R.string.prefs_notifications_opt3)
                            )
                        }
                        Spacer(
                            modifier = Modifier.weight(1f)
                        )


                        Switch(
                            checked = notificationOption3State,
                            onCheckedChange = { saveSetting(context, "", "notificationOption3", it, preferencesViewModel) }
                        )
                    }


                    // THEME SECTION
                    Row(
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = stringResource(R.string.prefs_theme).uppercase(),
                        )
                    }

                    // option 1
                    Row() {
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = stringResource(R.string.prefs_theme_opt1)
                            )
                        }
                        Spacer(
                            modifier = Modifier.weight(1f)
                        )

                        Switch(
                            checked = themeOption1State,
                            onCheckedChange = { saveSetting(context, "", "themeOption1", it, preferencesViewModel) }
                        )
                    }


                    // ANIMATIONS SECTION
                    Row(
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = stringResource(R.string.prefs_animations).uppercase(),
                        )
                    }

                    // option 1
                    Row() {
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = stringResource(R.string.prefs_animations_opt1)
                            )
                        }
                        Spacer(
                            modifier = Modifier.weight(1f)
                        )

                        Switch(
                            checked = animationOption1State,
                            onCheckedChange = { saveSetting(context, "Found landmark animation option", "animationOption1", it, preferencesViewModel)
                            }
                        )
                    }

                    // option 2 // TODO: Not sure if we're planning on adding any other animations so commenting this out for now.
//                    Row() {
//                        Column(
//                            horizontalAlignment = Alignment.Start
//                        ) {
//                            Text(
//                                text = stringResource(R.string.prefs_animations_opt2)
//                            )
//                        }
//                        Spacer(
//                            modifier = Modifier.weight(1f)
//                        )
//
//
//                        Switch(
//                            checked = animationOption2State,
//                            onCheckedChange = {
//                                saveSetting(context, "animationOption2", it, preferencesViewModel)
//                                 }
//                        )
//                    }


                    // NAME SECTION
                    Row(
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = stringResource(R.string.prefs_name).uppercase(),
                        )
                    }

                    // option 1
                    Row(
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = stringResource(R.string.prefs_name_opt1)
                        )
                    }
                    Row() {
                        TextField(
                            value = selectedUserName,
                            onValueChange = {
                                selectedUserName = it
                            },
                        )

                        Spacer(
                            modifier = Modifier.weight(1f)
                        )

                        SaveUserNameButton(context, selectedUserName, preferencesViewModel)
                    }
                }
            }
            BackToRaceOrHomeButtonContainer(navController, innerPadding, isRaceStartedModel.isRaceStarted, landmarkViewModel, false)
    })

    BackHandler {
        // user has a back to race button so doesn't need going back with system
    }

}

fun saveSetting(context: Context, settingDisplayName: String, settingName: String, settingValue: Boolean, preferencesViewModel: PreferencesViewModel) {
    Toast.makeText(context, "$settingDisplayName saved!", Toast.LENGTH_SHORT).show()
    preferencesViewModel.updatePreferenceState(settingName, settingValue)
}


@Composable
fun SaveUserNameButton(context: Context, selectedUserName: String, preferencesViewModel: PreferencesViewModel) {
    FilledIconButton( // https://semicolonspace.com/jetpack-compose-material3-icon-buttons/#filled
        modifier = Modifier
            .width(50.dp)
            .height(50.dp)
            .aspectRatio(1f), // 1:1 aspect ratio: square button
        onClick = {
            Toast.makeText(context, "Username saved!", Toast.LENGTH_SHORT).show()

            preferencesViewModel.updateUserName("userName", selectedUserName)
        }
    ) {
        Icon(
            imageVector = Icons.Filled.Done,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.surface,
        )
    }



}
