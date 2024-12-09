package services

import models.Auto
import models.Concesionaria
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter



object CRUD {
    private val archivoConcesionarias = File("src/main/resources/concesionarias.txt")
    private val archivoAutos = File("src/main/resources/autos.txt")

    // Crear una concesionaria
    fun crearConcesionaria(concesionaria: Concesionaria) {
        archivoConcesionarias.appendText(concesionaria.toFileString() + "\n")
    }

    // Crear un auto para una concesionaria específica
    fun crearAuto(concesionariaNombre: String, auto: Auto) {
        val concesionarias = leerConcesionarias()
        val concesionaria = concesionarias.find { it.nombre == concesionariaNombre }
        concesionaria?.let {
            it.autos.add(auto)
            it.numeroAutos = it.autos.size
            guardarConcesionarias(concesionarias)
            archivoAutos.appendText("${concesionariaNombre}|${auto.toFileString()}\n")
        }
    }

    // Leer todas las concesionarias
    fun leerConcesionarias(): MutableList<Concesionaria> {
        if (!archivoConcesionarias.exists()) return mutableListOf()
        return archivoConcesionarias.readLines().map { line ->
            val partes = line.split("|")
            Concesionaria(
                partes[0],
                partes[1],
                partes[2].toInt(),
                LocalDate.parse(partes[3], DateTimeFormatter.ISO_DATE),
                partes[4].toBoolean()
            )
        }.toMutableList()
    }

    // Leer autos de una concesionaria
    fun leerAutos(concesionariaNombre: String): List<Auto> {
        if (!archivoAutos.exists()) return emptyList()
        return archivoAutos.readLines()
            .filter { it.startsWith("$concesionariaNombre|") }
            .map { line ->
                val partes = line.split("|").drop(1)
                Auto(
                    partes[0],
                    partes[1],
                    partes[2].toInt(),
                    partes[3].toDouble(),
                    partes[4].toBoolean()
                )
            }
    }

    // Actualizar concesionaria
    fun actualizarConcesionaria(nombre: String, nuevaConcesionaria: Concesionaria) {
        val concesionarias = leerConcesionarias()
        val index = concesionarias.indexOfFirst { it.nombre == nombre }
        if (index != -1) {
            concesionarias[index] = nuevaConcesionaria
            guardarConcesionarias(concesionarias)
        }
    }

    // Actualizar auto
    fun actualizarAuto(concesionariaNombre: String, modelo: String, nuevoAuto: Auto) {
        val concesionarias = leerConcesionarias()
        val concesionaria = concesionarias.find { it.nombre == concesionariaNombre }
        concesionaria?.let {
            val index = it.autos.indexOfFirst { auto -> auto.modelo == modelo }
            if (index != -1) {
                it.autos[index] = nuevoAuto
                it.numeroAutos = it.autos.size
                guardarConcesionarias(concesionarias)
                guardarAutos(concesionariaNombre, it.autos)
            }
        }
    }

    // Eliminar concesionaria
    fun eliminarConcesionaria(nombre: String) {
        val concesionarias = leerConcesionarias().filter { it.nombre != nombre }
        guardarConcesionarias(concesionarias)
        archivoAutos.writeText(
            archivoAutos.readLines().filterNot { it.startsWith("$nombre|") }.joinToString("\n")
        )
    }

    // Eliminar auto
    fun eliminarAuto(concesionariaNombre: String, modelo: String) {
        val concesionarias = leerConcesionarias()
        val concesionaria = concesionarias.find { it.nombre == concesionariaNombre }
        concesionaria?.let {
            it.autos.removeIf { auto -> auto.modelo == modelo }
            it.numeroAutos = it.autos.size
            guardarConcesionarias(concesionarias)
            guardarAutos(concesionariaNombre, it.autos)
        }
    }

    // Métodos auxiliares
    private fun guardarConcesionarias(concesionarias: List<Concesionaria>) {
        archivoConcesionarias.writeText(concesionarias.joinToString("\n") { it.toFileString() })
    }

    private fun guardarAutos(concesionariaNombre: String, autos: List<Auto>) {
        archivoAutos.writeText(
            archivoAutos.readLines()
                .filterNot { it.startsWith("$concesionariaNombre|") }
                .joinToString("\n") + "\n" +
                    autos.joinToString("\n") { "$concesionariaNombre|${it.toFileString()}" }
        )
    }

    private fun Concesionaria.toFileString(): String {
        return "$nombre|$direccion|$numeroAutos|${fechaFundacion.format(DateTimeFormatter.ISO_DATE)}|$abierta"
    }

    private fun Auto.toFileString(): String {
        return "$marca|$modelo|$anio|$precio|$disponible"
    }
}