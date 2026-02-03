package com.dam.trabajopmdm.SQLiteControlador

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.dam.trabajopmdm.BaseDeDatosSQLite.FeedReaderContract
import java.util.Date

/**
 * Administra la base de datos SQLite y permite realizar acciones con la data y el record
 */
class ControladorSQLite(context: Context) {
    // Es recomendable instanciar el helper, pero abrir la conexión (readableDatabase/writableDatabase)
    // solo cuando se necesite, o usar 'lazy' si queremos mantenerla.
    private val dbHelper = FeedReaderContract.FeedReaderDbHelper(context)

    /**
     * Guarda un nuevo record en la base de datos (INSERT)
     * @param puntuacion La puntuación obtenida
     * @param fecha La fecha en milisegundos
     */
    fun guardarRecord(puntuacion: Int, fecha: Long) {
        val db = dbHelper.writableDatabase
        // Nuevo registro a insertar en la base de datos
        val values = ContentValues().apply {
            put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, "Partida Simon Dice")
            put(FeedReaderContract.FeedEntry.COLUMN_NAME_SCORE, puntuacion)
            put(FeedReaderContract.FeedEntry.COLUMN_NAME_DATE, fecha)
        }
        // Insertar el nuevo registro
        val newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values)
        Log.d("SQLite", "INSERT: Record guardado correctamente. ID fila: $newRowId. Score: $puntuacion")
    }

    /**
     * Permite obtener los datos de la base de datos (READ)
     */
    fun obtenerDatos(): List<String> {
        val db = dbHelper.readableDatabase // Abrir la base de datos en modo lectura

        // columnas a recuperar
        val projection = arrayOf(
            FeedReaderContract.FeedEntry.COLUMN_NAME_SCORE,
            FeedReaderContract.FeedEntry.COLUMN_NAME_DATE
        )

        val sortOrder = "${FeedReaderContract.FeedEntry.COLUMN_NAME_DATE} DESC"

        // Lista de datos
        val items = mutableListOf<String>()

        Log.d("SQLite", "READ: Iniciando lectura de datos...")
        // Recuperar los datos de la base de datos
        db.query(
            FeedReaderContract.FeedEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            sortOrder
        ).use { cursor ->
            // Recorrer los resultados
            while (cursor.moveToNext()) {
                // Obtener los valores de las columnas
                val score = cursor.getInt(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_SCORE))
                val dateLong = cursor.getLong(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_DATE))
                val dateString = Date(dateLong).toString()

                items.add("$score - $dateString")
                Log.d("SQLite", "READ: Fila leída -> Score=$score, Date=$dateString")
            }
        }
        Log.d("SQLite", "READ: Lectura finalizada.")

        return items
    }

    /**
     * Actualiza la puntuación del registro más reciente (UPDATE)
     * @param nuevaPuntuacion La nueva puntuación que queremos establecer
     */
    fun actualizarUltimoRecord(nuevaPuntuacion: Int) {
        val db = dbHelper.writableDatabase // Accede a la base en formato escritura

        //  Nuevos valores
        val values = ContentValues().apply {
            put(FeedReaderContract.FeedEntry.COLUMN_NAME_SCORE, nuevaPuntuacion)
        }

        // Actualizar el registro con la fecha más alta (el último insertado)
        //Buscar la fecha más reciente
        var fechaMasReciente: Long? = null
        val queryMaxDate = "SELECT MAX(${FeedReaderContract.FeedEntry.COLUMN_NAME_DATE}) FROM ${FeedReaderContract.FeedEntry.TABLE_NAME}"
        // Esto lo que hace es buscar la fecha más alta de la tabla de la base de datos y lo almacena en fechaMasReciente
        db.rawQuery(queryMaxDate, null).use { cursor ->
            // Si hay resultados, obtener la fecha
            if (cursor.moveToFirst()) {
                fechaMasReciente = cursor.getLong(0)
            }
        }

        if (fechaMasReciente != null) {
            //Actualizar donde la fecha coincida
            val selection = "${FeedReaderContract.FeedEntry.COLUMN_NAME_DATE} = ?"
            val selectionArgs = arrayOf(fechaMasReciente.toString())

            // Actualizar el registro
            val count = db.update(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs
            )
            Log.d("SQLite", "UPDATE: Se actualizó el último registro (Fecha: $fechaMasReciente). Filas afectadas: $count. Nuevo Score: $nuevaPuntuacion")
        } else {
            Log.d("SQLite", "UPDATE: No se encontró ningún registro para actualizar.")
        }
    }

    // Método auxiliar para cerrar la base de datos
    fun cerrar() {
        dbHelper.close()
    }
}