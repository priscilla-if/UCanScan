package nz.ac.uclive.dsi61.ucanscan.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import nz.ac.uclive.dsi61.ucanscan.R
import nz.ac.uclive.dsi61.ucanscan.navigation.Screens
import nz.ac.uclive.dsi61.ucanscan.ui.theme.UCanScanTheme

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun MainMenuScreen(context: Context,
                   navController: NavController) {
    // A surface container using the 'background' color from the theme
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.cherry_blossoms), // TODO placeholder cherry blossom
            // image at the moment, we can change to something else once we decide on a
            // better design?
            contentDescription = "background_image",
            contentScale = ContentScale.Crop,
            alpha = 0.5F
        )
        Scaffold(
            containerColor = Color.Transparent,
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        modifier = Modifier.size(size = 150.dp).clip(CircleShape)
                            .border(width = 2.dp, color = Color.White, shape = CircleShape),
                        painter = painterResource(id = R.drawable.cat_one),
                        contentDescription = "Placeholder Logo"

                    )
                    Greeting("Priscilla") // TODO: Name functionality
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            navController.navigate(Screens.Race.route)
                        },
                        modifier = Modifier.size(width = 200.dp, height = 130.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.start_race),
                            fontSize = 20.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            //TODO: Navigate to correct page
                            navController.navigate(Screens.Landmarks.route)
                        },
                        modifier = Modifier.size(width = 200.dp, height = 130.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.my_times),
                            fontSize = 20.sp
                        )
                    }
                }
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = Color.Transparent,
                    contentPadding = PaddingValues(16.dp)
                ) {

                    Button(
                        onClick = {
                            navController.navigate(Screens.Preferences.route)
                        },
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                imageVector = Icons.Outlined.Settings,
                                contentDescription = ""
                            )
                            Spacer(modifier = Modifier.width(width = 8.dp))
                            Text(
                                text = stringResource(R.string.preferences)
                            )
                        }
                    }
                    Spacer( // push the button to the left side of the screen
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Welcome, $name!",
        modifier = modifier,
        fontSize = 30.sp

    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UCanScanTheme {
        Greeting("Android")
    }
}

