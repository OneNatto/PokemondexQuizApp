package com.example.pokedexquizapp.repository

import androidx.collection.intLongMapOf
import com.example.pokedexquizapp.model.Pokemon
import com.example.pokedexquizapp.network.PokemonApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class PokemonApiRepository @Inject constructor(
    private val apiService: PokemonApiService
) {

    suspend fun getPokemon(pokemonId: Int) : Pokemon? {
        return withContext(Dispatchers.IO) {
            try {
                val pokemonResponse = apiService.getPokemon(pokemonId)

                val pokemonSpeciesResponse = apiService.getPokemonDetails(pokemonId)

                //日本語名取得
                val japaneseName = pokemonSpeciesResponse.names.find { it.language.name == "ja" }?.name

                //特性取得
                val flavorText = pokemonSpeciesResponse.flavors.find { it.language.name == "ja" }?.flavor

                val id = pokemonResponse.id
                val frontImageUrl = pokemonResponse.sprites.other.officialArtwork.frontImageUrl
                val backImageUrl = pokemonResponse.sprites.backImageUrl


                println(japaneseName)
                println(pokemonResponse)
                println("テスト")

                Pokemon(
                    id = id,
                    name = japaneseName ?: "",
                    flavor = flavorText ?: "",
                    frontImageUrl = frontImageUrl,
                    backImageUrl = backImageUrl
                )
            } catch(e: Exception) {
                e.printStackTrace()
                println("エラー：$e")
                null
            }
        }
    }
}