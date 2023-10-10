package nz.ac.uclive.dsi61.ucanscan.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import nz.ac.uclive.dsi61.ucanscan.R
import nz.ac.uclive.dsi61.ucanscan.UCanScanApplication
import nz.ac.uclive.dsi61.ucanscan.entity.Landmark
import nz.ac.uclive.dsi61.ucanscan.entity.Times
import nz.ac.uclive.dsi61.ucanscan.navigation.BottomNavigationBar
import nz.ac.uclive.dsi61.ucanscan.navigation.TopNavigationBar
import nz.ac.uclive.dsi61.ucanscan.viewmodel.FinishedRaceViewModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.IsRaceStartedModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.LandmarkViewModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.LandmarkViewModelFactory
import nz.ac.uclive.dsi61.ucanscan.viewmodel.StopwatchViewModel

// This is a temporary Screen for a Leaderboard (for navigation purposes)
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun LeaderboardScreen(context: Context,
                    navController: NavController, stopwatchViewModel : StopwatchViewModel, isRaceStartedModel : IsRaceStartedModel
) {

    val context = LocalContext.current
    val application = context.applicationContext as UCanScanApplication
    val finishedRaceViewModel: FinishedRaceViewModel = remember {
        FinishedRaceViewModel(repository = application.repository)
    }

    val isRaceStarted by isRaceStartedModel.isRaceStarted
    val allTimes by finishedRaceViewModel.allTimes.collectAsState(emptyList())
    

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }, content = {



                innerPadding ->

            val openDialog = remember { mutableStateOf(false)  }

            TopNavigationBar(
                navController = navController,
                stopwatchViewModel = stopwatchViewModel,
                onGiveUpClick = {
                    openDialog.value = true
                },
                isRaceStartedModel = isRaceStartedModel
            )

            StopwatchIncrementFunctionality(stopwatchViewModel)

            Column(

                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize().padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = if (isRaceStarted) 70.dp else 30.dp,
                    bottom = 16.dp)
            ) {



                Text(text = "Personal Bests",
                    fontSize = 24.sp)


                TimesDisplay(allTimes = allTimes)


            }

            BackToRaceOrHomeButtonContainer(navController, innerPadding, isRaceStartedModel.isRaceStarted)

        }
    )

    BackHandler {
        navController.popBackStack()
    }

}


@Composable
fun TimesDisplay(allTimes: List<Times>) {



    LazyColumn (modifier = Modifier.padding(bottom = 180.dp, top = 19.dp)) {
        itemsIndexed(allTimes) { index, time ->

            val medalImage = when (index) {
                0 -> R.drawable.first_medal
                1 -> R.drawable.second_medal
                2 -> R.drawable.third_medal
                else -> R.drawable.all_medal
            }

            Row(modifier = Modifier.padding(top =10.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    painter = painterResource(id = medalImage),
                    contentDescription = "Camera",
                    modifier = Modifier
                        .size(50.dp)
                )

                Text(text = "${time.dateAchieved}          ${convertTimeLongToMinutes(time.endTime)}")


              /*  Button(
                    modifier = Modifier
                        .size(80.dp),
                    shape = RoundedCornerShape(16.dp),
                    onClick = {
                        //TODO sharing functionality
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.share),
                        contentDescription = "Share",
                        modifier = Modifier
                            .size(40.dp)
                    )
                }*/

            }
        }
    }





}

fun convertTimeLongToMinutes(time: Long): String {
    val seconds = time / 1000
    val minutes = seconds / 60
    val actualSeconds = seconds % 60
    val hours = minutes / 60
    val actualMinutes = minutes % 60

    return "%02d:%02d:%02d".format(hours, actualMinutes, actualSeconds)
}