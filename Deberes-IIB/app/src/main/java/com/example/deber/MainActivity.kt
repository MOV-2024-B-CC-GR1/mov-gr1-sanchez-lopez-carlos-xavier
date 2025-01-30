package com.example.deber


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnVerConcesionarias: Button = findViewById(R.id.btnVerConcesionarias)
        val btnVerAutos: Button = findViewById(R.id.btnVerAutos)
        val btnCrearConcesionaria: Button = findViewById(R.id.btnCrearConcesionaria)
        val btnCrearAuto: Button = findViewById(R.id.btnCrearAuto)

        val dbHelper = DBHelper(this)
        val db = dbHelper.writableDatabase

        btnVerConcesionarias.setOnClickListener {
            val intent = Intent(this, VerConcesionariasActivity::class.java)
            startActivity(intent)
        }

        btnVerAutos.setOnClickListener {
            val intent = Intent(this, VerAutosActivity::class.java)
            startActivity(intent)
        }

        btnCrearConcesionaria.setOnClickListener {
            val intent = Intent(this, CrearConcesionariaActivity::class.java)
            startActivity(intent)
        }

        btnCrearAuto.setOnClickListener {
            val intent = Intent(this, CrearAutoActivity::class.java)
            startActivity(intent)
        }
    }
}
