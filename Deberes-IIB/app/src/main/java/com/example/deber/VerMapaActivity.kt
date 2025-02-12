package com.example.deber

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.deber.models.Concesionaria
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class VerMapaActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var db: SQLiteDatabase
    private lateinit var concesionariaSpinner: Spinner
    private lateinit var concesionarias: List<Concesionaria>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_mapa)

        val dbHelper = DBHelper(this)
        db = dbHelper.readableDatabase

        concesionariaSpinner = findViewById(R.id.spinnerConcesionaria)

        // Obtener la lista de concesionarias
        concesionarias = obtenerConcesionarias(db)

        // Crear un adaptador para el Spinner con los nombres de las concesionarias
        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            concesionarias.map { it.nombre }
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        concesionariaSpinner.adapter = spinnerAdapter

        concesionariaSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                val concesionariaSeleccionada = concesionarias[position]
                moverMapa(concesionariaSeleccionada.latitud, concesionariaSeleccionada.longitud, concesionariaSeleccionada.nombre)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Obtener el fragmento del mapa
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Mostrar la primera concesionaria al abrir el mapa
        if (concesionarias.isNotEmpty()) {
            val primera = concesionarias[0]
            moverMapa(primera.latitud, primera.longitud, primera.nombre)
        }
    }

    private fun moverMapa(latitud: Double, longitud: Double, titulo: String) {
        val ubicacion = LatLng(latitud, longitud)
        mMap.clear()  // Limpia marcadores anteriores
        mMap.addMarker(MarkerOptions().position(ubicacion).title(titulo))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 15f))
    }

    private fun obtenerConcesionarias(db: SQLiteDatabase): List<Concesionaria> {
        val concesionarias = mutableListOf<Concesionaria>()
        val cursor = db.query(
            ConcesionariaTable.TABLE_NAME,
            arrayOf(
                ConcesionariaTable.COLUMN_ID,
                ConcesionariaTable.COLUMN_NOMBRE,
                ConcesionariaTable.COLUMN_LATITUD,
                ConcesionariaTable.COLUMN_LONGITUD
            ),
            null, null, null, null, null
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(ConcesionariaTable.COLUMN_ID))
                val nombre = cursor.getString(cursor.getColumnIndexOrThrow(ConcesionariaTable.COLUMN_NOMBRE))
                val latitud = cursor.getDouble(cursor.getColumnIndexOrThrow(ConcesionariaTable.COLUMN_LATITUD))
                val longitud = cursor.getDouble(cursor.getColumnIndexOrThrow(ConcesionariaTable.COLUMN_LONGITUD))

                concesionarias.add(Concesionaria(id, nombre, "", 0, "", false, latitud, longitud))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return concesionarias
    }
}
