package com.example.dermatologyapp.Signin

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dermatologyapp.R
import com.example.dermatologyapp.ui.theme.DermatologyAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    registrationViewModel: RegistrationViewModel = viewModel(),
    navController: NavController
) {
    //zmienne
    var name by remember { mutableStateOf(TextFieldValue()) }
    var email by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }
    var confirmPassword by remember { mutableStateOf(TextFieldValue()) }

    var nameErrorState by remember { mutableStateOf(false) }
    var emailErrorState by remember { mutableStateOf(false) }
    var passwordErrorState by remember { mutableStateOf(false) }
    var confirmPasswordErrorState by remember { mutableStateOf(false) }

    var isExpanded by remember { mutableStateOf(false) }
    val years = (1930..2023).map { it.toString() }
    var yearStr by remember { mutableStateOf("2023") }
    var fieldSize by remember { mutableStateOf(Size.Zero)}

    var isGenderExpanded by remember { mutableStateOf(false) }
    val genders = listOf("kobieta", "mężczyzna")
    var genderStr by remember { mutableStateOf("kobieta") }

    val signUpInProgress by registrationViewModel.signUpInProgress.collectAsStateWithLifecycle()

    val context = LocalContext.current

    LaunchedEffect(key1 = signUpInProgress) {
        if (signUpInProgress == false){
            Toast.makeText(
                context,
                "Zarejestrowano pomyślnie",
                Toast.LENGTH_LONG
            ).show()
            navController.navigate("profile")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Utwórz konto",
            color = MaterialTheme.colorScheme.primary,
            fontSize = 30.sp
        )
        //pole na nazwe uzytkownika
        OutlinedTextField(
            value = name,
            onValueChange = {
                if(nameErrorState){
                    nameErrorState = false
                }
                name = it
                registrationViewModel.onEvent(SignUpUIEvent.NameChanged(it.text))
            },
            modifier = Modifier.fillMaxWidth(),
            isError = nameErrorState,
            label = {
                Text(text = "Nazwa użytkownika")
            }
        )
        if (nameErrorState) {
            Text(text = "Pole wymagane", color = MaterialTheme.colorScheme.primary)
        }
        Spacer(Modifier.size(16.dp))

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
                                registrationViewModel.onEvent(SignUpUIEvent.BirthYearChanged(yearStr))
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
                                registrationViewModel.onEvent(SignUpUIEvent.GenderChanged(genderStr))
                            }
                        )
                    }
                }
            }
        }
        Spacer(Modifier.size(16.dp))

        //Pole na email
        OutlinedTextField(
            value = email,
            onValueChange = {
                if (emailErrorState) {
                    emailErrorState = false
                }
                email = it
                registrationViewModel.onEvent(SignUpUIEvent.EmailChanged(it.text))
            },

            modifier = Modifier.fillMaxWidth(),
            isError = emailErrorState,
            label = {
                Text(text = "Email")
            },
        )
        if (emailErrorState) {
            Text(text = "Pole wymagane", color = MaterialTheme.colorScheme.primary)
        }
        Spacer(modifier = Modifier.height(16.dp))

        //pole na haslo
        var passwordVisibility by remember { mutableStateOf(true) }
        OutlinedTextField(
            value = password,
            onValueChange = {
                if (passwordErrorState) {
                    passwordErrorState = false
                }
                password = it
                registrationViewModel.onEvent(SignUpUIEvent.PasswordChanged(it.text))
            },
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Hasło")
            },
            isError = passwordErrorState,
            trailingIcon = {
                IconButton(onClick = {
                    passwordVisibility = !passwordVisibility
                }) {
                    Icon(
                        imageVector = if (passwordVisibility) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "visibility",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            visualTransformation = if (passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None
        )
        if (passwordErrorState) {
            Text(text = "Pole wymagane", color = MaterialTheme.colorScheme.primary)
        }
        Spacer(Modifier.height(16.dp))

        //pole na powtorzenie hasla
        var cPasswordVisibility by remember { mutableStateOf(true) }
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                if (confirmPasswordErrorState) {
                    confirmPasswordErrorState = false
                }
                confirmPassword = it
            },
            modifier = Modifier.fillMaxWidth(),
            isError = confirmPasswordErrorState,
            label = {
                Text(text = "Potwierdź hasło")
            },
            trailingIcon = {
                IconButton(onClick = {
                    cPasswordVisibility = !cPasswordVisibility
                }) {
                    Icon(
                        imageVector = if (cPasswordVisibility) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "visibility",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            visualTransformation = if (cPasswordVisibility) PasswordVisualTransformation() else VisualTransformation.None
        )
        if(confirmPasswordErrorState){
            val msg = if(confirmPassword.text.isEmpty()){
                "Pole wymagane"
            }else if (confirmPassword.text != password.text){
                "Niezgodne hasła"
            }else {
                ""
            }
            Text(
                text = msg,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.weight(1f))

        //przycisk zatwierdzajacy
        Button(
            onClick = {
                when {
                    name.text.isEmpty() -> {
                        nameErrorState = true
                    }
                    email.text.isEmpty() -> {
                        emailErrorState = true
                    }
                    password.text.isEmpty() -> {
                        passwordErrorState = true
                    }
                    confirmPassword.text.isEmpty() -> {
                        confirmPasswordErrorState = true
                    }
                    confirmPassword.text != password.text -> {
                        confirmPasswordErrorState = true
                    }
                    else -> {
                        registrationViewModel.onEvent(SignUpUIEvent.RegisterButtonClicked)
                    }
                }
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

//---------previews--------
//@Preview
//@Composable
//fun RegistrationScreenPreview() {
//    DermatologyAppTheme {
//        RegistrationScreen(  )
//    }
//}