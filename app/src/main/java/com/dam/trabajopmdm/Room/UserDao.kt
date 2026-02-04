package com.dam.trabajopmdm.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * Es la lista de cosas que puedes hacer con la base de datos:
 * getAll(): Coger todos los usuarios.
 * findByRecord(): Coger el usuario con el récord más alto.
 * insertAll(): Añadir nuevos usuarios.
 * update(): Modificar un usuario.
 */
@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    // Permitem filtrar por id
    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user ORDER BY record DESC LIMIT 1")
    fun findByRecord(): User?

    @Insert
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)

    @Update
    fun update(user: User)
}