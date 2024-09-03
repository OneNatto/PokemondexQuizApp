package com.example.pokedexquizapp.ui.result

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.pokedexquizapp.R
import com.example.pokedexquizapp.ui.quiz.QuizViewModel

@Composable
fun ResultScreen(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier,
    ) {
    val capturePokemonList = viewModel.capturePokemonList.collectAsState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.weight(2F))
        Text("捕まえたポケモン",
            fontSize = 21.sp
        )
        Spacer(modifier = Modifier.height(10.dp))
        Card(
            modifier = Modifier.padding(20.dp)
        ) {
            LazyVerticalGrid(
                verticalArrangement = Arrangement.Center,
                columns = GridCells.Fixed(3)
            ) {
                items(capturePokemonList.value) { capturePokemon ->
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .data(capturePokemon.frontImageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "",
                        error = painterResource(R.drawable.ic_broken_image),
                        placeholder = painterResource(R.drawable.loading_img),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.padding(10.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.weight(1F))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = painterResource(R.drawable.pokeball_1594373_640),
                contentDescription = "モンスターボール",
                modifier = Modifier
                    .size(60.dp)
            )
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )
                    ) {
                        append("${capturePokemonList.value.size}")
                    }

                    append(" /5")
                },
                style = TextStyle(fontSize = 26.sp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedButton(
            onClick = { viewModel.tryAgain() },
            modifier = Modifier.fillMaxWidth(0.7F)
        ) {
            Text("もう1回",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
                )
        }
        Spacer(modifier = Modifier.weight(2F))
    }
}
