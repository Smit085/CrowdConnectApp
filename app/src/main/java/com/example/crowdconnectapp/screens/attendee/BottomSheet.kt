package com.example.crowdconnectapp.screens.attendee

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.crowdconnectapp.components.qrcode.QrCodeAnalyzer
import com.google.common.util.concurrent.ListenableFuture

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(navController: NavController) {
    var inputText by rememberSaveable { mutableStateOf("") }
    var isStartQuiz by rememberSaveable { mutableStateOf(false) }
    var btnText by remember { mutableStateOf("Enter Code") }
    var code by remember { mutableStateOf("") }
    var manualCode by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCameraPermission = granted
        }
    )

    LaunchedEffect(true) {
        if (!hasCameraPermission) {
            launcher.launch(Manifest.permission.CAMERA)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (hasCameraPermission) {
                    Card(
                        shape = RoundedCornerShape(5.dp),
                    ) {
                        CameraPreview(
                            cameraProviderFuture = cameraProviderFuture,
                            lifecycleOwner = lifecycleOwner
                        ) { result ->
                            code = result
                            // Navigate to a new screen after scanning the code
                            if (code.isNotEmpty()) {
                                navController.navigate("startSession/$code")
                            } else {
                                Log.e("QRCode", "Invalid QR code format")
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
//                        Text(
//                            text = "Code: $code",
//                            style = MaterialTheme.typography.bodyMedium,
//                            fontWeight = FontWeight.Bold,
//                            textAlign = TextAlign.Center,
//                        )
                    }
                }
                InputSection(
                    manualCode = manualCode,
                    inputText = inputText,
                    onTitleChange = { inputText = it },
                    btnText = btnText,
                    onSubmit = {
                        if (inputText.isNotEmpty()) {
                            navController.navigate("startSession/$inputText")
                        } else {
                            btnText = "Submit"
                            isStartQuiz = true
                            manualCode = true
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun CameraPreview(
    cameraProviderFuture: ListenableFuture<ProcessCameraProvider>,
    lifecycleOwner: LifecycleOwner,
    onCodeScanned: (String) -> Unit
) {
    val context = LocalContext.current
    val previewView = remember { PreviewView(context) }

    DisposableEffect(lifecycleOwner) {
        val cameraProvider = cameraProviderFuture.get()

        val preview = Preview.Builder().build().apply {
            setSurfaceProvider(previewView.surfaceProvider)
        }

        val selector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(1280, 720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), QrCodeAnalyzer { result ->
            onCodeScanned(result)
        })

        try {
            cameraProvider.bindToLifecycle(
                lifecycleOwner, selector, preview, imageAnalysis
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

        onDispose {
            cameraProvider.unbindAll()
        }
    }

    AndroidView(
        factory = { previewView },
        modifier = Modifier
            .height(350.dp)
            .fillMaxWidth()
    )
}

@Composable
fun InputSection(
    manualCode: Boolean,
    inputText: String,
    onTitleChange: (String) -> Unit,
    btnText: String,
    onSubmit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (manualCode) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = inputText,
                onValueChange = onTitleChange,
                label = { Text("Enter Code") },
                placeholder = { Text("Type here") }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onSubmit,
            shape = RectangleShape,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = btnText)
        }
    }
}
