package nz.ac.uclive.dsi61.ucanscan.screens


import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.graphics.Color
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
import nz.ac.uclive.dsi61.ucanscan.viewmodel.StopwatchViewModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.StopwatchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun RaceScreen(context: Context,
               navController: NavController) {
        val openDialog = remember { mutableStateOf(false)  }

        TopAppBar(
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
                    modifier = Modifier
                        .size(width = 100.dp, height = 50.dp),
                    shape = RoundedCornerShape(16.dp),
                    onClick = {
                        openDialog.value = true
                    },
                ) {
                    Text(text = "Give Up")
                }

                if (openDialog.value) {

                    AlertDialog(
                        onDismissRequest = {
                            openDialog.value = false
                        },
                        title = {
                            Text(text = "Give Up")
                        },
                        text = {
                            Text("Are you sure you want to give up? Your progress will be lost.")
                        },
                        confirmButton = {
                            Button(

                                onClick = {
                                    openDialog.value = false
                                    navController.navigate(Screens.MainMenu.route)
                                    //reset stopwatch
                                    stopwatchViewModel.isRunning = false
                                    stopwatchViewModel.time = 0L
                                    stopwatchViewModel.startTime = 0L
                                }) {
                                Text("Give Up")
                            }
                        },
                        dismissButton = {
                            Button(

                                onClick = {
                                    openDialog.value = false
                                }) {
                                Text("Keep Racing")
                            }
                        })
                }
            },
        )
    Scaffold(

        bottomBar = {
            BottomNavigationBar(navController)
        }, content = {
            Column(
                modifier = Modifier.fillMaxSize().padding(top = 100.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    modifier = Modifier.padding(bottom = 70.dp),
                    text = stringResource(id = R.string.next_landmark),
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                )



                Box(
                    modifier = Modifier
                        .size(300.dp)
                        .background(Color.Gray, shape = CircleShape)
                ) {
                    Text(
                        text = stringResource(id = R.string.jack_erskine_landmark),
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


                }
        }}
    )}

@Composable
fun BackToRaceButtonContainer(navController: NavController, innerPadding: PaddingValues) {
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
                modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
                    .size(width = 200.dp, height = 90.dp)

            ) {
                Text(
                    text = stringResource(R.string.back_to_race),
                    fontSize = 20.sp
                )
            }
        }

    }
}
