package com.example.appacademia.dao.servidorSQL

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.appacademia.ftp.InterfaceFTP
import com.example.appacademia.model.Academia
import com.example.appacademia.model.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.SQLSyntaxErrorException
import java.sql.Statement

class AcademiaDAO : InterfaceDAO(){

    /***
     * Hacer insert en la base de datos de una academia.
     * @param context - El contexto de la aplicación.
     * @param academia - La academia que se va a añadir a la base de datos.
     */
    fun anadirAcademia(context: Context, academia: Academia) {
        var sentencia: PreparedStatement? = null
        var username = academia.username
        var contrasena = academia.contrasena
        var email = academia.email
        val nombre = academia.nombre
        val telefono = academia.telefono
        val direccion = academia.direccion
        val longitud = academia.longitud
        val latitud = academia.latitud
        val contrasenaCodificada = HashUtils.sha256(contrasena)

        conectar()
        try {
            conexion!!.autoCommit = false // para hacer transacción a la vez

            val sql = "INSERT INTO Academias (usuario,contrasena,email,nombre,telefono,direccion,img_perfil,latitud,longitud) VALUES (?,?,?,?,?,?,?,?,?)"

            sentencia = conexion!!.prepareStatement(sql)

            sentencia.setString(1, username)
            sentencia.setString(2, contrasenaCodificada)
            sentencia.setString(3, email)
            sentencia.setString(4, nombre)
            sentencia.setLong(5, telefono)
            sentencia.setString(6, direccion)
            sentencia.setString(7, "")
            sentencia.setDouble(8, latitud)
            sentencia.setDouble(9, longitud)

            sentencia.executeUpdate()

            conexion!!.commit() // para hacer transacción a la vez
        } catch (e: SQLException) {
            Toast.makeText(context,"Debes rellenar todos los campos", Toast.LENGTH_SHORT).show()
            try {
                conexion!!.rollback() // si al ejecutar da error, hacemos rollback
            } catch (e1: SQLException) {
                Toast.makeText(context,"Error de conexión", Toast.LENGTH_SHORT).show()
            }
        } finally {
            try {
                sentencia?.close()
            } catch (e: SQLException) {
                Log.e("SQL_ERROR", "Error al añadir academia: " + e.message.toString())
            }
            desconectar()
        }
    }

    /***
     * Hace select en academias para comprobar si existe o no.
     * @param correo - El correo electrónico de la academia.
     * @param nombreUsuario - El nombre de usuario de la academia.
     * @return Devuelve true si la academia ya existe en la base de datos, de lo contrario devuelve false.
     */
    fun existeRegistro(correo: String, nombreUsuario: String): Boolean {
        var sentencia: PreparedStatement? = null
        var resultado: ResultSet? = null
        var existe = false

        conectar()

        try {
            val sql = "SELECT COUNT(*) AS total FROM Academias WHERE email = ? OR usuario = ?"
            sentencia = conexion!!.prepareStatement(sql)
            sentencia.setString(1, correo)
            sentencia.setString(2, nombreUsuario)

            resultado = sentencia.executeQuery()

            if (resultado.next()) {
                val totalRegistros = resultado.getInt("total")
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
     * Hace select de academias en base al username.
     * @param academiaABuscar - El nombre de usuario de la academia a buscar.
     * @return Devuelve la academia encontrada o una academia vacía si no se encuentra.
     */
    fun consultarAcademia(academiaABuscar: String?): Academia {
        var sentencia: Statement? = null
        var resultado: ResultSet? = null
        conectar()

        var academia = Academia()
        if (academiaABuscar == "") {
            return academia
        }

        try {
            conexion!!.autoCommit = false // para hacer transacción a la vez

            val sql = "SELECT * FROM academias WHERE usuario LIKE '$academiaABuscar';"
            sentencia = conexion!!.createStatement()
            resultado = sentencia.executeQuery(sql)

            while (resultado.next()) {
                academia = getAcademia(resultado)
            }
            conexion!!.commit() // para hacer transacción a la vez
        } catch (syntax: SQLSyntaxErrorException) {
            return Academia()
        } catch (e: NullPointerException) {
            Log.e("SQL_ERROR", "Academia no existe" + e.message.toString())
        } catch (e: SQLException) {

            // para hacer transacción a la vez:
            try {
                conexion!!.rollback() // si al ejecutar da error, hacemos rollback
            } catch (e1: SQLException) {
                Log.e("SQL_ERROR", e.message.toString())
            }
        } finally {
            try {
                sentencia?.close()
                resultado?.close()
            } catch (e: SQLException) {
                Log.e("SQL_ERROR", e.message.toString())
            }
            desconectar()
        }
        return academia
    }

    /***
     * Hace select de academias en base al nombre.
     * @param context - El contexto de la aplicación.
     * @param academiaABuscar - El nombre de la academia a buscar.
     * @return Devuelve la academia encontrada o una academia vacía si no se encuentra.
     */
    fun consultarAcademiaconNombre(context: Context, academiaABuscar: String?): Academia {
        var sentencia: Statement? = null
        var resultado: ResultSet? = null
        conectar()

        var academia = Academia()
        if (academiaABuscar == "") {
            Toast.makeText(context,"Introduzca su nombre de academia", Toast.LENGTH_SHORT).show()
            return academia
        }

        try {
            conexion!!.autoCommit = false // para hacer transacción a la vez

            val sql = "SELECT * FROM academias WHERE nombre LIKE '$academiaABuscar';"
            sentencia = conexion!!.createStatement()
            resultado = sentencia.executeQuery(sql)

            while (resultado.next()) {
                academia = getAcademia(resultado)
            }
            conexion!!.commit() // para hacer transacción a la vez
        } catch (syntax: SQLSyntaxErrorException) {
            Toast.makeText(context,"Introduzca una academia válido", Toast.LENGTH_SHORT).show()
            return Academia()
        } catch (e: NullPointerException) {
            Toast.makeText(context,"La academia introducido no existe", Toast.LENGTH_SHORT).show()
        } catch (e: SQLException) {
            Toast.makeText(context,"La academia o contraseña no es correctos", Toast.LENGTH_SHORT).show()
            // para hacer transacción a la vez:
            try {
                conexion!!.rollback() // si al ejecutar da error, hacemos rollback
            } catch (e1: SQLException) {
                Toast.makeText(context,"Error de conexión", Toast.LENGTH_SHORT).show()
            }
        } finally {
            try {
                sentencia?.close()
                resultado?.close()
            } catch (_: SQLException) {

            }
            desconectar()
        }
        return academia
    }

    /**
     * Hace select de academias en base al código de academia.
     * @param academiaABuscar - El código de la academia a buscar.
     * @return Devuelve la academia encontrada o una academia vacía si no se encuentra.
     */
    fun consultarAcademiaConCodigo(academiaABuscar: Int): Academia {
        var sentencia: Statement? = null
        var resultado: ResultSet? = null
        conectar()

        var academia = Academia()
        if (academiaABuscar == 0) {
            Log.e("AcademiaDAO.consultarAcademiaConCodigo", "No hay codigo introducido")
//            Toast.makeText(context,
//                "Introduzca su código", Toast.LENGTH_SHORT).show()
            return academia
        }

        try {
            conexion!!.autoCommit = false // para hacer transacción a la vez

            val sql = "SELECT * FROM academias WHERE cod_academia LIKE '$academiaABuscar';"
            sentencia = conexion!!.createStatement()
            resultado = sentencia.executeQuery(sql)

            while (resultado.next()) {
                academia = getAcademia(resultado)
            }
            conexion!!.commit() // para hacer transacción a la vez
        } catch (syntax: SQLSyntaxErrorException) {
//            Toast.makeText(context,
//                "Introduzca un usuario válido", Toast.LENGTH_SHORT).show()
            return Academia()
        } catch (e: NullPointerException) {
//            Toast.makeText(context,
//                "El usuario introducido no existe", Toast.LENGTH_SHORT).show()
        } catch (e: SQLException) {
//            Toast.makeText(context,
//                "El usuario o contraseña no son correctos", Toast.LENGTH_SHORT).show()
            // para hacer transacción a la vez:
            try {
                conexion!!.rollback() // si al ejecutar da error, hacemos rollback
            } catch (e1: SQLException) {
//                Toast.makeText(context,
//                    "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        } finally {
            try {
                sentencia?.close()
                resultado?.close()
            } catch (_: SQLException) {

            }
            desconectar()
        }
        return academia
    }

    /**
     * Obtiene una academia a partir de un ResultSet.
     * @param resultado - El ResultSet que contiene los datos de la academia.
     * @return Devuelve la academia obtenida.
     */
    private fun getAcademia(resultado: ResultSet): Academia {
        val codAcademia = resultado.getInt("cod_academia")
        val username = resultado.getString("usuario")
        val contrasenaAcademia = resultado.getString("contrasena")
        val emailAcademia = resultado.getString("email")
        val nombreAcademia = resultado.getString("nombre")
        val telefonoAcademia = resultado.getLong("telefono")
        val direccionAcademia = resultado.getString("direccion")
        var imgAcademia = resultado.getString("img_perfil")
        val latitud = resultado.getDouble("latitud")
        val longitud = resultado.getDouble("longitud")

        return Academia(codAcademia,username,contrasenaAcademia, emailAcademia,nombreAcademia, telefonoAcademia, direccionAcademia, imgAcademia,latitud,longitud)
        //return Academia(codAcademia,username,contrasenaAcademia, emailAcademia,nombreAcademia, telefonoAcademia, direccionAcademia,ByteArray(7))
    }

    /**
     * Hace select de todas las academias con una ubicación.
     * @return Devuelve un conjunto de academias con ubicación.
     */
    fun obtenerListaAcademiasConUbicacion(): Set<Academia> {
        val academias: MutableSet<Academia> = mutableSetOf()
        var sentencia: Statement? = null
        var resultado: ResultSet? = null

        conectar()

        try {
            val sql = "SELECT * FROM Academias WHERE latitud IS NOT NULL AND longitud IS NOT NULL"
            sentencia = conexion!!.createStatement()
            resultado = sentencia.executeQuery(sql)

            while (resultado.next()) {
                val academia = getAcademia(resultado)
                academias.add(academia)
            }
        } catch (e: SQLException) {
            // Manejo de excepciones
        } finally {
            try {
                sentencia?.close()
                resultado?.close()
            } catch (e: SQLException) {
                Log.i("TAG", e.message.toString())
            }
            desconectar()
        }

        return academias
    }

    /**
     * Consulta el username y contraseña de una academia.
     * @param usuario - El nombre de usuario de la academia.
     * @param contrasena - La contraseña de la academia.
     * @return Devuelve la academia encontrada.
     */
    fun consultarUsuarioContrasena(usuario: String, contrasena: String): Academia {
        var sentencia: Statement? = null
        var resultado: ResultSet? = null
        conectar()
        var academia = Academia()

        try {
            conexion!!.autoCommit = false // para hacer transacción a la vez

            val sql = "SELECT * FROM academias WHERE usuario = '$usuario' AND contrasena = '$contrasena';"
            sentencia = conexion!!.createStatement()
            resultado = sentencia.executeQuery(sql)

            if(resultado.next()){
                academia = getAcademia(resultado)
            }
            conexion!!.commit() // para hacer transacción a la vez
        } catch (syntax: SQLSyntaxErrorException) {

        } catch (e: NullPointerException) {
            Log.i("TAGnull", "consultarUsuarioContrasena: " + e.message.toString())

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
        return academia
    }

    /**
     * Borra una academia de la base de datos.
     * @param context - El contexto de la aplicación.
     * @param academia - La academia a borrar.
     */
    fun borrarUsuario(context: Context, academia: Academia) {
        var sentencia: PreparedStatement? = null
        val codAcademia = academia.codAcademia

        conectar()
        try {
            val sql = "DELETE FROM academias WHERE academia = ?"
            sentencia = conexion!!.prepareStatement(sql)
            sentencia.setInt(1, codAcademia)
            sentencia.executeUpdate()
        } catch (_: SQLException) {
            Toast.makeText(context,
                "La academia no se ha encontrado", Toast.LENGTH_SHORT).show()
        } finally {
            try {
                sentencia?.close()
            } catch (_: SQLException) {

            }
            desconectar()
        }
    }

    /***
     * Permite editar los datos de una academia.
     * @param context - El contexto de la aplicación.
     * @param academia - La academia con los datos actualizados.
     */
    fun modificarAcademia(context: Context, academia: Academia) {
        var sentencia: PreparedStatement? = null
        var username = academia.username
        var contrasena = academia.contrasena
        var email = academia.email
        val nombre = academia.nombre
        val telefono = academia.telefono
        val direccion = academia.direccion
        val imgPerfil : String = academia.imgPerfil
        val latitud : Double = academia.latitud
        val longitud : Double = academia.longitud

        conectar()
        try {
            conexion!!.autoCommit = false // para hacer transacción a la vez
            val sql = "UPDATE academias SET usuario = ?, contrasena= ?,email = ?,nombre = ?, telefono = ?, direccion = ?, img_perfil = ?, latitud = ?, longitud = ?"
            sentencia = conexion!!.prepareStatement(sql)

            sentencia.setString(1, username)
            sentencia.setString(2, contrasena)
            sentencia.setString(3, email)
            sentencia.setString(4, nombre)
            sentencia.setString(5, direccion)
            sentencia.setLong(6, telefono)
            sentencia.setString(7, imgPerfil)
            sentencia.setDouble(8, latitud)
            sentencia.setDouble(9, longitud)

            sentencia.executeUpdate()
            conexion!!.commit() // para hacer transacción a la vez
        } catch (e: SQLException) {
            Toast.makeText(context,"Debe rellenar los campos obligatorios correctamente", Toast.LENGTH_SHORT).show()
            // para hacer transacción a la vez:
            try {
                conexion!!.rollback() // si al ejecutar da error, hacemos rollback
            } catch (e1: SQLException) {
                Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        } finally {
            try {
                sentencia?.close()
            } catch (e: SQLException) {
                Log.e("SQL_Exception", "Error al hacer Update de academia" )
            }
            desconectar()
        }
    }

    /**
     * Devuelve el número total de academias registradas en la base de datos.
     * @return El número total de academias.
     */
    fun obtenerNumeroDeAcademias(): Int {
        var sentencia: Statement? = null
        var resultado: ResultSet? = null
        var numeroDeAcademias = 0

        conectar()

        try {
            val sql = "SELECT COUNT(*) AS total FROM Academias"
            sentencia = conexion!!.createStatement()
            resultado = sentencia.executeQuery(sql)

            if (resultado.next()) {
                numeroDeAcademias = resultado.getInt("total")
            }
        } catch (e: SQLException) {
        } finally {
            try {
                sentencia?.close()
                resultado?.close()
            } catch (e: SQLException) {
            }
            desconectar()
        }

        return numeroDeAcademias
    }
}