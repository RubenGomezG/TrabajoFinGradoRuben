package com.example.appacademia.ui.fragments.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.appacademia.R
import com.example.appacademia.dao.local.dao.UsuarioDao
import com.example.appacademia.dao.local.database.LocalDatabase
import com.example.appacademia.dao.servidorSQL.AcademiaDAO
import com.example.appacademia.dao.servidorSQL.HashUtils
import com.example.appacademia.dao.servidorSQL.UsuarioDAO
import com.example.appacademia.databinding.FragmentLoginBinding
import com.example.appacademia.model.Academia
import com.example.appacademia.model.Usuario
import com.example.appacademia.ui.activities.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginFragment : Fragment() {

    private lateinit var btnLogin: Button
    private lateinit var txtForgotPass: TextView
    private lateinit var txtSignUpUsuario: TextView
    private lateinit var txtSignUpAcademia: TextView
    private lateinit var editUsername: EditText
    private lateinit var editPassword: EditText
    private lateinit var recordarme: CheckBox

    private lateinit var navController: NavController
    private var _binding: FragmentLoginBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val loginViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        navController = (activity as MainActivity).getNavController()
        //navController = requireActivity().findNavController(R.id.nav_host_fragment_activity_main)

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        btnLogin = binding.btnIniciarSesion
        recordarme = binding.checkRecordarme
        btnLogin.setOnClickListener {
            iniciarSesion(root)
        }

        txtForgotPass = binding.txtRestablecerContrasena
        txtForgotPass.setOnClickListener {
            gotoForgotPassword()
        }

        txtSignUpUsuario = binding.txtRegistroUsuario
        txtSignUpUsuario.setOnClickListener {
            gotoRegistroUsuario()
        }

        txtSignUpAcademia = binding.txtRegistroAcademia
        txtSignUpAcademia.setOnClickListener {
            gotoRegistroAcademia()
        }

        editUsername = binding.campoUsername
        editPassword = binding.campoContrasena

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun gotoForgotPassword() {
        navController.navigate(R.id.navigation_forgotPassword)
    }

    fun gotoRegistroUsuario() {
        navController.navigate(R.id.navigation_registroUsuario)
    }

    fun gotoRegistroAcademia() {
        navController.navigate(R.id.navigation_registroAcademia)
    }



    fun iniciarSesion(view: View) {
        //al hacer click en iniciar, regresar a buscar
        var usuario: Usuario? = null
        var academia: Academia? = null
        if (editUsername.text.isNullOrEmpty() || editPassword.text.isNullOrEmpty()) {
            Toast.makeText(
                context,
                "Por favor, rellene los campos de usuario y contraseña",
                Toast.LENGTH_LONG
            ).show()
            return
        } else {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                lifecycleScope.launch(Dispatchers.IO) {

                    val usuariodao = UsuarioDAO()
                    val academiadao = AcademiaDAO()
                    val contrasenaCodificada = HashUtils.sha256(editPassword.text.toString())
                    Log.i("contraseñas", contrasenaCodificada)
                    usuario = usuariodao.consultarUsuarioContrasena(
                        editUsername.text.toString(),
                        contrasenaCodificada
                    )
                    academia = academiadao.consultarUsuarioContrasena(
                        editUsername.text.toString(),
                        contrasenaCodificada
                    )

                    withContext(Dispatchers.Main) {
                        if (usuario?.username.isNullOrEmpty() && academia?.username.isNullOrEmpty()) {
                            Log.i("No ha llegado", "No ha llegado")

                            Toast.makeText(
                                context,
                                "Usuario o contraseña incorrectas",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Log.i("conexion 2", "No necesitará conectarse la próxima vez")
                            if (recordarme.isChecked) {
                                withContext(Dispatchers.IO) {
                                    val userDao =
                                        LocalDatabase.getInstance(requireContext()).userDao()

                                    userDao.deleteAllUsers()

                                    if (userDao.getAll().isEmpty()) {
                                        val newUser =
                                            com.example.appacademia.dao.local.entity.Usuario(
                                                editUsername.text.toString(),
                                                editPassword.text.toString(),
                                                null,
                                                null
                                            )
                                        userDao.insertAll(newUser)
                                        Log.i("Usuario Correcto", "No necesitará conectarse la próxima vez")
                                    }
                                }
                            }
                            Toast.makeText(
                                context,
                                "Conexión a la base de datos correcta",
                                Toast.LENGTH_LONG
                            ).show()
                            Thread.sleep(3000)
                            navController.navigate(R.id.navigation_buscar)
                            (activity as MainActivity).loggedIn = true
                            (activity as MainActivity).username = editUsername.text.toString()
                        }
                    }
                }
            }
        }
    }
}