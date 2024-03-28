package com.example.appacademia.ui.fragments.infoCurso

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.lifecycleScope
import com.example.appacademia.R
import com.example.appacademia.dao.servidorSQL.AcademiaDAO
import com.example.appacademia.dao.servidorSQL.CursoDAO
import com.example.appacademia.dao.servidorSQL.InscripcionesDAO
import com.example.appacademia.ftp.InterfaceFTP
import com.example.appacademia.model.Inscripcion
import com.example.appacademia.ui.activities.MainActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InfoCursoFragment : Fragment(), OnMapReadyCallback {
    private var mapView: MapView? = null
    private var googleMap: GoogleMap? = null

    private lateinit var nomCurso: TextView
    private lateinit var imgCurso: ImageView
    private lateinit var valoraCurso: RatingBar
    private lateinit var precioCurso: TextView
    private lateinit var descCurso: TextView
    private lateinit var fechaInicioCurso: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var scrollView: ScrollView

    /**
     * Crear el fragment
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /***
     * Crear la vista y enlazar todos sus elementos con las
     * variables del fragment para poder manipularlas.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     *
     * @return devuelve la vista proyectada en el fragment
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_info_curso, container, false)

        progressBar = view.findViewById(R.id.progressBar)
        scrollView = view.findViewById(R.id.scrollView)

        // Muestra el ProgressBar
        progressBar.visibility = View.VISIBLE

        // Oculta el contenido real
        scrollView.visibility = View.GONE
        // Inflate the layout for this fragment

        mapView = view.findViewById(R.id.info_curso_mapView)
        mapView?.onCreate(savedInstanceState)
        mapView?.onResume()
        nomCurso = view.findViewById(R.id.info_curso_titulo)
        imgCurso = view.findViewById(R.id.info_curso_imagen)
        valoraCurso = view.findViewById(R.id.info_curso_valor)
        precioCurso = view.findViewById(R.id.info_curso_precio)
        descCurso = view.findViewById(R.id.info_curso_descripcion)
        fechaInicioCurso = view.findViewById(R.id.info_curso_tipofecha)

        createNotificationChannel()

        val button = view.findViewById<Button>(R.id.info_curso_btndescripcion)
        button.setOnClickListener { inscribirse() }

        try {
            com.google.android.gms.maps.MapsInitializer.initialize(requireContext())
        } catch (e: Exception) {
            e.printStackTrace()
        }
            lifecycleScope.launch(Dispatchers.IO) {
                val cursoID = (activity as MainActivity).cursoActual

                val cursodao = CursoDAO()
                var curso = cursodao.consultarCurso(cursoID)

                val academiadao = AcademiaDAO()
                val academia = academiadao.consultarAcademiaConCodigo(curso.codAcademia)

                withContext(Dispatchers.Main) {

                    //obtener imagen de la academia y mostrarla en el curso
                    val interfaceFTP = InterfaceFTP()
                    val bitmap = interfaceFTP.downloadFileFromFTP(academia.imgPerfil)
                    Log.i("imag", "INFO CURSO: filename:" + academia.imgPerfil)
                    if (bitmap != null) {
                        interfaceFTP.colocarImage(bitmap, imgCurso)
                    }

                    //rellenar información sobre el curso
                    nomCurso.text = curso.nombre
                    valoraCurso.rating = curso.valoracion.toFloat()
                    fechaInicioCurso.text =  curso.fechaInicio.toString() + "\n" + curso.fechaFin
                    precioCurso.text = curso.precio.toString() + " €"
                    descCurso.text = curso.descripcion

                    // Después de cargar todos los datos oculta el progressbar y muestra la información
                    progressBar.visibility = View.GONE
                    scrollView.visibility = View.VISIBLE
                }

            }
        mapView?.getMapAsync(this)

        return view
    }

    /***
     * Cargar el mapa y colocar las academias donde correspondan
     *
     * @param googleMap - el mapa de base de google maps
     */
    override fun onMapReady(map: GoogleMap) {
        lifecycleScope.launch(Dispatchers.IO) {
            val cursoID = (activity as MainActivity).cursoActual
            val cursodao = CursoDAO()
            val academiadao = AcademiaDAO()

            var curso = cursodao.consultarCurso(cursoID)
            var academia = academiadao.consultarAcademiaConCodigo(curso.codAcademia)

            withContext(Dispatchers.Main) {
                googleMap = map

                // Añade un marcador en la ubicación deseada
                val ubicacionDeseada = LatLng(academia.latitud, academia.longitud)
                googleMap?.addMarker(MarkerOptions().position(ubicacionDeseada).title(academia.nombre))
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionDeseada, 15f))
            }
        }

    }

    /**
     * Al inscribirse en un curso, se envía una notificación push al usuario
     */
    private fun inscribirse() {
        val builder = NotificationCompat.Builder(requireContext(), "com.example.notificacion")
            .setSmallIcon(R.drawable.baseline_school_24)
            .setContentTitle("Inscripción de curso")
            .setContentText("Enhorabuena! te has inscrito al curso correctamente.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notificationManager: NotificationManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val nombreUsuario = (activity as MainActivity).username

        // Aquí obtén el código del curso actual (puedes obtenerlo de donde sea necesario)
        val codigoCurso = (activity as MainActivity).cursoActual

        // Inserta la inscripción en la base de datos
        lifecycleScope.launch(Dispatchers.IO) {
            val inscripcionesDAO = InscripcionesDAO()
            val inscripcion = Inscripcion(nombreUsuario,codigoCurso)
            val resultado = inscripcionesDAO.consultarInscripcion(nombreUsuario, codigoCurso)
            Log.i("abuela", resultado.toString())
            if (resultado.username == "" && resultado.codCurso == 0) {
                inscripcionesDAO.inscribir(inscripcion)
            }
        }

        with(NotificationManagerCompat.from(requireContext())) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Solicita permisos si aún no se han otorgado
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    123
                )
            }
            notificationManager.notify(1, builder.build())
        }
    }

    /**
     * Crear canal de notificación
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Canalcillo2"
            val descriptionText = "descripcion del canalcillo"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(
                "com.example.notificacion",
                name,
                importance
            ).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Este método se llama cuando la actividad está a
     * punto de comenzar a interactuar con el usuario.
     * En este punto, la actividad está en la parte superior
     * de la pila de actividades y está completamente visible
     */
    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    /**
     * Este método se llama cuando la actividad ya no está
     * en primer plano y no puede interactuar con el usuario.
     */
    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    /**
     * Este método se llama antes de que la actividad sea destruida
     */
    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    /**
     * Este método se llama cuando el sistema determina
     * que el nivel de memoria disponible es bajo
     */
    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }


}