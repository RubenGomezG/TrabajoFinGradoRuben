package com.example.appacademia.dao.servidorSQL

import com.example.appacademia.model.Conversacion
import com.example.appacademia.model.Mensaje
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.SQLSyntaxErrorException
import java.sql.Statement

class ConversacionDAO : InterfaceDAO() {

    /**
     * Añade una conversación en la base de datos basado en
     * el código del usuario y academia que participan en la misma.
     * El 1º usuario siempre será la academia y el código siempre será 1.
     * @param conversacion - La conversación a añadir.
     */
    fun anadirConversacion(conversacion : Conversacion) {
        var sentencia: PreparedStatement? = null
        val usuario1Id = conversacion.usuario1Id
        val usuario2Id = conversacion.usuario2Id

        conectar()
        try {
            conexion!!.autoCommit = false // para hacer transacción a la vez

            val sql = "INSERT INTO Conversaciones (usuario1_id, usuario2_id) VALUES (?,?)"

            sentencia = conexion!!.prepareStatement(sql)

            sentencia.setInt(1, usuario1Id)
            sentencia.setString(2, usuario2Id)

            sentencia.executeUpdate()

            conexion!!.commit() // para hacer transacción a la vez
        } catch (e: SQLException) {
            try {
                conexion!!.rollback() // si al ejecutar da error, hacemos rollback
            }
            catch (_: SQLException) { }
        } finally {
            try {
                sentencia?.close()
            }
            catch (_: SQLException) { }
            desconectar()
        }
    }

    /**
     * Consulta una Conversación en la base de datos basado en el código del usuario.
     * @param codigoUsuario - El código del usuario a consultar.
     * @return La conversación entre el usuario y la academia 1.
     */
    fun consultarConversacion(codigoUsuario : Int): Conversacion {
        var sentencia: Statement? = null
        var resultado: ResultSet? = null
        conectar()

        var conversacion = Conversacion()

        try {
            conexion!!.autoCommit = false // para hacer transacción a la vez

            val sql = "SELECT * FROM Conversaciones WHERE usuario1_id = 1 AND usuario2_id = '$codigoUsuario';"
            sentencia = conexion!!.createStatement()
            resultado = sentencia.executeQuery(sql)

            while (resultado.next()) {
                conversacion = getConversacion(resultado)
            }
            conexion!!.commit() // para hacer transacción a la vez
        } catch (syntax: SQLSyntaxErrorException) {
            return Conversacion()
        }
        catch (_: NullPointerException) {}
        catch (e: SQLException) {
            // para hacer transacción a la vez:
            try {
                conexion!!.rollback() // si al ejecutar da error, hacemos rollback
            }
            catch (_: SQLException) {}
        }
        finally {
            try {
                sentencia?.close()
                resultado?.close()
            }
            catch (_: SQLException) {}
            desconectar()
        }
        return conversacion
    }

    /**
     * Obtiene una conversación a partir de un ResultSet.
     * @param resultado - El ResultSet que contiene los datos de la conversación.
     * @return La conversación obtenida.
     */
    private fun getConversacion(resultado: ResultSet): Conversacion {
        val codConversacion = resultado.getInt("cod_conversacion")
        val codUsuario2 = resultado.getString("usuario2_id")
        return Conversacion(codConversacion, codUsuario2)
    }
}