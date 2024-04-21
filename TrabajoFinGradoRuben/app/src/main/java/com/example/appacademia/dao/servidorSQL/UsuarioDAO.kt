package com.example.appacademia.dao.servidorSQL

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.appacademia.model.Usuario
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.SQLSyntaxErrorException
import java.sql.Statement

class UsuarioDAO : InterfaceDAO() {

    /**
     * Método que se utiliza para registrar un usuario. Recibe un parámetro Usuario desde la interfaz,
     * codifica la contraseña a SHA-256 y mete el usuario en la base de datos
     * @param usuario
     */
    fun anadirUsuario(usuario: Usuario) {
        var sentencia: PreparedStatement? = null
        var username = usuario.username
        var contrasena = usuario.contrasena
        var email = usuario.email
        val nombre = usuario.nombre
        val apellidos = usuario.apellidos
        val telefono = usuario.telefono
        val edad : Int = usuario.edad
        val contrasenaCodificada = HashUtils.sha256(contrasena)
        conectar()
        try {
            conexion!!.autoCommit = false // para hacer transacción a la vez

            val sql = "INSERT INTO Usuarios (usuario,contrasena,email,nombre,apellidos,telefono,img_perfil,edad) VALUES (?,?,?,?,?,?,?,?)"
            
            sentencia = conexion!!.prepareStatement(sql)

            sentencia.setString(1, username)
            sentencia.setString(2, contrasenaCodificada)
            sentencia.setString(3, email)
            sentencia.setString(4, nombre)
            sentencia.setString(5, apellidos)
            sentencia.setLong(6, telefono)
            sentencia.setString(7, "")
            sentencia.setInt(8, edad)

            sentencia.executeUpdate()

            conexion!!.commit() // para hacer transacción a la vez
        } catch (e: SQLException) {
            //Toast.makeText(context,
            //    "Debes rellenar todos los campos",Toast.LENGTH_SHORT).show()
            try {
                conexion!!.rollback() // si al ejecutar da error, hacemos rollback
            } catch (e1: SQLException) {
             //   Toast.makeText(context,
             //       "Error de conexión",Toast.LENGTH_SHORT).show()
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
     * Método que se utiliza para comprobar si existe un usuario en la base de datos en base a
     * su correo o nombre de usuario.
     * @param nombreUsuario
     * @param correo
     */
    fun existeRegistro(correo: String, nombreUsuario: String): Boolean {
        var sentencia: PreparedStatement? = null
        var resultado: ResultSet? = null
        var existe = false

        conectar()

        try {
            val sql = "SELECT COUNT(*) AS total FROM Usuarios WHERE email = ? OR usuario = ?"
            sentencia = conexion!!.prepareStatement(sql)
            sentencia.setString(1, correo)
            sentencia.setString(2, nombreUsuario)

            resultado = sentencia.executeQuery()

            if (resultado.next()) {
                val totalRegistros = resultado.getInt("total")
                Log.i("resultados",totalRegistros.toString())
                existe = totalRegistros > 0
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            try {
                sentencia?.close()
                resultado?.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
            desconectar()
        }

        return existe
    }

    /***
     * Método que comprueba si el usuario y contraseña pertenecen a un usuario y devuelven
     * el mismo.
     * @param usuario
     * @param contrasena
     * @return Usuario
     */
    fun consultarUsuarioContrasena(usuario: String, contrasena: String): Usuario {
            var sentencia: Statement? = null
            var resultado: ResultSet? = null
            conectar()
            var usuario2 = Usuario()

            try {
                conexion!!.autoCommit = false // para hacer transacción a la vez

                val sql = "SELECT * FROM Usuarios WHERE usuario = '$usuario' AND contrasena = '$contrasena';"
                sentencia = conexion!!.createStatement()
                resultado = sentencia.executeQuery(sql)

                if(resultado.next()){
                    Log.i("Hay Usuario","Hay user")
                    usuario2 = getUsuario(resultado)
                }
                conexion!!.commit() // para hacer transacción a la vez
            } catch (syntax: SQLSyntaxErrorException) {

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
            return usuario2
        }

    /***
     * Obtener un usuario en base a su username
     * @param usuarioABuscar
     */
    fun consultarUsuario(usuarioABuscar: String): Usuario {
        var sentencia: Statement? = null
        var resultado: ResultSet? = null
        conectar()

        var usuario = Usuario()
        if (usuarioABuscar == "") {
            return usuario
        }

        try {
            conexion!!.autoCommit = false // para hacer transacción a la vez

            val sql = "SELECT * FROM Usuarios WHERE usuario LIKE '$usuarioABuscar';"
            sentencia = conexion!!.createStatement()
            resultado = sentencia.executeQuery(sql)

            while (resultado.next()) {
                usuario = getUsuario(resultado)
            }
            conexion!!.commit() // para hacer transacción a la vez
        } catch (syntax: SQLSyntaxErrorException) {
            return Usuario()
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
        return usuario
    }

    /***
     * Obtener una instancia de usuario desde un resultado MySql
     * @param resultado
     * @return Usuario
     */
    private fun getUsuario(resultado: ResultSet): Usuario {
        val username = resultado.getString("usuario")
        val contrasenaUsuario = resultado.getString("contrasena")
        val emailUsuario = resultado.getString("email")
        val nombreUsuario = resultado.getString("nombre")
        val apellidosUsuario = resultado.getString("apellidos") ?: ""
        val telefonoUsuario = resultado.getLong("telefono")
        val imgUsuario = resultado.getString("img_perfil") ?: ""
        val edadUsuario = resultado.getInt("edad")
        return Usuario(username,contrasenaUsuario, emailUsuario,nombreUsuario, apellidosUsuario, telefonoUsuario, imgUsuario, edadUsuario)
        //return Usuario(username, contrasenaUsuario, emailUsuario,nombreUsuario, apellidosUsuario, telefonoUsuario, "", edadUsuario)
    }

    /***
     * Borrar un usuario
     * No está utilizado en esta versión de la aplicación
     * @param usuario
     */
    fun borrarUsuario(usuario: Usuario) {
        var sentencia: PreparedStatement? = null
        val username = usuario.username

        conectar()
        try {
            val sql = "DELETE FROM Usuarios WHERE usuario = ?"
            sentencia = conexion!!.prepareStatement(sql)
            sentencia.setString(1, username)
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
     * Método que recibe un nuevo usuario completo al editar el perfil, y lo cambia completamente
     * en la base de datos
     * @param usuario
     */
    fun modificarUsuario(usuario: Usuario) {
        var sentencia: PreparedStatement? = null
        var username = usuario.username
        var contrasena = usuario.contrasena
        var email = usuario.email
        val nombre = usuario.nombre
        val apellidos = usuario.apellidos
        val telefono = usuario.telefono
        val imgPerfil : String = usuario.img_perfil
        val edad : Int = usuario.edad
        val contrasenaCodificada = HashUtils.sha256(contrasena)

        conectar()
        try {
            conexion!!.autoCommit = false // para hacer transacción a la vez
            val sql = "UPDATE Usuarios SET contrasena = ?, email = ?, nombre = ?, apellidos = ?, telefono = ?, img_perfil = ?, edad = ? WHERE usuario = ?"

            sentencia = conexion!!.prepareStatement(sql)

            sentencia.setString(1, contrasenaCodificada)
            sentencia.setString(2, email)
            sentencia.setString(3, nombre)
            sentencia.setString(4, apellidos)
            sentencia.setLong(5, telefono)
            sentencia.setString(6, imgPerfil)
            sentencia.setInt(7, edad)
            sentencia.setString(8, username)

            sentencia.executeUpdate()
            conexion!!.commit() // para hacer transacción a la vez
        } catch (e: SQLException) {

            // para hacer transacción a la vez:
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
     * Método que recibe un nuevo usuario sin cambiar la contraseña al editar el perfil, y lo cambia completamente
     * en la base de datos
     * @param usuario
     */
    fun modificarUsuarioSinContrasena(usuario: Usuario) {
        var sentencia: PreparedStatement? = null
        var username = usuario.username
        var email = usuario.email
        val nombre = usuario.nombre
        val apellidos = usuario.apellidos
        val telefono = usuario.telefono
        val imgPerfil : String = usuario.img_perfil
        val edad : Int = usuario.edad

        conectar()
        try {
            conexion!!.autoCommit = false // para hacer transacción a la vez
            val sql = "UPDATE Usuarios SET email = ?, nombre = ?, apellidos = ?, telefono = ?, img_perfil = ?, edad = ? WHERE usuario = ?"

            sentencia = conexion!!.prepareStatement(sql)

            sentencia.setString(1, email)
            sentencia.setString(2, nombre)
            sentencia.setString(3, apellidos)
            sentencia.setLong(4, telefono)
            sentencia.setString(5, imgPerfil)
            sentencia.setInt(6, edad)
            sentencia.setString(7, username)

            sentencia.executeUpdate()
            conexion!!.commit() // para hacer transacción a la vez
        } catch (e: SQLException) {

            // para hacer transacción a la vez:
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
}