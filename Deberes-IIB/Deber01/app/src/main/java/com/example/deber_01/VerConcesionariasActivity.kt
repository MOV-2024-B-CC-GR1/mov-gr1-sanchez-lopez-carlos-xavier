package com.example.deber_01

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.deber_01.services.CRUD

class VerConcesionariasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_concesionarias)

        val tvConcesionarias: TextView = findViewById(R.id.tvConcesionarias)
        CRUD.inicializar(this)

        // Obtener la lista de concesionarias
        val concesionarias = CRUD.leerConcesionarias()

        // Mostrar concesionarias en el TextView
        if (concesionarias.isNotEmpty()) {
            val sb = StringBuilder()
            for (concesionaria in concesionarias) {
                sb.append("Nombre: ${concesionaria.nombre}\n")
                sb.append("Dirección: ${concesionaria.direccion}\n")
                sb.append("Número de Autos: ${concesionaria.numeroAutos}\n")
                sb.append("Fecha de Fundación: ${concesionaria.fechaFundacion}\n")
                sb.append("Abierta: ${if (concesionaria.abierta) "Sí" else "No"}\n\n")
            }
            tvConcesionarias.text = sb.toString()
        } else {
            tvConcesionarias.text = "No hay concesionarias registradas."
        }
    }
}