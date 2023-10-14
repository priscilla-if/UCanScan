package nz.ac.uclive.dsi61.ucanscan.screens
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.media.MediaPlayer
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nz.ac.uclive.dsi61.ucanscan.Constants
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
    val configuration = LocalConfiguration.current
    val IS_LANDSCAPE = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val party = Party(
        emitter = Emitter(duration = 5, TimeUnit.SECONDS).perSecond(30)
    )

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

        if(IS_LANDSCAPE) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = Constants.TOP_NAVBAR_HEIGHT)
                    .padding(bottom = Constants.BOTTOM_NAVBAR_HEIGHT),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Column(
                    modifier = Modifier
                        .padding(32.dp)
                        .weight(0.33f)
                ) {
                    FoundLandmarkTitle(landmarkViewModel, IS_LANDSCAPE)
                }

                Column(
                    modifier = Modifier
                        .weight(0.33f)
                ) {
                    FoundLandmarkCircle(context, landmarkViewModel)
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(0.33f),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    FoundLandmarkButtons(navController, landmarkViewModel)
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = Constants.TOP_NAVBAR_HEIGHT)
                    .padding(bottom = Constants.BOTTOM_NAVBAR_HEIGHT),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FoundLandmarkTitle(landmarkViewModel, IS_LANDSCAPE)

                FoundLandmarkCircle(context, landmarkViewModel)

                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = innerPadding.calculateBottomPadding()),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FoundLandmarkButtons(navController, landmarkViewModel)
                }
            }
        }
    }
}


@Composable
fun FoundLandmarkTitle(landmarkViewModel: LandmarkViewModel, isLandscape: Boolean) {
    Text(
        text = stringResource(R.string.you_found),
        style = TextStyle(
            fontSize = 28.sp,
            textAlign = TextAlign.Center
        ),
        modifier = Modifier
            .fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(16.dp))

    landmarkViewModel.pastLandmark?.let {
        Text(
            text = it.name,
            style = TextStyle(
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(if(!isLandscape) {30.dp} else {0.dp})
        )
    }
}


@Composable
fun FoundLandmarkCircle(context: Context, landmarkViewModel: LandmarkViewModel) {
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
}


@Composable
fun FoundLandmarkButtons(navController: NavController, landmarkViewModel: LandmarkViewModel) {
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
        modifier = Modifier.size(width = 200.dp, height = Constants.MEDIUM_BTN_HEIGHT)

    ) {
        Text(
            text = stringResource(if (landmarkViewModel.currentLandmark == null) R.string.finish_race else R.string.back_to_race),
            fontSize = 20.sp
        )
    }

    Button(
        modifier = Modifier
            .size(Constants.MEDIUM_BTN_HEIGHT),
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



