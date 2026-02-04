package com.dam.trabajopmdm.Room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * s un "molde" que le dice a la base de datos cómo es un registro.
 * Define la tabla (llamada user) y sus columnas (uid, record, fecha).
 */
//Le añadimos la columna nombre
@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val uid: Int?,
    @ColumnInfo(name = "record") var record: Int?,
    @ColumnInfo(name = "fecha") var fecha: String?,
    @ColumnInfo(name = "nombre") var nombre: String?
)