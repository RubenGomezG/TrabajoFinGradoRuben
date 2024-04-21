package com.example.appacademia

import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.appacademia.dao.servidorSQL.AcademiaDAO
import com.example.appacademia.model.Academia
import com.example.appacademia.model.Curso
import com.example.appacademia.ui.activities.MainActivity
import com.example.appacademia.ui.fragments.infoCurso.InfoCursoFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class fragment_mapa : Fragment() , OnMapReadyCallback {

    private val REQUEST_LOCATION_PERMISSION = 123
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var academiaActual: String = ""

    /**
     * Crear el fragment
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /***
     * Al crear la vista, infla el XML
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

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mapa, container, false)
    }

    /***
     * Una vez creada la vista, enlazar todos sus elementos con las
     * variables del fragment para poder manipularlas
     *
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Ya se tienen permisos, puedes acceder a la ubicación.
            // Inicia tu lógica relacionada con la ubicación aquí.
            createMapFragment()
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        } else {
            // No se tienen permisos, solicita permisos.
            requestPermissions(
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }
    }

    /***
     * Solicita permisos de ubicación y comprueba la respuesta del usuario
     *
     * @param requestCode - codigo de solicitud
     * @param permissions - conjunto de permisos
     * @param grantResults - respuesta del usuario
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, puedes acceder a la ubicación.
                // Inicia tu lógica relacionada con la ubicación aquí.
                createMapFragment()
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
            } else {
                // Permiso denegado, toma medidas en consecuencia (puede mostrar un mensaje al usuario).
            }
        }
    }

    /***
     * Crea el mapa dentro del fragment
     */
    private fun createMapFragment() {
        val mapContainer = view?.findViewById<FrameLayout>(R.id.mapContainer)
        val mapFragment = SupportMapFragment()

        // Reemplazar el contenido de mapContainer con el fragmento del mapa
        childFragmentManager.beginTransaction()
            .replace(R.id.mapContainer, mapFragment)
            .commit()

        mapFragment.getMapAsync(this)
    }

    /***
     * Cargar el mapa y colocar las academias donde correspondan
     *
     * @param googleMap - el mapa de base de google maps
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Obtener la ubicación del usuario y animar la cámara
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Obtener la ubicación de todas las academias y agregar marcadores

            val academias: Set<Academia> = obtenerListaDeAcademiasConUbicacion()

            for (academia in academias) {
                val ubicacionAcademia = LatLng(academia.latitud, academia.longitud)
                val markerOptions = MarkerOptions()
                    .position(ubicacionAcademia)
                    .title(academia.nombre)
                    .snippet(academia.username)
                val marker = map.addMarker(markerOptions)
            }

            // Configurar un listener para el clic en el marcador
            map.setOnMarkerClickListener { clickedMarker ->
                // Obtener la academia asociada al marcador
                val academia = academias.find { it.nombre == clickedMarker.title }
                    academiaActual = clickedMarker.snippet!!

                val infoAcademiaFragment = InfoAcademiaFragment()
                infoAcademiaFragment.arguments = Bundle().apply {
                    putString("academiaID", academiaActual)
                }

                // Realizar la transacción de fragmentos al hacer clic en el marcador
                academia?.let {
                    val navController = (requireActivity() as MainActivity).getNavController()
                    // Navegar al fragmento deseado (InfoCursoFragment en este caso)
                    navController.navigate(R.id.navigation_infoAcademias, infoAcademiaFragment.arguments)
                }

                // Devolver true para consumir el clic en el marcador
                true
            }
            animateCameraToCurrentLocation()
        }
//        // Animar la cámara solo la primera vez
//        if (isFirstZoom) {
//            animateCameraToCurrentLocation()
//            isFirstZoom = false
//            Log.i("EL ZOOOOOOOOOOMM", isFirstZoom.toString())
//        }

    }

    /**
     * Obtiene la lista de academias junto a su ubicación
     *
     * @return devuelve un conjunto de academias
     */
    private fun obtenerListaDeAcademiasConUbicacion(): Set<Academia> {
        val academiaDAO = AcademiaDAO()
        var academia: Set<Academia> = HashSet()

        runBlocking {
            withContext(Dispatchers.IO) {
                academia = academiaDAO.obtenerListaAcademiasConUbicacion()
            }
        }
        return academia
    }

    /**
     * Cuando la ubicación del dispositivo está activada,
     * el mapa hará zoom automáticamente para enfocar al
     * área donde se encuentra el usuario
     */
    private fun animateCameraToCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            map.isMyLocationEnabled = true
            map.uiSettings.isMyLocationButtonEnabled = true

            // Obtener la ubicación del usuario y animar la cámara solo la primera vez
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    location?.let {
                        val currentLatLng = LatLng(it.latitude, it.longitude)

                        // Zoom solo la primera vez que se obtiene la ubicación
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                    }
                }
        }
    }
}