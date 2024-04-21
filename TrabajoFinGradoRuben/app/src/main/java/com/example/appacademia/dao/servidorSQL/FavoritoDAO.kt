package com.example.appacademia.dao.servidorSQL

import android.content.Context
import android.widget.Toast
import com.example.appacademia.ftp.InterfaceFTP
import com.example.appacademia.model.Favorito
import com.example.appacademia.model.Inscripcion
import com.example.appacademia.model.Usuario
import java.sql.Date
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.SQLSyntaxErrorException
import java.sql.Statement

class FavoritoDAO : InterfaceDAO(){

    /**
     * Método que añade un 'favorito' a la base de datos. Obtiene como parámetro un objeto
     * favorito, que contiene el nombre de usuario y el código de curso y lo inserta en la base
     * de datos
     * @param favorito
     */
    fun anadirFavorito(favorito: Favorito) {
        var sentencia: PreparedStatement? = null
        var username = favorito.username
        var codCurso = favorito.codCurso

        conectar()
        try {
            conexion!!.autoCommit = false // para hacer transacción a la vez

            val sql = "INSERT INTO Favoritos (cod_curso, usuario) VALUES (?,?)"

            sentencia = conexion!!.prepareStatement(sql)

            sentencia.setInt(1, codCurso)
            sentencia.setString(2, username)

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
     * Método que consulta si existe una relación en la base de datos entre un usuario y un código
     * de curso, y si existe, devuelve el objeto 'Favorito'
     * @param usuarioABuscar
     * @param codigoCurso
     * @return Favorito
     */
    fun consultarFavorito(usuarioABuscar: String, codigoCurso : Int): Favorito {
        var sentencia: Statement? = null
        var resultado: ResultSet? = null
        val username = usuarioABuscar
        val codCurso = codigoCurso
        conectar()

        var favorito = Favorito()
        if (username == "") {
            return favorito
        }

        try {
            conexion!!.autoCommit = false // para hacer transacción a la vez

            val sql = "SELECT * FROM Favoritos WHERE cod_curso = '$codCurso' AND usuario LIKE '$username';"
            sentencia = conexion!!.createStatement()
            resultado = sentencia.executeQuery(sql)

            while (resultado.next()) {
                favorito = getFavorito(resultado)
            }
            conexion!!.commit() // para hacer transacción a la vez
        } catch (syntax: SQLSyntaxErrorException) {
            return Favorito()
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
        return favorito
    }

    /**
     * Método que obtiene una instancia de 'Favorito' desde un ResultSet MySql
     * @param resultado
     * @return Favorito
     */
    private fun getFavorito(resultado: ResultSet): Favorito{
        val username = resultado.getString("usuario")
        val codCurso = resultado.getInt("cod_curso")
        return Favorito(username, codCurso)
    }

    /**
     * Método que borra un 'Favorito' de la base de datos
     * @param favorito
     */
    fun borrarFavorito( favorito: Favorito) {
        var sentencia: PreparedStatement? = null
        val username = favorito.username
        val codCurso = favorito.codCurso

        conectar()
        try {
            val sql = "DELETE FROM Favoritos WHERE cod_curso = ? AND usuario = ?"
            sentencia = conexion!!.prepareStatement(sql)
            sentencia.setInt(1, codCurso)
            sentencia.setString(2, username)
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
     * Método que obtiene la lista de 'Favorito' de un usuario pasado por parámetro
     * @param usuario
     * @return ArrayList<Favorito>
     */
    fun listaFavoritos(usuario: String) : ArrayList<Favorito>{
        val todosFavoritos = ArrayList<Favorito>()
        var sentencia: PreparedStatement? = null
        var resultado: ResultSet? = null
        val username = usuario
        var favorito = Favorito()

        conectar()
        try {
            conexion!!.autoCommit = false // para hacer transacción a la vez

            val sql = "SELECT * FROM Favoritos WHERE usuario = ?;"
            sentencia = conexion!!.prepareStatement(sql)
            sentencia.setString(1, username)
            resultado = sentencia.executeQuery()

            while (resultado.next()) {
                favorito = getFavorito(resultado)
                todosFavoritos.add(favorito)
            }

        } catch (syntax: SQLSyntaxErrorException) {
            return ArrayList()
        } catch (_: NullPointerException) {

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
        return todosFavoritos
    }
}