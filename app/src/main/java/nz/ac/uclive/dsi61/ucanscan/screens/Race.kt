package nz.ac.uclive.dsi61.ucanscan.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import androidx.navigation.NavController
import nz.ac.uclive.dsi61.ucanscan.R

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
         modifier = Modifier.fillMaxSize().padding(top = 100.dp),
         verticalArrangement = Arrangement.Center,
         horizontalAlignment = Alignment.CenterHorizontally
     ) {

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
             val context = LocalContext.current

             val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                 //handle image scanning
             }

             Button(
                 modifier = Modifier
                     .size(100.dp),
                 shape = RoundedCornerShape(16.dp),
                 onClick = {
                     if (checkCameraPermission(context)) {
                         val cameraIntent = android.content.Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                         cameraLauncher.launch(cameraIntent)
                     } else {
                         requestCameraPermission(context)
                     }
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
                     //TODO handle map btn onclick
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


private fun checkCameraPermission(context: Context): Boolean {
    val result = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
    return result == PackageManager.PERMISSION_GRANTED
}

private fun requestCameraPermission(context: Context) {
    ActivityCompat.requestPermissions(context as ComponentActivity, arrayOf(Manifest.permission.CAMERA), 0)
}

