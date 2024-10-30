package com.example.usecamera.ui.views

import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import android.support.v4.os.ResultReceiver
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.video.MediaSpec
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun CameraPreiewScreen(modifier: Modifier =Modifier){
    val lensFacing = CameraSelector.LENS_FACING_FRONT
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val preview =Preview.Builder().build()

    val previewView = remember {
        PreviewView(context)
    }
    val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()
    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview)
        preview.setSurfaceProvider(previewView.surfaceProvider)

    }
    Box(contentAlignment = Alignment.BottomCenter,modifier = modifier.fillMaxSize()){
        AndroidView(factory = {previewView}, modifier = Modifier.fillMaxSize())

        Button(onClick = { captureImgage(imageCapture =, context)}) {

            Text(text = "Tomar foto")
        }
    }
}

private suspend fun Context.getCameraProvider():ProcessCameraProvider =
    suspendCoroutine { continuation ->
        ProcessCameraProvider.getInstance(this)
            .also { cameraProvider ->
                cameraProvider.addListener({
                    continuation.resume(cameraProvider.get())
                }, ContextCompat.getMainExecutor(this))
            }

    }

private fun captureImgage(imageCapture: ImageCapture, context: Context){
    val name = "CameraXimage.jpeg"
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,name)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jeg")
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.P){
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
        }

    }
    val outpuOptions = ImageCapture.OutputFileOptions.Builder(context.contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues).build()
     imageCapture.takePicture(
        outpuOptions, ContextCompat.getMainExecutor(context),
         object : ImageCapture.OnImageSavedCallback{
             override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                 Toast.makeText(context, "Fotografía tomada",Toast.LENGTH_LONG).show()
             }

             override fun onError(exception: ImageCaptureException) {
                 Toast.makeText(context, "Error ${exception}",Toast.LENGTH_LONG).show()
             }
         }

    )

}