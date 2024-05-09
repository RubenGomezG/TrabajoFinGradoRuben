package com.example.appacademia.model

import java.util.Date

class Mensaje {
    var codMensaje: Int = 0
    var codConversacion: Int = 0
    var senderId: Int = 0
    var content: String = ""
    var timestamp: Date? = null



    constructor(codMensaje: Int, codConversacion: Int, senderId: Int, content: String, timestamp: Date?) {
        this.codMensaje = codMensaje
        this.codConversacion = codConversacion
        this.senderId = senderId
        this.content = content
        this.timestamp = timestamp
    }
    constructor(){
        this.codMensaje = 0
        this.codConversacion = 0
        this.senderId = 0
        this.content = ""
        this.timestamp = null
    }
}