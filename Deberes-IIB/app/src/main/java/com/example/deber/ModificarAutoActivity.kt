package com.example.deber

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.deber.models.Auto

class ModificarAutoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modificar_auto)

        val marcaEditText: EditText = findViewById(R.id.editTextMarca)
        val modeloEditText: EditText = findViewById(R.id.editTextModelo)
        val anioEditText: EditText = findViewById(R.id.editTextAnio)
        val precioEditText: EditText = findViewById(R.id.editTextPrecio)
        val disponibleCheckBox: CheckBox = findViewById(R.id.checkBoxDisponible)
        val btnGuardar: Button = findViewById(R.id.btnGuardar)

        // Obtener los datos del auto desde el intent
        val autoId = intent.getLongExtra("auto_id", -1)
        val marca = intent.getStringExtra("marca") ?: ""
        val modelo = intent.getStringExtra("modelo") ?: ""
        val anio = intent.getIntExtra("anio", 0)
        val precio = intent.getDoubleExtra("precio", 0.0)
        val disponible = intent.getBooleanExtra("disponible", false)

        // Mostrar los datos en los campos
        marcaEditText.setText(marca)
        modeloEditText.setText(modelo)
        anioEditText.setText(anio.toString())
        precioEditText.setText(precio.toString())
        disponibleCheckBox.isChecked = disponible

        btnGuardar.setOnClickListener {
            // Lógica para guardar los cambios
            Toast.makeText(this, "Auto modificado", Toast.LENGTH_SHORT).show()
            finish() // Cerrar la actividad
        }
    }
}