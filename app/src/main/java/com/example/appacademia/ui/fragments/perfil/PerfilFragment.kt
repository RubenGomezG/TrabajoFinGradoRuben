package com.example.appacademia.ui.fragments.perfil

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.appacademia.R
import com.example.appacademia.dao.firebase.FirebaseDAO
import com.example.appacademia.dao.local.database.LocalDatabase
import com.example.appacademia.dao.servidorSQL.AcademiaDAO
import com.example.appacademia.dao.servidorSQL.HashUtils
import com.example.appacademia.dao.servidorSQL.UsuarioDAO
import com.example.appacademia.databinding.FragmentLoginBinding
import com.example.appacademia.databinding.FragmentPerfilBinding
import com.example.appacademia.ftp.InterfaceFTP
import com.example.appacademia.model.Academia
import com.example.appacademia.model.Usuario
import com.example.appacademia.ui.activities.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PerfilFragment : Fragment() {
    private lateinit var navController: NavController
    private var _binding: FragmentPerfilBinding? = null

    private lateinit var btnLogin: Button
    private lateinit var txtForgotPass: TextView
    private lateinit var txtSignUpUsuario: TextView
    private lateinit var txtSignUpAcademia: TextView
    private lateinit var btnLoginGoogle: Button
    private lateinit var editUsername: EditText
    private lateinit var editPassword: EditText
    private lateinit var imgPerfil: ImageView
    private lateinit var recordarme: CheckBox
    private lateinit var progressBar: ProgressBar
    private lateinit var scrollView: ScrollView

    private lateinit var campoEmail: TextView
    private lateinit var campoContrasena: TextView
    private lateinit var campoDireccion: TextView
    private lateinit var campoApellidos: TextView
    private lateinit var campoNombre: TextView
    private lateinit var campoTelefono: TextView
    private lateinit var campoEdad: TextView
    private lateinit var btnEditarInfoPerfil: Button
    private lateinit var btnCrearCurso: Button


    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private var _binding2: FragmentLoginBinding? = null
    private val binding2 get() = _binding2!!

    private val RC_SIGN_IN = 9001

    private var esEditable: Boolean = false

    /***
     * Crear la vista y enlazar todos sus elementos con las
     * variables del fragment para poder manipularlas.
     * Dependiendo de si la sesión está iniciada o no,
     * la vista que se inflará y rellenará será Login o Perfil
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
        val perfilViewModel = ViewModelProvider(this).get(PerfilViewModel::class.java)

        navController = (activity as MainActivity).getNavController()
        _binding = FragmentPerfilBinding.inflate(inflater, container, false)
        _binding2 = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val root2: View = binding2.root

        if ((activity as MainActivity).loggedIn) {

            val patata = rellenarDatosPerfil()

            progressBar = binding.root.findViewById(R.id.progressBar)
            scrollView = binding.root.findViewById(R.id.scrollView)
            imgPerfil = binding.root.findViewById(R.id.imageView)
            imgPerfil.setOnClickListener {
                abrirOpsFotoPerfil()
            }

            campoEmail = binding.campoEmail
            campoApellidos = binding.campoApellidos
            campoEdad = binding.campoEdad
            campoNombre = binding.campoNombre
            campoTelefono = binding.campoTelefono
            campoDireccion = binding.campoDireccion
            campoContrasena = binding.campoContrasena
            btnEditarInfoPerfil = binding.btnEditarInfoPerfil
            btnCrearCurso = binding.btnAnadirCurso
            btnCrearCurso.setOnClickListener{
                goToCrearcurso()
            }

            // Muestra el ProgressBar
            progressBar.visibility = View.VISIBLE

            // Oculta el contenido real
            scrollView.visibility = View.GONE

            campoEmail.isEnabled = false
            campoContrasena.isEnabled = false
            campoApellidos.isEnabled = false
            campoDireccion.isEnabled = false
            campoTelefono.isEnabled = false
            campoNombre.isEnabled = false
            campoEdad.isEnabled = false

            btnEditarInfoPerfil.setOnClickListener {
                if (esEditable) {
                    val nomUsuario = binding.campoNomUser.text.toString()
                    val contrasena = campoContrasena.text.toString()
                    val telefono = campoTelefono.text.toString().toLong()
                    val direccion = campoDireccion.text.toString()
                    val email = campoEmail.text.toString()
                    val apellidos = campoApellidos.text.toString()
                    val nombre = campoNombre.text.toString()
                    val edad = campoEdad.text.toString().toInt()
                    var hayErrores = false

                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        campoEmail.error = "Correo electrónico inválido"
                        hayErrores = true
                    }
                    if (telefono.toString().length != 9) {
                        campoTelefono.error = "Número de teléfono inválido"
                        hayErrores = true
                    }
                    if (contrasena.isNotEmpty()) {
                        if (!Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\w\\d\\s]).{8,}\$").matches(
                                contrasena
                            )
                        ) {
                            campoContrasena.error =
                                "La contraseña no cumple con los requisitos de seguridad"
                            hayErrores = true
                        }
                    }
                    if (hayErrores) {
                        Toast.makeText(
                            requireContext(),
                            "Por favor, revisa los campos marcados",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@setOnClickListener
                    } else {
                        val usuarioModificado = Usuario(
                            nomUsuario,
                            contrasena,
                            email,
                            nombre,
                            apellidos,
                            telefono,
                            "$nomUsuario.jpg",
                            edad
                        )
                        val usuarioDAO = UsuarioDAO()
                        lifecycleScope.launch {
                            withContext(Dispatchers.IO) {
                                if (contrasena.isEmpty()) {
                                    usuarioDAO.modificarUsuarioSinContrasena(usuarioModificado)
                                } else {
                                    usuarioDAO.modificarUsuario(usuarioModificado)
                                }
                            }
                        }
                    }
                    btnEditarInfoPerfil.text = "Editar perfil"

                    campoEmail.isEnabled = false
                    campoContrasena.isEnabled = false
                    campoApellidos.isEnabled = false
                    campoNombre.isEnabled = false
                    campoDireccion.isEnabled = false
                    campoTelefono.isEnabled = false
                    campoEdad.isEnabled = false

                    esEditable=false
                }else{
                    btnEditarInfoPerfil.text = "Confirmar"

                    campoEmail.isEnabled = true
                    campoContrasena.isEnabled = true
                    campoApellidos.isEnabled = true
                    campoDireccion.isEnabled = true
                    campoTelefono.isEnabled = true
                    campoNombre.isEnabled = true
                    campoEdad.isEnabled = true

                    esEditable=true
                }
            }
            manejarDesplegable()

            return root

        } else {

            btnLogin = root2.findViewById(R.id.btn_iniciarSesion)
            recordarme = root2.findViewById(R.id.check_recordarme)
            btnLogin.setOnClickListener {
                iniciarSesion(requireView())
            }

            txtForgotPass = binding2.txtRestablecerContrasena
            txtForgotPass.setOnClickListener {
                gotoForgotPassword()
            }

            txtSignUpUsuario = binding2.txtRegistroUsuario
            txtSignUpUsuario.setOnClickListener {
                gotoRegistroUsuario()
            }

            txtSignUpAcademia = binding2.txtRegistroAcademia
            txtSignUpAcademia.setOnClickListener {
                gotoRegistroAcademia()
            }

            btnLoginGoogle = binding2.btnContinuarConGoogle
            btnLoginGoogle.setOnClickListener {
                IniciarSesionGoogle(requireView())
            }
            editUsername = binding2.campoUsername
            editPassword = binding2.campoContrasena

            return root2
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
     * Desde el Perfil, manejar los cambios en la vista
     * del desplegable que muestra campos adicionales
     */
    fun manejarDesplegable() {
        binding.contenedorAbrirDesplegable.setOnClickListener {
            if (binding.containerCamposExtra.visibility == View.VISIBLE) {
                binding.containerCamposExtra.visibility = View.GONE
                binding.arrowIcon.setImageResource(R.drawable.baseline_keyboard_arrow_down_24) // Cambiar a la flecha hacia abajo
            } else {
                binding.containerCamposExtra.visibility = View.VISIBLE
                binding.arrowIcon.setImageResource(R.drawable.baseline_keyboard_arrow_up_24) // Cambiar a la flecha hacia arriba
                rellenarCamposExtra()
            }
        }
    }

    /**
     * Adjuntar al perfil aquellos campos no comunes
     * entre usuario y academia según corresponda
     */
    fun rellenarCamposExtra() {
        lifecycleScope.launch(Dispatchers.IO) {
            val usuariodao = UsuarioDAO()
            val usernameLogueado = (activity as MainActivity).username
            val usuario = usuariodao.consultarUsuario(requireContext(), usernameLogueado)
            withContext(Dispatchers.Main) {
                if (usuario.username.isNullOrEmpty()) {
                    binding.containerApellidosUsuario.visibility = View.GONE
                    binding.containerEdadUsuario.visibility = View.GONE
                    binding.containerDireccAcademia.visibility = View.VISIBLE
                } else {
                    binding.containerApellidosUsuario.visibility = View.VISIBLE
                    binding.containerEdadUsuario.visibility = View.VISIBLE
                    binding.containerDireccAcademia.visibility = View.GONE
                }
                binding.containerTelefono.visibility = View.VISIBLE
                binding.containerNombreUsuario.visibility = View.VISIBLE
            }
        }
    }

    /**
     * Rellenar los datos del perfil según el usuario
     * o academia que tenga la sesión iniciada
     */
    fun rellenarDatosPerfil() {
        lifecycleScope.launch(Dispatchers.IO) {
            val usuariodao = UsuarioDAO()
            val academiadao = AcademiaDAO()
            val usernameLogueado = (activity as MainActivity).username

            if (!usernameLogueado.isNullOrEmpty()) {
                val usuario = usuariodao.consultarUsuario(requireContext(), usernameLogueado)
                val academia = academiadao.consultarAcademia(usernameLogueado)
                if(!usuario.username.isNullOrEmpty()){

                    withContext(Dispatchers.Main) {
                        // Actualiza la interfaz de usuario con los datos obtenidos de la base de datos
                        binding.campoNomUser.setText(usuario.username)
                        campoEmail.text = usuario.email
                        campoContrasena.text = usuario.contrasena
                        campoTelefono.text = usuario.telefono.toString()
                        campoApellidos.text = usuario.apellidos
                        campoNombre.text = usuario.nombre
                        campoEdad.text = usuario.edad.toString()
                        if (usuario.img_perfil != "") {
                            val ftpInterface = InterfaceFTP()

                            val bitmapImage =
                                ftpInterface.downloadFileFromFTP(usuario.username + ".jpg")
                            ftpInterface.colocarImage(bitmapImage, binding.imageView)
                        }
                        // Después de cargar todos los datos oculta el ProgressBar y muestra los datos
                        progressBar.visibility = View.GONE
                        scrollView.visibility = View.VISIBLE
                    }
                }else{

                    withContext(Dispatchers.Main) {
                        // Actualiza la interfaz de academia con los datos obtenidos de la base de datos
                        binding.campoNomUser.setText(academia.username)
                        campoEmail.text = academia.email
                        campoContrasena.text = academia.contrasena
                        campoTelefono.text = academia.telefono.toString()
                        campoDireccion.text = academia.direccion
                        campoNombre.text = academia.nombre

                        if (academia.imgPerfil != "") {
                            val ftpInterface = InterfaceFTP()
                            val bitmapImage =
                                ftpInterface.downloadFileFromFTP(academia.imgPerfil)
                            ftpInterface.colocarImage(bitmapImage, binding.imageView)
                        }

                        // Después de cargar todos los datos oculta el ProgressBar y muestra los datos
                        progressBar.visibility = View.GONE
                        scrollView.visibility = View.VISIBLE
                        btnCrearCurso.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    /**
     * Al hacer click en la foto de perfil,
     * mostrará la opción de cambiarla
     */
    fun abrirOpsFotoPerfil(){
        showImageSelectionDialog()
    }

    /**
     * Crear un diálogo que de la opción de abrir la galería del dispositivo
     */
    private fun showImageSelectionDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.op_cambiar_foto_perfil, null)
        val selectImageButton: Button = dialogView.findViewById(R.id.btn_select_image)

        val builder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            //.setTitle("Seleccionar imagen")

        val alertDialog = builder.create()

        selectImageButton.setOnClickListener {
            // Acción al hacer clic en el botón de selección de imagen
            selectImageFromGallery()
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    /**
     * Seleccionar una imagen de la galería
     */
    private fun selectImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE)

        //(activity as MainActivity).onActivityResult(requestCode, resultCode, data)

    }
    companion object {
         const val REQUEST_CODE_SELECT_IMAGE = 1001
    }

    /**
     * Desde el Login, ir a he olvidado mi contraseña
     */
    fun gotoForgotPassword() {
        navController.navigate(R.id.navigation_forgotPassword)
    }

    /**
     * Desde el Login, ir al registro de usuario
     */
    fun gotoRegistroUsuario() {
        navController.navigate(R.id.navigation_registroUsuario)
    }

    /**
     * Desde el Login, ir al registro de academia
     */
    fun gotoRegistroAcademia() {
        navController.navigate(R.id.navigation_registroAcademia)
    }

    /**
     * Desde el perfil, ir a crear un curso si eres una academia
     */
    fun goToCrearcurso() {
        navController.navigate(R.id.navigation_crearCurso)
    }

    /**
     * Intentar iniciar con Google, mostrar mensaje de error si falla
     */
    fun IniciarSesionGoogle(view: View) {
        try {
            signInWithGoogle()
        } catch (e: Exception) {
            Toast.makeText(
                activity,
                "Error durante el inicio de sesión con Google",
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    /**
     * Configuración de inicio de sesión con Google
     */
    private fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("61763895953-0btairr49dcgc12tpvke69uatr7u8oet.apps.googleusercontent.com")
            .requestEmail().build()

        val googleSignInClient = GoogleSignIn.getClient(activity as MainActivity, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    /**
     *  Verificar si el código de solicitud coincide
     *  con el código de inicio de sesión con Google
     *
     *  @param requestCode
     *  @param resultCode
     *  @param data
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleGoogleSignInResult(task)
        }
    }

    /**
     * Autenticar con Firebase usando la cuenta de Google
     *
     * @param task
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun handleGoogleSignInResult(task: Task<GoogleSignInAccount>) {
        try {
            // Obtener la cuenta de Google desde el resultado de la tarea
            val account = task.getResult(ApiException::class.java)
            // Autenticar con Firebase usando la cuenta de Google
            firebaseAuthWithGoogle(account)
        } catch (e: ApiException) {
            // Manejar el fallo en el inicio de sesión con Google
            Log.w(ContentValues.TAG, "Fallo en el inicio de sesión con Google", e)
            Toast.makeText(
                activity,
                "Fallo en el inicio de sesión con Google",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /***
     * Autenticar el inicio de sesión con Google
     *
     * @param account - cuenta con la que se ha iniciado
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {

        // Obtener credenciales de autenticación de Google
        val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
        // Autenticar con Firebase usando las credenciales de Google
        FirebaseDAO.getInstanceFirebase().signInWithCredential(credential)
            .addOnCompleteListener(activity as MainActivity) { task ->
                if (task.isSuccessful) {
                    // Obtener la cuenta de usuario de Firebase
                    val intent = Intent(activity as MainActivity, MainActivity::class.java)
                    intent.putExtra("username", account?.email)
                    startActivity(intent)
                }
                else {
                    val intent = Intent(activity as MainActivity, MainActivity::class.java)
                    intent.putExtra("username", account?.email)
                    startActivity(intent)
                }
            }
    }

    /***
     * Comprobar si se ha iniciado sesión,
     * ya sea como usuario o academia
     *
     * @param view - vista actual
     *
     * @return devuelve true si está iniciado y false si no lo está
     */
    fun iniciarSesion(view: View) : Boolean {
        //al hacer click en iniciar, regresar a buscar
        var usuario: Usuario? = null
        var academia: Academia? = null
        var resultado = false
        if (editUsername.text.isNullOrEmpty() || editPassword.text.isNullOrEmpty()) {
            Toast.makeText(
                context,
                "Por favor, rellene los campos de usuario y contraseña",
                Toast.LENGTH_LONG
            ).show()

            resultado = false
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
                            Toast.makeText(
                                context,
                                "Usuario o contraseña incorrectas",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            Log.i("Ha llegado", "Ha llegado")
                            if (recordarme.isChecked) {
                                withContext(Dispatchers.IO) {
                                    val userDao =
                                        LocalDatabase.getInstance(requireContext()).userDao()

                                    if (userDao.getAll().isEmpty()) {
                                        val newUser =
                                            com.example.appacademia.dao.local.entity.Usuario(
                                                editUsername.text.toString(),
                                                editPassword.text.toString(),
                                                null,
                                                null
                                            )
                                        userDao.insertAll(newUser)
                                    }
                                }
                            }
                            navController.navigate(R.id.navigation_buscar)
                            (activity as MainActivity).loggedIn = true
                            (activity as MainActivity).username = editUsername.text.toString()
                            resultado = true
                        }
                    }
                }

            }
        }
        return resultado
    }
}