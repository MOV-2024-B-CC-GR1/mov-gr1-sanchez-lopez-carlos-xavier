package com.example.deber

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "concesionarias.db", null, 2) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
            CREATE TABLE ${ConcesionariaTable.TABLE_NAME} (
                ${ConcesionariaTable.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${ConcesionariaTable.COLUMN_NOMBRE} TEXT NOT NULL,
                ${ConcesionariaTable.COLUMN_DIRECCION} TEXT NOT NULL,
                ${ConcesionariaTable.COLUMN_FECHA_FUNDACION} TEXT NOT NULL,
                ${ConcesionariaTable.COLUMN_ABIERTA} INTEGER,
                ${ConcesionariaTable.COLUMN_NUMERO_AUTOS} INTEGER DEFAULT 0,
                ${ConcesionariaTable.COLUMN_LATITUD} REAL,  
                ${ConcesionariaTable.COLUMN_LONGITUD} REAL
            )
        """
        db?.execSQL(createTableQuery)

        // Crear la tabla de autos
        val createAutoTableQuery = """
            CREATE TABLE ${AutoTable.TABLE_NAME} (
                ${AutoTable.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${AutoTable.COLUMN_MARCA} TEXT NOT NULL,
                ${AutoTable.COLUMN_MODELO} TEXT NOT NULL,
                ${AutoTable.COLUMN_ANIO} INTEGER NOT NULL,
                ${AutoTable.COLUMN_PRECIO} REAL NOT NULL,
                ${AutoTable.COLUMN_DISPONIBLE} INTEGER NOT NULL,
                ${AutoTable.COLUMN_CONCESIONARIA_ID} INTEGER,
                FOREIGN KEY(${AutoTable.COLUMN_CONCESIONARIA_ID}) REFERENCES ${ConcesionariaTable.TABLE_NAME}(${ConcesionariaTable.COLUMN_ID})
            )
        """
        db?.execSQL(createAutoTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS ${AutoTable.TABLE_NAME}")
        db?.execSQL("DROP TABLE IF EXISTS ${ConcesionariaTable.TABLE_NAME}")
        onCreate(db)
    }
}

object ConcesionariaTable {
    const val TABLE_NAME = "concesionarias"
    const val COLUMN_ID = "id"
    const val COLUMN_NOMBRE = "nombre"
    const val COLUMN_DIRECCION = "direccion"
    const val COLUMN_FECHA_FUNDACION = "fecha_fundacion"
    const val COLUMN_ABIERTA = "abierta"
    const val COLUMN_NUMERO_AUTOS = "numero_autos"
    const val COLUMN_LONGITUD = "longitud"
    const val COLUMN_LATITUD = "latitud"
}

object AutoTable{
    const val TABLE_NAME = "autos"
    const val COLUMN_ID = "id"
    const val COLUMN_MARCA = "marca"
    const val COLUMN_MODELO = "modelo"
    const val COLUMN_ANIO = "anio"
    const val COLUMN_PRECIO = "precio"
    const val COLUMN_DISPONIBLE = "disponible"
    const val COLUMN_CONCESIONARIA_ID = "concesionaria_id"
}