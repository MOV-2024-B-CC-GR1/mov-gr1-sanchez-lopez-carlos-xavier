package com.example.deber_01.models

data class Concesionaria(
    val nombre: String,
    val direccion: String,
    var numeroAutos: Int,
    val fechaFundacion: String,
    val abierta: Boolean,
    val autos: MutableList<Auto> = mutableListOf()

)
