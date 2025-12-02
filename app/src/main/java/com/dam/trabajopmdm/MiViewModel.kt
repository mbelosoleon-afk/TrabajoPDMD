package com.dam.trabajopmdm

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.ui.util.packInts
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.lifecycle.viewModelScope
import com.dam.mvvm_basic.Datos
import com.dam.mvvm_basic.Estados
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.processNextEventInCurrentThread
//Metemos el context en el constructor
class MiViewModel(private val context: Context): ViewModel() {
    // variable estados del juego
    val estadoActual: MutableStateFlow<Estados> = MutableStateFlow(Estados.INICIO)

    //variable numero random
    var _numbers = mutableStateOf(0)

    //variable puntuacion
    val puntuacion = MutableStateFlow<Int?>(0)

    //variable record con el metodo cargarRecord(), para obtener el valor
    val record = MutableStateFlow<Int>(cargarRecord())

    //variable ronda
    var ronda = MutableStateFlow<Int>(1)

    //variable posicion
    var posicion = 0

    //Función para crear un número random
    fun numeroRandom(){
        estadoActual.value = Estados.GENERANDO
        Log.d("ViewModel","Estado generando")
        _numbers.value = (0..3).random()
        Log.d("ViewModel","Número random generado: $_numbers")
        actualizarNumero(_numbers.value)
    }

    //Funcion para actualizar el número random
    fun actualizarNumero(numero: Int){
        Log.d("ViewModel","Actualizando el numero de la clase Datos")
        Datos.numero.add(numero)
        estadoActual.value = Estados.ADIVINANDO
        mostrarSerie(Datos.numero)
    }

    fun mostrarSerie(serie: ArrayList<Int>){
        Log.d("ViewModel","Estado adivinando, secuencia: $serie")
    }
    fun corregirOpcion(numeroColor:Int): Boolean{
        viewModelScope.launch {
            delay(1500)
        }
        Log.d("ViewModel","Combrobando si la opción escogida es correcta...")
        return if (numeroColor == Datos.numero[posicion]){
            Log.d("ViewModel","ES CORRECTO !")
            posicion++
            if (Datos.numero.size == posicion) {
                cambiarRonda()
            }
            puntuacion.value = puntuacion.value?.plus(1)

            true
        }else{
            Log.d("ViewModel","ERROR, HAS PERDIDO")
            derrota()
            false
        }
    }
    fun cambiarRonda(){
        posicion = 0
        ronda.value = ronda.value.plus(1)
        numeroRandom()
    }
    fun derrota(){
        //Cmprueba si la puntuacion supera al record, lo guarda en el viewModel
        //y guarda el nuevo valor en el sharedPreferences
        if(puntuacion.value!! > record.value){
            record.value = puntuacion.value!!
            guardarRecord(record.value)
        }
        puntuacion.value = 0
        posicion = 0
        ronda.value = 1
        estadoActual.value = Estados.INICIO
        Datos.numero = ArrayList()
    }

    //Funcion parar cargar datos del sharedPreferences,
    fun cargarRecord(): Int {
        //variable sharedPreference
        val sharedPrefs = context.getSharedPreferences("SimonDice", Context.MODE_PRIVATE)
        //Cargamos el número, si este no existe, el valor es 0
        return sharedPrefs.getInt("HighScore",0)
    }

    //Funcion para guardar el record en sharedPreferences
    fun guardarRecord(nuevoRecord: Int){
        val sharedPrefs = context.getSharedPreferences("SimonDice", Context.MODE_PRIVATE)
        with (sharedPrefs.edit()){
            putInt("HigsScore", nuevoRecord)
            apply() //Guarda de forma asíncrona
        }
    }
}
//DOCUMENTACIÓN: https://developer.android.com/training/data-storage/shared-preferences?hl=es-419#kotlin