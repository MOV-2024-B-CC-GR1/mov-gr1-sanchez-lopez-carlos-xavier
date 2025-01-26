package com.example.deber_01

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.deber_01.models.Auto
import com.example.deber_01.services.CRUD

class CrearAutoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_auto)

        val edtMarca: EditText = findViewById(R.id.edtMarca)
        val edtModelo: EditText = findViewById(R.id.edtModelo)
        val edtAnio: EditText = findViewById(R.id.edtAnio)
        val edtPrecio: EditText = findViewById(R.id.edtPrecio)
        val edtDisponible: EditText = findViewById(R.id.edtDisponible)
        val spinnerConcesionarias: Spinner = findViewById(R.id.spinnerConcesionarias)

        val btnCrear: Button = findViewById(R.id.btnCrear)
        CRUD.inicializar(this)

        // Poblar el Spinner con los nombres de las concesionarias
        val concesionarias = CRUD.leerConcesionarias().map { it.nombre }
        Log.d("CrearAutoActivity", "Concesionarias disponibles: $concesionarias")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, concesionarias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerConcesionarias.adapter = adapter

        btnCrear.setOnClickListener {
            val marca = edtMarca.text.toString()
            val modelo = edtModelo.text.toString()
            val anio = edtAnio.text.toString().toIntOrNull()
            val precio = edtPrecio.text.toString().toDoubleOrNull()
            val disponible = edtDisponible.text.toString().toBoolean()

            if (marca.isNotEmpty() && modelo.isNotEmpty() && anio != null && precio != null) {
                val auto = Auto(marca, modelo, anio, precio, disponible)
                // Aquí deberías definir la concesionaria a la que se asociará este auto

                // Obtener la concesionaria seleccionada
                val concesionariaSeleccionada = spinnerConcesionarias.selectedItem.toString()

                CRUD.crearAuto(concesionariaSeleccionada, auto)
                Toast.makeText(this, "Auto creado con éxito", Toast.LENGTH_SHORT).show()
                finish()  // Regresar a la pantalla anterior
            } else {
                Toast.makeText(this, "Por favor ingresa todos los datos correctamente", Toast.LENGTH_SHORT).show()
            }
        }
    }
}