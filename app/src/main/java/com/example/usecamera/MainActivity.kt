package com.example.usecamera

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.activity.enableEdgeToEdge
import com.example.usecamera.ui.theme.UseCameraTheme
import com.example.usecamera.ui.views.CameraPreiewScreen

class MainActivity : ComponentActivity() {
    private val cameraPermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestPermission()){
                isGranted ->
            if (isGranted){
                setCameraPreview() //se encarga de manipular la vista
            }else{

            }
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        when(PackageManager.PERMISSION_GRANTED){ //si el usuario da los permisos funciona
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            )->{
                setCameraPreview()
            }
            else->{//si no manda el cuadro de diálogo
                cameraPermissionRequest.launch(Manifest.permission.CAMERA)
            }
        }

    }

    private fun setCameraPreview(){
        enableEdgeToEdge()
        setContent{
            UseCameraTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding->
                    CameraPreiewScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }

}



