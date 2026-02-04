package com.dam.mvvm_basic

import androidx.compose.ui.graphics.Color

/**
 * Clase para almacenar los datos del juego
 */
object Datos {
    var numero: ArrayList<Int> = ArrayList()
}

/**
 * Colores utilizados
 */
enum class Colores(val color: Color, val txt: String) {
    CLASE_VERDE(color = Color.Green, txt = "Verde"),
    CLASE_ROJO(color = Color.Red, txt = "Rojo"),
    CLASE_AZUL(color = Color.Blue, txt = "Azul"),
    CLASE_AMARILLO(color = Color.Yellow, txt = "Amarillo"),
    CLASE_START(color = Color.Magenta, txt = "Start")
}

/**
 * Estados del juego
 */
enum class Estados(val start_activo: Boolean, val boton_activo: Boolean) {
    INICIO(start_activo = true, boton_activo = false),
    GENERANDO(start_activo = false, boton_activo = false),
    ADIVINANDO(start_activo = false, boton_activo = true),
}