package com.example.dermatologyapp.Signin

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dermatologyapp.Models.User
import com.example.dermatologyapp.ui.theme.DermatologyAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoogleAgeGenderForm(
    id: String,
    name: String,
    navController: NavController
) {
    var isExpanded by remember { mutableStateOf(false) }
    val years = (1930..2023).map { it.toString() }
    var yearStr by remember { mutableStateOf("2023") }
    var fieldSize by remember { mutableStateOf(Size.Zero) }

    var isGenderExpanded by remember { mutableStateOf(false) }
    val genders = listOf("kobieta", "mężczyzna")
    var genderStr by remember { mutableStateOf("kobieta") }

    val viewModel: GoogleUserAddViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Podaj dane",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 30.sp
        )

        //pola na rok urodzenia i plec
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            //Pole na dropdown z rokiem urodzenia
            ExposedDropdownMenuBox(
                expanded = isExpanded,
                onExpandedChange = {isExpanded = !isExpanded}
            ) {
                OutlinedTextField(
                    value = yearStr,
                    onValueChange = {},
                    label = { Text(text = "Rok urodzenia") },
                    singleLine = true,
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    },
                    modifier = Modifier
                        .width(175.dp)
                        .menuAnchor()
                        .onGloballyPositioned { coordinates ->
                            fieldSize = coordinates.size.toSize()
                        },
                )
                DropdownMenu(
                    expanded = isExpanded,
                    onDismissRequest = { isExpanded = false },
                    modifier = Modifier
                        .width(with(LocalDensity.current) { fieldSize.width.toDp() })
                        .height(400.dp)
                ) {
                    years.forEach{ year ->
                        DropdownMenuItem(
                            text = { Text(year) },
                            onClick = {
                                yearStr = year
                                isExpanded = false
                            }
                        )
                    }
                }
            }

            //pole na dropdown z plcia
            ExposedDropdownMenuBox(
                expanded = isGenderExpanded,
                onExpandedChange = {isGenderExpanded = !isGenderExpanded}
            ) {
                OutlinedTextField(
                    value = genderStr,
                    onValueChange = {},
                    label = { Text(text = "Płeć") },
                    singleLine = true,
                    readOnly = true,
                    trailingIcon = {
                        Icon(
                            imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                            contentDescription = null,
                            tint = Color.Black
                        )
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .menuAnchor()
                        .onGloballyPositioned { coordinates ->
                            fieldSize = coordinates.size.toSize()
                        },
                )
                DropdownMenu(
                    expanded = isGenderExpanded,
                    onDismissRequest = { isGenderExpanded = false },
                    modifier = Modifier
                        .width(with(LocalDensity.current) { fieldSize.width.toDp() })
                        .height(120.dp)
                ) {
                    genders.forEach{ gender ->
                        DropdownMenuItem(
                            text = { Text(gender) },
                            onClick = {
                                genderStr = gender
                                isGenderExpanded = false
                            }
                        )
                    }
                }
            }
        }
        Spacer(Modifier.size(16.dp))


        //przycisk zatwierdzajacy
        Button(
            onClick = {
                      viewModel.addGoogleUserToFirestore(
                          User(
                              id = id,
                              name = name,
                              birthYear = yearStr.toInt(),
                              gender = genderStr
                          )
                      )
                navController.navigate("profile")
            },
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .size(height = 50.dp, width = 150.dp)
        ) {
            Text(
                text = "Dalej",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

//@Preview
//@Composable
//fun GoogleAgeGenderFormPreview() {
//    DermatologyAppTheme {
//        GoogleAgeGenderForm()
//    }
//}
