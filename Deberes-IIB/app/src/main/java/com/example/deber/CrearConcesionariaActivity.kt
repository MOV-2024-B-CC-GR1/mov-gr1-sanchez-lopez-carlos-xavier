package com.example.deber

import android.content.ContentValues
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CrearConcesionariaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_concesionaria)

        val nombreEditText: EditText = findViewById(R.id.editTextNombre)
        val direccionEditText: EditText = findViewById(R.id.editTextDireccion)
        val fechaFundacionEditText: EditText = findViewById(R.id.editTextFechaFundacion)
        val abiertaCheckBox: CheckBox = findViewById(R.id.checkBoxAbierta)
        val btnCrearConcesionaria: Button = findViewById(R.id.btnCrearConcesionaria)

        val dbHelper = DBHelper(this)
        val db = dbHelper.writableDatabase

        btnCrearConcesionaria.setOnClickListener {
            val nombre = nombreEditText.text.toString()
            val direccion = direccionEditText.text.toString()
            val fechaFundacion = fechaFundacionEditText.text.toString()
            val abierta = abiertaCheckBox.isChecked

            if (nombre.isNotEmpty() && direccion.isNotEmpty() && fechaFundacion.isNotEmpty()) {
                val contentValues = ContentValues().apply {
                    put(ConcesionariaTable.COLUMN_NOMBRE, nombre)
                    put(ConcesionariaTable.COLUMN_DIRECCION, direccion)
                    put(ConcesionariaTable.COLUMN_FECHA_FUNDACION, fechaFundacion)
                    put(ConcesionariaTable.COLUMN_ABIERTA, if (abierta) 1 else 0)
                }

                val rowId = db.insert(ConcesionariaTable.TABLE_NAME, null, contentValues)
                if (rowId != -1L) {
                    Toast.makeText(this, "Concesionaria creada exitosamente", Toast.LENGTH_SHORT).show()
                    finish() // Cierra la actividad y regresa a la pantalla principal
                } else {
                    Toast.makeText(this, "Error al crear concesionaria", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            }
        }
    }
}