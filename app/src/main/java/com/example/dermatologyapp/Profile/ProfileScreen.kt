package com.example.dermatologyapp.Profile

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.dermatologyapp.Loading.LoadingScreen
import com.example.dermatologyapp.Signin.UserData

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    userData: UserData?,
    onSignOut: () -> Unit,
    navController: NavController,
    setGender : (String) -> Unit = {}
){
    Log.d("PROFILE SCREEN ENTRY", "dummy")
    val viewModel: ProfileViewModel = viewModel()
    val user by viewModel.user.collectAsStateWithLifecycle()
    val lesions by viewModel.lesions.collectAsStateWithLifecycle()
    var logoutDialogDisplayed by rememberSaveable { mutableStateOf(false) }
    var userLoaded by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(true) {
        viewModel.loadUserData()
    }

    LaunchedEffect(key1 = user) {
        Log.d("ProfileScreen", "user: $user")
        if (user != null) {
            user!!.gender?.let { setGender(it) }
        }
    }

    LaunchedEffect(key1 = lesions) {
        Log.d("TRY ME", "lesions count: ${lesions.size}")
        userLoaded = true
    }

    if (logoutDialogDisplayed) {
        LogoutDialog(
            onLogoutClick = {
                onSignOut()
                logoutDialogDisplayed = false
            },
            onDismiss = {
                logoutDialogDisplayed = false
            }
        )
    }

    Column(
        modifier = modifier.padding(all = 12.dp)
    ){
        ProfileUserElement(
            userData = user,
            onSignOut = {
                logoutDialogDisplayed = true
            },
            onRiskClick = {
                navController.navigate("risk_form")
            }
        )
        var isList by rememberSaveable { mutableStateOf(true) }
        var isBodyMap by rememberSaveable{ mutableStateOf(false) }
        ListBodySwitcher(
            modifier = Modifier,
            isList, onListChange = {
                isList = true
                isBodyMap = false
            },
            isBodyMap, onBodyMapChange = {
                isList = false
                isBodyMap = true
            })
        if(userLoaded){
            if(isList){
                MolesColumn(
                    onLesionClick = {
                        navController.navigate("lesion_info")
                    },
                    lesions = lesions,
                    addNewPhoto = {
                        Log.d("ProfileScreen", "addNewPhoto: $it")
                        navController.navigate("camera_start_update?id=${it}")
                    },
                    gender = user?.gender?:""
                )
            }else if(isBodyMap){
                BodyMapReadOnly(
                    navController = navController,
                    user = user!!,
                    lesions = lesions!!,
                    addNewPhoto = {
                        Log.d("ProfileScreen", "addNewPhoto: $it")
                        navController.navigate("camera_start_update?id=${it}")
                    }
                )
            }
        } else LoadingScreen()
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ProfileScreenComplete(
//    modifier: Modifier = Modifier
//){
//    DermatologyAppTheme {
//        Scaffold(
//            bottomBar = {
//                BottomNavigation(
//                textProfile = R.string.navbar_profile,
//                textBodyAreas = R.string.navbar_body_areas,
//                textUV = R.string.navbar_uv_index,
//                textKnowMore = R.string.navbar_know_more
//            )
//            }
//        ) {
//            padding ->
//            ProfileView(Modifier.padding(padding))
//        }
//    }
//}

//---------------------previews---------------------
//screen preview
//@Preview(showBackground = true, device = Devices.DEFAULT)
//@Composable
//fun ProfileViewPreview(){
//    DermatologyAppTheme {
//        ProfileView()
//    }
//}

//screen with navbar preview
//@Preview(showBackground = true, device = Devices.DEFAULT)
//@Composable
//fun ProfileScreenCompletePreview(){
//    ProfileScreenComplete()
//}