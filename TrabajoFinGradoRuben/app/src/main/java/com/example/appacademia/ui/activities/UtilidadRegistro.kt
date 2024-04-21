package com.example.appacademia.ui.activities

object UtilidadRegistro {
    private val usuarios = listOf("Ruben","Cristina","Endika")
    /**
     * El input no es válido si...
     * ...el username está vacío
     * ...el username existe previamente
     * ...la confirmedPassword no es igual que la password
     * ...la password es menor de 4 letras
     */
    fun validarInputRegistro(
        username: String,
        password: String,
        confirmedPassword: String
    ):Boolean{
        if (username.isEmpty() || password.isEmpty()){
            return false
        }
        if(username in usuarios){
            return false
        }
        if(password != confirmedPassword){
            return false
        }
        if(password.count {it.isLetter()} < 4){
            return false
        }
        return true
    }

}