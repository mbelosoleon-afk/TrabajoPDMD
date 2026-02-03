package com.dam.trabajopmdm.Interfaz

import android.content.Context
import com.dam.trabajopmdm.Instancia.Record
import java.util.Date

/**
 * Interfaz que recoge las funcionalidades que podremos utilizar con el Record obtenido en el juego
 */
interface InterfazRecord{

    // Permite recoger el record actual
    fun obtenerRecord(context: Context): Record


    // Actualiza el record
    fun actualizarRecord(context: Context, valorRecord: Int, valorData: Date): Int
}