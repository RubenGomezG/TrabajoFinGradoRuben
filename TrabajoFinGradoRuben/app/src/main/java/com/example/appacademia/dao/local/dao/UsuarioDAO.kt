package com.example.appacademia.dao.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Insert
import androidx.room.Delete
import com.example.appacademia.dao.local.entity.Usuario

/**
 * Interfaz DAO (Data Access Object) para la entidad Usuario.
 * Proporciona métodos para realizar operaciones CRUD (Create, Read, Update, Delete) en la tabla de usuarios.
 */
@Dao
interface UsuarioDao {
    /**
     * Obtiene todos los usuarios de la base de datos.
     * @return Lista de todos los usuarios.
     */
    @Query("SELECT * FROM usuario")
    fun getAll(): List<Usuario>

    /**
     * Carga usuarios por sus IDs.
     * @param nombreUsuarios - Arreglo de IDs de usuarios a cargar.
     * @return Lista de usuarios correspondientes a los IDs proporcionados.
     */
    @Query("SELECT * FROM usuario WHERE NombreUsuario IN (:nombreUsuarios)")
    fun loadAllByIds(nombreUsuarios: IntArray): List<Usuario>

    /**
     * Obtiene un usuario por su nombre de usuario.
     * @param nombreUsuario - Nombre de usuario del usuario a obtener.
     * @return Usuario correspondiente al nombre de usuario especificado, o nulo si no se encuentra.
     */
    @Query("SELECT * FROM usuario WHERE NombreUsuario = :nombreUsuario LIMIT 1")
    suspend fun getUsuario(nombreUsuario: String): Usuario?

    /**
     * Elimina todos los usuarios de la base de datos.
     */
    @Query("DELETE FROM usuario")
    fun deleteAllUsers()

    /**
     * Inserta uno o varios usuarios en la base de datos.
     * @param users - Usuarios a insertar.
     */
    @Insert
    fun insertAll(vararg users: Usuario)

    /**
     * Elimina un usuario de la base de datos.
     * @param user - Usuario a eliminar.
     */
    @Delete
    fun delete(user: Usuario)

}