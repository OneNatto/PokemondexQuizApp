package com.example.pokedexquizapp.ui.quiz
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.example.pokedexquizapp.model.Pokemon
import com.example.pokedexquizapp.model.QuizLevel
import com.example.pokedexquizapp.model.QuizRegionScope
import com.example.pokedexquizapp.repository.PokemonApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random


@HiltViewModel
class QuizViewModel @Inject constructor(
    private val pokemonApiRepository: PokemonApiRepository
) : ViewModel() {
    private val _correctPokemon = MutableStateFlow<Pokemon?>(null)
    val correctPokemon:StateFlow<Pokemon?> = _correctPokemon.asStateFlow()

    private val _capturePokemonList = MutableStateFlow<List<Pokemon>>(emptyList())
    val capturePokemonList:StateFlow<List<Pokemon>> = _capturePokemonList.asStateFlow()

    private val _isAnswered = MutableStateFlow(false)
    val isAnswered :StateFlow<Boolean> = _isAnswered.asStateFlow()

    private val _quizList = MutableStateFlow<List<Pokemon>>(emptyList())
    val quizList :StateFlow<List<Pokemon>> = _quizList.asStateFlow()

    private val _quizCount = MutableStateFlow(0)
    val quizCount:StateFlow<Int> = _quizCount.asStateFlow()

    private val _quizLevel = MutableStateFlow(QuizLevel.EASY)
    val quizLevel:StateFlow<QuizLevel> = _quizLevel.asStateFlow()

    private val _quizRegionScope = MutableStateFlow(QuizRegionScope.KANTO)
    val quizRegionScope:StateFlow<QuizRegionScope> = _quizRegionScope.asStateFlow()

    init {
        viewModelScope.launch {
            startQuiz()
        }
    }

    fun judgeQuiz(userAnswer: Int) {
        _isAnswered.value = true

        //正解の時
        if(_correctPokemon.value?.id == userAnswer) {
            val capturedPokemon = _correctPokemon.value
            val updatedList = _capturePokemonList.value.toMutableList()
            capturedPokemon?.let {
                updatedList.add(it)
            }

            _capturePokemonList.value = updatedList
        }

        viewModelScope.launch {
            delay(2000)
            _quizCount.value += 1
            _isAnswered.value = false
            startQuiz()
        }
    }

    //地方を選択
    fun selectQuizRegionScope(selectedScope:QuizRegionScope) {
        _quizRegionScope.value = selectedScope
        viewModelScope.launch {
            resetQuiz()
        }
    }

    //難易度選択
    fun selectQuizLevel(quizLevel: QuizLevel) {
        _quizLevel.value = quizLevel
        viewModelScope.launch {
            resetQuiz()
        }
    }

    //もう1回
    fun tryAgain() {
        viewModelScope.launch {
            resetQuiz()
        }
    }

    private suspend fun startQuiz() {
        val correctPokemonId = generateNumber(_quizRegionScope.value)
        val correctPokemon = pokemonApiRepository.getPokemon(correctPokemonId)
        if(correctPokemon != null) {
            _correctPokemon.value = correctPokemon
            _quizList.value = generateQuizList(correctPokemon)
        }
    }

    private suspend fun generateQuizList(correctPokemon: Pokemon) : List<Pokemon> {
        val pokemonSet = mutableSetOf<Pokemon>()
        //正解のポケモンを追加
        pokemonSet.add(correctPokemon)

        while(pokemonSet.size < 4) {
            val randomId = generateNumber(_quizRegionScope.value)
            val randomPokemon = pokemonApiRepository.getPokemon(randomId)
            if(randomPokemon != null) {
                pokemonSet.add(randomPokemon)
            }
        }

        return pokemonSet.shuffled()
    }

    private fun generateNumber(quizRegionScope: QuizRegionScope) :Int {
        return when(quizRegionScope) {
            QuizRegionScope.KANTO -> Random.nextInt(1,151)
            QuizRegionScope.JOHTO -> Random.nextInt(152,251)
            QuizRegionScope.HOENN -> Random.nextInt(252,386)
            QuizRegionScope.SINNOH -> Random.nextInt(387,493)
            QuizRegionScope.UNOVA -> Random.nextInt(494,649)
            QuizRegionScope.KALOS -> Random.nextInt(650,721)
            QuizRegionScope.ALOLA -> Random.nextInt(722,809)
            QuizRegionScope.GALAR -> Random.nextInt(810,898)
            QuizRegionScope.PALDEA -> Random.nextInt(899,1000)
            QuizRegionScope.ALL -> Random.nextInt(1,1000)
        }
    }

    private suspend fun resetQuiz() {
        startQuiz()
        _capturePokemonList.value = emptyList()
        _quizCount.value = 0
    }
}