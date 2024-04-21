package com.example.appacademia.dao.servidorSQL

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.appacademia.ftp.InterfaceFTP
import com.example.appacademia.model.Inscripcion
import com.example.appacademia.model.Usuario
import java.sql.Date
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.SQLSyntaxErrorException
import java.sql.Statement
import javax.security.auth.login.LoginException

class InscripcionesDAO : InterfaceDAO(){

    /**
     * Método que se utiliza para inscribir a un Usuario a un Curso. Recibe un parámetro Inscripción desde la interfaz,
     * y lo introduce en la base de datos
     * @param inscripcion El código de Curso y Username hechos objeto
     */
    fun inscribir(inscripcion: Inscripcion) {
        var sentencia: PreparedStatement? = null
        var username = inscripcion.username
        var codCurso = inscripcion.codCurso

        conectar()
        try {
            conexion!!.autoCommit = false // para hacer transacción a la vez

            val sql = "INSERT INTO Inscripciones (cod_curso, usuario) VALUES (?,?)"
            sentencia = conexion!!.prepareStatement(sql)
            sentencia.setInt(1, codCurso)
            sentencia.setString(2, username)
            sentencia.executeUpdate()

            conexion!!.commit() // para hacer transacción a la vez
        } catch (e: SQLException) {
            Log.i("TAG", e.message.toString())
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
     * Método que se utiliza para consultar una inscripción concreta según su usuario y curso
     * @param usuarioABuscar
     * @param codigoCurso
     */
    fun consultarInscripcion(usuarioABuscar: String, codigoCurso : Int): Inscripcion {
        var sentencia: Statement? = null
        var resultado: ResultSet? = null
        val username = usuarioABuscar
        val codCurso = codigoCurso
        conectar()

        var inscripcion = Inscripcion()
        if (username == "") {
            return inscripcion
        }

        try {
            conexion!!.autoCommit = false // para hacer transacción a la vez

            val sql =
                "SELECT * FROM inscripciones WHERE usuario LIKE '$username' AND cod_curso = '$codCurso';"
            sentencia = conexion!!.createStatement()
            resultado = sentencia.executeQuery(sql)

            while (resultado.next()) {
                inscripcion = getInscripcion(resultado)
            }
            conexion!!.commit() // para hacer transacción a la vez
        } catch (syntax: SQLSyntaxErrorException) {
            return Inscripcion()
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
        return inscripcion
    }

    /**
     * Obtiene una instancia de Inscripción desde un ResultSet MySql
     * @param resultado
     * @return Inscripcion
     */
    private fun getInscripcion(resultado: ResultSet): Inscripcion {
        val username = resultado.getString("usuario")
        val codCurso = resultado.getInt("cod_curso")
        return Inscripcion(username, codCurso)
    }

    /**
     * Borra una inscripción de la base de datos
     * @param inscripcion
     */
    fun borrarInscripcion(inscripcion: Inscripcion) {
        var sentencia: PreparedStatement? = null
        val username = inscripcion.username
        val codCurso = inscripcion.codCurso

        conectar()
        try {
            val sql = "DELETE FROM Inscripciones WHERE usuario = ? AND cod_curso = ?"
            sentencia = conexion!!.prepareStatement(sql)
            sentencia.setString(1, username)
            sentencia.setInt(2, codCurso)
            sentencia.executeUpdate()
        } catch (_: SQLException) {
        } finally {
            try {
                sentencia?.close()
            } catch (_: SQLException) {
            }
            desconectar()
        }
    }

    /**
     * Método que obtiene toda la lista de cursos(inscripciones) a los que está apuntado
     * un usuario
     * @param usuario
     * @return ArrayList<Inscripcion>
     */
    fun listaInscripciones( usuario: String) : ArrayList<Inscripcion>{
        val todasInscripciones = ArrayList<Inscripcion>()
        var sentencia: PreparedStatement? = null
        var resultado: ResultSet? = null
        val username = usuario
        var inscripcion = Inscripcion()

        conectar()
        try {
            conexion!!.autoCommit = false // para hacer transacción a la vez

            val sql = "SELECT * FROM Inscripciones WHERE usuario = ?;"
            sentencia = conexion!!.prepareStatement(sql)
            sentencia.setString(1, username)
            resultado = sentencia.executeQuery()

            while (resultado.next()) {
                inscripcion = getInscripcion(resultado)
                todasInscripciones.add(inscripcion)
            }

        } catch (syntax: SQLSyntaxErrorException) {

            return ArrayList()
        } catch (e: NullPointerException) {

        } catch (e: SQLException) {
            // para hacer transacción a la vez:
            try {
                conexion!!.rollback() // si al ejecutar da error, hacemos rollback
            } catch (_: SQLException) {

            }
        } finally {
            try {
                sentencia?.close()
                resultado?.close()
            } catch (_: SQLException) {
            }
            desconectar()
        }
        return todasInscripciones
    }
}