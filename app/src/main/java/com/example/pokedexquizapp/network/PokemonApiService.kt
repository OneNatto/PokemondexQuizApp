package com.example.pokedexquizapp.network

import com.example.pokedexquizapp.model.Pokemon
import com.example.pokedexquizapp.model.PokemonResponse
import com.example.pokedexquizapp.model.PokemonSpeciesResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonApiService {
    @GET("pokemon/{id}")
    suspend fun getPokemon(@Path("id") pokemonId: Int): PokemonResponse

    @GET("pokemon-species/{id}")
    suspend fun getPokemonDetails(@Path("id") pokemonId: Int): PokemonSpeciesResponse
}