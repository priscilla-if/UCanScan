package nz.ac.uclive.dsi61.ucanscan.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import nz.ac.uclive.dsi61.ucanscan.R
import nz.ac.uclive.dsi61.ucanscan.navigation.Screens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun CameraScreen(context: Context, navController: NavController) {
    var previewView by remember { mutableStateOf<PreviewView?>(null) }

    var cameraProvider: ProcessCameraProvider? by remember { mutableStateOf(null) }

    val requestPermissionLauncher =
        //Gets camera permissions
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                val lifecycleOwner = (context as ComponentActivity)

                val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

                cameraProviderFuture.addListener({

                    cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build()
                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                    val camera = cameraProvider?.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview
                    )

                    val newPreviewView = PreviewView(context)
                    newPreviewView.implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    preview.setSurfaceProvider(newPreviewView.surfaceProvider)

                    previewView = newPreviewView
                }, ContextCompat.getMainExecutor(context))
            } else {
                //Goes back to race screen if user didn't accept camera permissions
                navController.navigate(Screens.Race.route)
            }
        }

    LaunchedEffect(requestPermissionLauncher) {
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    DisposableEffect(Unit) {
        onDispose {

            cameraProvider?.unbindAll()
            cameraProvider = null
        }
    }

    previewView?.let { CameraPreview(it, navController) }
}


@Composable
fun CameraPreview(previewView: PreviewView, navController: NavController) {
    AndroidView(
        factory = { previewView },
        modifier = Modifier.fillMaxSize()
    ) {}
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(
            modifier = Modifier
                .size(height = 90.dp, width = 90.dp),
            shape = RoundedCornerShape(16.dp),
            onClick = {
                //TODO add image analysis functionality for the code
            },
            ) {
            Icon(
                painter = painterResource(id = R.drawable.scancode),
                contentDescription = "Camera",
                modifier = Modifier
                    .size(100.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate(Screens.Race.route)

            },
            modifier = Modifier.size(width = 200.dp, height = 90.dp)
        ) {
            Text(
                text = stringResource(R.string.back_to_race),
                fontSize = 20.sp
            )
        }


    }



}
