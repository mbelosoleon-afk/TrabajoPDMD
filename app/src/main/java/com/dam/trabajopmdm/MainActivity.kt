package com.dam.trabajopmdm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.dam.trabajopmdm.ui.theme.TrabajoPMDMTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val miViewModel: MiViewModel = MiViewModel()
        enableEdgeToEdge()
        setContent {
            IU(miViewModel)
        }
    }
}

val db = Room.databaseBuilder(
    applicationContext,
    AppDatabase::class.java, "database-name"
)
    .allowMainThreadQueries()
    .build()

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TrabajoPMDMTheme {
        IU(miViewModel = MiViewModel())
    }
}