package com.example.dermatologyapp.Navigation

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.annotation.ExperimentalCoilApi
import com.example.dermatologyapp.Camera.CameraStart
import com.example.dermatologyapp.FormUser.QuestionFrame
import com.example.dermatologyapp.FormUser.QuestionViewModel
import com.example.dermatologyapp.Profile.ProfileScreen
import com.example.dermatologyapp.Signin.UserData
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dermatologyapp.FormUser.FormMainScreen
import com.example.dermatologyapp.LearnMore.LearnMoreScreen
import com.example.dermatologyapp.Profile.BodyMap
import com.example.dermatologyapp.Symptoms.SymptomsList
import com.example.dermatologyapp.Symptoms.SymptomsViewModel

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalPermissionsApi::class, ExperimentalCoilApi::class,
    ExperimentalCoroutinesApi::class
)
@Composable
fun AppNavigation(
    userData: UserData?,
    onSignOut: () -> Unit,
    context: Context
){
    val navController = rememberNavController()
    var gender by remember {mutableStateOf("kobieta")}
    val setGender: (String) -> Unit = {
        Log.d("AppNav", "setGender: $it")
        gender = it
    }

    var navbarDisplayed by remember { mutableStateOf(true) }

    NavHost(navController = navController, startDestination = "profile"){
        composable("profile"){
            navbarDisplayed = true
            ProfileScreen(userData = userData, onSignOut = onSignOut, navController = navController, setGender = setGender)
        }
        composable("learn_more") {
            navbarDisplayed = true
            LearnMoreScreen()
        }
        composable("add"){
            navbarDisplayed = false
            CameraStart(context = context, navController = navController)
        }
        composable("camera_start_update?id={id}"){ navBackStackEntry ->
            navbarDisplayed = false
            val id = navBackStackEntry.arguments?.getString("id")
            CameraStart(context = context, navController = navController, caller="update", lesionId = id?:"")
        }
        composable("learn"){
            Log.d("AppNavigation", "profile")
        }
        composable("risk_form"){
            FormMainScreen(
                formPunctationList = listOf(),
                formDone = false,
                onLearnClick = { /*TODO*/ },
                onFormClick = { /*TODO*/ },
                navController = navController
            )
        }
        composable("questions"){
            QuestionFrame(
                questionViewModel = viewModel<QuestionViewModel>(),
                navController = navController
            )
        }
//        composable("lesion_info"){
//            LesionScreen(
//                onImagePreview = { /*TODO*/ },
//                lesions = listOf(),
//                diagnosis = "",
//                localization = "",
//                symptoms = listOf()
//            )
//        }
        composable("lesion_analysis_body_map?photoUrl={photoUrl}&token={token}&caller={caller}&lesionId={lesionId}") { navBackstackEntry ->
            Log.d("AppNavigation", "lesion_analysis_body_map")
            val url = navBackstackEntry.arguments?.getString("photoUrl")
            val token = navBackstackEntry.arguments?.getString("token")
            val caller = navBackstackEntry.arguments?.getString("caller")
            val lesionId = navBackstackEntry.arguments?.getString("lesionId")
            if (url != null) {
                Log.d("AppNavigation", "lesion_analysis_body_map url: $token")
                BodyMap(gender = gender, navController = navController, photoUrl=url, token = token?:"", caller = caller?:"", lesionId = lesionId?:"")
            }
        }
        composable("symptoms?photoUrl={photoUrl}&x={x}&y={y}&bodySide={bodySide}&token={token}&caller={caller}&lesionId={lesionId}") { navBackstackEntry ->
            Log.d("AppNavigation", "symptoms")
            val photoUrl = navBackstackEntry.arguments?.getString("photoUrl")
            val x = navBackstackEntry.arguments?.getString("x")?.toFloat()
            val y = navBackstackEntry.arguments?.getString("y")?.toFloat()
            val bodySide = navBackstackEntry.arguments?.getString("bodySide")
            val token = navBackstackEntry.arguments?.getString("token")
            val caller = navBackstackEntry.arguments?.getString("caller")
            val lesionId = navBackstackEntry.arguments?.getString("lesionId")
            SymptomsList(uid = userData?.userId, context = context, navController = navController, viewModel = viewModel<SymptomsViewModel>(), photoUrl = photoUrl?:"", x = x?:0f, y = y?:0f, bodySide = bodySide?:"", token = token?:"", caller = caller?:"", lesionId = lesionId?:"")
        }
    }

    Box(
        contentAlignment = Alignment.BottomEnd
    ) {
        if (navbarDisplayed) {
            BottomNavigation(navController = navController)
        }
    }
}