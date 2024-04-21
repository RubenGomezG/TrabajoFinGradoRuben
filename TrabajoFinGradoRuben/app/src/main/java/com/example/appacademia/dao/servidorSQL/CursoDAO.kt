package com.example.appacademia.dao.servidorSQL

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.appacademia.model.Curso
import java.sql.Date
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.SQLSyntaxErrorException
import java.sql.Statement

/**
 * Clase que maneja las operaciones relacionadas con los cursos en la base de datos.
 */class CursoDAO : InterfaceDAO() {

    /**
     * Agrega un nuevo curso a la base de datos.
     * @param curso - El curso a agregar.
     */
    fun anadirCurso(curso : Curso) {
        var sentencia: PreparedStatement? = null
        var nombre = curso.nombre
        var fechaInicio = curso.fechaInicio
        val fechaFin = curso.fechaFin
        val precio = curso.precio
        val valoracion = 0.0
        val descripcion = curso.descripcion
        val tipo = curso.tipo
        val codAcademia = curso.codAcademia
        val fechaUtil1: java.util.Date = java.util.Date()
        val fechaSqlTimestamp1: java.sql.Timestamp = java.sql.Timestamp(fechaUtil1.time)
        val fechaUtil2: java.util.Date = java.util.Date()
        val fechaSqlTimestamp2: java.sql.Timestamp = java.sql.Timestamp(fechaUtil2.time)

        conectar()
        try {
            conexion!!.autoCommit = false // para hacer transacción a la vez

            val sql = "INSERT INTO Cursos (nombre_curso, fecha_inicio_curso, fecha_fin_curso, precio, valoracion , descripcion, tipo, cod_academia) VALUES (?,?,?,?,?,?,?,?)"

            sentencia = conexion!!.prepareStatement(sql)

            sentencia.setString(1, nombre)
            sentencia.setTimestamp(2, fechaSqlTimestamp1)
            sentencia.setTimestamp(3, fechaSqlTimestamp2)
            sentencia.setDouble(4, precio)
            sentencia.setDouble(5, valoracion)
            sentencia.setString(6, descripcion)
            sentencia.setString(7, tipo)
            sentencia.setInt(8, codAcademia)

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
     * Consulta un curso en la base de datos basado en el código del curso.
     * @param codigoCurso - El código del curso a consultar.
     * @return El curso encontrado.
     */
    fun consultarCurso(codigoCurso : Int): Curso {
        var sentencia: Statement? = null
        var resultado: ResultSet? = null
        conectar()

        var curso = Curso()

        try {
            conexion!!.autoCommit = false // para hacer transacción a la vez

            val sql = "SELECT * FROM Cursos WHERE cod_curso = '$codigoCurso';"
            sentencia = conexion!!.createStatement()
            resultado = sentencia.executeQuery(sql)

            while (resultado.next()) {
                curso = getCurso(resultado)
            }
            conexion!!.commit() // para hacer transacción a la vez
        } catch (syntax: SQLSyntaxErrorException) {

            return Curso()
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
        return curso
    }

    /**
     * Obtiene un curso a partir de un ResultSet.
     * @param resultado - El ResultSet que contiene los datos del curso.
     * @return El curso obtenido.
     */
    private fun getCurso(resultado: ResultSet): Curso {
        val codCurso = resultado.getInt("cod_curso")
        val nombre = resultado.getString("nombre_curso")
        val fechaInicio = resultado.getDate("fecha_inicio_curso")
        val fechaFin = resultado.getDate("fecha_fin_curso")
        val precio = resultado.getDouble("precio")
        val valoracion = resultado.getDouble("valoracion")
        val descripcion = resultado.getString("descripcion")
        val tipo = resultado.getString("tipo")
        val codAcademia = resultado.getInt("cod_academia")
        return Curso(codCurso,nombre,fechaInicio, fechaFin, precio, valoracion , descripcion, tipo, codAcademia)
    }

    /**
     * Borra un curso de la base de datos.
     * @param context - El contexto de la aplicación.
     * @param curso - El curso a borrar.
     */
    fun borrarCurso(context: Context, curso: Curso) {
        var sentencia: PreparedStatement? = null
        val codCurso = curso.codCurso

        conectar()
        try {
            val sql = "DELETE FROM cursos WHERE cod_curso = ?"
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
    fun modificarCurso(context: Context, curso: Curso) {
        var sentencia: PreparedStatement? = null
        var nombre = curso.nombre
        var fechaInicio = curso.fechaInicio
        var fechaFin = curso.fechaFin
        val precio = curso.precio
        val valoracion = curso.valoracion
        val descripcion = curso.descripcion
        val tipo = curso.tipo
        val codAcademia = curso.codAcademia

        conectar()
        try {
            conexion!!.autoCommit = false // para hacer transacción a la vez
            val sql = "UPDATE cursos SET nombre_curso = ?, fecha_inicio_curso = ?, fecha_fin_curso = ?, precio = ?, valoracion = ?, descripcion = ?, tipo = ?, cod_academia = ?"
            sentencia = conexion!!.prepareStatement(sql)

            sentencia.setString(1, nombre)
            sentencia.setDate(2, fechaInicio as Date?)
            sentencia.setDate(3, fechaFin as Date?)
            sentencia.setDouble(4, precio)
            sentencia.setDouble(5, valoracion)
            sentencia.setString(6, descripcion)
            sentencia.setString(7, tipo)
            sentencia.setInt(8, codAcademia)


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
     * Obtiene una lista de cursos de la base de datos según los filtros y el orden indicados.
     * @param nombreCurso - El nombre del curso a buscar.
     * @param filtro - El filtro para la búsqueda.
     * @param orden - El orden para la lista de cursos.
     * @return La lista de cursos filtrada y ordenada.
     */
    fun obtenerCursosConFiltros(
        nombreCurso: String,
        filtro: String,
        orden: String

    ): ArrayList<Curso> {
        val todosCursos = ArrayList<Curso>()
        var sentencia: PreparedStatement? = null
        var resultado: ResultSet? = null

        try {
            conectar()

            var sql = "SELECT * FROM Cursos"

            // Construir la cláusula WHERE según los parámetros recibidos
            var whereClause = ""

            if (nombreCurso.isNotEmpty()) {
                whereClause += " nombre_curso LIKE ? AND"
            }

            if (filtro != "Filtrar por") {
                whereClause += " tipo = ? AND"
            }

            if (whereClause.isNotEmpty()) {
                whereClause = whereClause.removeSuffix(" AND")
                sql += " WHERE$whereClause"
            }

            if (orden != "Ordenar por") {
                when (orden) {
                    "fecha 🔼" -> sql += " ORDER BY fecha_inicio_curso ASC"
                    "Precio" -> sql += " ORDER BY precio ASC"
                    "Valoración" -> sql += " ORDER BY valoracion DESC"
                }
            }

            sentencia = conexion!!.prepareStatement(sql)

            var parameterIndex = 1
            if (nombreCurso.isNotEmpty()) {
                sentencia.setString(parameterIndex++, "%$nombreCurso%")
            }
            if (filtro != "Filtrar por") {
                sentencia.setString(parameterIndex++, filtro)
            }

            resultado = sentencia.executeQuery()

            while (resultado.next()) {
                val curso = getCurso(resultado)
                todosCursos.add(curso)
            }

        } catch (e: SQLException) {
            e.printStackTrace()

            Log.e("SQL_ERROR", "Error al ejecutar la consulta SQL: ${e.message}")
        } finally {
            try {
                sentencia?.close()
                resultado?.close()
                desconectar()
            } catch (e: SQLException) {
                e.printStackTrace()

                Log.e("SQL_ERROR", "Error al cerrar los recursos: ${e.message}")

            }
        }
        return todosCursos
    }

    /**
     * Obtiene todos los cursos de la base de datos.
     * @return La lista de todos los cursos.
     */
    fun obtenerTodosLosCursos(): ArrayList<Curso> {
        val todosCursos = ArrayList<Curso>()
        var sentencia: PreparedStatement? = null
        var resultado: ResultSet? = null

        try {
            conectar()

            val sql = "SELECT * FROM Cursos;"
            sentencia = conexion!!.prepareStatement(sql)
            resultado = sentencia.executeQuery()

            while (resultado.next()) {
                val curso = getCurso(resultado)
                todosCursos.add(curso)
            }
        } catch (e: SQLException) {
            e.printStackTrace()
            Log.e("SQL_ERROR", "Error al ejecutar la consulta SQL: ${e.message}")
        } finally {
            try {
                sentencia?.close()
                resultado?.close()
                desconectar()
            } catch (e: SQLException) {
                e.printStackTrace()
                Log.e("SQL_ERROR", "Error al cerrar los recursos: ${e.message}")
            }
        }

        return todosCursos
    }

    /**
     * Obtiene todos los cursos de una academia específica.
     * @param codAcadenia - El código de la academia.
     * @return La lista de cursos de la academia especificada.
     */
    fun obtenerTodosLosCursosDeAcademia(codAcadenia : Int): ArrayList<Curso> {
        val todosCursosDeAcademia = ArrayList<Curso>()
        var sentencia: PreparedStatement? = null
        var resultado: ResultSet? = null

        try {
            conectar()

            val sql = "SELECT * FROM Cursos WHERE cod_academia = ?;"
            sentencia = conexion!!.prepareStatement(sql)
            sentencia.setInt(1,codAcadenia)
            resultado = sentencia.executeQuery()

            while (resultado.next()) {
                val curso = getCurso(resultado)
                todosCursosDeAcademia.add(curso)
            }

        } catch (e: SQLException) {
            e.printStackTrace()
            Log.e("SQL_ERROR obtenercursos", "Error al ejecutar la consulta SQL: ${e.message}")
        } finally {
            try {
                sentencia?.close()
                resultado?.close()
                desconectar()
            } catch (e: SQLException) {
                e.printStackTrace()
                Log.e("SQL_ERROR", "Error al cerrar los recursos: ${e.message}")
            }
        }

        return todosCursosDeAcademia
    }

    /**
     * Obtiene una lista de cursos filtrada y ordenada según los parámetros indicados.
     * @param context - El contexto de la aplicación.
     * @param textoABuscar - El texto a buscar.
     * @param filtro - El filtro para la búsqueda.
     * @param orden - El orden para la lista de cursos.
     * @return La lista de cursos filtrada y ordenada.
     */
    fun listaCursos(context: Context, textoABuscar : String, filtro : String, orden : String) : ArrayList<Curso>{
        val todosCursos = ArrayList<Curso>()
        var sentencia: PreparedStatement? = null
        var resultado: ResultSet? = null
        var curso = Curso()
        var textoABuscar = textoABuscar
        var filtro = filtro
        var orden = orden

        when (orden) {
            "Fecha \uD83D\uDD3C" ->  orden = "fecha_inicio_curso"
            "Precio" ->  orden = "precio"
            "Valoración" -> orden = "valoracion"
            else -> { // Note the block
                orden = ""
            }
        }
        conectar()
        try {
            conexion!!.autoCommit = false // para hacer transacción a la vez
            var sql : String = "SELECT * FROM `cursos`;"
            if (textoABuscar == "" && orden == "" && filtro == "Filtrar por") {
                sentencia = conexion!!.prepareStatement(sql)
            }
            else if(textoABuscar == "" && orden == ""){
                sql += " WHERE tipo = ?;"
                sentencia = conexion!!.prepareStatement(sql)
                sentencia.setString(1, filtro)
            }
            else if(textoABuscar == "" && filtro == "Filtrar por"){
                sql += " ORDER BY ?;"
                sentencia = conexion!!.prepareStatement(sql)
                sentencia.setString(1, orden)
            }
            else if(orden == "" && filtro == "Filtrar por"){
                sql += " WHERE nombre_curso = ?;"
                sentencia = conexion!!.prepareStatement(sql)
                sentencia.setString(1, textoABuscar)
            }
            else if(textoABuscar == ""){
                sql += " WHERE tipo = ? ORDER BY ?;"
                sentencia = conexion!!.prepareStatement(sql)
                sentencia.setString(1, filtro)
                sentencia.setString(2, orden)
            }
            else if(orden == ""){
                sql += " WHERE nombre_curso = ?% AND tipo = ?;"
                sentencia = conexion!!.prepareStatement(sql)
                sentencia.setString(1, textoABuscar)
                sentencia.setString(2, filtro)
            }
            else if(filtro == "Filtrar por"){
                sql += " WHERE nombre_curso = ?% ORDER BY ?;"
                sentencia = conexion!!.prepareStatement(sql)
                sentencia.setString(1, textoABuscar)
                sentencia.setString(2, orden)
            }
            else{
                sql += " WHERE nombre_curso = ?% AND tipo = ? ORDER BY ?;"
                sentencia = conexion!!.prepareStatement(sql)
                sentencia.setString(1, textoABuscar)
                sentencia.setString(2, filtro)
                sentencia.setString(3, orden)
            }

            resultado = sentencia.executeQuery()

            while (resultado.next()) {
                curso = getCurso(resultado)
                todosCursos.add(curso)
            }

        } catch (syntax: SQLSyntaxErrorException) {
            Log.e("SQL_ERROR", "Error: ${syntax.message}")
            return ArrayList()
        } catch (e: NullPointerException) {
            Log.e("SQL_ERROR", "Error: ${e.message}")
        } catch (e: SQLException) {
            Log.e("SQL_ERROR", "Error: ${e.message}")
            // para hacer transacción a la vez:
            try {
                conexion!!.rollback() // si al ejecutar da error, hacemos rollback
            } catch (_: SQLException) {
                Log.e("SQL_ERROR", "Error: ${e.message}")
            }
        } finally {
            try {
                sentencia?.close()
                resultado?.close()
            } catch (_: SQLException) {
            }
            desconectar()
        }
        return todosCursos
    }
}