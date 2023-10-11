package nz.ac.uclive.dsi61.ucanscan.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PointF
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
import androidx.compose.foundation.Canvas
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
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

//TODO: replace all 'barcode' with 'qrcode' before merging

var qrCodeValue by mutableStateOf<String?>(null)


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
@Composable
fun CameraScreen(context: Context, navController: NavController) {
    var previewView by remember { mutableStateOf<PreviewView?>(null) }
    var cameraProvider: ProcessCameraProvider? by remember { mutableStateOf(null) }
    val cameraExecutor = Executors.newSingleThreadExecutor()
    val application = context.applicationContext as UCanScanApplication

    // create BarcodeScanner object
    val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
        .build()
    val barcodeScanner = BarcodeScanning.getClient(options)

    // Barcode corner points: mutable state to store them, & callback function to update them
    var barcodeCornerPoints by remember { mutableStateOf<List<PointF>?>(null) }
    val updateCornerPoints: (List<PointF>?) -> Unit = { points ->
        barcodeCornerPoints = points
    }


    val requestPermissionLauncher =
        //Gets camera permissions
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                val lifecycleOwner = (context as ComponentActivity)
//                val cameraProvider = ProcessCameraProvider.getInstance(context)
                val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

                cameraProviderFuture.addListener({

                    cameraProvider = cameraProviderFuture.get()
                    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                    val previewUseCase = Preview.Builder().build()
                    val analysisUseCase = ImageAnalysis.Builder().build()
                    val camera = cameraProvider?.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        previewUseCase,
                        analysisUseCase
                    )

                    val newPreviewView = PreviewView(context)
                    newPreviewView.implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    previewUseCase.setSurfaceProvider(newPreviewView.surfaceProvider)

                    previewView = newPreviewView

                    // define the actual functionality of our analysis use case
                    analysisUseCase.setAnalyzer(
                        // newSingleThreadExecutor() will let us perform analysis on a single worker thread
                        Executors.newSingleThreadExecutor()
                    ) { imageProxy ->
                        previewView?.let {
                            processImageProxy(barcodeScanner, imageProxy,
                                updateCornerPoints = { points ->
                                    updateCornerPoints(points) // if points is null, the canvas will get cleared of the red dots
                                }
                            )
                        }
                    }


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

    previewView?.let { CameraPreview(application, it, navController, qrCodeValue) }


    // Draw on the screen to give feedback to user
    // points don't get transformed, so they don't display in the correct place over the qr code - commenting this out :')
//    DrawBarcodePoints(barcodeCornerPoints)
    DrawBarcodeTextBox()

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

        BackToRaceButton(navController)
    }

}



@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
// https://beakutis.medium.com/using-googles-mlkit-and-camerax-for-lightweight-barcode-scanning-bb2038164cdc
private fun processImageProxy(
    barcodeScanner: BarcodeScanner, imageProxy: ImageProxy,
    updateCornerPoints: (List<PointF>?) -> Unit // Pass in the callback function
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
                if (barcode != null && barcode.rawValue?.isNotEmpty() == true) {
                    qrCodeValue = barcode.rawValue
                } else {
                    // when qrCodeValue is null, nothing will get drawn on the canvas when we are not hovering over a qr code
                    qrCodeValue = null
                }

                val cornerPoints = processBarcodeCornerPoints(barcode)
                updateCornerPoints(cornerPoints)
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
 * and returns them as a list of PointF. Returns null if there are no corner points found.
 */
private fun processBarcodeCornerPoints(barcode: Barcode?): List<PointF>? {
    val cornerPoints = barcode?.cornerPoints?.map { PointF(it.x.toFloat(), it.y.toFloat()) }
    if (cornerPoints == null) {
        Log.d("FOO", "No barcode corner points detected.")
    }
    return cornerPoints
}


/**
 * Draws a QR code's corner points, when the camera is hovering over a QR code.
 */
@Composable
fun DrawBarcodePoints(barcodeCornerPoints: List<PointF>?) {
    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        // Draw circles based on barcodeCornerPoints
        if (!barcodeCornerPoints.isNullOrEmpty()) {
            for (point in barcodeCornerPoints) {
                val radius = 16f
                drawCircle(
                    color = Color(204, 0, 17), // UC red (#ucopenday!)
                    center = Offset(point.x, point.y),
                    radius = radius
                )
            }
        }
    }
}

/**
 *  Draws a rectangle with text saying the value of the QR code,
 *  when the camera is hovering over a QR code.
 *
 *  Code initially based on: https://developer.android.com/jetpack/compose/graphics/draw/overview#draw-text
 */
@Composable
fun DrawBarcodeTextBox() {
    val textMeasurer = rememberTextMeasurer()
    val qrCodeText = qrCodeValue ?: ""

    if(qrCodeText != "") {
        Spacer(
            modifier = Modifier
                .drawWithCache {
                    val textPadding = 16.dp

                    // Set up the text, and measure it so we can base the rect's dimensions off of it.
                    val measuredText =
                        textMeasurer.measure(
                            AnnotatedString(qrCodeText),
                            style = TextStyle(fontSize = 32.sp)
                        )

                    // Set up the rect's size, which is the text's size plus the padding all around.
                    val rectSizeWithPadding = Size(
                        measuredText.size.width + 2 * textPadding.toPx(),
                        measuredText.size.height + 2 * textPadding.toPx()
                    )

                    // Draw the rect.
                    onDrawBehind {
                        val cornerRadius = 16.dp.toPx()
                        // position the rect 25% of the way down the screen
                        val rectX = (size.width - rectSizeWithPadding.width) / 4    // size is the screen size
                        val rectY = (size.height - rectSizeWithPadding.height) / 4
                        drawRoundRect(
                            Color(255, 255, 255),
                            size = rectSizeWithPadding,
                            topLeft = Offset(rectX, rectY),
                            cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                        )

                        // Set up the text. In order to give it a colour we must use textPaint.
                        drawIntoCanvas { canvas ->
                            val textColor = Color(0, 128, 255).toArgb()
                            val textPaint = androidx.compose.ui.graphics
                                .Paint()
                                .asFrameworkPaint()
                                .apply {
                                    color = textColor
                                    textSize = 32.sp.toPx()
                                }

                            // Draw the text, based on the sizes of the text and the rect.
                            val textX = (size.width - rectSizeWithPadding.width) / 4 + textPadding.toPx()
                            val textY = (size.height + measuredText.size.height) / 4 - textPaint.fontMetrics.descent + textPadding.toPx() * 2
                            canvas.nativeCanvas.drawText(qrCodeText, textX, textY, textPaint)
                        }
                    }
                }
                .fillMaxSize()
        )
    }
}
