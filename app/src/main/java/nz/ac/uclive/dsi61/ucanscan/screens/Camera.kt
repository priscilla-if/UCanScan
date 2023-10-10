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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.sp
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
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.NativePaint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.toSize


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
    val updateCornerPoints: (List<PointF>?) -> Unit = { points ->
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
                                    updateCornerPoints(points) // if points is null, the canvas will get cleared of the red dots
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



    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        // Draw circles based on barcodeCornerPoints
        println("size points: " + (barcodeCornerPoints?.size))
        if (barcodeCornerPoints != null && barcodeCornerPoints!!.isNotEmpty()) {
            for (point in barcodeCornerPoints!!) {
                println("point!")
                val radius = 16f
                drawCircle(
                    color = Color(204, 0, 17), // UC red (#ucopenday!)
                    center = Offset(point.x, point.y),
                    radius = radius
                )
            }
        }
    }


    //TODO: add comments; remove print statements
    //TODO: make text & rect not show when qrCodeValue = null

    val textMeasurer = rememberTextMeasurer()
    val qrCodeText = qrCodeValue ?: ""

    Spacer(
        modifier = Modifier
            .drawWithCache {
                val textPadding = 16.dp

//                    val qrCodeText = qrCodeValue ?: ""
                val measuredText =
                    textMeasurer.measure(
                        AnnotatedString(qrCodeText),
                        style = TextStyle(fontSize = 32.sp)
                    )

//                    if (qrCodeValue != null) {
//
//                    }

                val rectSizeWithPadding = Size(
                    measuredText.size.width + 2 * textPadding.toPx(),
                    measuredText.size.height + 2 * textPadding.toPx()
                )


                onDrawBehind {
                    val cornerRadius = 16.dp.toPx()
                    val rectX = (size.width - rectSizeWithPadding.width) / 4 // position the rect 25% of the way down the screen
                    val rectY = (size.height - rectSizeWithPadding.height) / 4
//                        val rectX = (barcodeCornerPoints?.get(0)?.x) ?: ((size.width - rectSizeWithPadding.width) / 4)
//                        val rectY = (barcodeCornerPoints?.get(0)?.y) ?: ((size.height - rectSizeWithPadding.height) / 4)
                    drawRoundRect(Color(255, 255, 255), size = rectSizeWithPadding, topLeft = Offset(rectX, rectY),
                        cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                    )


                    drawIntoCanvas { canvas ->
                        val textColor = Color(0, 128, 255).toArgb()
                        val textPaint = androidx.compose.ui.graphics.Paint().asFrameworkPaint().apply {
                            color = textColor
                            textSize = 32.sp.toPx()
                        }


                        val textX = (size.width - rectSizeWithPadding.width) / 4 + textPadding.toPx()
                        val textY = (size.height + measuredText.size.height) / 4 - textPaint.fontMetrics.descent + textPadding.toPx() * 2
//                            val textX = (barcodeCornerPoints?.get(0)?.x?.plus(textPadding.toPx())) ?: ((size.width - rectSizeWithPadding.width) / 4 + textPadding.toPx())
//                            val textY = (barcodeCornerPoints?.get(0)?.y) ?: ((size.height - rectSizeWithPadding.height) / 4 - textPaint.fontMetrics.descent + textPadding.toPx() * 2)
//                            val textY = (barcodeCornerPoints?.get(0)?.y?.plus((- textPaint.fontMetrics.descent + textPadding.toPx() * 2))) ?: ((size.height - rectSizeWithPadding.height) / 4 + textPadding.toPx())
                        canvas.nativeCanvas.drawText(qrCodeText, textX, textY, textPaint)
                    }



                }
            }
            .fillMaxSize()
    )

}


//fun createTextPaint(color: Color): NativePaint {
//    val paint = androidx.compose.ui.graphics.Paint().asFrameworkPaint()
//    paint.color = color.toArgb()
//    return paint
//}


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
                barcode?.rawValue?.let { value ->
                    Log.d("FOO", value)
                    qrCodeValue = value
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
    if (cornerPoints != null) {
        for (p in cornerPoints) {
            println(p)
        }
    } else {
        println("No corner points detected.")
    }

    return cornerPoints
}
