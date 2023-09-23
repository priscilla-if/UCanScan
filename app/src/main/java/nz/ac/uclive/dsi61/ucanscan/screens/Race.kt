package nz.ac.uclive.dsi61.ucanscan.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.ComponentActivity
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
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {

            val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                //handle image scanning
            }

            val context = LocalContext.current

            IconButton(
                onClick = {
                    if (checkCameraPermission(context)) {
                        val cameraIntent = android.content.Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                        cameraLauncher.launch(cameraIntent)
                    } else {
                        requestCameraPermission(context)
                    }
                },
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.White, shape = RoundedCornerShape(percent = 50))
            ) {

                Icon(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = "Camera"
                )
            }


            IconButton(
                onClick = {
                    //map btn click
                }
            ) {
                Icon(
                    imageVector = Icons.Default.MailOutline,
                    contentDescription = "Map Icon",
                    tint = Color.Black
                )
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