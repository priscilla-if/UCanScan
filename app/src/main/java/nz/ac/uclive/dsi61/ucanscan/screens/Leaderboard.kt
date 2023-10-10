package nz.ac.uclive.dsi61.ucanscan.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
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
                modifier = Modifier.fillMaxSize().padding(16.dp)
            ) {



                Text(text = "Personal Bests")


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
    LazyColumn {
        items(allTimes) { time ->
            Text(text = "Date: ${time.dateAchieved}, End Time: ${time.endTime}")

        }
    }
}

