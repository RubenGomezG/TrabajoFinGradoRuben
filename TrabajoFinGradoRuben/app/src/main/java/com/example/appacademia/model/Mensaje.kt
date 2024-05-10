package com.example.appacademia.model

import java.util.Date

class Mensaje {
    var codMensaje: Int = 0
    var codConversacion: Int = 0
    var senderUsername: String = ""
    var senderCodAcademia: Int = 0
    var content: String = ""
    var timestamp: Date? = null



    constructor(codMensaje: Int, codConversacion: Int, senderUsername: String, senderCodAcademia: Int, content: String, timestamp: Date?) {
        this.codMensaje = codMensaje
        this.codConversacion = codConversacion
        this.senderUsername = senderUsername
        this.senderCodAcademia = senderCodAcademia
        this.content = content
        this.timestamp = timestamp
    }
    constructor(){
        this.codMensaje = 0
        this.codConversacion = 0
        this.senderUsername = ""
        this.senderCodAcademia = 0
        this.content = ""
        this.timestamp = null
    }
}