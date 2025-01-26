package com.example.deber_01.services

import android.content.Context
import com.example.deber_01.models.Auto
import com.example.deber_01.models.Concesionaria
import java.io.File
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter


object CRUD {
    private lateinit var context: Context

    fun inicializar(contexto: Context) {
        context = contexto
        verificarArchivos()  // Asegura que los archivos se creen si no existen
    }

    private val archivoConcesionarias: File
        get() = File(context.filesDir, "concesionarias.txt")

    private val archivoAutos: File
        get() = File(context.filesDir, "autos.txt")


    fun verificarArchivos() {
        // Crear los archivos si no existen
        if (!archivoConcesionarias.exists()) {
            try {
                archivoConcesionarias.createNewFile() // Crea el archivo vacío si no existe
            } catch (e: IOException) {
                println("Error al crear el archivo de concesionarias: ${e.message}")
            }
        }

        if (!archivoAutos.exists()) {
            try {
                archivoAutos.createNewFile() // Crea el archivo vacío si no existe
            } catch (e: IOException) {
                println("Error al crear el archivo de autos: ${e.message}")
            }
        }
    }


    // Crear una concesionaria
    fun crearConcesionaria(concesionaria: Concesionaria) {
        try {
            val concesionarias = leerConcesionarias().toMutableList()
            if (concesionarias.any { it.nombre == concesionaria.nombre }) {
                throw IllegalArgumentException("La concesionaria ya existe.")
            }
            concesionarias.add(concesionaria)
            guardarConcesionarias(concesionarias)
        } catch (e: IOException) {
            println("Error al leer el archivo de concesionarias: ${e.message}")
        } catch (e: IllegalArgumentException) {
            println(e.message)
        }
    }

    // Crear un auto para una concesionaria específica
    fun crearAuto(concesionariaNombre: String, auto: Auto) {
        try {
            val concesionarias = leerConcesionarias()
            val concesionaria = concesionarias.find { it.nombre == concesionariaNombre }
                ?: throw IllegalArgumentException("La concesionaria no existe.")
            if (concesionaria.autos.any { it.modelo == auto.modelo }) {
                throw IllegalArgumentException("El auto con este modelo ya existe en la concesionaria.")
            }
            concesionaria.autos.add(auto)
            concesionaria.numeroAutos = concesionaria.autos.size
            guardarConcesionarias(concesionarias)
            archivoAutos.appendText("${concesionariaNombre}|${auto.toFileString()}\n")
        } catch (e: IOException) {
            println("Error al leer el archivo de autos: ${e.message}")
        } catch (e: IllegalArgumentException) {
            println(e.message)
        }
    }


    // Leer todas las concesionarias
    fun leerConcesionarias(): MutableList<Concesionaria> {
        return try {
            if (!archivoConcesionarias.exists()) return mutableListOf()
            archivoConcesionarias.readLines().map { line ->
                val partes = line.split("|")
                Concesionaria(
                    partes[0],
                    partes[1],
                    partes[2].toInt(),
                    LocalDate.parse(partes[3], DateTimeFormatter.ISO_DATE).toString(),
                    partes[4].toBoolean()
                )
            }.toMutableList()
        } catch (e: IOException) {
            println("Error al leer el archivo de concesionarias: ${e.message}")
            mutableListOf()
        }
    }

    // Leer autos de una concesionaria
    fun leerAutos(concesionariaNombre: String): List<Auto> {
        return try {
            if (!archivoAutos.exists()) return emptyList()
            archivoAutos.readLines()
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
        } catch (e: IOException) {
            println("Error al leer el archivo de autos: ${e.message}")
            emptyList()
        }
    }

    // Actualizar concesionaria
    fun actualizarConcesionaria(nombre: String, nuevaConcesionaria: Concesionaria) {
        try {
            val concesionarias = leerConcesionarias()
            val index = concesionarias.indexOfFirst { it.nombre == nombre }
            if (index != -1) {
                concesionarias[index] = nuevaConcesionaria
                guardarConcesionarias(concesionarias)
            } else {
                throw IllegalArgumentException("Concesionaria no encontrada.")
            }
        } catch (e: IOException) {
            println("Error al actualizar concesionaria: ${e.message}")
        } catch (e: IllegalArgumentException) {
            println(e.message)
        }
    }

    // Actualizar auto
    fun actualizarAuto(concesionariaNombre: String, modelo: String, nuevoAuto: Auto) {
        try {
            val autos = leerAutos(concesionariaNombre).toMutableList()
            val index = autos.indexOfFirst { it.modelo == modelo }
            if (index != -1) {
                autos[index] = nuevoAuto
                guardarAutos(concesionariaNombre, autos)
            } else {
                throw IllegalArgumentException("Auto no encontrado.")
            }
        } catch (e: IOException) {
            println("Error al actualizar auto: ${e.message}")
        } catch (e: IllegalArgumentException) {
            println(e.message)
        }
    }

    // Eliminar concesionaria
    fun eliminarConcesionaria(nombre: String) {
        try {
            val concesionarias = leerConcesionarias().filter { it.nombre != nombre }
            guardarConcesionarias(concesionarias)
            archivoAutos.writeText(
                archivoAutos.readLines().filterNot { it.startsWith("$nombre|") }.joinToString("\n")
            )
        } catch (e: IOException) {
            println("Error al eliminar concesionaria: ${e.message}")
        }
    }

    // Eliminar auto
    fun eliminarAuto(concesionariaNombre: String, modelo: String) {
        try {
            val concesionarias = leerConcesionarias()
            val concesionaria = concesionarias.find { it.nombre == concesionariaNombre }
                ?: throw IllegalArgumentException("Concesionaria no encontrada.")
            concesionaria.autos.removeIf { it.modelo == modelo }
            concesionaria.numeroAutos = concesionaria.autos.size
            guardarConcesionarias(concesionarias)
            guardarAutos(concesionariaNombre, concesionaria.autos)
        } catch (e: IOException) {
            println("Error al eliminar auto: ${e.message}")
        } catch (e: IllegalArgumentException) {
            println(e.message)
        }
    }

    // Métodos auxiliares
    private fun guardarConcesionarias(concesionarias: List<Concesionaria>) {
        try {
            archivoConcesionarias.writeText(concesionarias.joinToString("\n") { it.toFileString() })
        } catch (e: IOException) {
            println("Error al guardar concesionarias: ${e.message}")
        }
    }

    private fun guardarAutos(concesionariaNombre: String, autos: List<Auto>) {
        try {
            // Primero, eliminamos las entradas de la concesionaria en autos.txt
            val lineasRestantes = archivoAutos.readLines()
                .filterNot { it.startsWith("$concesionariaNombre|") }

            // Luego, agregamos las nuevas líneas de autos para esa concesionaria
            val nuevasLineas = autos.joinToString("\n") { "$concesionariaNombre|${it.toFileString()}" }

            // Escribimos el archivo de autos con los datos actualizados
            archivoAutos.writeText((lineasRestantes + nuevasLineas).joinToString("\n"))
        } catch (e: IOException) {
            println("Error al guardar autos: ${e.message}")
        }
    }

    private fun Concesionaria.toFileString(): String {
        return "$nombre|$direccion|$numeroAutos|${fechaFundacion.format(DateTimeFormatter.ISO_DATE)}|$abierta"
    }

    private fun Auto.toFileString(): String {
        return "$marca|$modelo|$anio|$precio|$disponible"
    }
}
