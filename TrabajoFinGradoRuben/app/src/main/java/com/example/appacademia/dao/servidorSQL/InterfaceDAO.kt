package com.example.appacademia.dao.servidorSQL

import android.util.Log
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.sql.*

/**
 * Interfaz que implementarán el resto de DAO. Contiene las credenciales de acceso a la base de datos
 * y los métodos de utilidad que serán común a todas
 */
abstract class InterfaceDAO{

    /***
     * Credenciales para acceder a la base de datos
     */
    companion object {
        const val URL = "jdbc:mysql://lhcp3379.webapps.net:3306/bu5x9cts_businessplus"
        const val USUARIO = "bu5x9cts_rubengomez"
        const val CONTRASENA = "Clash1ng;"
    }

    var conexion: Connection? = null

    /***
     * Crear base de datos si no existe en base a un SQL script
     */
    fun crearBase() {
        var entrada: BufferedReader? = null
        var sentencia: Statement? = null
        val nombreArchivo =
            "sql/ScriptCreacionAppAcademia.sql"
        conectar()

        try {
            entrada = BufferedReader(FileReader(nombreArchivo))
            var linea = entrada.readLine()
            conexion!!.autoCommit = false
            while (linea != null) {
                linea = entrada.readLine()
                try {
                    sentencia = conexion!!.createStatement()
                    sentencia.execute(linea)
                } catch (e: SQLException) {
                }
            }
            conexion!!.commit() // para hacer transacción a la vez
        } catch (e: SQLException) {
            // para hacer transacción a la vez:
            try {
                conexion!!.rollback() // si al ejecutar da error, hacemos rollback
            } catch (e1: SQLException) {
            }
        } catch (e: IOException) {
        } finally {
            try {
                if (entrada != null) {
                    try {
                        entrada.close()
                    } catch (e: NullPointerException) {
                    } catch (e: IOException) {
                    }
                }
                sentencia?.close()
            } catch (e: SQLException) {
            }
            desconectar()
        }
    }

    /***
     * Conectar con la base de datos
     */
    fun conectar() {
        // Configuramos la conexión con la base de datos
        try {
            // Cargamos el driver de MySQL
            Class.forName("com.mysql.jdbc.Driver")

            // Establecemos la conexión con la base de datos
            conexion = DriverManager.getConnection(URL, USUARIO, CONTRASENA)
            Log.i("conexion","Conexion exitosa a la bbdd")

        } catch (e: Exception) {
            Log.e("conexion2", "Conexion erronea")
            Log.e("conexion2", e.toString())
        }
    }

    /***
     * Desconectar con la base de datos
     */
    fun desconectar() {
        try {
            conexion!!.close()
        } catch (e: SQLException) {
            Log.i("Conexion","No se ha desconectado")
        }
    }

}
