package com.example.appacademia.ui.activities

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView

import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.appacademia.R
import com.example.appacademia.RecyclerCursosAcademia
import com.example.appacademia.dao.firebase.FirebaseDAO
import com.example.appacademia.dao.local.database.LocalDatabase
import com.example.appacademia.dao.servidorSQL.FavoritoDAO
import com.example.appacademia.dao.servidorSQL.InscripcionesDAO
import com.example.appacademia.dao.servidorSQL.UsuarioDAO
import com.example.appacademia.databinding.ActivityMainBinding
import com.example.appacademia.ftp.InterfaceFTP
import com.example.appacademia.model.Curso
import com.example.appacademia.model.Favorito
import com.example.appacademia.model.Inscripcion
import com.example.appacademia.ui.fragments.OpsToolbarFragment
import com.example.appacademia.ui.fragments.buscar.RecyclerBuscar
import com.example.appacademia.ui.fragments.cursos.RecyclerCursos
import com.example.appacademia.ui.fragments.favoritos.RecyclerFavoritos
import com.example.appacademia.ui.fragments.perfil.PerfilFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(), RecyclerBuscar.OnItemClickListener,
    RecyclerBuscar.OnItemCheckedChangeListener, RecyclerCursosAcademia.OnItemClickListener,
    RecyclerCursosAcademia.OnItemCheckedChangeListener, RecyclerCursos.OnItemClickListener,
    RecyclerCursos.OnItemCheckedChangeListener, RecyclerFavoritos.OnItemClickListener,
    RecyclerFavoritos.OnItemCheckedChangeListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var btnBack: ImageButton
    var loggedIn: Boolean = false
    var username: String = ""
    var cursoActual: Int = 0
    private var auth: FirebaseAuth = FirebaseDAO.getInstanceFirebase()

    /***
     * Una vez creada la actividad, enlazar todos sus elementos con las
     * variables del activity para poder manipularlas
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeLocalDatabase()

        //NAVEGACIÓN BOTONES INFERIORES:
        val navView: BottomNavigationView = binding.contenedorInferior
        navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_perfil -> {
                    // Manejar el clic en el botón de perfil de la barra de abajo
                    manejarClicPerfil()
                    Log.i("patata", "perfil ")
                    true
                }

                R.id.navigation_buscar -> {
                    // Manejar el clic en el botón de perfil
                    gotoBuscar()
                    true
                }

                R.id.navigation_cursos -> {
                    // Manejar el clic en el botón de perfil
                    gotoCursos()
                    Log.i("patata", "cursos ")
                    true
                }

                R.id.navigation_favoritos -> {
                    // Manejar el clic en el botón de perfil
                    gotoFavoritos()
                    true
                }

                else -> false
            }
        }
        navView.setupWithNavController(navController)

        //Eliminar toolbar por defecto:
        supportActionBar?.hide()

        //Comprobar si hace falta mostrar opcion iniciar sesion o ya está iniciada
        handleIntentData()

        //inicializar el botón de back
        btnBack = binding.btnBack
        btnBack.setOnClickListener {
            goBack()
        }
    }

    /**
     * Comprueba si la sesión está iniciada
     */
    private fun handleIntentData() {
        val extras = intent.extras
        if (extras != null) {
            val dato = extras.getString("username")
            if (dato != null) {
                username = dato
            }
            val dato2 = extras.getBoolean("isLogged")
            if (dato2) {
                loggedIn = true
            } else {
                goToRegistro()
            }

        }
    }

    /**
     * Registra la sesión activa en la base de datos local
     */
    private fun initializeLocalDatabase() {
        val userDao = LocalDatabase.getInstance(this).userDao()

        lifecycleScope.launch(Dispatchers.IO) {
            val users = userDao.getAll()
            if (userDao.getAll().isNotEmpty()) {
                for (user in users) {
                    loggedIn = true
                    username = user.NombreUsuario
                }
            }
        }
    }

    /**
     * Gestiona la navegación al perfil
     */
    private fun manejarClicPerfil() {
        gotoPerfil()
    }

    /**
     * Devuelve la navegación de la actividad
     */
    fun getNavController(): NavController {
        return navController
    }

    /**
     * Añadir un fragment superpuesto en la pantalla
     *
     * @param fragment - fragment a añadir
     */
    private fun addOverlayFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .add(R.id.container, fragment)
            .commit()
    }

    /**
     * Eliminar fragmento del host
     *
     * @param fragment - fragmento a eliminar
     */
    private fun removeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .remove(fragment)
            .commit()
    }

    /**
     * Ir a perfil
     */
    private fun gotoPerfil() {
        navController.navigate(R.id.navigation_perfil)
    }

    /**
     * Ir a buscar
     */
    private fun gotoBuscar() {
        navController.navigate(R.id.navigation_buscar)
    }

    /**
     * Ir a cursos
     */
    private fun gotoCursos() {
        navController.navigate(R.id.navigation_cursos)
    }

    /**
     * Ir a favoritos
     */
    private fun gotoFavoritos() {
        navController.navigate(R.id.navigation_favoritos)
    }

    /**
     * Ir a la información ampliada de un curso
     *
     * @param curso - curso seleccionado
     */
    private fun gotoInfoCurso(curso: Curso) {
        val bundle = Bundle().apply {
            putInt("cursoId", curso.codCurso)
        }
        navController.navigate(R.id.navigation_infoCursos)
    }

    /**
     * Ir al registro de usuario
     */
    fun goToRegistro() {
        navController.navigate(R.id.navigation_registroUsuario)
    }

    /**
     * Retroceder en la navegación
     */
    fun goBack() {
        onBackPressed()
    }

    /**
     * Abrir la información ampliada de un curso al hacer click en él
     *
     * @param curso - curso seleccionado
     */
    override fun onItemClick(curso: Curso) {
        cursoActual = curso.codCurso
        gotoInfoCurso(curso)
    }

    /**
     * Inscribir al usuario en un curso en la ventana de Favoritos
     *
     * @param curso - curso seleccionado
     */
    override fun onButtonClick2(curso: Curso) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            lifecycleScope.launch(Dispatchers.IO) {
                val builder =
                    NotificationCompat.Builder(this@MainActivity, "com.example.notificacion")
                        .setSmallIcon(R.drawable.baseline_school_24)
                        .setContentTitle("Inscripción de curso")
                        .setContentText("Enhorabuena! te has inscrito al curso correctamente.")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setAutoCancel(true)

                val notificationManager: NotificationManager =
                    this@MainActivity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                lifecycleScope.launch(Dispatchers.IO) {
                    val inscripcionesDAO = InscripcionesDAO()
                    val inscripcion = Inscripcion(username, curso.codCurso)
                    val resultado = inscripcionesDAO.consultarInscripcion(username, curso.codCurso)
                    Log.i("abuela", resultado.toString())
                    if (resultado.username == "" && resultado.codCurso == 0) {
                        inscripcionesDAO.inscribir(inscripcion)
                    }

                    with(NotificationManagerCompat.from(this@MainActivity)) {
                        if (ActivityCompat.checkSelfPermission(
                                this@MainActivity,
                                android.Manifest.permission.POST_NOTIFICATIONS
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            // Solicita permisos si aún no se han otorgado
                            ActivityCompat.requestPermissions(
                                this@MainActivity,
                                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                                123
                            )
                        }
                        notificationManager.notify(1, builder.build())
                    }
                }
            }
        }
    }

    /**
     * Inscribir al usuario en un curso en la ventana de Buscar
     *
     * @param curso - curso seleccionado
     */
    override fun onButtonClick(curso: Curso) {
        if (loggedIn) {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val builder =
                        NotificationCompat.Builder(this@MainActivity, "com.example.notificacion")
                            .setSmallIcon(R.drawable.baseline_school_24)
                            .setContentTitle("Inscripción de curso")
                            .setContentText("Enhorabuena! te has inscrito al curso correctamente.")
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setAutoCancel(true)

                    val notificationManager: NotificationManager =
                        this@MainActivity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    lifecycleScope.launch(Dispatchers.IO) {
                        val inscripcionesDAO = InscripcionesDAO()
                        val inscripcion = Inscripcion(username, curso.codCurso)
                        val resultado =
                            inscripcionesDAO.consultarInscripcion(username, curso.codCurso)
                        Log.i("abuela", resultado.toString())
                        if (resultado.username == "" && resultado.codCurso == 0) {
                            inscripcionesDAO.inscribir(inscripcion)
                        }

                        with(NotificationManagerCompat.from(this@MainActivity)) {
                            if (ActivityCompat.checkSelfPermission(
                                    this@MainActivity,
                                    android.Manifest.permission.POST_NOTIFICATIONS
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                // Solicita permisos si aún no se han otorgado
                                ActivityCompat.requestPermissions(
                                    this@MainActivity,
                                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                                    123
                                )
                            }
                            notificationManager.notify(1, builder.build())
                        }
                    }
                }
            }
        }
    }

    /**
     * Efectuar cambios del botón de abandonar curso en la base de datos
     */
    override fun onClickCancelarSub(curso: Curso) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            lifecycleScope.launch(Dispatchers.IO) {
                val inscripcionesDAO = InscripcionesDAO()
                val inscripcion = Inscripcion(username, curso.codCurso)
                inscripcionesDAO.borrarInscripcion(inscripcion)
            }
        }
    }

    /**
     * Marcar o desmarcar un curso como favorito y hacer las
     * operaciones correspondientes en la base de datos
     *
     * @param curso - curso seleccionado
     * @param isChecked - saber si está desmarcado o no
     */
    override fun onItemCheckedChanged(curso: Curso, isChecked: Boolean) {
        val favoritoDAO = FavoritoDAO()
        val codCurso = curso.codCurso
        val favorito = Favorito(username, codCurso)
        if (isChecked) {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val favorito2 =
                        favoritoDAO.consultarFavorito(favorito.username, favorito.codCurso)
                    if (favorito.username != "" && favorito2.username != favorito.username && favorito2.codCurso != codCurso) {
                        favoritoDAO.anadirFavorito(favorito)
                    }
                }
            }
        } else {
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val favorito2 = favoritoDAO.consultarFavorito(username, codCurso)
                    if (favorito.username != "" && favorito2.username == favorito.username && favorito2.codCurso == codCurso)
                        favoritoDAO.borrarFavorito(favorito)
                    Log.i("TAG", username)
                }
            }
        }
    }

    /**
     * Cerrar la sesión activa
     *
     * @param view - vista actual
     */
    fun cerrarSesion(view: View) {
        if (loggedIn) {
            Log.i("huevos", auth.currentUser.toString())
            if (auth.currentUser != null) {
                auth.currentUser!!.delete()
            }

            lifecycleScope.launch(Dispatchers.IO) {
                val userDao = LocalDatabase.getInstance(this@MainActivity).userDao()
                userDao.deleteAllUsers()
            }

            loggedIn = false
            username = ""
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Cambiar la foto del perfil del usuario o
     * academia por una elegida en la galería
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Si se ha seleccionado una imagen el resultcode es -1 (RESULT_OK = -1), si no se selecciona nada resultcode = 0 y no entra en el if
        if (resultCode == AppCompatActivity.RESULT_OK) {
            // Procesar la imagen seleccionada desde la galería
            val selectedImageUri = data?.data

            //Usamos un metodo de la interfaz para colocar la nueva imagen
            val interfaz = InterfaceFTP()
            //encontramos el imageview donde vamos a poner la nueva imagen
            val fotoPerfil: ImageView = findViewById(R.id.imageView)

            //Busca la imagen desde el URI utilizando el ContentResolver y decodifica el InputStream en un objeto Bitmap
            val resolver = contentResolver
            val inputStream = selectedImageUri?.let { resolver.openInputStream(it) }
            val imagenSelected: Bitmap = BitmapFactory.decodeStream(inputStream)

            //Asignar la nueva imagen como foto de perfil
            interfaz.colocarImage(imagenSelected, fotoPerfil)

            //hacemos otro hilo para subir la imagen al servidor
            GlobalScope.launch(Dispatchers.IO) {
                interfaz.uploadFileToFTP(imagenSelected, username + ".jpg")
                Log.i("imag", "onActivityResult: IF - MAINACTIVITY _ tras uploadFile")
            }

        }
    }
}