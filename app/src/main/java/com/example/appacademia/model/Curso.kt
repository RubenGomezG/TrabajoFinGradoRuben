package com.example.appacademia.model

import java.util.Date

// Clase que representa un curso
class Curso {
    // Atributos del curso
    var codCurso: Int = 0 // Código del curso
    var nombre: String = "" // Nombre del curso
    var fechaInicio: Date? = null // Fecha de inicio del curso
    var fechaFin: Date? = null // Fecha de finalización del curso
    var precio: Double = 0.0 // Precio del curso
    var valoracion: Double = 0.0 // Valoración del curso
    var descripcion: String = "" // Descripción del curso
    var tipo: String = "" // Tipo de curso
    var codAcademia: Int = 0 // Código de la academia asociada al curso
    var ignoreItemClick = false // Variable para ignorar clics en la interfaz de usuario

    // Constructor primario que toma todos los atributos
    constructor(
        codCurso: Int,
        nombre: String,
        fechaInicio: Date?,
        fechaFin: Date?,
        precio: Double,
        valoracion: Double,
        descripcion: String,
        tipo: String,
        codAcademia: Int
    ) {
        // Inicialización de los atributos
        this.codCurso = codCurso
        this.nombre = nombre
        this.fechaInicio = fechaInicio
        this.fechaFin = fechaFin
        this.precio = precio
        this.valoracion = valoracion
        this.descripcion = descripcion
        this.tipo = tipo
        this.codAcademia = codAcademia
    }

    // Constructor sin argumentos que inicializa todos los atributos con valores predeterminados
    constructor() {
        // Inicialización de los atributos con valores predeterminados
        this.codCurso = 0
        this.nombre = ""
        this.fechaInicio = null
        this.fechaFin = null
        this.precio = 0.0
        this.valoracion = 0.0
        this.descripcion = ""
        this.tipo = ""
        this.codAcademia = 0
    }

    // Sobrescritura de la función toString() para proporcionar una representación de cadena de la instancia de la clase
    override fun toString(): String {
        return "Curso(cod_curso=$codCurso, nombre='$nombre', fechaInicio=$fechaInicio, fechaFin=$fechaFin, precio='$precio', valoración='$valoracion', descripcion='$descripcion', tipo='$tipo', cod_academia=$codAcademia)"
    }
}
