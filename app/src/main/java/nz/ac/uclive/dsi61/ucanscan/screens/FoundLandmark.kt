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

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun FoundLandmarkScreen(context: Context,
               navController: NavController) {

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
