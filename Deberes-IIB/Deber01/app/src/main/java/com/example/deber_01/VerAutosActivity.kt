package com.example.deber_01

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.example.deber_01.services.CRUD

class VerAutosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_autos)

        // Referencias a los componentes
        val spinnerConcesionarias: Spinner = findViewById(R.id.spinnerConcesionarias)
        val tvAutos: TextView = findViewById(R.id.tvAutos)

        CRUD.inicializar(this)

        // Obtener la lista de concesionarias (esto debe estar en tu implementación)
        val concesionarias = CRUD.leerConcesionarias().map { it.nombre }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, concesionarias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerConcesionarias.adapter = adapter

        // Configurar el listener para cambios en el Spinner
        spinnerConcesionarias.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                // Obtener la concesionaria seleccionada
                val selectedConcesionaria = spinnerConcesionarias.selectedItem.toString()

                // Obtener los autos correspondientes a la concesionaria seleccionada
                val autos = CRUD.leerAutos(selectedConcesionaria)

                // Mostrar los autos en el TextView
                if (autos.isNotEmpty()) {
                    val sb = StringBuilder()
                    for (auto in autos) {
                        sb.append("Marca: ${auto.marca}\n")
                        sb.append("Modelo: ${auto.modelo}\n")
                        sb.append("Año: ${auto.anio}\n")
                        sb.append("Precio: ${auto.precio}\n")
                        sb.append("Disponible: ${if (auto.disponible) "Sí" else "No"}\n\n")
                    }
                    tvAutos.text = sb.toString()
                } else {
                    tvAutos.text = "No hay autos registrados para esta concesionaria."
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // No hacer nada si no hay selección
            }
        }

        // Llamar a onItemSelected inicialmente para mostrar los autos de la concesionaria seleccionada al inicio
        spinnerConcesionarias.setSelection(0)
    }
}
