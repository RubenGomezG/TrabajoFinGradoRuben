package com.example.appacademia.ui.fragments.registroAcademia

import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.appacademia.R
import com.example.appacademia.dao.servidorSQL.AcademiaDAO
import com.example.appacademia.dao.servidorSQL.UsuarioDAO
import com.example.appacademia.databinding.FragmentRegistroAcademiaBinding
import com.example.appacademia.model.Academia
import com.example.appacademia.model.Usuario
import com.example.appacademia.ui.activities.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistroAcademiaFragment : Fragment() {

    private lateinit var btnRegistrar: Button
    private lateinit var navController: NavController
    private var _binding: FragmentRegistroAcademiaBinding? = null

    private lateinit var btnTogglePassword: ImageButton
    private lateinit var btnTogglePasswordConfirmar: ImageButton
    private lateinit var campoContrasena: EditText
    private lateinit var campoContrasenaConfirmar: EditText

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    /***
     * Crear la vista y enlazar todos sus elementos con las
     * variables del fragment para poder manipularlas
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     *
     * @return devuelve la vista proyectada en el fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val registroAcademiaViewModel =
            ViewModelProvider(this).get(RegistroAcademiaViewModel::class.java)

        navController = (activity as MainActivity).getNavController()

        _binding = FragmentRegistroAcademiaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        campoContrasena = binding.campoContrasena
        btnTogglePassword = binding.btnTogglePassword
        setupPasswordToggle(campoContrasena, btnTogglePassword)

        campoContrasenaConfirmar = binding.campoContrasenaConfirmar
        btnTogglePasswordConfirmar = binding.btnTogglePasswordConfirmar
        setupPasswordToggle(campoContrasenaConfirmar, btnTogglePasswordConfirmar)

        btnRegistrar = binding.btnRegistrarse
        btnRegistrar.setOnClickListener {
            validarForm(root)
        }

        return root
    }

    /**
     * Hacer que en los campos de contraseña se oculten o no los caracteres
     *
     * @param editText - Campo de texto
     * @param toggleButton - icono para mostrar contraseña
     */
    private fun setupPasswordToggle(editText: EditText, toggleButton: ImageButton) {
        var isPasswordVisible = false

        toggleButton.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            editText.inputType = if (isPasswordVisible) {
                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD or InputType.TYPE_CLASS_TEXT
            } else {
                InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            }

            editText.setSelection(editText.text.length)

            val iconResource = if (isPasswordVisible) R.drawable.baseline_arrow_drop_down_24 else R.drawable.baseline_arrow_drop_up_24
            toggleButton.setImageResource(iconResource)
        }
    }

    /***
     * Al destruir la vista, borramos el contenido del binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Validar que todos los campos se hayan rellenado correctamente
     *
     * @param view - vista actual
     */
    fun validarForm(view: View) {
        try {
            val nombre = view.findViewById<EditText>(R.id.campoNombre)?.text.toString()
            val telefono = view.findViewById<EditText>(R.id.campoTelefono)?.text.toString().toLong()
            val email = view.findViewById<EditText>(R.id.campoEmail)?.text.toString()
            val contrasena = view.findViewById<EditText>(R.id.campoContrasena)?.text.toString()
            val nomUsuario = view.findViewById<EditText>(R.id.campoNomUser)?.text.toString()
            val contrasenaConfirm =
                view.findViewById<EditText>(R.id.campoContrasenaConfirmar)?.text.toString()
            val direccion = view.findViewById<EditText>(R.id.campoDireccion)?.text.toString()

            var hayErrores = false

            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                view.findViewById<EditText>(R.id.campoEmail)?.error = "Correo electrónico inválido"
                hayErrores = true
            }
            if (telefono.toString().length != 9) {
                view.findViewById<EditText>(R.id.campoTelefono)?.error =
                    "Número de teléfono inválido"
                hayErrores = true
            }
            if (!Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\d\\s]).{8,}\$").matches(
                    contrasena
                )
            ) {
                view.findViewById<EditText>(R.id.campoContrasena)?.error =
                    "La contraseña no cumple con los requisitos de seguridad"
                hayErrores = true
            }
            if (hayErrores) {
                Toast.makeText(
                    requireContext(),
                    "Por favor, revisa los campos marcados",
                    Toast.LENGTH_SHORT
                ).show()
                return
            }

            if (contrasena.compareTo(contrasenaConfirm) == 0) {
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            val academiaDAO = AcademiaDAO()
                            val academia = Academia(
                                0,
                                nomUsuario,
                                contrasena,
                                email,
                                nombre,
                                telefono,
                                direccion
                            )

                            if (academiaDAO.existeRegistro(email, nomUsuario)) {
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(
                                        requireContext(),
                                        "El correo o el nombre de usuario ya existen",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                return@launch
                            }
                            academiaDAO.anadirAcademia(requireContext(), academia)
                            withContext(Dispatchers.Main) {
                                gotoLogin()
                            }

                        } catch (e: Exception) {
                            Log.i("exceptiónc", e.message.toString())
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    "Error al validar y agregar academia",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            } else {
                view.findViewById<EditText>(R.id.campoContrasena)?.error =
                    "Las contraseñas no coinciden"
                Toast.makeText(
                    requireContext(),
                    "Por favor, revisa los campos marcados",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } catch (ex: Exception) {
            Toast.makeText(
                requireContext(),
                "Error al validar y agregar academia",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Ir al login
     */
    fun gotoLogin() {
        navController.navigate(R.id.navigation_login)
    }
}