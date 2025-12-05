package com.dam.trabajopmdm

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

// Usamos RobolectricTestRunner para simular el entorno de Android
@RunWith(RobolectricTestRunner::class)
class ControladorPreferenceTest {

    // Contexto de Android proporcionado por Robolectric
    private lateinit var context: Context

    // Se ejecuta antes de cada prueba para configurar el entorno
    @Before
    fun setup() {
        // Inicializa el contexto de la aplicación
        context = ApplicationProvider.getApplicationContext()
        // Opcional: Limpiar SharedPreferences antes de cada prueba
        context.getSharedPreferences("simondice_app", Context.MODE_PRIVATE).edit().clear().commit()
    }

    // --- Tests para el Record (Puntuación más alta) ---

    @Test
    fun obtenerRecord_valorInicial_esCero() {
        // ACT
        val recordInicial = ControladorPreference.obtenerRecord(context)

        // ASSERT
        // Por defecto, debe devolver 0 si no se ha guardado nada.
        assertEquals(0, recordInicial)
    }

    @Test
    fun actualizarYObtenerRecord_guardaCorrectamente() {
        // ARRANGE
        val nuevoRecord = 15

        // ACT
        ControladorPreference.actualizarRecord(context, nuevoRecord)
        val recordGuardado = ControladorPreference.obtenerRecord(context)

        // ASSERT
        // El valor guardado debe coincidir con el valor recuperado.
        assertEquals(nuevoRecord, recordGuardado)
    }

    @Test
    fun actualizarRecord_sobrescribeValorAnterior() {
        // ARRANGE
        val primerRecord = 5
        val segundoRecord = 25
        ControladorPreference.actualizarRecord(context, primerRecord)

        // ACT
        ControladorPreference.actualizarRecord(context, segundoRecord)
        val recordFinal = ControladorPreference.obtenerRecord(context)

        // ASSERT
        // Debe recuperar el último valor guardado.
        assertEquals(segundoRecord, recordFinal)
    }

    // --- Tests para la Fecha ---

    @Test
    fun obtenerFecha_valorInicial_esVacio() {
        // ACT
        val fechaInicial = ControladorPreference.obtenerFecha(context)

        // ASSERT
        // Por defecto, debe devolver "" si no se ha guardado nada (según la implementación).
        assertEquals("", fechaInicial)
    }

    @Test
    fun actualizarYObtenerFecha_guardaCorrectamente() {
        // ARRANGE
        val nuevaFecha = "2025-12-05 11:30"

        // ACT
        ControladorPreference.actualizarFecha(context, nuevaFecha)
        val fechaGuardada = ControladorPreference.obtenerFecha(context)

        // ASSERT
        // La cadena de fecha guardada debe coincidir con la recuperada.
        assertEquals(nuevaFecha, fechaGuardada)
    }

    @Test
    fun actualizarFecha_sobrescribeValorAnterior() {
        // ARRANGE
        val primeraFecha = "2024-01-01"
        val segundaFecha = "2024-05-15"
        ControladorPreference.actualizarFecha(context, primeraFecha)

        // ACT
        ControladorPreference.actualizarFecha(context, segundaFecha)
        val fechaFinal = ControladorPreference.obtenerFecha(context)

        // ASSERT
        // Debe recuperar el último valor de fecha guardado.
        assertEquals(segundaFecha, fechaFinal)
    }
}