package com.dam.trabajopmdm

import android.content.Context
import android.health.connect.datatypes.ExerciseCompletionGoal.ActiveCaloriesBurnedGoal
import android.nfc.Tag
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableTargetMarker
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dam.mvvm_basic.Colores
import com.dam.mvvm_basic.Datos
import com.dam.mvvm_basic.Estados
import kotlinx.coroutines.processNextEventInCurrentThread

//interfaz de usuario
//Modificado desde cero

@Composable
fun IU(miViewModel: MiViewModel) {
    Interfaz(miViewModel)
}

    @Composable
    fun Interfaz(miViewModel: MiViewModel){
        val puntuacionObtenida by miViewModel.puntuacion.collectAsState()
        val rondaObtenida by miViewModel.ronda.collectAsState()
        val recordObtenido by miViewModel.record.collectAsState()
        val estado by miViewModel.estadoActual.collectAsState()

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally){
                Puntuacion(puntuacionObtenida, rondaObtenida, recordObtenido, estado)
                Botonera(miViewModel)
                Boton_Start(miViewModel)
            }
        }
    }

    @Composable
    fun Puntuacion(puntuacion: Int?, ronda: Int,record: Int, estados: Estados){
        Text(text="Estado: $estados")
        Text(text="Ronda: $ronda")
        Text(text="Puntuacion: $puntuacion\n Record: $record")
    }
    @Composable
    fun Boton(miViewModel: MiViewModel, enum_color: Colores) {
        val activo = miViewModel.estadoActual.collectAsState().value
        Spacer(modifier = Modifier.size(10.dp))
        Button(
            enabled = activo.boton_activo,
            colors = ButtonDefaults.buttonColors(enum_color.color),
            onClick = {
                Log.d("Juego", "Dentro del boton: ${enum_color.ordinal}")
                Log.d("Juego", "Dentro del boton - Estado: ${activo.ordinal}")
                miViewModel.corregirOpcion(enum_color.ordinal)
            },
            modifier = Modifier
                .size((80).dp, (40).dp)
        ) {
            Text(text = enum_color.txt, fontSize = 10.sp)
        }
    }

    @Composable
    fun Botonera(miViewModel: MiViewModel) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                Boton(miViewModel, Colores.CLASE_ROJO)
                Boton(miViewModel, Colores.CLASE_AZUL)
            }
            Row {
                Boton(miViewModel, Colores.CLASE_AMARILLO)
                Boton(miViewModel, Colores.CLASE_VERDE)
            }
        }
    }

    @Composable
    fun Boton_Start(miViewModel: MiViewModel) {
        val estado = miViewModel.estadoActual.collectAsState().value
        Button(
            enabled = estado.start_activo,
            onClick = {
                Log.d("Juego", "Empieza la partida")
                miViewModel.numeroRandom()
            }) {
            Text(text = "Start")
        }
    }
@Preview(showBackground = true)
@Composable
fun IUPreview(){
    //Obtiene el contexto actual
    val context = LocalContext.current
    //Pasa el contexto al constructor MiViewModel
    IU(miViewModel = MiViewModel(context))

}