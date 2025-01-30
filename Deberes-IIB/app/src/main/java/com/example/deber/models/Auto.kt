package com.example.deber.models

data class Auto(
    val id: Long,
    val marca: String,
    val modelo: String,
    val anio: Int,
    val precio: Double,
    val disponible: Boolean,
    val concesionariaId: Long
)