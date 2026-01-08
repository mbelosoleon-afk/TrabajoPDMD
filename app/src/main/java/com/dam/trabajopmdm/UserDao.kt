package com.dam.trabajopmdm

import User
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import org.intellij.lang.annotations.JdkConstants

@Dao
interface UserDao{
    // Consulta q recupera todos los usuarios de la tabla. Devuelve una lista de objetos User
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    // Consulta que inserta uno o varios usuarios en la base de datos
    @Insert
    fun insertAll(vararg users: User)
}