package com.example.appacademia.ui.activities

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class UtilidadRegistroTest {
    @Test
    fun `registro devuelve true`() {
        val result = UtilidadRegistro.validarInputRegistro(
            "RubenL",
            "abcdefghi",
            "abcdefghi"
        )
        assertThat(result).isTrue()
    }

    @Test
    fun `username existente devuelve false`() {
        val result = UtilidadRegistro.validarInputRegistro(
            "Endika",
            "12345678",
            "12345678"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `ConfirmarContrasena erronea devuelve false`() {
        val result = UtilidadRegistro.validarInputRegistro(
            "Maria",
            "12345678",
            "abcdefghi"
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `contrasena vacia devuelve false`() {
        val result = UtilidadRegistro.validarInputRegistro(
            "Antón",
            "",
            ""
        )
        assertThat(result).isFalse()
    }

    @Test
    fun `menos de 4 letras en la contrasena devuelve false`() {
        val result = UtilidadRegistro.validarInputRegistro(
            "Pedro",
            "abc1234",
            "abc1234"
        )
        assertThat(result).isFalse()
    }
}