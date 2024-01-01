package com.example.dermatologyapp.Profile

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dermatologyapp.Lesions.LesionScreen
import com.example.dermatologyapp.Models.Lesion
import com.example.dermatologyapp.Models.User
import com.example.dermatologyapp.R
import com.example.dermatologyapp.ui.theme.DermatologyAppTheme
import java.util.Locale


//mole place ilustration
@Composable
fun MolePlacePicture(
    @DrawableRes drawable: Int
){
    Image(
        painter = painterResource(id = drawable),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(64.dp)
    )
}

//mole element
@Composable
fun MoleElement(
    @DrawableRes drawable: Int,
    locationString: String,
    onLesionClick: () -> Unit,
    timestamp: String
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onLesionClick() },
        verticalAlignment = Alignment.CenterVertically
    ){
        MolePlacePicture(drawable = drawable)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = locationString.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                fontSize = 16.sp
            )
            Text(
                text = timestamp,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(Color.Gray)
    )
}

fun locationToString(location: List<Double>, bodySide: String): String {
    var locationName = "zaznaczenie poza ciałem"
    for ((k, v) in BODY_MAP_COORDINATES) {
        if (location[0] >= v[0].x && location[0] <= v[1].x && location[1] >= v[0].y && location[1] <= v[1].y) {
            locationName = k
        }
    }

    if (locationName.contains("noga") || locationName.contains("ramię") || locationName.contains("głowa")) {
        return locationName
    } else if (locationName.contains("tułów")) {
        return if (bodySide == "front") "brzuch" else "plecy"
    } else {
        return if (bodySide == "front") "szyja" else "kark"
    }
}

fun locationStringToDrawable(locationString: String) : Int {
    return when (locationString) {
        "plecy" -> R.drawable.back_mole_place
        "szyja" -> R.drawable.neck_mole_place
        "kark" -> R.drawable.neck_mole_place
        "lewe ramię" -> R.drawable.arm_mole_place
        "prawe ramię" -> R.drawable.arm_mole_place
        "głowa" -> R.drawable.head
        "lewa noga" -> R.drawable.leg
        "prawa noga" -> R.drawable.leg
        "tułów" -> R.drawable.chest_mole_place
        else -> R.drawable.arm_mole_place
    }
}

//list of mole elements in a column
@Composable
fun MolesColumn(
    modifier: Modifier = Modifier,
    onLesionClick: () -> Unit,
    lesions: List<Lesion> = listOf(),
    addNewPhoto: (Any?) -> Unit,
    gender: String = ""
){

    var selectedLesion by remember {
        mutableStateOf<Lesion?>(null)
    }

    var displayLesionInfo by remember { mutableStateOf(false)}

    var selectedLestionLocation by remember {
        mutableStateOf("")
    }

    if (displayLesionInfo) {
        LesionScreen(
            onImagePreview = {},
            lesions = selectedLesion!!,
            diagnosis = "",
            localization = selectedLestionLocation,
            symptoms = listOf(),
            nullifySelectedLesion = {
                displayLesionInfo = false
            },
            addNewPhoto = addNewPhoto
        )
    } else {
        LazyColumn(
            modifier = Modifier.height(500.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            items(lesions) { item ->
                val location = getBodyPart(Offset((item.location?.get(0)?.toFloat() ?: 0f) - 45f, item.location?.get(1)?.toFloat() ?: 0f), bodySide = item.bodySide?:"", gender = gender, caller = "view")
                MoleElement(
                    drawable = locationStringToDrawable(location!!),
                    onLesionClick = {
                        selectedLesion = item
                        selectedLestionLocation = location
                        displayLesionInfo = true
                    },
                    locationString = location,
                    timestamp = item.photosTimestamps!![item.photosTimestamps.size - 1].toDate().toString()
                )
            }
        }
    }
}

//---------------------data---------------------
data class DrawableStrings(
    @DrawableRes val drawable: Int,
    @StringRes val text1: Int,
    @StringRes val text2: Int
)

val molesData = listOf(
    Triple(R.drawable.arm_mole_place, R.string.arm_mole_place, R.string.status_safe),
    Triple(R.drawable.back_mole_place, R.string.back_mole_place, R.string.status_not_analyzed),
    Triple(R.drawable.chest_mole_place, R.string.chest_mole_place, R.string.status_suspicious),
    Triple(R.drawable.neck_mole_place, R.string.neck_mole_place, R.string.status_not_analyzed)
).map { DrawableStrings(it.first, it.second, it.third) }

//---------------------previews---------------------
//mole place ilustration preview
@Preview(showBackground = true)
@Composable
fun MolePlacePicturePreview(){
    DermatologyAppTheme {
        MolePlacePicture(drawable = R.drawable.arm_mole_place)
    }
}

//mole element preview
//@Preview(showBackground = true)
//@Composable
//fun MoleElementPreview(){
//    DermatologyAppTheme {
//        MoleElement(
//            drawable = R.drawable.arm_mole_place,
//            onLesionClick = {}
//        )
//    }
//}

//moles list preview
//@Preview(showBackground = true)
//@Composable
//fun MolesColumnPreview(){
//    DermatologyAppTheme {
//        MolesColumn(onLesionClick = {})
//    }
//}