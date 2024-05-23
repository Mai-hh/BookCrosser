package com.huaihao.bookcrosser.ui.main.requests

import android.Manifest
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material.icons.rounded.Upload
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.huaihao.bookcrosser.R
import com.huaihao.bookcrosser.ui.common.LimitedOutlinedTextField
import com.huaihao.bookcrosser.ui.common.UploadImageDialog
import com.huaihao.bookcrosser.ui.theme.BookCrosserTheme
import com.huaihao.bookcrosser.util.hasLocationPermission
import com.huaihao.bookcrosser.viewmodel.main.ShelfABookEvent
import com.huaihao.bookcrosser.viewmodel.main.ShelfABookUiState
import java.util.concurrent.Executors


const val exampleImageAddress =
    "https://i.pinimg.com/564x/cc/e4/ef/cce4ef60f77a3cf3b36f5b9897ae378d.jpg"

@androidx.annotation.OptIn(ExperimentalGetImage::class)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ShelfABookScreen(uiState: ShelfABookUiState, onEvent: (ShelfABookEvent) -> Unit) {

    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    val options = remember {
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(
                Barcode.FORMAT_EAN_13,
                Barcode.FORMAT_QR_CODE,
            )
            .enableAllPotentialBarcodes()
            .build()
    }

    val barcodeScanner = remember {
        BarcodeScanning.getClient(options)
    }

    var text by rememberSaveable {
        mutableStateOf("")
    }

    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA
        )
    )

    val controller = remember {
        LifecycleCameraController(context.applicationContext).apply {
            setEnabledUseCases(CameraController.IMAGE_CAPTURE or CameraController.VIDEO_CAPTURE)
            setImageAnalysisAnalyzer(
                context.mainExecutor
            ) { imageProxy ->
                imageProxy.image?.let { image ->
                    val img = InputImage.fromMediaImage(
                        image,
                        imageProxy.imageInfo.rotationDegrees
                    )

                    barcodeScanner.process(img).addOnCompleteListener { task ->
                        text = if (!task.isSuccessful) task.exception!!.localizedMessage.toString()
                        else task.result?.firstOrNull()?.rawValue ?: "No barcode found"
                        Log.d("Barcode", "barcode: $text")
                        clearImageAnalysisAnalyzer()
                        imageProxy.close()
                    }
                }

            }
        }
    }

    LaunchedEffect(!context.hasLocationPermission()) {
        permissionState.launchMultiplePermissionRequest()
    }

    if (uiState.showUploadDialog) {
        UploadImageDialog(
            onDismiss = {
                onEvent(ShelfABookEvent.DismissUploadCoverDialog)
            },
            onConfirm = { coverUrl ->
                onEvent(ShelfABookEvent.CoverUrlChange(coverUrl))
                onEvent(ShelfABookEvent.DismissUploadCoverDialog)
            },
            imageUrl = uiState.coverUrl,
            imageDescription = "封面"
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {

        if (uiState.showScanningBarcode) {
            Box(modifier = Modifier.fillMaxSize()) {
                CameraScreen(onEvent = onEvent)
            }
        } else {
            Column(
                Modifier
                    .fillMaxSize(),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    ConstraintLayout(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        val (cover, action) = createRefs()
                        OutlinedCard(modifier = Modifier
                            .constrainAs(cover) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                            }
                            .fillMaxSize()) {

                            uiState.coverUrl.ifBlank {
                                Image(
                                    painter = painterResource(id = R.mipmap.bc_logo_foreground),
                                    contentDescription = "封面",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                                return@OutlinedCard
                            }

                            AsyncImage(
                                model = uiState.coverUrl,
                                placeholder = painterResource(id = R.mipmap.bc_logo_foreground),
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        FilterChip(
                            selected = true,
                            onClick = { onEvent(ShelfABookEvent.ShowUploadCoverDialog) },
                            label = {
                                Text(text = "上传封面")
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Upload,
                                    contentDescription = "上传封面",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            modifier = Modifier.constrainAs(action) {
                                bottom.linkTo(cover.bottom)
                                end.linkTo(cover.end, margin = 8.dp)
                            }
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(2f)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp)
                            .padding(bottom = 8.dp)
                            .fillMaxSize()
                    ) {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                        ) {
                            Text(
                                text = "书籍信息",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Column(Modifier.padding(vertical = 8.dp)) {
                                LimitedOutlinedTextField(
                                    label = "书名",
                                    value = uiState.title,
                                    onValueChange = { onEvent(ShelfABookEvent.TitleChange(it)) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                LimitedOutlinedTextField(
                                    label = "作者",
                                    value = uiState.author,
                                    onValueChange = { onEvent(ShelfABookEvent.AuthorChange(it)) },
                                    modifier = Modifier.fillMaxWidth()
                                )

                                Box(modifier = Modifier.fillMaxWidth()) {
                                    LimitedOutlinedTextField(
                                        label = "ISBN",
                                        value = uiState.isbn,
                                        isError = uiState.isbnError != null,
                                        supportingText = {
                                            uiState.isbnError?.let {
                                                Text(text = it, color = MaterialTheme.colorScheme.error)
                                            }
                                        },
                                        onValueChange = { onEvent(ShelfABookEvent.IsbnChange(it)) },
                                        modifier = Modifier.fillMaxWidth()
                                    )

                                    IconButton(
                                        onClick = { onEvent(ShelfABookEvent.ShowScanning) },
                                        modifier = Modifier.align(Alignment.CenterEnd)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.CameraAlt,
                                            contentDescription = null)
                                    }
                                }

                                LimitedOutlinedTextField(
                                    label = "简介（可选）",
                                    maxLength = 100,
                                    maxLines = 4,
                                    singleLine = false,
                                    value = uiState.description,
                                    onValueChange = { onEvent(ShelfABookEvent.DescriptionChange(it)) },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "位置信息",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                )

                                Spacer(modifier = Modifier.width(16.dp))

                                AssistChip(
                                    onClick = {
                                        onEvent(ShelfABookEvent.GetCurrentLocation)
                                    },
                                    label = {
                                        Text(text = "获取当前位置")
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Rounded.LocationOn,
                                            contentDescription = "获取当前位置",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                )
                            }

                            Row(Modifier.padding(vertical = 8.dp)) {
                                OutlinedTextField(
                                    label = {
                                        Text("经度")
                                    },
                                    value = if (uiState.location != null) uiState.location!!.longitude.toString() else "",
                                    onValueChange = { },
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.weight(0.1f))
                                OutlinedTextField(
                                    label = {
                                        Text("纬度")
                                    },
                                    value = if (uiState.location != null) uiState.location!!.latitude.toString() else "",
                                    onValueChange = { },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Button(onClick = {
                                onEvent(ShelfABookEvent.ShelfBook)
                            }, Modifier.fillMaxWidth()) {
                                Text(text = "图书上架")
                            }
                        }
                    }
                }
            }
        }
    }

}

@androidx.annotation.OptIn(ExperimentalGetImage::class)
@Composable
fun CameraScreen(onEvent: (ShelfABookEvent) -> Unit = {}) {
    val context = LocalContext.current
    val previewView: PreviewView = remember { PreviewView(context) }
    val cameraController = remember { LifecycleCameraController(context) }
    val lifecycleOwner = LocalLifecycleOwner.current
    cameraController.bindToLifecycle(lifecycleOwner)
    cameraController.cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
    previewView.controller = cameraController

    val executor = remember { Executors.newSingleThreadExecutor() }
    val textRecognizer = remember { BarcodeScanning.getClient(BarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_EAN_13).build()) }

    var text by rememberSaveable {
        mutableStateOf("")
    }
    var isLoading by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

        Row(modifier = Modifier
            .align(Alignment.BottomCenter)
            .padding(16.dp)
        ) {
            IconButton(onClick = { onEvent(ShelfABookEvent.CloseScanning) }) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(54.dp)
                )
            }

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                IconButton(
                    onClick = {
                        isLoading = true
                        cameraController.setImageAnalysisAnalyzer(executor) { imageProxy ->
                            imageProxy.image?.let { image ->
                                val img = InputImage.fromMediaImage(
                                    image,
                                    imageProxy.imageInfo.rotationDegrees
                                )

                                textRecognizer.process(img).addOnCompleteListener { task ->
                                    isLoading = false
                                    text = if (!task.isSuccessful) task.exception!!.localizedMessage.toString()
                                    else task.result.firstOrNull()?.rawValue ?: "找不到有效ISBN条形码"

                                    cameraController.clearImageAnalysisAnalyzer()
                                    imageProxy.close()
                                }
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Rounded.RestartAlt,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(54.dp)
                    )
                }
            }
        }
    }

    if (text.isNotEmpty()) {
        Dialog(onDismissRequest = { text = "" }) {
            Card(modifier = Modifier.fillMaxWidth(0.8f)) {
                Text(
                    text = "ISBN：$text",
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp),
                    style = MaterialTheme.typography.bodySmall
                )
                Button(
                    onClick = {
                         onEvent(ShelfABookEvent.DoneScanning(text))
                    },

                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Text(text = "完成")
                }
            }
        }
    }
}

@Composable
fun CameraPreview(
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        factory = { it ->
            PreviewView(it).apply {
                this.controller = controller
                controller.bindToLifecycle(lifecycleOwner)
            }
        },
        modifier = modifier
    )
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ShelfABookScreenPreview() {
    BookCrosserTheme {
        ShelfABookScreen(
            ShelfABookUiState(
                coverUrl = exampleImageAddress,
                title = "哈里波特",
                author = "J.K.罗琳",
                isbn = "9787530218562",
                description = "哈利波特是一部由英国作家J.K.罗琳创作的奇幻小说，讲述了一个孤儿哈利波特在霍格沃茨魔法学校的冒险故事。"
            ),
            onEvent = {}
        )
    }
}
