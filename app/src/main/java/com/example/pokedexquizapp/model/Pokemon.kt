package com.example.pokedexquizapp.model

import com.google.gson.annotations.SerializedName

data class Pokemon(
    val id: Int,
    val name: String,
    val flavor: String,
    val frontImageUrl: String,
    val backImageUrl: String
)

//pokemon/{id}用
data class PokemonResponse (
    val id: Int,
    val sprites: Sprites
)

data class Sprites (
    @SerializedName("back_default") val backImageUrl: String,
    val other: OtherSprites
)

data class OtherSprites (
    @SerializedName("official-artwork") val officialArtwork: OfficialArtwork
)

data class OfficialArtwork (
    @SerializedName("front_default") val frontImageUrl: String
)

data class PokemonSpeciesResponse (
    val id: Int,
    val names: List<NameEntry>,
    @SerializedName("flavor_text_entries") val flavors: List<FlavorEntry>
)

data class FlavorEntry(
    @SerializedName("flavor_text") val flavor: String,
    val language: LanguageEntry
)

data class NameEntry(
    val name: String,
    val language: LanguageEntry
)

data class LanguageEntry(
    val name: String
)

