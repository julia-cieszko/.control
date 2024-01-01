package com.example.dermatologyapp.Profile

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Man3
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.dermatologyapp.Models.User
import com.example.dermatologyapp.Signin.UserData
import com.example.dermatologyapp.ui.theme.DermatologyAppTheme

//user name and risk factor
@Composable
fun UserInfo(
    userData: User?,
    onRiskClick: () -> Unit
){
    Column() {
        if(userData?.name != null) {
            Text(
                text = userData.name,
                style = MaterialTheme.typography.headlineMedium,
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { onRiskClick() }
        ) {
            Icon(
                imageVector = Icons.Filled.Favorite,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(
                text = "Informacje o ryzyku",
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun SignOutButton(
    onSignOut: () -> Unit
){
    IconButton(
        onClick = onSignOut,
        modifier = Modifier.padding(0.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Logout,
            contentDescription = "Base menu",
            modifier = Modifier
                .size(28.dp)
        )
    }
}

//profile user element
@Composable
fun ProfileUserElement(
    userData: User?,
    onSignOut: () -> Unit,
    onRiskClick: () -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        UserInfo(
            userData = userData,
            onRiskClick = onRiskClick
        )
        SignOutButton(onSignOut)
    }
}

//switcher list-body map
@Composable
fun ListBodySwitcher(
    modifier: Modifier = Modifier,
    isList: Boolean,
    onListChange: () -> Unit,
    isBodyMap: Boolean,
    onBodyMapChange: () -> Unit
){
    val listButtonColor = if (isList) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.secondary
    }

    val mapButtonColor = if (isBodyMap) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.secondary
    }

    Row(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ){
        //list button
        Button(
            onClick = onListChange,
            colors = ButtonDefaults.buttonColors(containerColor = listButtonColor),
            modifier = Modifier.width(150.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = null
                )
                Text(
                    text = "Lista"
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        //body map button
        Button(
            onClick = onBodyMapChange,
            colors = ButtonDefaults.buttonColors(containerColor = mapButtonColor),
            modifier = Modifier.width(150.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    imageVector = Icons.Filled.Man3,
                    contentDescription = null
                )
                Text(
                    text = "Mapa"
                )
            }
        }
    }
}

//---------------------previews---------------------
//@Preview(showBackground = true)
//@Composable
//fun UserInfoPreview(){
//    DermatologyAppTheme {
//        UserInfo()
//    }
//}

//list and body map switcher preview
//@Preview(showBackground = true)
//@Composable
//fun ListBodySwitcherPreview(){
//    DermatologyAppTheme {
//        ListBodySwitcher()
//    }
//}

//user profile element preview
@Preview(showBackground = true)
@Composable
fun ProfileUserElementPreview(){
    DermatologyAppTheme {
        ProfileUserElement(userData = null, onSignOut = {}, onRiskClick = {})
    }
}