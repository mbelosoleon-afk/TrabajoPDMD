package com.dam.trabajopmdm

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.test.core.app.ApplicationProvider
import com.dam.mvvm_basic.Datos
import com.dam.mvvm_basic.Estados
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.LocalDateTime

@RunWith(RobolectricTestRunner::class)
class MiViewModelTest {

    // Configuración para manejar las corrutinas (especialmente el delay(1500))
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var application: Application
    private lateinit var viewModel: MiViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        application = ApplicationProvider.getApplicationContext()
        viewModel = MiViewModel(application)

        // Limpiar el estado y las preferencias antes de cada prueba
        Datos.numero.clear()
        application.getSharedPreferences("simondice_app", Context.MODE_PRIVATE).edit().clear().commit()

        // Resetear variables de estado
        viewModel.posicion = 0
        viewModel.puntuacion.value = 0
        viewModel.ronda.value = 1
        viewModel.estadoActual.value = Estados.INICIO
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun corregirOpcion_OpcionCorrectaYNoFinDeSecuencia_AvanzaYSumaPuntuacion() = runTest(testDispatcher) {
        // ARRANGE: Secuencia: [0, 1]. Espera acertar '0'
        Datos.numero.addAll(listOf(0, 1))
        viewModel.posicion = 0
        viewModel.puntuacion.value = 5

        // ACT
        val esCorrecto = viewModel.corregirOpcion(0)
        advanceUntilIdle() // Ejecuta el delay(1500)

        // ASSERT
        assertTrue(esCorrecto)
        assertEquals(1, viewModel.posicion) // Posición avanza
        assertEquals(6, viewModel.puntuacion.value) // Puntuación incrementa
    }


    @Test
    fun corregirOpcion_OpcionIncorrecta_LlamaADerrotaYDevuelveFalse() = runTest(testDispatcher) {
        // ARRANGE: Secuencia: [1].
        Datos.numero.addAll(listOf(1))
        viewModel.puntuacion.value = 10
        ControladorPreference.actualizarRecord(application, 15) // Record: 15

        // ACT: Jugador elige '3' (Incorrecto)
        val esCorrecto = viewModel.corregirOpcion(3)
        advanceUntilIdle() // Ejecuta el delay(1500) y llama a derrota()

        // ASSERT
        assertFalse(esCorrecto)
        // Comprobar que derrota() ha reseteado el estado:
        assertEquals(0, viewModel.puntuacion.value)
        assertEquals(Estados.INICIO, viewModel.estadoActual.value)
        assertTrue(Datos.numero.isEmpty())
        assertEquals(15, ControladorPreference.obtenerRecord(application)) // Record no cambia (10 < 15)
    }

    @Test
    fun derrota_ConNuevoRecord_ActualizaRecordFechaYReseteaElJuego() {
        // ARRANGE
        val recordAnterior = 5
        val posibleRecord = 15 // Nuevo Record
        ControladorPreference.actualizarRecord(application, recordAnterior)
        ControladorPreference.actualizarFecha(application, "FechaAntigua")

        // Configurar el estado del juego antes de la derrota
        viewModel.puntuacion.value = posibleRecord
        viewModel.ronda.value = 5
        Datos.numero.addAll(listOf(1, 2, 3))

        // ACT
        viewModel.derrota(posibleRecord)

        // ASSERT
        // Persistencia actualizada
        assertEquals(posibleRecord, ControladorPreference.obtenerRecord(application))
        assertTrue(ControladorPreference.obtenerFecha(application)!!.isNotEmpty())
        assertFalse(ControladorPreference.obtenerFecha(application) == "FechaAntigua")

        // Estado reseteado
        assertEquals(0, viewModel.puntuacion.value)
        assertEquals(1, viewModel.ronda.value)
        assertEquals(Estados.INICIO, viewModel.estadoActual.value)
        assertTrue(Datos.numero.isEmpty())
    }

    @Test
    fun derrota_SinNuevoRecord_ReseteaElJuegoYNoActualizaRecord() {
        // ARRANGE
        val recordAnterior = 20
        val posibleRecord = 10 // Puntuación menor
        ControladorPreference.actualizarRecord(application, recordAnterior)
        ControladorPreference.actualizarFecha(application, "FechaAntigua")

        // Configurar el estado del juego
        viewModel.puntuacion.value = posibleRecord
        viewModel.ronda.value = 5

        // ACT
        viewModel.derrota(posibleRecord)

        // ASSERT
        // Estado reseteado
        assertEquals(0, viewModel.puntuacion.value)
        assertEquals(1, viewModel.ronda.value)
        assertEquals(Estados.INICIO, viewModel.estadoActual.value)

        // Persistencia NO actualizada
        assertEquals(recordAnterior, ControladorPreference.obtenerRecord(application))
        assertEquals("FechaAntigua", ControladorPreference.obtenerFecha(application))
    }
}