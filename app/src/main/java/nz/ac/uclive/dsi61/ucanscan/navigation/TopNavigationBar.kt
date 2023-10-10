package nz.ac.uclive.dsi61.ucanscan.navigation


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import nz.ac.uclive.dsi61.ucanscan.R
import nz.ac.uclive.dsi61.ucanscan.viewmodel.IsRaceStartedModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.LandmarkViewModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.StopwatchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopNavigationBar(
    navController: NavController,
    stopwatchViewModel: StopwatchViewModel,
    onGiveUpClick: () -> Unit,
    modifier: Modifier = Modifier,
    isRaceStartedModel: IsRaceStartedModel,
    landmarkViewModel: LandmarkViewModel
) {
    var openDialog by rememberSaveable { mutableStateOf(false) }

    val isRaceStarted by isRaceStartedModel.isRaceStarted


    if (isRaceStarted) {


        TopAppBar(
            modifier = modifier,
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                titleContentColor = MaterialTheme.colorScheme.primary,
            ),
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.stopwatch),
                        contentDescription = "Stopwatch",
                        modifier = Modifier.size(50.dp)
                    )

                    val seconds = stopwatchViewModel.time / 1000
                    val minutes = seconds / 60
                    val actualSeconds = seconds % 60
                    val hours = minutes / 60
                    val actualMinutes = minutes % 60

                    Text(text = "%02d:%02d:%02d".format(hours, actualMinutes, actualSeconds))
                }
            },
            actions = {
                Button(
                    onClick = {
                        openDialog = true
                    },
                    modifier = Modifier.size(width = 100.dp, height = 50.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(text = stringResource(id = R.string.give_up))
                }

                if (openDialog) {
                    AlertDialog(
                        onDismissRequest = {
                            openDialog = false
                        },
                        title = {
                            Text(text = stringResource(id = R.string.give_up))
                        },
                        text = {
                            Text(text = stringResource(id = R.string.give_up_desc))
                        },
                        confirmButton = {
                            Button(
                                onClick = {
                                    openDialog = false
                                    navController.navigate(Screens.MainMenu.route)
                                    // reset stopwatch
                                    stopwatchViewModel.isRunning = false
                                    stopwatchViewModel.time = 0L
                                    stopwatchViewModel.startTime = 0L
                                    isRaceStartedModel.setRaceStarted(false)
                                    landmarkViewModel.resetLandmarks()

                                }
                            ) {
                                Text(text = stringResource(id = R.string.give_up))
                            }
                        },
                        dismissButton = {
                            Button(
                                onClick = {
                                    openDialog = false
                                }
                            ) {
                                Text(text = stringResource(id = R.string.keep_racing))
                            }
                        }
                    )
                }
            }
        )
    }
}
