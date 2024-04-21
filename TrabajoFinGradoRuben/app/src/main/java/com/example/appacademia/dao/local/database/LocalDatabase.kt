package com.example.appacademia.dao.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlin.concurrent.Volatile
import com.example.appacademia.dao.local.dao.UsuarioDao
import com.example.appacademia.dao.local.entity.Usuario

/**
 * Clase que representa la base de datos local de la aplicación.
 */
@Database(entities = [Usuario::class], version = 1)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun userDao(): UsuarioDao

    /**
     * Método abstracto que proporciona acceso al DAO (Data Access Object) de Usuario.
     * @return DAO de Usuario.
     */
    companion object {
        private const val DATABASE_NAME = "LocalUser.db"

        @Volatile
        private var INSTANCE: LocalDatabase? = null

        /**
         * Obtiene una instancia de la base de datos local.
         * @param context - Contexto de la aplicación.
         * @return Instancia de la base de datos local.
         */
        fun getInstance(context: Context): LocalDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LocalDatabase::class.java,
                        DATABASE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }
}