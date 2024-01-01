package com.example.dermatologyapp.Symptoms

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.android.volley.toolbox.Volley
import com.example.dermatologyapp.ApiRequest.AnalysisApiConnection
import com.example.dermatologyapp.ApiRequest.VolleyAnalysisCallback
import com.example.dermatologyapp.Loading.LoadingScreen
import com.example.dermatologyapp.Models.Lesion

//objawy
val skinSymptoms = listOf(
    "Uwypuklenie ponad poziom skóry",
    "Krwawienie",
    "Swędzenie",
    "Owrzodzenie",
    "Obrzęk",
    "Zmienność w wyglądzie"
)
@Composable
fun SymptomItem(symptom: String, isChecked: Boolean, onSymptomSelected: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { newCheckedState ->
                    onSymptomSelected(symptom)
                },
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = symptom)
        }
    }
}

@Composable
fun SymptomsList(uid: String? = "", context: Context, viewModel: SymptomsViewModel, navController: NavController, photoUrl: String, bodySide: String, x: Float, y: Float, token: String, caller: String = "add", lesionId: String = "") {
    var isAnalyzing by remember { mutableStateOf(false)}

    if (isAnalyzing) {
        LoadingScreen()
    } else {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier.weight(1f)
            ){
                Column {
                    Text(
                        text = "Cechy znamienia",
                        style = TextStyle(fontSize = 32.sp),
                        modifier = Modifier.padding(16.dp)
                    )
                    skinSymptoms.forEach { symptom ->
                        val isChecked = viewModel.selectedSymptoms.contains(symptom)

                        SymptomItem(symptom, isChecked) { selectedSymptom ->
                            viewModel.toggleSymptom(selectedSymptom)
                        }
                    }
                }
            }
            FloatingActionButton(
                onClick = {
                    Log.d("SymptomsList", "photoUrl: $photoUrl&token=$token")
                    Log.d("SymptomsList", "x: $x")
                    Log.d("SymptomsList", "y: $y")

                    val queue = Volley.newRequestQueue(context)
                    val connection = AnalysisApiConnection(context)

                    queue.add(connection.requestData(uid = uid!!, photoUrl = "$photoUrl&token=$token", x = x, y = y, bodySide = bodySide, symptoms = viewModel.selectedSymptoms, caller = caller, lesionId = lesionId, object :
                        VolleyAnalysisCallback {
                        override fun onSuccess(lesion: Lesion) {
                            Log.d("SymptomsList", "onSuccess: $lesion")
                            navController.navigate("profile")
                        }
                    }))
                    isAnalyzing = true
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(imageVector = Icons.Default.Check, contentDescription = "Add")
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Analizuj",
                        style = TextStyle(fontSize = 22.sp),
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        }
    }
}

//-------previews------
//@Preview
//@Composable
//fun SymptomItemPreview(){
//    DermatologyAppTheme {
//        SymptomItem(isChecked = true, symptom = "Swędzenie", onSymptomSelected = {})
//    }
//}
//
//@Preview
//@Composable
//fun SymptomesListPreview(){
//    DermatologyAppTheme {
//        SymptomsList(viewModel = SymptomsViewModel())
//    }
//}
