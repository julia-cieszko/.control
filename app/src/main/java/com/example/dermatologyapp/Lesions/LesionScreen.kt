package com.example.dermatologyapp.Lesions

import android.provider.ContactsContract.DisplayPhoto
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.dermatologyapp.Models.Lesion
import com.example.dermatologyapp.R
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.dermatologyapp.ui.theme.DermatologyAppTheme
import kotlinx.coroutines.launch

//pojedyncze zdjecie
@Composable
fun LesionPhoto(
    source: String = "",
    onImagePreview: (String) -> Unit
) {
    AsyncImage(
        model = source,
        placeholder = painterResource(id = R.drawable.arm_mole_place),
        contentDescription = null,
        modifier = Modifier.clickable { onImagePreview(source) }
    )
}

//galeryjka złozona z kilku zdjec
@Composable
fun LesionsGallery(
    onImagePreview: (String) -> Unit,
    imgUrls: MutableList<String>
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
        contentPadding = PaddingValues(horizontal = 8.dp),
        modifier = Modifier.height(400.dp)
    ) {
        items(imgUrls) {
            LesionPhoto(source = it, onImagePreview = onImagePreview)
        }
    }
}

//Informacje
@Composable
fun LesionInfo(
    diagnosis: String,
    localization: String,
    symptoms: List<String>
) {
    Column() {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Wynik analizy: ",
                fontSize = 16.sp
            )
            Text(
                text = if(diagnosis == "melanoma") "czerniak" else "nie wykryto czerniaka",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        if(diagnosis == "melanoma") {
            Text(
                text = "Pilnie skonsultuj się z lekarzem!",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Lokalizacja: ",
                fontSize = 16.sp
            )
            Text(
                text = localization,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Objawy: ",
            fontSize = 16.sp
        )
        if(symptoms.isEmpty()){
            Text(
                text = "brak",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        } else {
            symptoms.forEach { symptom ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Filled.Circle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(8.dp)
                    )
                    Text(
                        text = symptom,
                        fontSize = 16.sp,
                        lineHeight = 20.sp
                    )
                }
            }
        }
    }
}

//caly ekran
@Composable
fun LesionScreen(
    onImagePreview: () -> Unit,
    lesions: Lesion,
    diagnosis: String,
    localization: String,
    symptoms: List<String>,
    nullifySelectedLesion: () -> Unit,
    addNewPhoto: (Any?) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    var selectedImageUrl by remember {
        mutableStateOf(lesions.photoUrl!![lesions.photoUrl.size - 1])
    }

    var selectedImageTimestamp by remember {
        mutableStateOf(lesions.photosTimestamps!![lesions.photosTimestamps.size - 1].toDate().toString())
    }

    BackHandler(true) {
        println("back pressed")
        nullifySelectedLesion()
    }

    Column(
        modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState()),
    ) {
        AsyncImage(model = selectedImageUrl, contentDescription = "Lesion image", modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.CenterHorizontally))
        Text(
            text = selectedImageTimestamp,
            fontSize = 12.sp,
            color = Color.Gray
        )
        LesionInfo(
            diagnosis = lesions.diagnostics!!,
            localization = localization,
            symptoms = lesions.symptoms
        )
        Button(
            onClick = {
                Log.d("LesionScreen", "addNewPhoto: ${lesions.id}")
                addNewPhoto(lesions.id) },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .width(300.dp)
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp, bottom = 16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = null
                )
                Text(
                    text = "Dodaj kolejne zdjęcie"
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        LesionsGallery(
            onImagePreview = {
                selectedImageUrl = it
                selectedImageTimestamp = lesions.photosTimestamps!![lesions.photoUrl!!.indexOf(it)].toDate().toString()
            },
            imgUrls = lesions.photoUrl!!
        )
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Preview
@Composable
fun LesionInfoPreview1() {
    DermatologyAppTheme {
        LesionInfo(
            diagnosis = "czerniak",
            localization = "lewa noga",
            symptoms = listOf("swędzenie", "krwawienie", "owrzodzenie")
        )
    }
}

@Preview
@Composable
fun LesionInfoPreview2() {
    DermatologyAppTheme {
        LesionInfo(
            diagnosis = "nie wykryto czerniaka",
            localization = "lewa noga",
            symptoms = listOf("swędzenie", "krwawienie", "owrzodzenie")
        )
    }
}

@Preview
@Composable
fun LesionInfoPreview3() {
    DermatologyAppTheme {
        LesionInfo(
            diagnosis = "nie wykryto czerniaka",
            localization = "lewa noga",
            symptoms = listOf()
        )
    }
}