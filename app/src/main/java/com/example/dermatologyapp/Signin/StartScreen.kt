package com.example.dermatologyapp.Signin

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dermatologyapp.R
import com.example.dermatologyapp.ui.theme.DermatologyAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartScreen(
    state: SignInGoogleState,
    onCreateAccountClick: () -> Unit,
    onSignInClick: () -> Unit,
    onGoogleSignInClick: () -> Unit,
    loginViewModel: LoginViewModel = viewModel(),
    onSuccess: () -> Unit = {}
){
    val isLoginSuccessful by loginViewModel.isLoginSuccessful.collectAsStateWithLifecycle()

    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let {error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    LaunchedEffect(key1 = isLoginSuccessful) {
        if(isLoginSuccessful){
            onSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp),
    ){
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Image(
                    painterResource(id = R.drawable.logo_control),
                    contentDescription = "logo placeholder",
                    modifier = Modifier.size(150.dp),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Zdrowa skóra z .Control",
                    fontSize = 20.sp
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //funkcje logowania
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Masz już konto?",
                fontSize = 16.sp
            )

            var email by remember{ mutableStateOf(TextFieldValue()) }
            var password by remember{ mutableStateOf(TextFieldValue()) }
            var emailErrorState by remember{ mutableStateOf(false) }
            var passwordErrorState by remember{ mutableStateOf(false) }

            //pole na email
            OutlinedTextField(
                value = email,
                onValueChange = {
                    if(emailErrorState){
                        emailErrorState = false
                    }
                    email = it
                    loginViewModel.onEvent(LoginUIEvent.EmailChanged(it.text))
                },
                isError = emailErrorState,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = "Podaj email"
                    )
                }
            )
            if(emailErrorState){
                Text(
                    text = "Pole wymagane",
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(Modifier.size(8.dp))

            //pole na haslo
            var passwordVisibility by remember { mutableStateOf(true) }
            OutlinedTextField(
                value = password,
                onValueChange = {
                    if (passwordErrorState){
                        passwordErrorState = false
                    }
                    password = it
                    loginViewModel.onEvent(LoginUIEvent.PasswordChanged(it.text))
                },
                isError = passwordErrorState,
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(
                        text = "Podaj hasło"
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = { passwordVisibility = !passwordVisibility }
                    ) {
                        Icon(
                            imageVector = if (passwordVisibility) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "visibility",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                visualTransformation = if(passwordVisibility) PasswordVisualTransformation() else VisualTransformation.None
            )
            if(passwordErrorState){
                Text(
                    text = "Pole wymagane",
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(Modifier.size(8.dp))

            //przycisk logowania
            OutlinedButton(
                onClick = {
                    when {
                        email.text.isEmpty() -> {
                            emailErrorState = true
                        }
                        password.text.isEmpty() -> {
                            passwordErrorState = true
                        }
                        else -> {
                            loginViewModel.onEvent(LoginUIEvent.LoginButtonClicked)
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
                modifier = Modifier.width(300.dp)
            ) {
                Text(
                    text = "Zaloguj się",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "lub:",
                fontSize = 16.sp
            )

            //przycisk google
            Spacer(modifier = Modifier.height(4.dp))
            Button(
                onClick = onGoogleSignInClick,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
                modifier = Modifier.width(300.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Image(
                        painterResource(id = R.drawable.google_icon),
                        contentDescription = "google login icon",
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = "Zaloguj się przez Google",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            //przycisk utworz konto
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = onCreateAccountClick,
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.primary),
                modifier = Modifier.width(300.dp)
            ) {
                Text(
                    text = "Utwórz konto",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview
@Composable
fun StartScreenPreview(){
    DermatologyAppTheme {
        StartScreen(
            state = SignInGoogleState(true),
            onSignInClick = {},
            onCreateAccountClick = {},
            onGoogleSignInClick = {}
        )
    }
}