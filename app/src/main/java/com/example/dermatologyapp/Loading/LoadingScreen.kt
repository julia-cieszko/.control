package com.example.dermatologyapp.Loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.dermatologyapp.R
import com.example.dermatologyapp.ui.theme.DermatologyAppTheme

@Composable
fun LoadingScreen() {
    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(R.raw.loadinganimation)
    )

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LottieAnimation(
            composition = composition,
            modifier = Modifier.fillMaxSize(),
            iterations = LottieConstants.IterateForever
        )
    }
}

@Preview
@Composable
fun LoadingScreenPreview() {
    DermatologyAppTheme {
        LoadingScreen()
    }
}