package com.example.dermatologyapp.Profile

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SwapHorizontalCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.dermatologyapp.Lesions.LesionScreen
import com.example.dermatologyapp.Models.Lesion
import com.example.dermatologyapp.Models.User
import com.example.dermatologyapp.R
import com.example.dermatologyapp.ui.theme.DermatologyAppTheme

// (lewy górny, prawy dolny)
val BODY_MAP_COORDINATES = hashMapOf<String, List<Offset>>( // front też, jakaś bardziej symetryczna jest xD
    "głowa" to listOf(Offset(492.6F, 12.8F), Offset(575.4F, 12.8F), Offset(607.4F, 93.8F), Offset(570.5F, 159.7F), Offset(500.5F, 159.7F), Offset(462.6F, 93.8F)),
    "szyja" to listOf(Offset(570.5F, 159.7F), Offset(500.5F, 159.7F), Offset(607.4F, 220.8F), Offset(462.6F, 220.8F)),
    "prawe ramię" to listOf(Offset(415.6F, 245.8F), Offset(462.6F, 220.8F), Offset(450.6F, 295.7F), Offset(422.6F, 675.6F), Offset(358.7F, 630.6F)),
    "lewe ramię" to listOf(Offset(607.4F, 220.8F), Offset(640.4F, 245.8F), Offset(630.4F, 675.6F), Offset(699.6F, 630.6F), Offset(617.4F, 295.7F)),
    "tułów" to listOf(Offset(462.6F, 220.8F), Offset(607.4F, 220.8F), Offset(617.4F, 295.7F), Offset(620.4F, 522.7F), Offset(440.6F, 522.7F), Offset(450.6F, 295.7F)),
    "prawa noga" to listOf(Offset(440.6F, 522.7F), Offset(530.5F, 522.7F), Offset(530.5F, 1100.6F), Offset(440.6F, 1100.6F)),
    "lewa noga" to listOf(Offset(530.5F, 522.7F), Offset(620.3F, 522.7F), Offset(620.4F, 1100.6F), Offset(530.7F, 1100.6F))
)

fun getBodyPart(location: Offset, gender: String, bodySide: String, caller: String = "add"): String {
    Log.d("GET BODY PART", "loc: $location, gender: $gender, bodySide: $bodySide, caller: $caller")
    val maleFrontPolygons = hashMapOf<String, List<Offset>>(
        "głowa" to listOf(Offset(492.6F, 12.8F), Offset(575.4F, 12.8F), Offset(607.4F, 93.8F), Offset(570.5F, 164.7F), Offset(500.5F, 164.7F), Offset(462.6F, 93.8F)),
        "szyja" to listOf(Offset(570.5F, 164.7F), Offset(500.5F, 164.7F), Offset(607.4F, 205.8F), Offset(462.6F, 205.8F)),
        "prawe ramię" to listOf(Offset(415.6F, 230.8F), Offset(462.6F, 205.8F), Offset(450.6F, 315.7F), Offset(422.6F, 645.6F), Offset(358.7F, 630.6F)),
        "lewe ramię" to listOf(Offset(607.4F, 205.8F), Offset(640.4F, 230.8F), Offset(650.4F, 645.6F), Offset(709.6F, 630.6F), Offset(617.4F, 315.7F)),
        "tułów" to listOf(Offset(462.6F, 205.8F), Offset(607.4F, 205.8F), Offset(617.4F, 315.7F), Offset(615.4F, 522.7F), Offset(450.6F, 522.7F), Offset(450.6F, 315.7F)),
        "prawa noga" to listOf(Offset(450.6F, 522.7F), Offset(530.5F, 522.7F), Offset(530.5F, 1100.6F), Offset(450.6F, 1100.6F)),
        "lewa noga" to listOf(Offset(530.5F, 522.7F), Offset(610.3F, 522.7F), Offset(610.4F, 1100.6F), Offset(530.7F, 1100.6F))
    )

    val maleBackPolygons = hashMapOf<String, List<Offset>>(
        "głowa" to listOf(Offset(482.6F, 12.8F), Offset(565.4F, 12.8F), Offset(597.4F, 93.8F), Offset(560.5F, 154.7F), Offset(490.5F, 154.7F), Offset(452.6F, 93.8F)),
        "szyja" to listOf(Offset(560.5F, 154.7F), Offset(490.5F, 154.7F), Offset(597.4F, 205.8F), Offset(452.6F, 205.8F)),
        "prawe ramię" to listOf(Offset(410.6F, 225.8F), Offset(452.6F, 205.8F), Offset(445.6F, 325.7F), Offset(405.6F, 640.6F), Offset(341.7F, 615.6F)),
        "lewe ramię" to listOf(Offset(597.4F, 205.8F), Offset(645.4F, 225.8F), Offset(650.4F, 645.6F), Offset(709.6F, 630.6F), Offset(600.4F, 325.7F)),
        "tułów" to listOf(Offset(452.6F, 205.8F), Offset(597.4F, 205.8F), Offset(600.4F, 325.7F), Offset(620.4F, 522.7F), Offset(440.6F, 522.7F), Offset(445.6F, 325.7F)),
        "prawa noga" to listOf(Offset(440.6F, 522.7F), Offset(525.5F, 522.7F), Offset(525.5F, 1100.6F), Offset(440.6F, 1100.6F)),
        "lewa noga" to listOf(Offset(525.5F, 522.7F), Offset(620.3F, 522.7F), Offset(620.4F, 1100.6F), Offset(525.7F, 1100.6F))
    )

    val femaleBackPolygons = hashMapOf<String, List<Offset>>( // front też, jakaś bardziej symetryczna jest xD
        "głowa" to listOf(Offset(492.6F, 12.8F), Offset(575.4F, 12.8F), Offset(607.4F, 93.8F), Offset(570.5F, 159.7F), Offset(500.5F, 159.7F), Offset(462.6F, 93.8F)),
        "szyja" to listOf(Offset(570.5F, 159.7F), Offset(500.5F, 159.7F), Offset(607.4F, 220.8F), Offset(462.6F, 220.8F)),
        "prawe ramię" to listOf(Offset(415.6F, 245.8F), Offset(462.6F, 220.8F), Offset(450.6F, 295.7F), Offset(422.6F, 675.6F), Offset(358.7F, 630.6F)),
        "lewe ramię" to listOf(Offset(607.4F, 220.8F), Offset(640.4F, 245.8F), Offset(630.4F, 675.6F), Offset(699.6F, 630.6F), Offset(617.4F, 295.7F)),
        "tułów" to listOf(Offset(462.6F, 220.8F), Offset(607.4F, 220.8F), Offset(617.4F, 295.7F), Offset(620.4F, 522.7F), Offset(440.6F, 522.7F), Offset(450.6F, 295.7F)),
        "prawa noga" to listOf(Offset(440.6F, 522.7F), Offset(530.5F, 522.7F), Offset(530.5F, 1100.6F), Offset(440.6F, 1100.6F)),
        "lewa noga" to listOf(Offset(530.5F, 522.7F), Offset(620.3F, 522.7F), Offset(620.4F, 1100.6F), Offset(530.7F, 1100.6F))
    )

    val maleFrontPolygonsSelection = hashMapOf<String, List<Offset>>()

    for ((k, v) in maleFrontPolygons) {
        val p = mutableListOf<Offset>()
        for (point in v) {
            p.add(Offset(point.x + 45f, point.y))
        }
        maleFrontPolygonsSelection[k] = p
    }

    val maleBackPolygonsSelection = hashMapOf<String, List<Offset>>()

    for ((k, v) in maleBackPolygons) {
        val p = mutableListOf<Offset>()
        for (point in v) {
            p.add(Offset(point.x + 45f, point.y))
        }
        maleBackPolygonsSelection[k] = p
    }

    val femalePolygonsSelection = hashMapOf<String, List<Offset>>()

    for ((k, v) in femaleBackPolygons) {
        val p = mutableListOf<Offset>()
        for (point in v) {
            p.add(Offset(point.x + 45f, point.y))
        }
        femalePolygonsSelection[k] = p
    }

    val polygons = if(caller == "add") (if(gender == "kobieta") femalePolygonsSelection else if (bodySide == "Przód") maleFrontPolygonsSelection else maleBackPolygonsSelection) else (if(gender == "kobieta") femaleBackPolygons else if (bodySide == "Przód") maleFrontPolygons else maleBackPolygons)

    for ((part, vertices) in polygons) {
        if (polygonContainsPoint(vertices, location)) {
            return part
        }
    }
    return "zaznaczenie poza ciałem"
}

fun polygonContainsPoint(vertices: List<Offset>, point: Offset): Boolean {
    var crossings = 0
    for (i in vertices.indices) {
        val a = vertices[i]
        val j = if (i + 1 == vertices.size) 0 else i + 1
        val b = vertices[j]
        if (segmentCrossed(point, a, b)) crossings++
    }
    return crossings % 2 != 0
}

fun segmentCrossed(point: Offset, a: Offset, b: Offset) : Boolean{
    var px = point.x; var py = point.y; var ax = a.x; var ay = a.y; var bx = b.x; var by = b.y
    if (ay > by) {
        ax = b.x; ay = b.y; bx = a.x; by = a.y
    }

    if (py == ay || py == by) py += 0.0001f
    if (py > by || py < ay || px > Math.max(ax, bx)) {
        return false
    } else if (px < Math.min(ax, bx)) {
        return true
    } else {
        val red = (if (ax != bx) (by - ay) / (bx - ax) else Float.MAX_VALUE)
        val blue = (if (ax != px) (py - ay) / (px - ax) else Float.MAX_VALUE)
        return blue >= red
    }
}

//funkcja do samego obrazka czlowieczka
//@Composable
//fun BodyImage(
//    modifier: Modifier = Modifier,
//    @DrawableRes drawable: Int
//){
//    val image = ImageVector.vectorResource(id = drawable)
//    val painter = rememberVectorPainter(image = image)
//
//    Surface(
//        modifier = Modifier.height(600.dp)
//    ) {
//        Canvas(
//            modifier = Modifier
//                .fillMaxSize()
//                .pointerInput(Unit) {
//                    detectTapGestures(
//                        onTap = { tapOffset -> Log.d("LOCATION", tapOffset.toString()) }
//                    )
//                }
//
//        ) {
//            with(painter) {
//                draw(painter.intrinsicSize)
//            }
//        }
//    }
//}
@Composable
fun BodyImage(
    modifier: Modifier = Modifier,
    location: Offset,
    locationName: String = "zaznaczenie poza ciałem",
    onLocationChange: (Offset) -> Unit,
    @DrawableRes drawable: Int
){
    val altPainter = painterResource(id = drawable)

    Surface(
        modifier = Modifier.height(420.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { tapOffset ->
                            Log.d("LOCATION", tapOffset.toString())
                            onLocationChange(tapOffset)
                        }
                    )
                }

        ) {
            with(altPainter) {
                val translationX = (size.width - intrinsicSize.width) / 2 * 1.4f
                val translationY = (size.height - intrinsicSize.height) / 1.25f*0.01f

                Log.d(translationX.toString(), translationY.toString())

                translate(translationX, translationY) {
                    draw(Size((intrinsicSize.width / 1.4).toFloat(), (intrinsicSize.height / 1.4).toFloat()))
                }
            }
            for ((k, v) in BODY_MAP_COORDINATES) {
                val color = listOf(Color.Red, Color.Blue, Color.Green, Color.Cyan, Color.Magenta, Color.Yellow).random()
                for (point in v) {
                    drawCircle(color, radius = 3f, center = Offset(point.x + 45f, point.y))
                }

            }
            if (location != Offset.Unspecified) {
                drawCircle(if (locationName != "zaznaczenie poza ciałem") Color.Red else Color.Gray, radius = 10f, center = location)
            }
        }
    }
}

//przycisk odwracajacy
@Composable
fun FlipButton(
    onFlip: () -> Unit = {},
    modifier: Modifier = Modifier
){
    FilledTonalButton(
        onClick = { onFlip() },
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
    ) {
        Row(
            modifier = modifier.width(100.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ){
            Icon(
                imageVector = Icons.Filled.SwapHorizontalCircle,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Odwróć",
                style = TextStyle(fontSize = 16.sp),
            )
        }
    }
}

//calosc ekranu
@Composable
fun BodyMap(
    gender: String = "kobieta",
    modifier: Modifier = Modifier,
    photoUrl: String = "",
    token: String= "",
    navController: NavController,
    caller: String = "add",
    lesionId: String = ""
) {
    val bodySide = "Front"
    val bodyFrame = "Feminine"

    var spotLocation by remember {mutableStateOf(Offset.Unspecified)}
    var locationName by remember {
        mutableStateOf("zaznaczenie poza ciałem")
    }

    var isFront by remember { mutableStateOf(true) }
    val onFlipClick: () -> Unit = {
        isFront = !isFront
        spotLocation = Offset.Unspecified
    }

    val onLocationChange: (Offset) -> Unit = f@{
        spotLocation = it
        locationName = getBodyPart(it, gender, if (isFront) "Przód" else "Tył")
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Wybierz lokalizację",
            style = TextStyle(fontSize = 32.sp),
            modifier = Modifier.padding(16.dp)
        )
        if (gender == "kobieta") {
            if (isFront) {
                BodyImage(drawable = R.drawable.feminine_front, location = spotLocation, onLocationChange = onLocationChange, locationName = locationName)
            } else {
                BodyImage(drawable = R.drawable.feminine_back, location = spotLocation, onLocationChange = onLocationChange, locationName = locationName)
            }
        } else if (gender == "mężczyzna") {
            if (isFront) {
                BodyImage(drawable = R.drawable.masculine_front, location = spotLocation, onLocationChange = onLocationChange, locationName = locationName)
            } else {
                BodyImage(drawable = R.drawable.masculine_back, location = spotLocation, onLocationChange = onLocationChange, locationName = locationName)
            }
        }
        Text(
            text = if (isFront) "Przód" else "Tył",
            style = TextStyle(fontSize = 22.sp),
            modifier = Modifier.padding(bottom = 6.dp)
        )
        FlipButton(onFlip = onFlipClick)
        Text(
            text = locationName,
            style = TextStyle(fontSize = 14.sp, color = if (locationName == "zaznaczenie poza ciałem") Color.Red else Color.Gray),
            modifier = Modifier.padding(bottom = 6.dp)
        )
        Button(
            modifier = Modifier.width(200.dp),
            onClick = {
                navController.navigate("symptoms?photoUrl=$photoUrl&x=${spotLocation.x}&y=${spotLocation.y}&bodySide=${if(isFront) "Przód" else "Tył"}&token=$token&caller=$caller&lesionId=$lesionId")
            }
        ) {
            Text("Dalej")
        }
    }
}

@Composable
fun BodyImageReadOnly(
    lesions: List<Lesion>,
    modifier: Modifier = Modifier,
    location: Offset,
    onLocationChange: (Offset) -> Unit,
    bodySide: String,
    @DrawableRes drawable: Int
){
//    val image = ImageVector.vectorResource(id = drawable)
//    val painter = rememberVectorPainter(image = image)

    val altPainter = painterResource(id = drawable)

    Surface(
        modifier = Modifier.height(420.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { tapOffset ->
                            Log.d("LOCATION", tapOffset.toString())
                            onLocationChange(tapOffset)
                        }
                    )
                }

        ) {
            with(altPainter) {

                val translationX = (size.width - intrinsicSize.width) / 2 * 1.4f
                val translationY = (size.height - intrinsicSize.height) / 1.25f*0.01f

                Log.d(translationX.toString(), translationY.toString())

                translate(translationX, translationY) {
                    draw(Size((intrinsicSize.width / 1.4).toFloat(), (intrinsicSize.height / 1.4).toFloat()))
                }
            }
            for (lesion in lesions) {
                Log.d("LOCATION RENDERING", lesion.location.toString())
                if (lesion.location != null && lesion.bodySide == bodySide) {
                    drawCircle(Color.Red, radius = 10f, center = Offset(lesion.location[0].toFloat() - 45f, lesion.location[1].toFloat()))
                }
            }
        }
    }
}

@Composable
fun BodyMapReadOnly(
    user: User,
    lesions: List<Lesion>,
    addNewPhoto: (Any?) -> Unit,
    navController: NavController
) {
    val bodySide = "Front"
    val bodyFrame = "Feminine"

    var spotLocation by remember {mutableStateOf(Offset.Unspecified)}
    var locationName by remember {
        mutableStateOf("zaznaczenie poza ciałem")
    }

    var selectedLesion by remember {
        mutableStateOf<Lesion?>(null)
    }

    var displayLesionInfo by remember { mutableStateOf(false)}

    var selectedLestionLocation by remember {
        mutableStateOf("")
    }

    var isFront by remember { mutableStateOf(true) }

    val onLocationChange: (Offset) -> Unit = f@{
        spotLocation = it
        for (lesion in lesions) {
            if (lesion.location != null && lesion.bodySide == if(isFront) "Przód" else "Tył") {
                if (it.x >= lesion.location[0] - 20 - 45 && it.x <= lesion.location[0] + 20 - 45 && it.y >= lesion.location[1] - 20 && it.y <= lesion.location[1] + 20) {
                    selectedLesion = lesion
                    selectedLestionLocation = getBodyPart(it, user.gender?:"", lesion.bodySide?:"", caller = "view")
                    displayLesionInfo = true
                    return@f
                }
            }
        }
    }

    val onFlipClick: () -> Unit = {
        isFront = !isFront
        spotLocation = Offset.Unspecified
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (user.gender == "kobieta") {
                if (isFront) {
                    BodyImageReadOnly(drawable = R.drawable.feminine_front, location = spotLocation, onLocationChange = onLocationChange, lesions = lesions, bodySide = "Przód")
                } else {
                    BodyImageReadOnly(drawable = R.drawable.feminine_back, location = spotLocation, onLocationChange = onLocationChange, lesions = lesions, bodySide = "Tył")
                }
            } else if (user.gender == "mężczyzna") {
                if (isFront) {
                    BodyImageReadOnly(drawable = R.drawable.masculine_front, location = spotLocation, onLocationChange = onLocationChange, lesions = lesions, bodySide = "Przód")
                } else {
                    BodyImageReadOnly(drawable = R.drawable.masculine_back, location = spotLocation, onLocationChange = onLocationChange, lesions = lesions, bodySide = "Tył")
                }
            }
            Text(
                text = if (isFront) "Przód" else "Tył",
                style = TextStyle(fontSize = 16.sp),
                modifier = Modifier.padding(bottom = 6.dp)
            )
            FlipButton(onFlip = onFlipClick)
        }
    }
}


//---------------------previews---------------------
//@Preview(showBackground = true)
//@Composable
//fun BodyImagePreview(){
//    DermatologyAppTheme {
//        BodyImage(drawable = R.drawable.feminine_frame_back)
//    }
//}
//
//@Preview
//@Composable
//fun FlipButtonPreview(){
//    DermatologyAppTheme {
//        FlipButton()
//    }
//}
//
//@Preview(showBackground = true, device = Devices.DEFAULT)
//@Composable
//fun BodyMapPreview(){
//    DermatologyAppTheme {
//        BodyMap()
//    }
//}