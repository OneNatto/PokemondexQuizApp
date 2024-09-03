package com.example.pokedexquizapp.ui.quiz

import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import com.example.pokedexquizapp.R
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.pokedexquizapp.model.QuizLevel
import com.example.pokedexquizapp.model.QuizRegionScope


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier,
) {
    val quizLevel = viewModel.quizLevel.collectAsState()
    var showQuizLevelDialog by remember{ mutableStateOf(false) }

    Scaffold(
        topBar = {
                 CenterAlignedTopAppBar(
                     title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Image(
                                painter = painterResource(R.drawable.pokeball_1594373_640),
                                contentDescription = "モンスターボール",
                                modifier = Modifier
                                    .size(50.dp)
                                    .padding(end = 5.dp)
                            )
                            Text("ポケモンクイズ")
                        }
                     }
                 )
        },
        floatingActionButton = {
            IconButton(
                onClick = { showQuizLevelDialog = !showQuizLevelDialog },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = Color.White,
                    containerColor = Color.DarkGray
                ),
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "クイズ難易度",
                    modifier = Modifier.size(30.dp)
                )
            }
        }
    ) {
        QuizContent(
            quizLevel = quizLevel.value,
            viewModel = viewModel,
            modifier = modifier
                .fillMaxSize()
                .padding(it),
        )

        if(showQuizLevelDialog) {
            AlertDialog(
                onDismissRequest = { showQuizLevelDialog = false },
                modifier = Modifier
                    .fillMaxWidth(0.7F)
                    .height(200.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceEvenly
                ) {
                    QuizLevel.entries.forEach { level ->
                        OutlinedButton(
                            onClick = {
                                viewModel.selectQuizLevel(level)
                                showQuizLevelDialog = false
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                level.title,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun QuizContent(
    quizLevel: QuizLevel,
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier
) {
    val correctPokemon = viewModel.correctPokemon.collectAsState()
    val isAnswered = viewModel.isAnswered.collectAsState()
    val quizList = viewModel.quizList.collectAsState()
    val capturePokemonList = viewModel.capturePokemonList.collectAsState()
    var userAnswer by remember{ mutableIntStateOf(0) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        QuizRegionScopeSelectionDropdown(
            viewModel,
            modifier = Modifier.fillMaxWidth(0.7F)
        )
        Spacer(modifier = Modifier.weight(1F))
        if(quizLevel == QuizLevel.EASY) {
            //後ろ姿クイズ
            Text("この後ろ姿は？", style = TextStyle(fontSize = 24.sp))
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .data(correctPokemon.value?.backImageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "",
                error = painterResource(R.drawable.ic_broken_image),
                placeholder = painterResource(R.drawable.loading_img),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(100.dp)
            )
        }else if(quizLevel == QuizLevel.NORMAL) {
            //説明文クイズ
            Text(correctPokemon.value?.flavor ?: "", style = TextStyle(fontSize = 20.sp))
            Spacer(modifier = Modifier.height(10.dp))
            Text("このポケモンは？", style = TextStyle(fontSize = 24.sp))
        } else if(quizLevel == QuizLevel.VERYHARD) {
            //図鑑NOクイズ
            Text("図鑑No.${correctPokemon.value?.id}のポケモンは？", style = TextStyle(fontSize = 26.sp))
        }
        Spacer(modifier = Modifier.weight(1F))
        LazyColumn {
            items(quizList.value) { pokemon ->
                OutlinedButton(
                    onClick = {
                        userAnswer = pokemon.id
                        viewModel.judgeQuiz(pokemon.id)
                    },
                    modifier = Modifier.fillMaxWidth(0.7F)
                ) {
                    if(isAnswered.value)
                        if(correctPokemon.value?.id == pokemon.id)
                            Text(
                                text =  "◯",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Red
                                )
                            ) else if(userAnswer == pokemon.id)
                            Text("×",
                                style = TextStyle(
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Blue
                                )
                            )
                    if(quizLevel != QuizLevel.EASY) {
                        AsyncImage(
                            model = ImageRequest.Builder(context = LocalContext.current)
                                .data(pokemon.frontImageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = "",
                            error = painterResource(R.drawable.ic_broken_image),
                            placeholder = painterResource(R.drawable.loading_img),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(70.dp)
                        )
                    }
                    Text(
                        pokemon.name,
                        style= TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        Spacer(modifier = Modifier.weight(1F))
        Text("正解したらポケモンGET！", fontWeight = FontWeight.Bold)
        Row(
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                painter = painterResource(R.drawable.pokeball_1594373_640),
                contentDescription = "モンスターボール",
                modifier = Modifier
                    .size(45.dp)
                    .padding(end = 5.dp)
            )
            Text(
                buildAnnotatedString {
                    append("現在の捕獲数：")

                    withStyle(
                        style = SpanStyle(
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )) {
                        append("${capturePokemonList.value.size}")
                    }

                    append(" /5")
                },
                style = TextStyle(fontSize = 18.sp)
            )        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizRegionScopeSelectionDropdown(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier
    ) {
    var expanded by remember { mutableStateOf(false) }
    val quizRegionScope = viewModel.quizRegionScope.collectAsState()

    // ドロップダウンメニューのアイテムリスト
    val quizScopeList = QuizRegionScope.entries.toList()

    Box(
        modifier = modifier
    ) {
        // 選択肢の表示
        OutlinedButton(
            onClick = { expanded = true },
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.DarkGray,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(quizRegionScope.value.regionName)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth(0.7F)
        ) {
            quizScopeList.forEach { region ->
                DropdownMenuItem(
                    onClick = {
                        viewModel.selectQuizRegionScope(region)
                        expanded = false
                    },
                    text = {
                        Text(region.regionName)
                    },
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}