package com.example.deber

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deber.models.Concesionaria
import com.example.deber.models.ConcesionariaAdapter

class VerConcesionariasActivity : AppCompatActivity() {

    private lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ver_concesionarias)

        val dbHelper = DBHelper(this)
        db = dbHelper.readableDatabase

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewConcesionarias)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val concesionarias = obtenerConcesionarias(db)
        val adapter = ConcesionariaAdapter(concesionarias)
        recyclerView.adapter = adapter
    }

    private fun obtenerConcesionarias(db: SQLiteDatabase): List<Concesionaria> {
        val concesionarias = mutableListOf<Concesionaria>()
        val cursor = db.query(
            ConcesionariaTable.TABLE_NAME,
            arrayOf(
                ConcesionariaTable.COLUMN_ID,
                ConcesionariaTable.COLUMN_NOMBRE,
                ConcesionariaTable.COLUMN_DIRECCION,
                ConcesionariaTable.COLUMN_FECHA_FUNDACION,
                ConcesionariaTable.COLUMN_ABIERTA,
                ConcesionariaTable.COLUMN_NUMERO_AUTOS
            ),
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
                val direccion = cursor.getString(cursor.getColumnIndex(ConcesionariaTable.COLUMN_DIRECCION))
                val fechaFundacion = cursor.getString(cursor.getColumnIndex(ConcesionariaTable.COLUMN_FECHA_FUNDACION))
                val abierta = cursor.getInt(cursor.getColumnIndex(ConcesionariaTable.COLUMN_ABIERTA)) == 1
                val numeroAutos = cursor.getInt(cursor.getColumnIndex(ConcesionariaTable.COLUMN_NUMERO_AUTOS))

                val concesionaria = Concesionaria(
                    id = id,
                    nombre = nombre,
                    direccion = direccion,
                    numeroAutos = numeroAutos,
                    fechaFundacion = fechaFundacion,
                    abierta = abierta
                )

                concesionarias.add(concesionaria)
            } while (cursor.moveToNext())
        }
        cursor.close()

        return concesionarias
    }
}