package com.example.dermatologyapp.Camera

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Space
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberAsyncImagePainter
import com.android.volley.toolbox.Volley
import com.example.dermatologyapp.ApiRequest.BlurApiConnection
import com.example.dermatologyapp.ApiRequest.VolleyCallback
import com.example.dermatologyapp.Loading.LoadingScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@RequiresApi(Build.VERSION_CODES.P)
@ExperimentalCoilApi
@ExperimentalCoroutinesApi
@ExperimentalPermissionsApi
@Composable
fun CameraStart(modifier: Modifier = Modifier, context: Context, navController: NavController, caller: String = "add", lesionId: String = "") {
    val emptyImageUri = Uri.parse("file://dev/null")
    var imageUri by remember { mutableStateOf(emptyImageUri) }
    var isBlurred by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var isUploading by remember { mutableStateOf(false) }
    val viewModel: AddPhotoViewModel = viewModel()
    val photoUrl by viewModel.photoUrl.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = photoUrl) {
        if (photoUrl != "") {
            navController.navigate("lesion_analysis_body_map?photoUrl=$photoUrl&caller=$caller&lesionId=$lesionId")
        }
    }

    if (imageUri != emptyImageUri && !isUploading) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 16.dp),
                painter = rememberAsyncImagePainter(imageUri),
                contentDescription = "Captured image",
            )
            Spacer(modifier = Modifier.height(16.dp))
            if(isBlurred){
                Text(
                    text = "Wykryto rozmycie zdjęcia. Może to wpłynąć na wynik analizy.",
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(all=16.dp)
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                modifier = Modifier.width(200.dp),
                onClick = {
                    coroutineScope.launch {
                        withContext(Dispatchers.IO) {
                            viewModel.uploadPhoto(File(imageUri.path!!))
                            isUploading = true
                        }
                    }
                }
            ) {
                Text("Dalej")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                modifier = Modifier.width(200.dp),
                onClick = { imageUri = emptyImageUri }
            ) {
                Text("Wykonaj ponownie")
            }
        }
    } else if (isLoading || isUploading){
        LoadingScreen()
    } else {
        CameraCapture(
            modifier = modifier,
            setLoading = {
                isLoading = true
            },
            onImageFile = { file ->
                imageUri = file.toUri()
                val queue = Volley.newRequestQueue(context)
                val connection = BlurApiConnection(context)

                queue.add(connection.requestData(imageUri, object: VolleyCallback {
                    override fun onSuccess(result: String) {
                        val strSplit = result.split(":")[1]
                        val strClean = strSplit.replace("\"","").replace("}","")
                        val resultDbl = strClean.toDouble()
                        Log.d("DOUBLE_TEST", "test: ${strClean.toDouble()}")
                        isBlurred = resultDbl < 200
                        Log.d("GOT RESPONSE2", resultDbl.toString())
                    }
                }))
                isLoading = false
            }
        )
    }
}