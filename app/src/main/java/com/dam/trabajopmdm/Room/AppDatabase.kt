package com.dam.trabajopmdm.Room

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Es la clase principal que une todo.
 * Le dice a Room:
 * ◦Qué "plantillas" (entidades) usar (en este caso, User).
 * ◦Qué "acciones" (DAOs) están disponibles (en este caso, UserDao).
 */
@Database(entities = [User::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao // Permite acceder a los métodos del DAO
}