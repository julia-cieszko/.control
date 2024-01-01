package com.example.dermatologyapp.Loading


import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun NoInternetScreen() {
    Text(
        text = "Brak połączenia z internetem",
        modifier = Modifier.fillMaxSize().wrapContentHeight(Alignment.CenterVertically),
        textAlign = TextAlign.Center,
        fontSize = 20.sp
    )
}