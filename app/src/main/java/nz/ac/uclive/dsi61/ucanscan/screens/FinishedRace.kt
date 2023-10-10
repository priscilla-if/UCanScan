package nz.ac.uclive.dsi61.ucanscan.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import nz.ac.uclive.dsi61.ucanscan.R
import nz.ac.uclive.dsi61.ucanscan.UCanScanApplication
import nz.ac.uclive.dsi61.ucanscan.entity.Times
import nz.ac.uclive.dsi61.ucanscan.navigation.BottomNavigationBar
import nz.ac.uclive.dsi61.ucanscan.navigation.Screens
import nz.ac.uclive.dsi61.ucanscan.navigation.TopNavigationBar
import nz.ac.uclive.dsi61.ucanscan.viewmodel.FinishedRaceViewModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.FinishedRaceViewModelFactory
import nz.ac.uclive.dsi61.ucanscan.viewmodel.IsRaceStartedModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.StopwatchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun FinishedRaceScreen(context: Context,
                      navController: NavController, stopwatchViewModel : StopwatchViewModel, isRaceStartedModel : IsRaceStartedModel
) {


    stopwatchViewModel.isRunning = false
    isRaceStartedModel.setRaceStarted(false)


    val timeToSave = Times(
        endTime = stopwatchViewModel.time
    )

    val application = context.applicationContext as UCanScanApplication

    val finishedRaceViewModel: FinishedRaceViewModel = viewModel(factory = FinishedRaceViewModelFactory(application.repository))


    DisposableEffect(Unit) {
        finishedRaceViewModel.addTimeToDb(timeToSave)
        onDispose {}
    }

    stopwatchViewModel.startTime = 0L


    Scaffold(
        containerColor = colorResource(R.color.light_green),
        bottomBar = {
            BottomNavigationBar(navController)
        }, content = {

                innerPadding ->

            val openDialog = remember { mutableStateOf(false) }

            TopNavigationBar(
                navController = navController,
                stopwatchViewModel = stopwatchViewModel,
                onGiveUpClick = {
                    openDialog.value = true
                },
                isRaceStartedModel = isRaceStartedModel
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 90.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {



                Text(text = stringResource(R.string.finished_the_race),
                    fontSize = 28.sp,
                modifier = Modifier.padding(top = 0.dp))

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = stringResource(R.string.final_time),
                    fontSize = 28.sp,
                    modifier = Modifier.padding(bottom = 30.dp))


                Box(
                    modifier = Modifier
                        .size(300.dp)
                        .background(colorResource(R.color.light_grey), shape = CircleShape)
                ) {
                    Text(text = convertTimeLongToMinutes(stopwatchViewModel.time),
                        fontSize = 48.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 120.dp)
                            .align(Alignment.Center),
                        style = TextStyle(fontWeight = FontWeight.Bold)
                    )

                }



                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = innerPadding.calculateBottomPadding()),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {

                    Button(
                        onClick = {
                                navController.navigate(Screens.MainMenu.route)
                        },
                        modifier = Modifier.size(width = 200.dp, height = 90.dp)

                    ) {
                        Text(
                            text = stringResource(R.string.back_to_home),
                            fontSize = 20.sp
                        )
                    }

                    Button(
                        modifier = Modifier
                            .size(90.dp),
                        shape = RoundedCornerShape(16.dp),
                        onClick = {
                            //TODO sharing functionality

                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.share),
                            contentDescription = "Share",
                            modifier = Modifier
                                .size(100.dp)
                        )
                    }

                }
            }


        }
    )


}