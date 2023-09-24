package nz.ac.uclive.dsi61.ucanscan.screens

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ToggleButton
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MovableContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.uclive.dsi61.ucanscan.R
import nz.ac.uclive.dsi61.ucanscan.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun PreferencesScreen(context: Context,
               navController: NavController) {

    val switchState = remember {
        mutableStateOf(true)
    }


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            //horizontalArrangement = Arrangement.Center
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.preferences)
            )

            Spacer(modifier = Modifier.height(16.dp))

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
                    Switch(
                        checked = switchState.value,
                        onCheckedChange = { switchState.value = it }
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
                            text = stringResource(R.string.prefs_notifications_opt2)
                        )
                    }
                    Spacer(
                        modifier = Modifier.weight(1f)
                    )
                    Switch(
                        checked = switchState.value,
                        onCheckedChange = { switchState.value = it }
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
                        checked = switchState.value,
                        onCheckedChange = { switchState.value = it }
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


                // ANIMATIONS SECTION
                Row(
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(
                        text = stringResource(R.string.prefs_animations).uppercase(),
                    )
                }

            }
        }



    }
}

// https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#switch
//@Composable
//fun Switch(
//    checked: Boolean,
//    onCheckedChange: ((Boolean) -> Unit)?,
//    modifier: Modifier = Modifier,
//    thumbContent: (@Composable () -> Unit)? = null,
//    enabled: Boolean = true,
//    colors: SwitchColors = SwitchDefaults.colors(),
//    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
//): Unit {
//
//}

//fun Switch(
//    modifier = Modifier.semantics { contentDescription = "Demo with icon" },
//    checked = checked,
//    onCheckedChange = { checked = it },
//    thumbContent = icon
//) {
//
//}



//@Composable
//fun Switch(
//    checked: Boolean = isChecked,
//    onCheckedChange: () -> Unit ={isChecked.value=it},
//    colors: SwitchColors = SwitchDefaults.colors(
//        checkedThumbColor = Color.DarkGray,
//        uncheckedThumbColor = Color.DarkGray,
//        checkedTrackColor = Color.Blue,
//        uncheckedTrackColor = Color.Blue,
//    )
//) {
//
//
//}
