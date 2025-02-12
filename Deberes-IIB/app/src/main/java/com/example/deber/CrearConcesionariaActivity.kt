package com.example.deber

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class CrearConcesionariaActivity : AppCompatActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitud: Double = 0.0
    private var longitud: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_concesionaria)

        val nombreEditText: EditText = findViewById(R.id.editTextNombre)
        val direccionEditText: EditText = findViewById(R.id.editTextDireccion)
        val fechaFundacionEditText: EditText = findViewById(R.id.editTextFechaFundacion)
        val abiertaCheckBox: CheckBox = findViewById(R.id.checkBoxAbierta)
        val btnCrearConcesionaria: Button = findViewById(R.id.btnCrearConcesionaria)

        // Inicializar el proveedor de ubicación
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Obtener la ubicación actual
        obtenerUbicacionActual()

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
                    put(ConcesionariaTable.COLUMN_LATITUD, latitud)  // Guardar latitud
                    put(ConcesionariaTable.COLUMN_LONGITUD, longitud) // Guardar longitud
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

    private fun obtenerUbicacionActual() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }

        val locationRequest = com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY

        fusedLocationClient.getCurrentLocation(locationRequest, null).addOnSuccessListener { location: Location? ->
            if (location != null) {
                latitud = location.latitude
                longitud = location.longitude
                Toast.makeText(this, "Ubicación obtenida: $latitud, $longitud", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No se pudo obtener la ubicación en tiempo real", Toast.LENGTH_SHORT).show()
            }
        }
    }

}