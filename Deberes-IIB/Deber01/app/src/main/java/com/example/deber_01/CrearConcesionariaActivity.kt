package com.example.deber_01

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.deber_01.models.Concesionaria
import com.example.deber_01.services.CRUD

class CrearConcesionariaActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_concesionaria)

        val edtNombre: EditText = findViewById(R.id.edtNombre)
        val edtDireccion: EditText = findViewById(R.id.edtDireccion)
        val edtFechaFundacion: EditText = findViewById(R.id.edtFechaFundacion)  // Fecha en formato YYYY-MM-DD
        val edtAbierta: EditText = findViewById(R.id.edtAbierta)  // "true" o "false"
        val btnCrear: Button = findViewById(R.id.btnCrear)
        CRUD.inicializar(this)

        btnCrear.setOnClickListener {
            val nombre = edtNombre.text.toString()
            val direccion = edtDireccion.text.toString()
            val fechaFundacion = edtFechaFundacion.text.toString()
            val abierta = edtAbierta.text.toString().toBoolean()

            if (nombre.isNotEmpty() && direccion.isNotEmpty() && fechaFundacion.isNotEmpty()) {
                try {
                    val concesionaria = Concesionaria(
                        nombre,
                        direccion,
                        0,
                        fechaFundacion,
                        abierta
                    )
                    CRUD.crearConcesionaria(concesionaria)
                    Toast.makeText(this, "Concesionaria creada con éxito", Toast.LENGTH_SHORT).show()
                    finish()  // Regresar a la pantalla anterior
                } catch (e: IllegalArgumentException) {
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor ingresa todos los datos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}