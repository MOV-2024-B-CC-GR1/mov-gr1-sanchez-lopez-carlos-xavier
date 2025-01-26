package com.example.deber_01

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.deber_01.services.CRUD


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializamos el contexto una sola vez, aquí no hace falta usarlo directamente
        CRUD.inicializar(this)  // 'this' es el contexto de la actividad

        // Botón para ver concesionarias
        val btnVerConcesionarias: Button = findViewById(R.id.btnVerConcesionarias)
        btnVerConcesionarias.setOnClickListener {
            val intent = Intent(this, VerConcesionariasActivity::class.java)
            startActivity(intent)
        }

        // Botón para crear concesionarias
        val btnCrearConcesionaria: Button = findViewById(R.id.btnCrearConcesionaria)
        btnCrearConcesionaria.setOnClickListener {
            val intent = Intent(this, CrearConcesionariaActivity::class.java)
            startActivity(intent)
        }

        // Botón para ver autos
        val btnVerAutos: Button = findViewById(R.id.btnVerAutos)
        btnVerAutos.setOnClickListener {
            val intent = Intent(this, VerAutosActivity::class.java)
            startActivity(intent)
        }

        // Botón para crear auto
        val btnCrearAuto: Button = findViewById(R.id.btnCrearAuto)
        btnCrearAuto.setOnClickListener {
            val intent = Intent(this, CrearAutoActivity::class.java)
            startActivity(intent)
        }
    }
}
