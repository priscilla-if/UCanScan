package nz.ac.uclive.dsi61.ucanscan.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.AlertDialog
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
import androidx.core.content.ContextCompat.startActivity
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
fun FinishedRaceScreen(context: Context, navController: NavController,
                       stopwatchViewModel: StopwatchViewModel, isRaceStartedModel: IsRaceStartedModel
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

            val isGiveUpDialogOpen = remember { mutableStateOf(false) }
            val isShareDialogOpen = remember { mutableStateOf(false) }


            TopNavigationBar(
                navController = navController,
                stopwatchViewModel = stopwatchViewModel,
                onGiveUpClick = {
                    isGiveUpDialogOpen.value = true
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
                    Text(
                        text = convertTimeLongToMinutes(stopwatchViewModel.time),
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
                            isShareDialogOpen.value = true
                        },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.share),
                            contentDescription = "Share",
                            modifier = Modifier
                                .size(100.dp)
                        )
                    }


                    if (isShareDialogOpen.value) {
                        AlertDialog(
                            title = {
                                Text(
//                                    style = MaterialTheme.typography.body1,
                                    fontWeight = FontWeight.Bold,
                                    text = stringResource(R.string.share_dialog_title)
                                )
                            },
                            text = {
                                val options = listOf(stringResource(R.string.share_via_email), stringResource(R.string.share_via_text), stringResource(R.string.share_via_phonecall))
                                LazyColumn {
                                    items(options) { option ->
                                        Text(
                                            modifier = Modifier.clickable {
                                                isShareDialogOpen.value = false
                                                println("finishtime value 2 " + convertTimeLongToMinutes(stopwatchViewModel.time))
                                                DispatchAction(context, option, convertTimeLongToMinutes(stopwatchViewModel.time))
                                            },
//                                            style = MaterialTheme.typography.body1,
                                            text = option
                                        )
                                    }
                                }
                            },
                            onDismissRequest = { isShareDialogOpen.value = false },
                            confirmButton  = {},
                            dismissButton = {}
                        )
                    }
                }
            }
        }
    )
}



//TODO: bug: when go into share via email, choose an app, then go back to app, time on finish race screen is 0s
// ...actually i cant replicate this bug anymore...
fun DispatchAction(context: Context, option: String, raceFinishTime: String) {
    println("finishtime value 3 " + raceFinishTime)
    // we manually get the strings from the string resource IDs because
    // using stringResource() to do it would require this function to be composable
    val email = context.resources.getString(R.string.share_via_email) // get string value given resource id
    val text = context.resources.getString(R.string.share_via_text)
    val call = context.resources.getString(R.string.share_via_phonecall)
    val shareTitleString = context.resources.getString(R.string.share_finished_race_email_subject)
    val shareBodyPt1String = context.resources.getString(R.string.share_finished_race_msg_pt1)
    val shareBodyPt2String = context.resources.getString(R.string.share_finished_race_msg_pt2)

    when (option) {
        email -> {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_SUBJECT, shareTitleString)
            intent.putExtra(Intent.EXTRA_TEXT, shareBodyPt1String + raceFinishTime + shareBodyPt2String)
            startActivity(context, intent, null)
        }
        text -> {
            val uri = Uri.parse("smsto:")
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            intent.putExtra("sms_body", shareBodyPt1String + raceFinishTime + shareBodyPt2String)
            startActivity(context, intent, null)
        }
        call -> {
            val uri = Uri.parse("tel:")
            val intent = Intent(Intent.ACTION_DIAL, uri)
            startActivity(context, intent, null)
        }
    }
}