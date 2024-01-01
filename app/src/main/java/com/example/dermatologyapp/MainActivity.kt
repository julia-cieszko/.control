package com.example.dermatologyapp

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.dermatologyapp.ui.theme.DermatologyAppTheme

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dermatologyapp.Loading.NoInternetScreen
import com.example.dermatologyapp.Navigation.AppNavigation
import com.example.dermatologyapp.Signin.GoogleAgeGenderForm
import com.example.dermatologyapp.Signin.GoogleAuthUiClient
import com.example.dermatologyapp.Signin.RegistrationScreen
import com.example.dermatologyapp.Signin.RegistrationViewModel
import com.example.dermatologyapp.Signin.SignInGoogleState
import com.example.dermatologyapp.Signin.StartScreen
import com.example.dermatologyapp.Signin.SignInGoogleViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@ExperimentalPermissionsApi
class MainActivity : ComponentActivity() {

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ctx = this

        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        val isConnected = activeNetwork?.isConnectedOrConnecting == true

        setContent {
            DermatologyAppTheme {
                if(!isConnected){
                    Column {
                        NoInternetScreen()
                    }
                } else {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        var startRoute = ""
                        if(googleAuthUiClient.getSignedInUser() == null){
                            startRoute = "sign_in"
                        }else {
                            startRoute = "profile"
                        }
                        val navController = rememberNavController()
                        NavHost(navController = navController, startDestination = startRoute) {
                            composable("sign_in") {
                                val viewModel = viewModel<SignInGoogleViewModel>()
                                val state by viewModel.state.collectAsStateWithLifecycle()
                                val userExists by viewModel.userExists.collectAsStateWithLifecycle()
                                val id by viewModel.id.collectAsStateWithLifecycle()
                                val name by viewModel.name.collectAsStateWithLifecycle()

                                //zapamietaj ze jestes zalogowany
                                LaunchedEffect(key1 = Unit){
                                    if(googleAuthUiClient.getSignedInUser() != null) {
                                        navController.navigate("profile")
                                    }
                                }

                                val launcher = rememberLauncherForActivityResult(
                                    contract = ActivityResultContracts.StartIntentSenderForResult(),
                                    onResult = {result ->
                                        if(result.resultCode == RESULT_OK){
                                            lifecycleScope.launch (Dispatchers.IO){
                                                val signInResult = googleAuthUiClient.signInWithIntent(
                                                    intent = result.data ?: return@launch
                                                )
                                                viewModel.onSignInResult(signInResult)
                                                withContext(Dispatchers.Main){
                                                    viewModel.doesGoogleUserExist(id)
                                                }
                                            }
                                        }
                                    }
                                )

                                LaunchedEffect(key1 = state.isSignInSuccessful){
                                    if(state.isSignInSuccessful){
                                        Toast.makeText(
                                            applicationContext,
                                            "Zalogowano pomyślnie",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        Log.d("CHECK_USER_EXISTS", "")
                                        if (userExists) {
                                            navController.navigate("profile")
                                        } else {
                                            navController.navigate("google_age_gender_form?id=$id&name=$name")
                                        }
                                        viewModel.resetState()
                                    }
                                }

                                StartScreen(
                                    state = state,
                                    onGoogleSignInClick = {
                                        lifecycleScope.launch {
                                            val signInIntentSender = googleAuthUiClient.signIn()
                                            launcher.launch(
                                                IntentSenderRequest.Builder(
                                                    signInIntentSender ?: return@launch
                                                ).build()
                                            )
                                        }
                                    },
                                    onCreateAccountClick = {navController.navigate("register")},
                                    onSignInClick = {},
                                    onSuccess = {navController.navigate("profile")}
                                )
                            }
                            composable(route = "profile"){
                                AppNavigation(
                                    context = ctx,
                                    userData = googleAuthUiClient.getSignedInUser(),
                                    onSignOut = {
                                        lifecycleScope.launch {
                                            googleAuthUiClient.signOut()
                                            navController.navigate("sign_in")
                                        }
                                    }
                                )
                            }
                            composable(route = "google_age_gender_form?id={id}&name={name}") {
                                    navBackStackEntry ->
                                val id = navBackStackEntry.arguments?.getString("id")
                                val name = navBackStackEntry.arguments?.getString("name")
                                Log.d("CREDENTIAL", "id: $id, name: $name")
                                GoogleAgeGenderForm(id = id?:"", name = name?:"", navController = navController)
                            }

                            composable(route = "register") {
                                RegistrationScreen(navController = navController)
                            }
                        }
                    }
                }
            }
        }
    }
}