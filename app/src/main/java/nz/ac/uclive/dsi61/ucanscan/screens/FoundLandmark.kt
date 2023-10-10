package nz.ac.uclive.dsi61.ucanscan.screens
import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import nz.ac.uclive.dsi61.ucanscan.R
import nz.ac.uclive.dsi61.ucanscan.navigation.BottomNavigationBar
import nz.ac.uclive.dsi61.ucanscan.navigation.Screens
import nz.ac.uclive.dsi61.ucanscan.navigation.TopNavigationBar
import nz.ac.uclive.dsi61.ucanscan.viewmodel.IsRaceStartedModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.LandmarkViewModel
import nz.ac.uclive.dsi61.ucanscan.viewmodel.StopwatchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun FoundLandmarkScreen(context: Context,
                        navController: NavController, stopwatchViewModel : StopwatchViewModel, isRaceStartedModel : IsRaceStartedModel,
                        landmarkViewModel: LandmarkViewModel
) {
        Scaffold(
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

                    landmarkViewModel.currentLandmark?.let {
                        Text(
                            text = it.name,
                            fontSize = 28.sp,
                            modifier = Modifier.padding(bottom = 30.dp)
                        )
                    }


                    Box(
                        modifier = Modifier
                            .size(300.dp)
                            .background(colorResource(R.color.light_grey), shape = CircleShape)
                    ) {
                        val fileNameParts = landmarkViewModel.currentLandmark?.name?.split(" ", "-")
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
                                .clip(CircleShape))
                    }



                    Row(
                        modifier = Modifier.fillMaxSize()
                            .padding(bottom = innerPadding.calculateBottomPadding()),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {

                        Button(
                            onClick = {
                                // When we go back to the race screen, we have now updated the next landmark
                                // and the current index we are on
                                landmarkViewModel.updateCurrentIndex(landmarkViewModel.currentIndex + 1)
                                landmarkViewModel.updateLandmarks()
                                navController.navigate(Screens.Race.route)
                            },
                            modifier = Modifier.size(width = 200.dp, height = 90.dp)

                        ) {
                            Text(
                                text = stringResource(R.string.back_to_race),
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

                )}




