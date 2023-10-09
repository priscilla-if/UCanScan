package nz.ac.uclive.dsi61.ucanscan.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
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
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nz.ac.uclive.dsi61.ucanscan.R
import nz.ac.uclive.dsi61.ucanscan.UCanScanApplication
import nz.ac.uclive.dsi61.ucanscan.entity.Landmark
import nz.ac.uclive.dsi61.ucanscan.navigation.Screens
import java.util.concurrent.Executors


var qrCodeValue by mutableStateOf<String?>(null)


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun CameraScreen(context: Context, navController: NavController) {
    var previewView by remember { mutableStateOf<PreviewView?>(null) }
    val cameraExecutor = Executors.newSingleThreadExecutor()
    val application = context.applicationContext as UCanScanApplication


    // create BarcodeScanner object
    val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
        .build()
    val barcodeScanner = BarcodeScanning.getClient(options)


    val requestPermissionLauncher =
        //Gets camera permissions
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                val lifecycleOwner = (context as ComponentActivity)
                val cameraProvider = ProcessCameraProvider.getInstance(context)
                cameraProvider.addListener({
                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                    val previewUseCase = Preview.Builder().build()
                    val analysisUseCase = ImageAnalysis.Builder().build()

                    // define the actual functionality of our analysis use case
                    analysisUseCase.setAnalyzer(
                        // newSingleThreadExecutor() will let us perform analysis on a single worker thread
                        Executors.newSingleThreadExecutor()
                    ) { imageProxy ->
                        processImageProxy(barcodeScanner, imageProxy)
                    }

                    val camera = cameraProvider.get().bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        previewUseCase,
                        analysisUseCase
                    )

                    val newPreviewView = PreviewView(context)
                    newPreviewView.implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    previewUseCase.setSurfaceProvider(newPreviewView.surfaceProvider)

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

    previewView?.let { CameraPreview(application, it, navController, qrCodeValue) }
}

@Composable
fun CameraPreview(application: UCanScanApplication, previewView: PreviewView, navController: NavController, qrCodeValue: String?) {
    val scope = rememberCoroutineScope()

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
                CoroutineScope(Dispatchers.IO).launch {
                    val landmark: Landmark? = qrCodeValue?.let {
                            application.repository.getLandmarkByName(it)
                        }

                    if (landmark != null) {
                        if(landmark.isFound) {
                            //duplicate
                            Log.d("FOO", ":O landmark already scanned!")
                        } else {
                            Log.d("FOO", ":D adding landmark to DB!")

                            scope.launch {
                                landmark.isFound = true
                                application.repository.updateLandmark(landmark)
                            }
                        }
                    }
                }




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



@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
// https://beakutis.medium.com/using-googles-mlkit-and-camerax-for-lightweight-barcode-scanning-bb2038164cdc
private fun processImageProxy(
    barcodeScanner: BarcodeScanner,
    imageProxy: ImageProxy
) {

    imageProxy.image?.let { image ->
        val inputImage =
            InputImage.fromMediaImage(
                image,
                imageProxy.imageInfo.rotationDegrees
            )

        barcodeScanner.process(inputImage)
            .addOnSuccessListener { barcodeList ->
                val barcode = barcodeList.getOrNull(0)
                // `rawValue` is the decoded value of the barcode
                barcode?.rawValue?.let { value ->
                    Log.d("FOO", value)
                    qrCodeValue = value
                }
            }
            .addOnFailureListener {
                // This failure will happen if the barcode scanning model
                // fails to download from Google Play Services
            }.addOnCompleteListener {
                // When the image is from CameraX analysis use case, must
                // call image.close() on received images when finished
                // using them. Otherwise, new images may not be received
                // or the camera may stall.
                imageProxy.image?.close()
                imageProxy.close()
            }
    }
}
