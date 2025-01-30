package com.example.deber.models

data class Concesionaria(
    val id: Long = 0,  // Añadir el campo id, con valor por defecto 0 (para la base de datos)
    val nombre: String,
    val direccion: String,
    var numeroAutos: Int,
    val fechaFundacion: String,
    val abierta: Boolean,
    val autos: MutableList<Auto> = mutableListOf()

){
    override fun toString(): String {
        return nombre // Muestra solo el nombre en el Spinner
    }
}