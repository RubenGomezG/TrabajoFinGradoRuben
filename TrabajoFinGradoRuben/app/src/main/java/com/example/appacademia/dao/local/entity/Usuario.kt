package com.example.appacademia.dao.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Clase que representa la entidad Usuario en la base de datos local.
 * @property NombreUsuario Nombre de usuario del usuario.
 * @property Contrasena Contraseña del usuario.
 * @property DarkMode Indica si el usuario tiene activado el modo oscuro.
 * @property Idioma Idioma preferido del usuario.
 */
@Entity
data class Usuario(
    @PrimaryKey @ColumnInfo(name = "NombreUsuario") val NombreUsuario: String,
    @ColumnInfo(name = "Contrasena") val Contrasena: String,
    @ColumnInfo(name = "DarkMode") val DarkMode: Boolean?,
    @ColumnInfo(name = "Idioma") val Idioma: String?
)
