package models

import java.time.LocalDate

data class Concesionaria(
    val nombre: String,
    val direccion: String,
    var numeroAutos: Int,
    val fechaFundacion: LocalDate,
    val abierta: Boolean,
    val autos: MutableList<Auto> = mutableListOf()

)
