package com.example.dermatologyapp.ApiRequest

import android.content.Context
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.dermatologyapp.Models.Lesion
import org.json.JSONObject

class AnalysisApiConnection(val context: Context) {
    val ROOT_URL = "http://83.229.84.216:5000/analyze"

    public fun requestData(uid: String, photoUrl: String, x: Float, y: Float, bodySide: String, symptoms: List<String>, caller: String, lesionId: String, callback: VolleyAnalysisCallback) : JsonObjectRequest {
        val body = JSONObject(mapOf(
            "uid" to uid,
            "photoUrl" to photoUrl.replace("photo/", "photo%2F").replace("\\/", "/"),
            "location" to listOf(x, y),
            "bodySide" to bodySide,
            "symptoms" to symptoms,
            "caller" to caller,
            "lesionId" to lesionId
        ))
        Log.d("BODY", body.toString())
        var responseData : JSONObject

        val jor = object: JsonObjectRequest(
            Method.POST, ROOT_URL, body,
            f@{response ->
                run {
                    responseData = response
                    Log.d("GOT RESPONSE", response.toString())
                    val diagnostics = responseData.getString("diagnostics")
                    val quality = listOf<Double>(responseData.getJSONArray("quality").get(0).toString().toDouble())
                    Log.d("GOT RESPONSE", response.toString())
                    callback.onSuccess(Lesion(location = mutableListOf(x.toDouble(), y.toDouble()), bodySide = bodySide, symptoms = symptoms, diagnostics = diagnostics, quality = quality))
                }
            },
            Response.ErrorListener{ error -> run{
                Log.d("API ERROR", error.message.toString())
            }}){
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                return headers
            }
        }
        return jor
    }
}