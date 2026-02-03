package com.dam.trabajopmdm.BaseDeDatosSQLite


import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

object FeedReaderContract {

    // Contenido de la tabla de la base de datos
    object FeedEntry : BaseColumns {
        const val TABLE_NAME = "TablaDeRecords"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_SCORE = "record"
        const val COLUMN_NAME_DATE = "date"
    }

    // "Script" que se lanza para crear las tablas.
    // Se usa INTEGER para la fecha (timestamp) en lugar de DATE.
    private const val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${FeedEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${FeedEntry.COLUMN_NAME_TITLE} TEXT," +
                "${FeedEntry.COLUMN_NAME_SCORE} INTEGER," +
                "${FeedEntry.COLUMN_NAME_DATE} INTEGER)" // Guardaremos la fecha  en Integer porque no funciona Data


    // Reinicia la base de datos
    private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${FeedEntry.TABLE_NAME}"

    /**
     * Clase interna que crea la base de datos y la estructura de la tabla
     * @param context El contexto de la aplicación
     */
    class FeedReaderDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(SQL_CREATE_ENTRIES)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            // Esta política de actualización es simple: descarta los datos y vuelve a empezar.
            db.execSQL(SQL_DELETE_ENTRIES)
            onCreate(db)
        }

        override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            onUpgrade(db, oldVersion, newVersion)
        }

        // Constantes de la base de datos
        // companion: Permite acceder a los miembros de la clase sin necesidad de crear una instancia.
        companion object {
            const val DATABASE_VERSION = 2
            const val DATABASE_NAME = "FeedReader.db"
        }
    }
}