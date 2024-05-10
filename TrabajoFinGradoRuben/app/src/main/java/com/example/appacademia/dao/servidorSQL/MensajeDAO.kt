package com.example.appacademia.dao.servidorSQL

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.appacademia.model.Conversacion
import com.example.appacademia.model.Mensaje
import java.sql.Date
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.SQLSyntaxErrorException
import java.sql.Statement

class MensajeDAO : InterfaceDAO() {
    fun anadirMensaje(mensaje: Mensaje) {
        var sentencia: PreparedStatement? = null
        var codConversacion = mensaje.codConversacion
        var senderId = mensaje.senderId
        var content = mensaje.content
        val timestamp = mensaje.timestamp
        val fechaUtil1: java.util.Date = java.util.Date()
        val fechaSqlTimestamp1: java.sql.Timestamp = java.sql.Timestamp(fechaUtil1.time)

        conectar()
        try {
            conexion!!.autoCommit = false // para hacer transacción a la vez

            val sql = "INSERT INTO Mensajes (cod_conversacion, senderId, contenido, timestamp) VALUES (?,?,?,?)"

            sentencia = conexion!!.prepareStatement(sql)

            sentencia.setInt(1, codConversacion)
            sentencia.setInt(2, senderId)
            sentencia.setString(2, content)
            sentencia.setTimestamp(3, fechaSqlTimestamp1)


            sentencia.executeUpdate()

            conexion!!.commit() // para hacer transacción a la vez
        } catch (e: SQLException) {
            try {
                conexion!!.rollback() // si al ejecutar da error, hacemos rollback
            } catch (e1: SQLException) {
            }
        } finally {
            try {
                sentencia?.close()
            } catch (_: SQLException) {
            }
            desconectar()
        }
    }

    /**
     * Consulta un mensaje en la base de datos basado en el código del curso.
     * @param codigoMensaje - El código del mensaje a consultar.
     * @return El mensaje encontrado.
     */
    fun consultarMensaje(codigoMensaje : Int): Mensaje {
        var sentencia: Statement? = null
        var resultado: ResultSet? = null
        conectar()

        var mensaje = Mensaje()

        try {
            conexion!!.autoCommit = false // para hacer transacción a la vez

            val sql = "SELECT * FROM Mensajes WHERE cod_mensaje = '$codigoMensaje';"
            sentencia = conexion!!.createStatement()
            resultado = sentencia.executeQuery(sql)

            while (resultado.next()) {
                mensaje = getMensaje(resultado)
            }
            conexion!!.commit() // para hacer transacción a la vez
        } catch (syntax: SQLSyntaxErrorException) {

            return Mensaje()
        } catch (e: NullPointerException) {

        } catch (e: SQLException) {

            // para hacer transacción a la vez:
            try {
                conexion!!.rollback() // si al ejecutar da error, hacemos rollback
            } catch (e1: SQLException) {

            }
        } finally {
            try {
                sentencia?.close()
                resultado?.close()
            } catch (_: SQLException) {

            }
            desconectar()
        }
        return mensaje
    }

    /**
     * Obtiene un mensaje a partir de un ResultSet.
     * @param resultado - El ResultSet que contiene los datos del curso.
     * @return El mensaje obtenido.
     */
    private fun getMensaje(resultado: ResultSet): Mensaje {
        val codMensaje = resultado.getInt("cod_mensaje")
        val codConversacion = resultado.getInt("cod_conversacion")
        val senderId = resultado.getInt("senderId")
        val content = resultado.getString("contenido")
        val timestamp = resultado.getDate("timestamp")
        return Mensaje(codMensaje, codConversacion, senderId, content, timestamp)
    }

    /**
     * Borra un mensaje de la base de datos.
     * @param context - El contexto de la aplicación.
     * @param mensaje - El curso a borrar.
     */
    fun borrarMensaje(context: Context, mensaje: Mensaje) {
        var sentencia: PreparedStatement? = null
        val codCurso = mensaje.codMensaje

        conectar()
        try {
            val sql = "DELETE FROM Mensajes WHERE cod_mensaje = ?"
            sentencia = conexion!!.prepareStatement(sql)
            sentencia.setInt(1, codCurso)
            sentencia.executeUpdate()
        } catch (_: SQLException) {
            Toast.makeText(context,"El curso no se ha encontrado", Toast.LENGTH_SHORT).show()
        } finally {
            try {
                sentencia?.close()
            } catch (_: SQLException) {

            }
            desconectar()
        }
    }

    /**
     * Modifica un curso en la base de datos.
     * @param context - El contexto de la aplicación.
     * @param curso - El curso con los datos actualizados.
     */
    fun modificarCurso(context: Context, mensaje: Mensaje) {
        var sentencia: PreparedStatement? = null
        var senderInt = mensaje.senderId
        var content: String = mensaje.content
        var timestamp = mensaje.timestamp

        conectar()
        try {
            conexion!!.autoCommit = false // para hacer transacción a la vez
            val sql = "UPDATE cursos SET senderId = ?, contenido = ?, timestamp = ?"
            sentencia = conexion!!.prepareStatement(sql)

            sentencia.setInt(1, senderInt)
            sentencia.setString(2, content)
            sentencia.setDate(3, timestamp as Date?)


            sentencia.executeUpdate()
            conexion!!.commit() // para hacer transacción a la vez
        } catch (e: SQLException) {
            Toast.makeText(context,"Debe rellenar los campos obligatorios correctamente", Toast.LENGTH_SHORT).show()
            // para hacer transacción a la vez:
            try {
                conexion!!.rollback() // si al ejecutar da error, hacemos rollback
            } catch (e1: SQLException) {
                Toast.makeText(context,"Error de conexión", Toast.LENGTH_SHORT).show()
            }
        } finally {
            try {
                sentencia?.close()
            } catch (_: SQLException) {

            }
            desconectar()
        }
    }

    /**
     * Obtiene todos los cursos de una academia específica.
     * @param codConversacion - El código de la academia.
     * @return La lista de cursos de la academia especificada.
     */
    fun obtenerTodosLosMensajesDeUsuario(codConversacion: Int): ArrayList<Mensaje> {
        val todosMensajesDeUsuario = ArrayList<Mensaje>()
        var sentencia: PreparedStatement? = null
        var resultado: ResultSet? = null
        val codigo = codConversacion

        try {
            conectar()

            val sql = "SELECT * FROM Mensajes WHERE cod_conversacion = ?;"
            sentencia = conexion!!.prepareStatement(sql)
            sentencia.setInt(1,codigo)
            resultado = sentencia.executeQuery()

            while (resultado.next()) {
                val mensaje = getMensaje(resultado)
                todosMensajesDeUsuario.add(mensaje)
            }

        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            try {
                sentencia?.close()
                resultado?.close()
                desconectar()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }

        return todosMensajesDeUsuario
    }
}