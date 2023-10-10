package nz.ac.uclive.dsi61.ucanscan.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Point
import android.graphics.PointF
import android.graphics.Rect
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
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
import androidx.compose.ui.draw.drawBehind
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
import androidx.compose.foundation.Canvas
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
//import androidx.compose.ui.graphics.drawscope.drawRect


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

    // Barcode corner points: mutable state to store them, & callback function to update them
    var barcodeCornerPoints by remember { mutableStateOf<List<PointF>?>(null) }
    val updateCornerPoints: (List<PointF>) -> Unit = { points ->
        barcodeCornerPoints = points
    }


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
                        previewView?.let {
                            processImageProxy(barcodeScanner, imageProxy,
                                updateCornerPoints = { points ->
                                    // Update UI or perform actions with cornerPoints here
                                    // For example, you can draw rectangles or perform other visualizations

                                    // You can use the Canvas composable to draw rectangles based on corner points
//                                    Canvas(modifier = Modifier.fillMaxSize()) {
//                                        points.forEach { point ->
//                                            drawRect(
//                                                color = Color.Red,
//                                                topLeft = Offset(point.x.toFloat(), point.y.toFloat()),
//                                                size = Size(50f, 50f) // Adjust the size as needed
//                                            )
//                                        }
//                                    }
                                    updateCornerPoints(points)

                                    //TODO: do i need stuff here?????

                                }
                            )
                        }
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



    // Use the barcodeCornerPoints in a Canvas composable
//    Canvas(modifier = Modifier.fillMaxSize()) {
//        barcodeCornerPoints?.let { points ->
//            for (point in points) {
//                val radius = 10f // Adjust the radius as needed
//                drawCircle(
//                    color = Color.Red,
//                    center = Offset(point.x.toFloat(), point.y.toFloat()),
//                    radius = radius
//                )
//            }
//        }
//    }

    // Use a Box with a Canvas composable to draw circles
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            // Draw circles based on barcodeCornerPoints
            println("size: " + (barcodeCornerPoints?.size))
            barcodeCornerPoints?.let { points ->
                for (point in points) {
                    println("point!")
                    val radius = 16f // Adjust the radius as needed
                    drawCircle(
                        color = Color(204, 0, 17), // UC red (#ucopenday!)
                        center = Offset(point.x, point.y),
                        radius = radius
                    )
                }
            }
        }
    }

}



@Composable
fun CameraPreview(application: UCanScanApplication, previewView: PreviewView,
                  navController: NavController, qrCodeValue: String?) {
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
    barcodeScanner: BarcodeScanner, imageProxy: ImageProxy,
    updateCornerPoints: (List<PointF>) -> Unit // Pass in the callback function
) {

    imageProxy.image?.let { image ->
        val inputImage = InputImage.fromMediaImage(
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

                processBarcodeCornerPoints(barcode, updateCornerPoints)
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

/**
 * Helper function for processImageProxy() that gets the barcode's corner points
 * and then calls a callback function to update them.
 */
private fun processBarcodeCornerPoints(barcode: Barcode?, updateCornerPoints: (List<PointF>) -> Unit) {
    val cornerPoints = barcode?.cornerPoints?.map { PointF(it.x.toFloat(), it.y.toFloat()) }
    if (cornerPoints != null) {
        for (p in cornerPoints) {
            println(p)
        }
    } else {
        println("No corner points detected.")
    }

    cornerPoints?.let { updateCornerPoints(it) }
}
