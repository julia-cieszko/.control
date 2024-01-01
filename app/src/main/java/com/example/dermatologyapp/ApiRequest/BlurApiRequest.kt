package com.example.dermatologyapp.ApiRequest

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.net.URI

@Suppress("DEPRECATION")
class BlurApiConnection(val context : Context) {
    val ROOT_URL = "https://blur-api.onrender.com/api/blur"

    public fun requestData(fileURI : Uri, callback: VolleyCallback) : JsonObjectRequest {
        val bmp : Bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, fileURI)
        val body = JSONObject(mapOf("image" to encodeImage(bmp)))
        var responseData : JSONObject

        val jor = object: JsonObjectRequest(Method.POST, ROOT_URL, body,
            f@{response ->
                run {
                    Log.d("GOT RESPONSE", response.toString())
                    callback.onSuccess(response.toString())
                }
            },
            Response.ErrorListener{error -> run{
                Log.d("API ERROR", error.message.toString())
            }}){
        }
        return jor
    }

    private fun encodeImage(bm: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

}
