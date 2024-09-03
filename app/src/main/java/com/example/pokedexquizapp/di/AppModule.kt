package com.example.pokedexquizapp.di

import com.example.pokedexquizapp.network.PokemonApiService
import com.example.pokedexquizapp.repository.PokemonApiRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val baseUrl = "https://pokeapi.co/api/v2/"

        return  Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providePokemonApiServices(retrofit: Retrofit): PokemonApiService {
        return retrofit.create(PokemonApiService::class.java)
    }

    @Provides
    @Singleton
    fun providePokemonApiRepository(apiService: PokemonApiService): PokemonApiRepository {
        return PokemonApiRepository(apiService)
    }
}