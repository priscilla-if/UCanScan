package nz.ac.uclive.dsi61.ucanscan.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.IconButton
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavController
import nz.ac.uclive.dsi61.ucanscan.R
import nz.ac.uclive.dsi61.ucanscan.navigation.Screens

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun RaceScreen(context: Context,
               navController: NavController) {
    // A surface container using the 'background' color from the theme


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {




     Column(
         //modifier = Modifier.fillMaxSize().padding(top = 0.dp),
         verticalArrangement = Arrangement.Center,
         horizontalAlignment = Alignment.CenterHorizontally
     ) {
         val openDialog = remember { mutableStateOf(false)  }

         TopAppBar(
             colors = TopAppBarDefaults.smallTopAppBarColors(
                 containerColor = MaterialTheme.colorScheme.primaryContainer,
                 titleContentColor = MaterialTheme.colorScheme.primary,
             ),
             title = {
                 Text("TIMER HERE")
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
                                     //TODO Reset stopwatch here
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



         Text( modifier = Modifier.padding(bottom = 70.dp) ,
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
                     //TODO
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


     }



    }
}
