package com.example.appacademia.model

import java.util.Date

class Mensaje {
    var codMensaje: Int = 0
    var senderId: Int = 0
    var content: String = ""
    var timestamp: Date? = null



    constructor(codMensaje: Int, senderId: Int, content: String, timestamp: Date?) {
        this.codMensaje = codMensaje
        this.senderId = senderId
        this.content = content
        this.timestamp = timestamp
    }
    constructor(){
        this.codMensaje = 0
        this.senderId = 0
        this.content = ""
        this.timestamp = null
    }
}