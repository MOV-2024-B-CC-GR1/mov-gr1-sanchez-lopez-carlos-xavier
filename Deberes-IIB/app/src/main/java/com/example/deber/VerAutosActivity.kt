package com.example.deber

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deber.models.Auto
import com.example.deber.models.Concesionaria

class VerAutosActivity : AppCompatActivity() {

    private lateinit var db: SQLiteDatabase
    private lateinit var recyclerView: RecyclerView
    private lateinit var autoAdapter: AutoAdapter
    private lateinit var concesionariaSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_autos)

        concesionariaSpinner = findViewById(R.id.spinnerConcesionaria)
        recyclerView = findViewById(R.id.recyclerViewAutos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dbHelper = DBHelper(this)
        db = dbHelper.readableDatabase

        // Obtener la lista de concesionarias
        val concesionarias = obtenerConcesionarias(db)

        // Crear un adaptador para el Spinner con las concesionarias
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, concesionarias)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        concesionariaSpinner.adapter = spinnerAdapter

        // Configurar el listener del Spinner
        concesionariaSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val concesionariaSeleccionada = concesionarias[position]
                val autos = obtenerAutosPorConcesionaria(db, concesionariaSeleccionada.id)

                // Configurar el adaptador del RecyclerView con los listeners
                autoAdapter = AutoAdapter(
                    autos,
                    onModificarClickListener = { auto -> modificarAuto(auto) },
                    onEliminarClickListener = { auto -> eliminarAuto(auto) }
                )
                recyclerView.adapter = autoAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No hacer nada
            }
        }
    }

    private fun modificarAuto(auto: Auto) {
        // Lógica para modificar el auto
        Toast.makeText(this, "Modificar auto: ${auto.marca} ${auto.modelo}", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, ModificarAutoActivity::class.java).apply {
            putExtra("auto_id", auto.id)
            putExtra("marca", auto.marca)
            putExtra("modelo", auto.modelo)
            putExtra("anio", auto.anio)
            putExtra("precio", auto.precio)
            putExtra("disponible", auto.disponible)
        }
        startActivity(intent)
    }

    private fun eliminarAuto(auto: Auto) {
        // Lógica para eliminar el auto
        val dbHelper = DBHelper(this)
        val db = dbHelper.writableDatabase

        val rowsDeleted = db.delete(
            AutoTable.TABLE_NAME,
            "${AutoTable.COLUMN_ID} = ?",
            arrayOf(auto.id.toString())
        )

        if (rowsDeleted > 0) {
            Toast.makeText(this, "Auto eliminado", Toast.LENGTH_SHORT).show()
            // Actualizar la lista de autos
            val concesionariaSeleccionada = (concesionariaSpinner.selectedItem as Concesionaria)
            val autos = obtenerAutosPorConcesionaria(db, concesionariaSeleccionada.id)
            autoAdapter = AutoAdapter(
                autos,
                onModificarClickListener = { auto -> modificarAuto(auto) },
                onEliminarClickListener = { auto -> eliminarAuto(auto) }
            )
            recyclerView.adapter = autoAdapter
        } else {
            Toast.makeText(this, "Error al eliminar el auto", Toast.LENGTH_SHORT).show()
        }
    }

    private fun obtenerConcesionarias(db: SQLiteDatabase): List<Concesionaria> {
        val concesionarias = mutableListOf<Concesionaria>()
        val cursor = db.query(
            ConcesionariaTable.TABLE_NAME,
            arrayOf(ConcesionariaTable.COLUMN_ID, ConcesionariaTable.COLUMN_NOMBRE),
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
                    direccion = "",
                    numeroAutos = 0,
                    fechaFundacion = "",
                    abierta = false,
                    latitud = 0.0,
                    longitud = 0.0
                )

                concesionarias.add(concesionaria)
            } while (cursor.moveToNext())
        }
        cursor.close()

        return concesionarias
    }

    private fun obtenerAutosPorConcesionaria(db: SQLiteDatabase, concesionariaId: Long): List<Auto> {
        val autos = mutableListOf<Auto>()
        val cursor = db.query(
            AutoTable.TABLE_NAME,
            arrayOf(
                AutoTable.COLUMN_ID,
                AutoTable.COLUMN_MARCA,
                AutoTable.COLUMN_MODELO,
                AutoTable.COLUMN_ANIO,
                AutoTable.COLUMN_PRECIO,
                AutoTable.COLUMN_DISPONIBLE
            ),
            "${AutoTable.COLUMN_CONCESIONARIA_ID} = ?",
            arrayOf(concesionariaId.toString()),
            null,
            null,
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(AutoTable.COLUMN_ID))
                val marca = cursor.getString(cursor.getColumnIndex(AutoTable.COLUMN_MARCA))
                val modelo = cursor.getString(cursor.getColumnIndex(AutoTable.COLUMN_MODELO))
                val anio = cursor.getInt(cursor.getColumnIndex(AutoTable.COLUMN_ANIO))
                val precio = cursor.getDouble(cursor.getColumnIndex(AutoTable.COLUMN_PRECIO))
                val disponible = cursor.getInt(cursor.getColumnIndex(AutoTable.COLUMN_DISPONIBLE)) == 1

                val auto = Auto(
                    id = id,
                    marca = marca,
                    modelo = modelo,
                    anio = anio,
                    precio = precio,
                    disponible = disponible,
                    concesionariaId = concesionariaId
                )

                autos.add(auto)
            } while (cursor.moveToNext())
        }
        cursor.close()

        return autos
    }
}