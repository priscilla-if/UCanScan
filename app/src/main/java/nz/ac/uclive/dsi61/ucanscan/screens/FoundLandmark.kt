package nz.ac.uclive.dsi61.ucanscan.screens
import android.annotation.SuppressLint
import android.content.Context
import android.media.MediaPlayer
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nz.ac.uclive.dsi61.ucanscan.R
import nz.ac.uclive.dsi61.ucanscan.navigation.BottomNavigationBar
import nz.ac.uclive.dsi61.ucanscan.navigation.Screens
import nz.ac.uclive.dsi61.ucanscan.navigation.TopNavigationBar
import nz.ac.uclive.dsi61.ucanscan.viewmodel.IsRaceStartedModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.LandmarkViewModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.StopwatchViewModel
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun FoundLandmarkScreen(context: Context,
                        navController: NavController, stopwatchViewModel : StopwatchViewModel, isRaceStartedModel : IsRaceStartedModel,
                        landmarkViewModel: LandmarkViewModel
) {

    val party = Party(
        emitter = Emitter(duration = 5, TimeUnit.SECONDS).perSecond(30)
    )
    val isShareDialogOpen = remember { mutableStateOf(false) }

    // Media player should play sound when screen is created
    val mMediaPlayer = remember { MediaPlayer.create(context, R.raw.achievement_sound) }
    val soundPlayed = remember { mutableStateOf(false) }

    if (!soundPlayed.value) {
        mMediaPlayer.start()
        soundPlayed.value = true
    }
    DisposableEffect(Unit) {
        onDispose {
            mMediaPlayer.release()
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }

    ) {

            innerPadding ->

        val openDialog = remember { mutableStateOf(false) }

        KonfettiView(
            modifier = Modifier.fillMaxSize(),
            parties = listOf(party)
        )

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
            modifier = Modifier.fillMaxSize().padding(top = 90.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Text(
                text = "You found",
                fontSize = 28.sp,
                modifier = Modifier.padding(top = 0.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            landmarkViewModel.pastLandmark?.let {
                Text(
                    text = it.name,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 30.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(300.dp)
                    .background(colorResource(R.color.light_grey), shape = CircleShape)
            ) {
                val fileNameParts = landmarkViewModel.pastLandmark?.name?.split(" ", "-")
                val fileName = fileNameParts?.joinToString("_")?.lowercase()
                // Create a resource ID for a named image in the "drawable" directory
                val resourceId = context.resources.getIdentifier(
                    "landmark_$fileName",
                    "drawable",
                    context.packageName
                )

                val drawableId = if (resourceId != 0) {
                    resourceId
                } else {
                    R.drawable.landmark_no_image
                }

                Image(
                    painter = painterResource(id = drawableId),
                    contentDescription = null,
                    modifier = Modifier
                        .size(300.dp)
                        .clip(CircleShape)
                )
            }

            Row(
                modifier = Modifier.fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding()),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {


                Button(
                    onClick = {
                        // If the current landmark we are searching for is now null
                        // (gets updated when we scan a landmark in Camera.kt),
                        // we should go to the finished race screen.
                        if (landmarkViewModel.currentLandmark == null) {
                            navController.navigate(Screens.FinishedRace.route)
                        } else {
                            navController.navigate(Screens.Race.route)
                        }
                    },
                    modifier = Modifier.size(width = 200.dp, height = 90.dp)

                ) {
                    Text(
                        text = stringResource(if (landmarkViewModel.currentLandmark == null) R.string.finish_race else R.string.back_to_race),
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
                                fontWeight = FontWeight.Bold,
                                text = stringResource(R.string.share_dialog_title)
                            )
                        },
                        text = {
                            val options = listOf(
                                stringResource(R.string.share_via_email),
                                stringResource(R.string.share_via_text),
                                stringResource(R.string.share_via_phonecall)
                            )
                            LazyColumn {
                                items(options) { option ->
                                    Text(
                                        modifier = Modifier
                                            .clickable {
                                                isShareDialogOpen.value = false
                                                landmarkViewModel.pastLandmark?.let {
                                                    DispatchAction(
                                                        context,
                                                        option,
                                                        it.name,
                                                        false
                                                    )
                                                }
                                            }
                                            .padding(vertical = 16.dp),
                                        style = TextStyle(fontSize = 18.sp),
                                        text = option
                                    )
                                }
                            }
                        },
                        onDismissRequest = { isShareDialogOpen.value = false },
                        confirmButton = {},
                        dismissButton = {}
                    )
                }

            }
        }
    }
}










