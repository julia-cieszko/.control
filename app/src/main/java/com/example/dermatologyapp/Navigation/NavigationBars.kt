package com.example.dermatologyapp.Navigation

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DashboardCustomize
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.dermatologyapp.R
import com.example.dermatologyapp.ui.theme.DarkBlue
import com.example.dermatologyapp.ui.theme.DermatologyAppTheme


@Composable
fun BottomNavigation(
    navController: NavHostController
) {
    var selectedState by rememberSaveable { mutableStateOf(navController.currentBackStackEntry?.destination?.route) }
    if (true){
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.secondary,
        ) {
            //profile
            NavigationBarItem(
                selected = selectedState == "profile",
                onClick = {
                    navController.navigate("profile")
                    selectedState = navController.currentBackStackEntry?.destination?.route
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = "Profil",
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = DarkBlue,
                    selectedTextColor = DarkBlue,
                    indicatorColor = MaterialTheme.colorScheme.secondary
                )
            )

            //add
            NavigationBarItem(
                selected = selectedState == "add",
                onClick = {
                    navController.navigate("add")
                    selectedState = navController.currentBackStackEntry?.destination?.route
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = "Kamera",
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = DarkBlue,
                    selectedTextColor = DarkBlue,
                    indicatorColor = MaterialTheme.colorScheme.secondary
                )
            )

            //know more
            NavigationBarItem(
                selected = selectedState == "learn_more",
                onClick = {
                    navController.navigate("learn_more")
                    selectedState = navController.currentBackStackEntry?.destination?.route
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = null
                    )
                },
                label = {
                    Text(
                        text = "Baza wiedzy",
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = DarkBlue,
                    selectedTextColor = DarkBlue,
                    indicatorColor = MaterialTheme.colorScheme.secondary
                )
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun BottomNavigationPreview(){
//    DermatologyAppTheme {
//        BottomNavigation()
//    }
//}

