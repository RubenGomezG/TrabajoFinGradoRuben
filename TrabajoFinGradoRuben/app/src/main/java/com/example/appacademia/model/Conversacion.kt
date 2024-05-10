package com.example.appacademia.model

import java.util.Date

class Conversacion {
    var codConversacion: Int = 0
    var usuario1Id: Int = 0
    var usuario2Id: String = ""




    constructor(codConversacion: Int, usuario2Id: String) {
        this.codConversacion = codConversacion
        this.usuario1Id = 1
        this.usuario2Id = usuario2Id

    }
    constructor(){
        this.codConversacion = 0
        this.usuario1Id  = 0
        this.usuario2Id = ""
    }
}