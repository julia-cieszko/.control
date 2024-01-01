package com.example.dermatologyapp.BodyAreas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.dermatologyapp.ui.theme.DermatologyAppTheme

//przycisk dodaj nowy obszar
@Composable
fun AddAreaButton(
    modifier: Modifier = Modifier
){
    Button(
        onClick = { /*TODO*/ },
        colors = ButtonDefaults.buttonColors(containerColor  = MaterialTheme.colorScheme.primary),
        shape = CircleShape,
        modifier = modifier.size(45.dp),
        contentPadding = PaddingValues(1.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = null,
            modifier = Modifier.size(40.dp)
        )
    }
}

//rzad dodaj nowy obszar
@Composable
fun AddAreaRow(
    modifier: Modifier = Modifier
){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        AddAreaButton()
        Text(
            text = "Dodaj nowy obszar",
            style = MaterialTheme.typography.bodyLarge,
            modifier = modifier.padding(start = 16.dp)
        )
    }
}

//Karta obszaru
@Composable
fun AreaCard(
    modifier: Modifier = Modifier,
    text: String
){
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier
            .size(width = 200.dp, height = 50.dp)
    ){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                textAlign = TextAlign.Center
            )
        }
    }
}

//siatka z kartami obszaru
@Composable
fun AreaGrid(
    modifier: Modifier = Modifier,
    contentList: MutableList<String>
){
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.height(300.dp)
    ){
        items(contentList) { item -> 
            AreaCard(text = item) 
        }
    }
}

//calosc
@Composable
fun BodyAreasScreen(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AddAreaRow(modifier = Modifier.padding(vertical = 16.dp))
        AreaGrid(contentList = areaCardsContent, modifier = Modifier.padding(vertical = 16.dp))
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun BodyAreasScreen(
//    modifier: Modifier = Modifier
//){
//    DermatologyAppTheme {
//        Scaffold(
//            topBar = { TopNavigation() }
//        ) {
//                padding ->
//            BodyAreasScreenContent(Modifier.padding(padding))
//        }
//    }
//}

//previews
//przycisk
@Preview()
@Composable
fun AddAreaButtonPreview(){
    DermatologyAppTheme {
        AddAreaButton()
    }
}

//rzadek dodaj nowy obszar
@Preview()
@Composable
fun AddAreaRowPreview(){
    DermatologyAppTheme {
        AddAreaRow()
    }
}

//karta obszaru
@Preview()
@Composable
fun AreaCardPreview(){
    DermatologyAppTheme {
        AreaCard(text = "text")
    }
}

//siatka kart
@Preview()
@Composable
fun AreaGridPreview(){
    DermatologyAppTheme {
        AreaGrid(contentList = areaCardsContent)
    }
}

//zawartosc ekranu
//@Preview()
//@Composable
//fun BodyAreasScreenContentPreview(){
//    DermatologyAppTheme {
//        BodyAreasScreenContent()
//    }
//}

//z gornym navbarem
@Preview()
@Composable
fun BodyAreasScreenPreview(){
    BodyAreasScreen()
}