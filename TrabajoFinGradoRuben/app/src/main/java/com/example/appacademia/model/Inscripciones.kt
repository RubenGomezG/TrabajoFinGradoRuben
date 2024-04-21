package com.example.appacademia.model

// Clase que representa una inscripción
class Inscripcion {
     // Atributos de la inscripción
     var username: String = "" // Nombre de usuario que se inscribe en un curso
     var codCurso: Int = 0 // Código del curso al que se inscribe el usuario

     // Constructor primario que toma todos los atributos
     constructor(
          username: String,
          codCurso: Int
     ) {
          // Inicialización de los atributos
          this.username = username
          this.codCurso = codCurso
     }

     // Constructor sin argumentos que inicializa todos los atributos con valores predeterminados
     constructor() {
          // Inicialización de los atributos con valores predeterminados
          this.username = ""
          this.codCurso = 0
     }

     // Sobrescritura de la función toString() para proporcionar una representación de cadena de la instancia de la clase
     override fun toString(): String {
          return "Inscripciones(username='$username', codCurso=$codCurso)"
     }
}
