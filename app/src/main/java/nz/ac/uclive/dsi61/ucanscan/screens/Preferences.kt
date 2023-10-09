package nz.ac.uclive.dsi61.ucanscan.screens

import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import nz.ac.uclive.dsi61.ucanscan.R
import nz.ac.uclive.dsi61.ucanscan.UCanScanApplication
import nz.ac.uclive.dsi61.ucanscan.navigation.BottomNavigationBar
import nz.ac.uclive.dsi61.ucanscan.viewmodel.PreferencesViewModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.PreferencesViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun PreferencesScreen(context: Context,
               navController: NavController) {

    val toner = ToneGenerator(AudioManager.STREAM_ALARM, ToneGenerator.MAX_VOLUME)

    val application = context.applicationContext as UCanScanApplication
   // val preferencesViewModel : PreferencesViewModel = viewModel(factory = PreferencesViewModelFactory(application.repository))

    val preferencesViewModel: PreferencesViewModel = viewModel(
        factory = PreferencesViewModelFactory(application.repository)
    )


/*    var notificationOption1State by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        preferencesViewModel.getPreferenceStateByName("notifications1").collect { state ->
            notificationOption1State = state
        }
    }*/

    var notificationOption1State by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        preferencesViewModel.getPreferenceStateByName("notifications1").collect { state ->
            notificationOption1State = state
        }
    }

   // var notificationOption1State by remember { mutableStateOf(preferencesViewModel.getPreferenceStateByName("notifications1")) }
    val notificationOption2State = remember { mutableStateOf(true) }
    val notificationOption3State = remember { mutableStateOf(true) }
    val themeOption1State = remember { mutableStateOf(true) }
    val animationOption1State = remember { mutableStateOf(true) }
    val animationOption2State = remember { mutableStateOf(true) }
    var selectedUserName by remember { mutableStateOf("we have no DB yet") } //TODO: get value from db


    Scaffold( bottomBar={ BottomNavigationBar(navController) },
        content={ innerPadding -> Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.preferences)
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Column(
                    //
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
                    Row(
                        //
                    ) {
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



/*

                        Switch(
                            checked = notificationOption1State,
                            onCheckedChange = { isChecked ->
                                saveSetting(context, "notifications1", isChecked, preferencesViewModel)
                                notificationOption1State = isChecked
                            }
                        )

*/

                        Switch(
                            checked = notificationOption1State,
                            onCheckedChange = {    saveSetting(context, "notifications1", it, preferencesViewModel)
                                notificationOption1State = it
                            }
                        )


                }

                    //TODO fix the other options once first one works

                    // option 2
                    Row(
                        //
                    ) {
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
                            checked = notificationOption2State.value,
                            onCheckedChange = { notificationOption2State.value = it
                                saveSetting(context, "notifications2",  it, preferencesViewModel) } //TODO: name
                        )
                    }

                    // option 3
                    Row(
                        //
                    ) {
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
                            checked = notificationOption3State.value,
                            onCheckedChange = { notificationOption3State.value = it
                                saveSetting(context, "Notification Option 3", it, preferencesViewModel) } //TODO: name
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
                    Row(
                        //
                    ) {
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
                            checked = themeOption1State.value,
                            onCheckedChange = { themeOption1State.value = it
                                saveSetting(context, "Theme",  it, preferencesViewModel) }
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
                    Row(
                        //
                    ) {
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
                            checked = animationOption1State.value,
                            onCheckedChange = { animationOption1State.value = it
                                saveSetting(context, "Animation Option 1",  it, preferencesViewModel) } //TODO: name
                        )
                    }

                    // option 2
                    Row(
                        //
                    ) {
                        Column(
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = stringResource(R.string.prefs_animations_opt2)
                            )
                        }
                        Spacer(
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = animationOption2State.value,
                            onCheckedChange = { animationOption2State.value = it
                                saveSetting(context, "Animation Option 2", it, preferencesViewModel) } //TODO: name
                        )
                    }


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
                    Row(
                        //
                    ) {
                        TextField(
                            value = selectedUserName,
                            onValueChange = {
                                selectedUserName = it
                            },
                        )

                        Spacer(
                            modifier = Modifier.weight(1f)
                        )

                      //  SaveUserNameButton(context, selectedUserName)
                    }
                }
            }
            // TODO: Add a conditional so that this only displays if a race is in progress.
            BackToRaceButtonContainer(navController, innerPadding)

        }

    })

    BackHandler {
        navController.popBackStack()
    }

}

fun saveSetting(context: Context, settingName: String, settingValue: Boolean, preferencesViewModel: PreferencesViewModel) {
    Toast.makeText(context, "$settingName saved!", Toast.LENGTH_SHORT).show()
    println("Setting val before: " + preferencesViewModel.getPreferenceStateByName(settingName) )

    preferencesViewModel.updatePreferenceState(settingName, settingValue)
    println("Setting val after: " + preferencesViewModel.getPreferenceStateByName(settingName))
}




/*@Composable
fun SaveUserNameButton(context: Context, selectedUserName: String) {
    FilledIconButton( // https://semicolonspace.com/jetpack-compose-material3-icon-buttons/#filled
        modifier = Modifier
            .width(50.dp)
            .height(50.dp)
            .aspectRatio(1f), // 1:1 aspect ratio: square button
        onClick = {
            saveSetting(context, "Name", selectedUserName)
        }
    ) {
        Icon(
            imageVector = Icons.Filled.Done,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.surface,
        )
    }



}*/
