package com.example.dermatologyapp.LearnMore

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.dermatologyapp.ui.theme.DermatologyAppTheme

//Informacje podstawowe
val melBasic1 = "Czerniak to nowotwór złośliwy wywodzący się z melanocytów."
val melBasic2 = "Melanocyty to komórki wytwarzające melaninę. Znajdują się w skórze w i błonach śluzowych."
val melBasic3 = "Melanina to barwnik nadający kolor między innymi naszej skórze. Działa ona jak " +
        "naturalny filtr chroniący przed nadmiernym promieniowaniem ultrafioletowym."
val melBasic4 = "Jeżeli w pewnym obszarze skóry zgromadzi się dużo melanocytów, powstanie ciemniejsza zmiana " +
        "znana jako pieprzyk. Znaczna większość z nich nie jest niebezpieczna dla zdrowia."
val melBasic5 = "Jeżeli w pewnym obszarze skóry zgromadzi się dużo melanocytów, które zbudowane są " +
        "inaczej niż zwykłe melanocyty, powstanie znamię dysplastyczne, które różni się od innych. Często jest większe, " +
        "bardziej wypukłe, nierówne. Samo znamię dysplastyczne rzadko przekształca się w czerniaka, " +
        "jednak jego obecność zwiększa ryzyko zachorowania."
val melBasic6 = "Jeżeli w pewnym obszarze skóry zgromadzi się dużo nieprawidłowo zbudowanych melanocytów, " +
        "które zaczną się niekontrolowanie dzielić, powstanie czerniak."
val melBasicList = listOf<String>(melBasic1, melBasic2, melBasic3, melBasic4, melBasic5, melBasic6)

//Czynniki ryzyka
val melRisk1 = "Ekspozycja na promieniowanie ultrafioletowe poprzez przebywanie w obszarach intensywnie nasłonecznionych " +
        "lub korzystanie z solarium."
val melRisk2 = "Posiadanie dużej ilości pieprzyków."
val melRisk3 = "Jasna karnacja, włosy rude lub w odcieniach blondu, oczy zielone, szare lub niebieskie."
val melRisk4 = "Posiadanie znamion dysplastycznych, czyli pieprzyków różniących się od innych - większych, " +
        "wypukłych, nierównych."
val melRisk5 = "Doświadczenie intensywnych oparzeń słonecznych w dzieciństwie lub w okresie dojrzewania."
val melRisk6 = "Przyjęcie przeszczepu narządu."
val melRisk7 = "Występowanie innych nowotworów skóry takich jak podstawnokomórkowy lub płaskonabłonkowy " +
        "rak skóry oraz ziarniak grzybiasty."
val melRisk8 = "Czynniki genetyczne, osobista lub rodzinna historia występowania czerniaka."
val melRiskList = listOf<String>(melRisk1, melRisk2, melRisk3, melRisk4, melRisk5, melRisk6, melRisk7, melRisk8)

//Objawy znamienia czerniakowego
val melSymptom1 = "Asymetryczny kształt"
val melSymptom2 = "Nierówny obwód"
val melSymptom3 = "Różne kolory w obrębie jednego znamienia: od jasnobrązowego do czarnego, czasami czerowny"
val melSymptom4 = "Średnica większa niż 6 mm"
val melSymptom5 = "Zmnienność wyglądu w czasie"
val melSymptom6 = "Objawy towarzyszące, które mogą wystąpić: krwawienie, swędzenie, owrzodzenie, obrzęk"
val melSymptom7 = "Czasami znamię czerniakowe nie wykazuje żadnej z wyżej wymienionych cech i objawia się " +
        "jako zmiana o symetrycznym, okrągłym kształcie i równych brzegach. Może mieć ciemniejsze zabarwienie " +
        "lub być bardziej wypukła od zwykłego pieprzyka, albo niczym się od niego nie różnić. " +
        "Takie znamiona są szczególnie niebezpieczne ze względu na trudność w ich zaobserwowaniu oraz " +
        " większą szybkość przechodzenia do trudniej wyleczalnych etapów choroby."
val melSymptomList = listOf<String>(melSymptom1, melSymptom2, melSymptom3, melSymptom4, melSymptom5, melSymptom6, melSymptom7)

//Profilaktyka
val melPrevention1 = "Regularna obserwacja instniejących znamion oraz wszystkich obszarów skóry."
val melPreviention2 = "Stosowanie kremów z filtem UV oraz ubieranie odzieży ochronnej " +
        "w każdym przypadku narażenia na intensywne lub długotrwałe przebywanie na słońcu."
val melPreviention3 = "Unikanie korzystania z solarium"
val melPrevention4 = "Przebywanie w pomieszczeniu lub w miejscu zacienionym, gdy promieniowanie " +
        "słoneczne jest intensywne."
val melPrevention5 = "Wizyty u dermatologa odbywane regularnie lub w każdym przypadku zaobserwowania " +
        "niepokojącej zmiany skórnej."
val melPreventionList = listOf<String>(melPrevention1, melPreviention2, melPreviention3, melPrevention4, melPrevention5)

//Diagnostyka
val melDiagnostics1 = "Diagnostyka czerniaka opiera się o metody nieinwazyjne oraz inwazyjne."
val melDiagnostics2 = "Najpopularniejszą metodą nieinwazyjną jest dermatoskopia, czyli " +
        "obserwacja znamienia przez przyrząd pozwalający na uzyskanie obrazu w odpowiednim " +
        "powiększeniu oraz na uwydatnienie cech wyglądu zmiany skórnej, słabo widocznych dla ludzkiego " +
        "oka."
val melDiagnostics3 = "Metodą inwazyjną jest biopsja, czyli pobranie tkanki budującej znamię. Pobrany materiał " +
        "jest następnie badany, co pozwala na określenie między innymi stopnia zaawansowania choroby."
val melDiagnosticList = listOf<String>(melDiagnostics1, melDiagnostics2, melDiagnostics3)

//pojedynczy podpunkt po rozwinieciu
@Composable
fun LearnMorePoint(
    text: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = Icons.Filled.Circle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(8.dp)
                .offset(y = 8.dp)
        )
        Text(
            text = text,
            fontSize = 16.sp,
            lineHeight = 20.sp
        )
    }
}

//pojedynczy element listy
@Composable
fun LearnMoreElement(
    title: String,
    content: List<String>,
    isExpanded: Boolean,
    onExpandedChange: () -> Unit
){
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { onExpandedChange() },
                colors = IconButtonDefaults.iconButtonColors(containerColor = Color.Transparent)
            ) {
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null
                )
            }
        }
        Divider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = Color.Black
        )
        if (isExpanded) {
            content.forEach { point ->
                LearnMorePoint(text = point)
            }
        }
    }
}

//cala lista
@Composable
fun LearnMoreScreen(
    basicList: List<String> = melBasicList,
    riskList: List<String> = melRiskList,
    symptomList: List<String> = melSymptomList,
    preventionList: List<String> = melPreventionList,
    diagnosticsList: List<String> = melDiagnosticList
) {
    var isBasicListExpanded by remember { mutableStateOf(false) }
    var isRiskListExpanded by remember { mutableStateOf(false) }
    var isSymptomListExpanded by remember { mutableStateOf(false) }
    var isPreventionListExpanded by remember { mutableStateOf(false) }
    var isDiagnosticsListExpanded by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(all = 16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
        ,
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary),
        ) {
            Text(
                text = "Pamiętaj - wynik analizy wskazany przez aplikację nie jest ostateczną diagnozą. " +
                        "W celu jej uzyskania skonsultuj się z lekarzem.",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(all = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        LearnMoreElement(
            title = "Informacje podstawowe",
            content = basicList,
            isExpanded = isBasicListExpanded,
            onExpandedChange = { isBasicListExpanded = !isBasicListExpanded }
        )
        LearnMoreElement(
            title = "Czynniki ryzyka",
            content = riskList,
            isExpanded = isRiskListExpanded,
            onExpandedChange = { isRiskListExpanded = !isRiskListExpanded }
        )
        LearnMoreElement(
            title = "Objawy znamienia czerniakowego",
            content = symptomList,
            isExpanded = isSymptomListExpanded,
            onExpandedChange = { isSymptomListExpanded = !isSymptomListExpanded }
        )
        LearnMoreElement(
            title = "Profilaktyka",
            content = preventionList,
            isExpanded = isPreventionListExpanded,
            onExpandedChange = { isPreventionListExpanded = !isPreventionListExpanded }
        )
        LearnMoreElement(
            title = "Diagnostyka",
            content = diagnosticsList,
            isExpanded = isDiagnosticsListExpanded,
            onExpandedChange = { isDiagnosticsListExpanded = !isDiagnosticsListExpanded }
        )
        Spacer(modifier = Modifier.height(100.dp))
    }
}

//----------previews----------
@Preview
@Composable
fun LearnMorePointPreview() {
    DermatologyAppTheme {
        LearnMorePoint(text = melPrevention4)
    }
}

@Preview
@Composable
fun LearnMoreElementPreviewFalse() {
    DermatologyAppTheme {
        LearnMoreElement(title = "Profilaktyka", content = melPreventionList, isExpanded = false) {

        }
    }
}

@Preview
@Composable
fun LearnMoreElementPreviewTrue() {
    DermatologyAppTheme {
        LearnMoreElement(title = "Profilaktyka", content = melPreventionList, isExpanded = true) {

        }
    }
}

@Preview
@Composable
fun LearnMoreScreenPreview() {
    DermatologyAppTheme {
        LearnMoreScreen()
    }
}