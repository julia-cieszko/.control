package com.example.dermatologyapp.Camera

import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File

class AddPhotoViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference
    private val _photoUrl = MutableStateFlow("")
    val photoUrl = _photoUrl.asStateFlow()
    @RequiresApi(Build.VERSION_CODES.P)
    fun uploadPhoto(file: File) {
        viewModelScope.launch {
            val bmp = ImageDecoder.decodeBitmap(ImageDecoder.createSource(file))
            val imageRef = storageRef.child("photos/${System.currentTimeMillis()}.jpg")
            val baos = ByteArrayOutputStream()
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            val task = imageRef.putBytes(data)
            task
                .addOnFailureListener{ exception ->
                    Log.d("BITMAP_EXCEPTION", "exception: $exception")
                }
                .addOnSuccessListener { result ->
                    imageRef.downloadUrl.addOnSuccessListener { result1 ->
                        _photoUrl.value = result1.toString()
                        Log.d("BITMAP", "result: $result1")
                    }
                }
        }
    }
}