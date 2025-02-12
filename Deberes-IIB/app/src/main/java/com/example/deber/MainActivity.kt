package com.example.deber


import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnVerConcesionarias: Button = findViewById(R.id.btnVerConcesionarias)
        val btnVerAutos: Button = findViewById(R.id.btnVerAutos)
        val btnCrearConcesionaria: Button = findViewById(R.id.btnCrearConcesionaria)
        val btnCrearAuto: Button = findViewById(R.id.btnCrearAuto)
        val btnVerMapa: Button = findViewById(R.id.btnVerMapa)

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
        btnVerMapa.setOnClickListener {
            val intent = Intent(this, VerMapaActivity::class.java)

            startActivity(intent)
        }
        // Inicializar el proveedor de ubicación
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)

    }
}
