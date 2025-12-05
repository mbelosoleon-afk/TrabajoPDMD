package com.dam.trabajopmdm

import android.content.Context
import android.icu.text.RelativeDateTimeFormatter
import androidx.core.content.edit

object ControladorPreference {
    private const val PREFS_NAME = "simondice_app"

    private const val KEY_RECORD = "record"

    private const val KEY_FECHA = "fecha"

    fun actualizarRecord(context: Context, nuevoRecord: Int) {
        // Obtenemos las preferencias compartidas
        val sharedPreferences = context.getSharedPreferences(com.dam.trabajopmdm.ControladorPreference.PREFS_NAME, Context.MODE_PRIVATE)
        // Usamos la extensión KTX edit {} para no bloquear el hilo y aplicar cambios
        // 'put' pone un valor con clave KEY_RECORD y valor nuevoRecord
        sharedPreferences.edit {
            putInt(KEY_RECORD, nuevoRecord)
        }
    }

    fun obtenerRecord(context: Context): Int {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getInt(KEY_RECORD, 0)
    }

    fun actualizarFecha(context: Context, nuevaFecha: String) {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit {
            putString(KEY_FECHA, nuevaFecha)
        }
    }

    fun obtenerFecha(context: Context): String? {
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(KEY_FECHA,"")
    }

}
