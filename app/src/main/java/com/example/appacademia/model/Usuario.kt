package com.example.appacademia.model

// Clase que representa un usuario
class Usuario {
    // Atributos del usuario
    var username: String = "" // Nombre de usuario
    var contrasena: String = "" // Contraseña
    var email: String = "" // Correo electrónico
    var nombre: String = "" // Nombre del usuario
    var apellidos: String = "" // Apellidos del usuario
    var telefono: Long = 0 // Número de teléfono
    var img_perfil: String = "" // URL de la imagen de perfil
    var edad: Int = 0 // Edad del usuario

    // Constructor que toma todos los atributos
    constructor(
        username: String,
        contrasena: String,
        email: String,
        nombre: String,
        apellidos: String,
        telefono: Long,
        img_perfil: String,
        edad: Int
    ) {
        // Inicialización de los atributos
        this.username = username
        this.contrasena = contrasena
        this.email = email
        this.nombre = nombre
        this.apellidos = apellidos
        this.telefono = telefono
        this.img_perfil = img_perfil
        this.edad = edad
    }

    // Constructor que toma algunos atributos con una imagen de perfil predeterminada
    constructor(
        username: String,
        contrasena: String,
        email: String,
        nombre: String,
        apellidos: String,
        telefono: Long,
        edad: Int
    ) {
        // Inicialización de los atributos con una imagen de perfil predeterminada
        this.username = username
        this.contrasena = contrasena
        this.email = email
        this.nombre = nombre
        this.apellidos = apellidos
        this.telefono = telefono
        this.img_perfil = ""
        this.edad = edad
    }

    // Constructor sin argumentos que inicializa todos los atributos con valores predeterminados
    constructor() {
        // Inicialización de los atributos con valores predeterminados
        this.username = ""
        this.contrasena = ""
        this.email = ""
        this.nombre = ""
        this.apellidos = ""
        this.telefono = 0
        this.img_perfil = ""
        this.edad = 0
    }

    // Sobrescritura de la función toString() para proporcionar una representación de cadena de la instancia de la clase
    override fun toString(): String {
        return "Usuario(username='$username', contrasena='$contrasena', email='$email', nombre='$nombre', apellidos='$apellidos', telefono=$telefono, img_perfil=$img_perfil, edad=$edad)"
    }
}
