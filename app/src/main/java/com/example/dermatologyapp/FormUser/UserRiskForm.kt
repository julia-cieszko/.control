package com.example.dermatologyapp.FormUser

import android.app.Application
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dermatologyapp.ui.theme.DermatologyAppTheme
import com.example.dermatologyapp.userRiskPunctation

//opcje wyboru w posatci radio buttons, stateless
@Composable
fun RadioOptions(
    question: FormQuestion,
    selectedOption: Int,
    onSelectionChange: (Int) -> Unit,
    modifier: Modifier = Modifier
){
    val radioOptions = listOf("Nie", "Tak")

    Column(
        modifier = Modifier.selectableGroup()
    ){
        radioOptions.forEachIndexed{ index, text ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ){
                RadioButton(
                    selected = index == selectedOption,
                    onClick = {
                        onSelectionChange(index)
                    }
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

//zmiana koloru przycisku na chwile klikniecia

//przyciski wstecz i dalej
@Composable
fun FormButtons(
    questionIndex: Int,
    onQuestionIndexPlus: (Int) -> Unit,
    onQuestionIndexMinus: (Int) -> Unit,
    interactionSourcePlus: MutableInteractionSource,
    interactionSourceMinus: MutableInteractionSource,
    modifier: Modifier = Modifier
){
    val isPressedPlus by interactionSourcePlus.collectIsPressedAsState()
    val isPressedMinus by interactionSourceMinus.collectIsPressedAsState()
    val colorPlus = if (isPressedPlus) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
    val colorMinus = if (isPressedMinus) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if(questionIndex == 0){
            FilledTonalButton(
                onClick = { onQuestionIndexPlus(questionIndex) },
                interactionSource = interactionSourcePlus,
                colors = ButtonDefaults.buttonColors(containerColor = colorPlus),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Dalej",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }else if (questionIndex > 0){
            FilledTonalButton(
                onClick = { onQuestionIndexMinus(questionIndex) },
                interactionSource = interactionSourceMinus,
                colors = ButtonDefaults.buttonColors(containerColor = colorMinus),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Wstecz",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            FilledTonalButton(
                onClick = { onQuestionIndexPlus(questionIndex) },
                interactionSource = interactionSourcePlus,
                colors = ButtonDefaults.buttonColors(containerColor = colorPlus),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Dalej",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

//wskaznik progresu
@Composable
fun Dot(isFilled: Boolean) {
    Icon(
        imageVector = Icons.Filled.Circle,
        contentDescription = null,
        tint = if (isFilled){
            MaterialTheme.colorScheme.primary
        }else{
            MaterialTheme.colorScheme.secondary
        },
        modifier = Modifier
            .size(24.dp)
            .padding(horizontal = 4.dp)

    )
}
@Composable
fun FormProgress(
    totalQuestionNumber: Int,
    currentIndex: Int, //stan
    modifier: Modifier = Modifier
){
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
    ) {
        for (index in 0 until totalQuestionNumber) {
            Dot(index <= currentIndex)
        }
    }
}

@Composable
fun QuestionFrame(
    questionViewModel: QuestionViewModel,
    modifier: Modifier = Modifier,
    navController: NavController
){
    var questionIndex by rememberSaveable { mutableStateOf(0) } //na poczatku numer pytania to 0
    val question = questionViewModel.formQuestions.get(questionIndex)
    var selectedOption by rememberSaveable { mutableStateOf(0) }
    val interactionSourcePlus = remember { MutableInteractionSource() }
    val interactionSourceMinus = remember { MutableInteractionSource() }
    val totalQuestionNumber = 10

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp)
    ) {
        Text(
            text = question.text,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        //to "it" to jest wlasnie to 0 albo 1, jest to wartosc parametru przymowanego
        //przez ta funkcje, pamietamy: onSelectionChange: (Int)-> Unit
        Box(modifier = Modifier.weight(1f)) {
            RadioOptions(question,
                selectedOption, onSelectionChange = {
                    selectedOption = it
                    question.punctation = it
                })
        }
        Log.d("punktacja czastkowa", "${question.punctation}")

        FormButtons(
            questionIndex = questionIndex,
            onQuestionIndexMinus = {
                if (questionIndex != 0) {
                    questionIndex = it - 1
                    selectedOption =
                        questionViewModel.formQuestions.get(questionIndex).punctation
                    userRiskPunctation -= question.punctation
                }
            },
            onQuestionIndexPlus = {
                if (questionIndex != 9) {
                    questionIndex = it + 1
                    selectedOption =
                        questionViewModel.formQuestions.get(questionIndex).punctation
                    userRiskPunctation += question.punctation
                } else {
                    questionViewModel.uploadAnwers()
                    navController.navigate("risk_form")
                }
            },
            interactionSourcePlus = interactionSourcePlus,
            interactionSourceMinus = interactionSourceMinus,
        )

        Log.d("indeks pytania", "$questionIndex")
        Log.d("punktacja calkowita", "$userRiskPunctation")
        Spacer(modifier = Modifier.height(16.dp))
        FormProgress(
            totalQuestionNumber = totalQuestionNumber,
            currentIndex = questionIndex,
        )
        Spacer(modifier = Modifier.height(70.dp))
    }
}


//---------------------previews---------------------
//@Preview
//@Composable
//fun RadioOptionsPreview(){
//    DermatologyAppTheme {
//        RadioOptions()
//    }
//}

//@Preview
//@Composable
//fun FormButtonsPreview(){
//    DermatologyAppTheme {
//        FormButtons()
//    }
//}
//@Preview
//@Composable
//fun QuestionFramePreview(){
//    DermatologyAppTheme {
//        QuestionFrame(questionViewModel = viewModel<QuestionViewModel>())
//    }
//}