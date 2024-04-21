package com.example.appacademia.model

import java.text.DecimalFormat

// Definición de la clase Academia
class Academia {
    // Atributos de la academia
    var codAcademia: Int = 0 // Código de la academia
    var username: String = "" // Nombre de usuario
    var contrasena: String = "" // Contraseña
    var email: String = "" // Correo electrónico
    var nombre: String = "" // Nombre de la academia
    var telefono: Long = 0 // Número de teléfono
    var direccion: String = "" // Dirección
    var imgPerfil: String = "" // URL de la imagen de perfil
    var longitud: Double = 0.0 // Longitud geográfica
    var latitud: Double = 0.0 // Latitud geográfica

    // Constructor primario que toma todos los atributos
    constructor(
        codAcademia: Int,
        username: String,
        contrasena: String,
        email: String,
        nombre: String,
        telefono: Long,
        direccion: String,
        imgPerfil: String,
        longitud: Double,
        latitud: Double
    ) {
        // Inicialización de los atributos
        this.codAcademia = codAcademia
        this.username = username
        this.contrasena = contrasena
        this.email = email
        this.nombre = nombre
        this.telefono = telefono
        this.direccion = direccion
        this.imgPerfil = imgPerfil
        this.longitud = longitud
        this.latitud = latitud
    }

    // Constructor secundario que toma solo algunos atributos
    constructor(
        codAcademia: Int,
        username: String,
        contrasena: String,
        email: String,
        nombre: String,
        telefono: Long,
        direccion: String
    ) {
        // Llamada al constructor primario con valores predeterminados para algunos atributos
        this.codAcademia = codAcademia
        this.username = username
        this.contrasena = contrasena
        this.email = email
        this.nombre = nombre
        this.telefono = telefono
        this.direccion = direccion
    }

    // Constructor sin argumentos que inicializa todos los atributos con valores predeterminados
    constructor() {
        // Inicialización de los atributos con valores predeterminados
        this.codAcademia = 0
        this.username = ""
        this.contrasena = ""
        this.email = ""
        this.nombre = ""
        this.telefono = 0
        this.direccion = ""
        this.imgPerfil = ""
    }

    // Sobrescritura de la función toString() para proporcionar una representación de cadena de la instancia de la clase
    override fun toString(): String {
        return "Academia(cod_academia=$codAcademia, username='$username', contrasena='$contrasena', email='$email', nombre='$nombre', telefono=$telefono, direccion=$direccion, img_perfil=$imgPerfil, longitud=$longitud, latitud=$latitud)"
    }
}
