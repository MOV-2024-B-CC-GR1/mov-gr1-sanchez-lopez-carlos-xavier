package com.example.deber

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.deber.models.Concesionaria

class CrearAutoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_auto)

        val marcaEditText: EditText = findViewById(R.id.editTextMarca)
        val modeloEditText: EditText = findViewById(R.id.editTextModelo)
        val anioEditText: EditText = findViewById(R.id.editTextAnio)
        val precioEditText: EditText = findViewById(R.id.editTextPrecio)
        val disponibleCheckBox: CheckBox = findViewById(R.id.checkBoxDisponible)
        val concesionariaSpinner: Spinner = findViewById(R.id.spinnerConcesionarias)
        val btnCrearAuto: Button = findViewById(R.id.btnCrearAuto)

        val dbHelper = DBHelper(this)
        val db = dbHelper.readableDatabase

        // Obtener la lista de concesionarias
        val concesionarias = obtenerConcesionarias(db)

        // Crear un adaptador para el Spinner con las concesionarias
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, concesionarias)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        concesionariaSpinner.adapter = adapter

        btnCrearAuto.setOnClickListener {
            val marca = marcaEditText.text.toString()
            val modelo = modeloEditText.text.toString()
            val anioStr = anioEditText.text.toString()
            val precioStr = precioEditText.text.toString()
            val concesionariaSeleccionada = concesionarias[concesionariaSpinner.selectedItemPosition]
            val disponible = disponibleCheckBox.isChecked

            if (marca.isNotEmpty() && modelo.isNotEmpty() && anioStr.isNotEmpty() && precioStr.isNotEmpty()) {
                val anio = anioStr.toIntOrNull()  // Convertir a Int de forma segura
                val precio = precioStr.toDoubleOrNull()  // Convertir a Double de forma segura

                if (anio != null && precio != null) {
                    val contentValues = ContentValues().apply {
                        put(AutoTable.COLUMN_MARCA, marca)
                        put(AutoTable.COLUMN_MODELO, modelo)
                        put(AutoTable.COLUMN_ANIO, anio)
                        put(AutoTable.COLUMN_PRECIO, precio)
                        put(AutoTable.COLUMN_DISPONIBLE, if (disponible) 1 else 0)
                        put(AutoTable.COLUMN_CONCESIONARIA_ID, concesionariaSeleccionada.id)
                    }

                    val rowId = db.insert(AutoTable.TABLE_NAME, null, contentValues)
                    if (rowId != -1L) {
                        actualizarNumeroAutos(concesionariaSeleccionada.id, db)
                        Toast.makeText(this, "Auto creado exitosamente", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Error al crear el auto", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Por favor ingresa valores válidos para el año y precio", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Función para obtener la lista de concesionarias
    private fun obtenerConcesionarias(db: SQLiteDatabase): List<Concesionaria> {
        val concesionarias = mutableListOf<Concesionaria>()
        val cursor = db.query(
            ConcesionariaTable.TABLE_NAME,
            arrayOf(ConcesionariaTable.COLUMN_ID, ConcesionariaTable.COLUMN_NOMBRE), // Selecciona solo los campos necesarios
            null,
            null,
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(ConcesionariaTable.COLUMN_ID))
                val nombre = cursor.getString(cursor.getColumnIndex(ConcesionariaTable.COLUMN_NOMBRE))

                val concesionaria = Concesionaria(
                    id = id,
                    nombre = nombre,
                    direccion = "",  // Dejar estos campos vacíos si no son necesarios
                    numeroAutos = 0,
                    fechaFundacion = "",
                    abierta = false
                )

                concesionarias.add(concesionaria)
            } while (cursor.moveToNext())
        }
        cursor.close()

        return concesionarias
    }
    private fun actualizarNumeroAutos(concesionariaId: Long, db: SQLiteDatabase) {
        val sqlUpdate = """
        UPDATE ${ConcesionariaTable.TABLE_NAME}
        SET ${ConcesionariaTable.COLUMN_NUMERO_AUTOS} = 
            (SELECT COUNT(*) FROM ${AutoTable.TABLE_NAME} WHERE ${AutoTable.COLUMN_CONCESIONARIA_ID} = ?)
        WHERE ${ConcesionariaTable.COLUMN_ID} = ?
    """
        val stmt = db.compileStatement(sqlUpdate)
        stmt.bindLong(1, concesionariaId)
        stmt.bindLong(2, concesionariaId)
        stmt.executeUpdateDelete()
    }


}