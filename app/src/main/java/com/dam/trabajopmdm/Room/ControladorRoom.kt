package com.dam.trabajopmdm.Room

import android.content.Context
import androidx.room.Room
import com.dam.trabajopmdm.Interfaz.InterfazRecord
import com.dam.trabajopmdm.Instancia.Record
import java.util.Date

/**
 * Es la clase que usas en tu app para hablar con la base de datos.
 * ◦Crea y configura la base de datos.
 * ◦Usa el UserDao para ejecutar las acciones (leer, guardar, etc.).
 */
class ControladorRooms(private val applicationContext: Context): InterfazRecord {
    // Esto sirve para crear la base de datos
    val db = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java, "database-name"
    ).allowMainThreadQueries() // Permite que se ejecute en el hilo principal
        .build()
    // Permite acceder a los métodos del DAO
    val userDao = db.userDao()

    override fun obtenerRecord(context: Context): Record {
        // Busca usuarios por el record
        val userByRecord: User? = userDao.findByRecord() // SELECT * FROM user ORDER BY record DESC LIMIT 1

        var record = Record

        if (userByRecord?.record != null){
            record.valorRecord = userByRecord.record!!
            record.fechaSuperacion = Date(userByRecord.fecha)
        }

        return record
    }

    override fun actualizarRecord(
        context: Context,
        valorRecord: Int,
        valorData: Date
    ): Int {
        try {
            // Añade usuarios a la base
            val newUser = User(null, valorRecord, valorData.toString()) // INSERT INTO user (uid, record) VALUES (1, 12)
            userDao.insertAll(newUser)

            // Permite actualizar un usuario
            newUser.record = 15
            userDao.update(newUser)
            return 1

        }catch (a:Exception){
            return -1
        }
    }

    fun cerrar(){
        db.close()
    }
}