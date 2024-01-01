package com.example.dermatologyapp.FormUser

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dermatologyapp.Models.User
import com.example.dermatologyapp.ui.theme.DermatologyAppTheme

//zawartosc glowna
@Composable
fun FormMainScreen(
    formPunctationList: List<Int>,
    formDone: Boolean,
    onLearnClick: () -> Unit,
    onFormClick: () -> Unit,
    riskFactorsList: List<String> = riskFactors,
    navController: NavController,
) {
    val viewModel : FormResultsViewModel = viewModel()
    val answers by viewModel.answers.collectAsStateWithLifecycle()
    val gender by viewModel.gender.collectAsStateWithLifecycle()
    val age by viewModel.age.collectAsStateWithLifecycle()

    var isLoaded by rememberSaveable {
        mutableStateOf(false)
    }

    val scrollState = rememberScrollState()

    LaunchedEffect(key1 = true) {
        viewModel.loadAnswersFromFirestore()
    }
    LaunchedEffect(key1 = age) {
        isLoaded = true
    }

    if(isLoaded) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .verticalScroll(state = scrollState)
            ) {
                if (age >= 60) {
                    RiskWarning(text = riskWarningAge)
                }
                if (gender == "kobieta") {
                    RiskWarning(text = riskWarningWoman)
                }
                if (gender == "mężczyzna") {
                    RiskWarning(text = riskWarningMan)
                }
                if (answers.sum() == 0 && answers.isEmpty()) {
                    NoFormContent()
                } else if (answers.sum() == 0 && answers.isNotEmpty()) {
                    NoFactorsContent()
                } else if (answers.sum() > 0) {
                    Text(
                        text = "Twoje czynniki ryzyka: ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    for (i in 0..9) {
                        if (answers[i] != 0) {
                            RiskFactorElement(riskFactorText = riskFactorsList[i])
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { navController.navigate("learn_more") },
                        border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "Dowiedz się więcej",
                            fontSize = 14.sp
                        )
                    }

                    Button(
                        onClick = { navController.navigate("questions") },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (answers.isEmpty()) "Odpowiedz na pytania" else "Wykonaj ponownie",
                            fontSize = 14.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

//zawartosc przy niewykonaniu formularza
@Composable
fun NoFormContent() {
    Text(
        text = "Odpowiedz na 10 pytań, żeby dowiedzieć się czy posiadasz cechy podwyższające ryzyko" +
                " zachorowania na czerniaka.",
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        lineHeight = 18.sp
    )
}

//formularz wykonany ale wciaz na 0 punktow
@Composable
fun NoFactorsContent() {
    Text(
        text = "Nie posiadasz żadnych dodatkowych czynników podwyższających ryzyko zachorowania na czerniaka.",
        fontSize = 16.sp,
        textAlign = TextAlign.Center,
        lineHeight = 18.sp
    )
}

//lista czynnikow ryzyka zgodna z pytaniami
val factor1 = "Obecność znamion o wyróżniającym się wyglądzie"
val factor2 = "Częste narażanie skóry na promieniowanie UV"
val factor3 = "Korzystanie z solarium"
val factor4 = "Obecność ponad 50 znamion na całym ciele"
val factor5 = "Rodzinna lub osobista historia występowania czerniaka"
val factor6 = "Posiadanie cech wyglądu osób z grupy podwyższonego ryzyka"
val factor7 = "Skłonność do oparzeń słonecznych"
val factor8 = "Doświadczenie ponad 5 oparzeń słonecznych w dzieciństwie lub w okresie dojrzewania"
val factor9 = "Historia związana z przeszczepem narządu i przyjmowaniem leków immunosupresyjnych"
val factor10 = "Obeność innych nowotworów skóry"
val riskFactors = listOf(factor1, factor2, factor3, factor4, factor5, factor6, factor7, factor8, factor9, factor10)

//pojedynczy czynnik ryzyka
@Composable
fun RiskFactorElement(
    riskFactorText: String
) {
    val riskFactorTextConcat = buildAnnotatedString {
        withStyle(
            style = SpanStyle(color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold)
        ) {
            append("• ")
        }
        append(riskFactorText)
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = riskFactorTextConcat,
            fontSize = 16.sp,
            lineHeight = 18.sp
        )
    }
}

//teksty
//jezeli zalogowana osoba ma 60 lat i wiecej:
val riskWarningAge = "Osoby powyżej 60 roku życia wykazują wyższe ryzyko zachorowania na czerniaka."

//zalogowana osoba jest kobieta
val riskWarningWoman = "U kobiet czerniak najczęściej występuje na nogach."

//zalogowana osoba jest mezczyzna
val riskWarningMan = "U mężczyzn czerniak najczęściej występuje na tułowiu."

@Composable
fun RiskWarning(
    text: String
){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clipToBounds()
            .padding(horizontal = 32.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(4.dp, color = MaterialTheme.colorScheme.secondary)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}


//robocze listy punktacji
val punctationList1 = listOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
val punctationList2 = listOf(1, 1, 1, 1, 1, 1, 1, 1, 1, 1)

//--------previews--------
//@Preview
//@Composable
//fun NoFormDonePreview() {
//    DermatologyAppTheme {
//        FormMainScreen(
//            formPunctationList = punctationList1,
//            formDone = false,
//            onLearnClick = { /*TODO*/ },
//            onFormClick = { /*TODO*/ })
//    }
//}

//@Preview
//@Composable
//fun FormDoneNoRiskPreview() {
//    DermatologyAppTheme {
//        FormMainScreen(
//            formPunctationList = punctationList1,
//            formDone = true,
//            onLearnClick = { /*TODO*/ },
//            onFormClick = { /*TODO*/ })
//    }
//}
//
//@Preview
//@Composable
//fun FormDoneYesRiskPreview() {
//    DermatologyAppTheme {
//        FormMainScreen(
//            formPunctationList = punctationList2,
//            formDone = true,
//            onLearnClick = { /*TODO*/ },
//            onFormClick = { /*TODO*/ })
//    }
//}

@Preview
@Composable
fun RiskWarningPreview(){
    DermatologyAppTheme {
        RiskWarning("Osoby powyżej 60 roku życia wykazują wyższe ryzyko zachorowania na czerniaka.")
    }
}