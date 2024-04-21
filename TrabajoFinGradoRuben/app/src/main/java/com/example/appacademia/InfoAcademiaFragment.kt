package com.example.appacademia

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appacademia.dao.servidorSQL.AcademiaDAO
import com.example.appacademia.dao.servidorSQL.CursoDAO
import com.example.appacademia.ftp.InterfaceFTP
import com.example.appacademia.model.Academia
import com.example.appacademia.model.Curso
import com.example.appacademia.ui.activities.MainActivity
import com.example.appacademia.ui.fragments.buscar.RecyclerBuscar
import com.example.appacademia.ui.fragments.cursos.CursosViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class InfoAcademiaFragment : Fragment(), OnMapReadyCallback {
    private var mapView: MapView? = null
    private var googleMap: GoogleMap? = null
    private var academiadao: AcademiaDAO = AcademiaDAO()
    private var academia: Academia = Academia()

    private lateinit var nomAcademia: TextView
    private lateinit var imgAcademia: ImageView
    private lateinit var telefAcademia: TextView
    private lateinit var direcAcademia: TextView
    private lateinit var emailAcademia: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var scrollView: ScrollView
    private lateinit var recyclerView: RecyclerView

    /***
     * Crear la vista e infla el XML
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

        val view = inflater.inflate(R.layout.fragment_info_academia, container, false)
        val fragmentMapa =
            childFragmentManager.findFragmentById(R.id.mapContainer) as? fragment_mapa
        val academiaID = arguments?.getString("academiaID")

        progressBar = view.findViewById(R.id.progressBar)
        scrollView = view.findViewById(R.id.scrollView)

        // Muestra el ProgressBar
        progressBar.visibility = View.VISIBLE

        // Oculta el contenido real
        scrollView.visibility = View.GONE
        // Inflate the layout for this fragment

        mapView = view.findViewById(R.id.info_academia_mapView)
        mapView?.onCreate(savedInstanceState)
        mapView?.onResume()
        nomAcademia = view.findViewById(R.id.info_academia_titulo)
        imgAcademia = view.findViewById(R.id.info_academia_imagen)
        telefAcademia = view.findViewById(R.id.info_academia_telefono)
        direcAcademia = view.findViewById(R.id.info_academia_direccion)
        emailAcademia = view.findViewById(R.id.lbl_email_academia)

        //val academiaID = fragmentMapa!!.academiaActual
        lifecycleScope.launch(Dispatchers.IO) {
            academiadao = AcademiaDAO()

            academia = academiadao.consultarAcademia(academiaID)
            withContext(Dispatchers.Main) {


                //Log.i("patatas", "fritas con ketchup")
                nomAcademia.text = academia.nombre
                //imgCurso.setImageURI(cursoActual.)
                telefAcademia.text = "Teléfono: " + academia.telefono.toString()
                emailAcademia.text = academia.email
                direcAcademia.text = academia.direccion
                val interfaceFTP = InterfaceFTP()
                val bitmap = interfaceFTP.downloadFileFromFTP(academia.imgPerfil)
                // Haz algo con el bitmap, por ejemplo, mostrarlo en la imagen de la academia
                if (bitmap != null) {
                    interfaceFTP.colocarImage(bitmap, imgAcademia)
                }
                rellenarRecyclerView()

                //ubicaCurso.
                // Después de cargar todos los datos
                // Oculta el ProgressBar
                progressBar.visibility = View.GONE

                // Muestra el contenido real
                scrollView.visibility = View.VISIBLE
            }

        }

        val cursosViewModel =
            ViewModelProvider(this).get(CursosViewModel::class.java)

        mapView?.getMapAsync(this)

        return view
    }

    /***
     * Al crear la vista, enlazar todos sus elementos con las
     * variables del fragment para poder manipularlas
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     *
     * @return devuelve la vista proyectada en el fragment
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.listaCardsCursos2)

        rellenarRecyclerView()
    }

    /***
     * Muestra la ubicación específica de la academia
     *
     * @param map - Mapa de base de Google
     */
    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Obtén la ubicación de la academia y añade un marcador en la ubicación deseada
        lifecycleScope.launch(Dispatchers.IO) {
            val academiadao = AcademiaDAO()
            val academiaID = arguments?.getString("academiaID")
            var academia = academiadao.consultarAcademia(academiaID)
            val ubicacionAcademia = LatLng(academia.latitud, academia.longitud)
            withContext(Dispatchers.Main) {
                googleMap?.addMarker(
                    MarkerOptions().position(ubicacionAcademia).title(academia.nombre)
                )
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacionAcademia, 15f))

            }
        }
    }

    /***
     * Rellenar el recyclcer de los cursos que ofrece una academia
     */
    fun rellenarRecyclerView() {
        lifecycleScope.launch(Dispatchers.IO) {
            val academiaDAO = AcademiaDAO()
            var academiaDef = academiaDAO.consultarAcademiaconNombre(requireContext(),nomAcademia.text.toString())

            val cursosDAO = CursoDAO()

            val listaDeCursos = cursosDAO.obtenerTodosLosCursosDeAcademia(academiaDef.codAcademia)

            withContext(Dispatchers.Main) {
                val adaptador = RecyclerCursosAcademia(
                    listaDeCursos,
                    requireActivity() as RecyclerCursosAcademia.OnItemClickListener,
                    requireActivity() as RecyclerCursosAcademia.OnItemCheckedChangeListener,
                    (activity as MainActivity).username
                )

                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = adaptador

                // Oculta el ProgressBar
                progressBar.visibility = View.GONE

                // Muestra el contenido real
                scrollView.visibility = View.VISIBLE
            }
        }
    }

}