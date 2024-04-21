package com.example.appacademia.ui.fragments.crearCurso

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.appacademia.R
import com.example.appacademia.dao.servidorSQL.AcademiaDAO
import com.example.appacademia.dao.servidorSQL.CursoDAO
import com.example.appacademia.databinding.FragmentCrearCursoBinding
import com.example.appacademia.model.Curso
import com.example.appacademia.ui.activities.MainActivity
import com.google.type.DateTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class CrearCursoFragment : Fragment() {
    private var _binding: FragmentCrearCursoBinding? = null
    private lateinit var navController: NavController

    private lateinit var btnCrear: Button

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    /***
     * Al crear la vista, infla el XML y asocia los
     * elementos a las variables del fragment
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
        val crearCursoViewModel = ViewModelProvider(this).get(CrearCursoViewModel::class.java)

        navController = (activity as MainActivity).getNavController()

        _binding = FragmentCrearCursoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        btnCrear = binding.btnCrearCurso
        btnCrear.setOnClickListener {
            crearCurso(root)
        }

        // Encontrar el Spinner en la vista
        val campoTipoSpinner: Spinner = binding.campoTipo

        // Configurar un adaptador para el Spinner
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.opciones_array,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Asignar el adaptador al Spinner
        campoTipoSpinner.adapter = adapter

        return root
    }

    /***
     * Al destruir la vista, borramos el contenido del binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Crear un curso nuevo en la base de datos
     * según los datos introducidos
     *
     * @param view - vista actual
     */
    fun crearCurso(view: View){
        try {
            val nombre = view.findViewById<EditText>(R.id.campoNombre)?.text.toString()
            val fechaInicio = view.findViewById<EditText>(R.id.campoFechaInicio)?.text.toString()
            val fechaFin = view.findViewById<EditText>(R.id.campoFechaFin)?.text.toString()
            val precio = view.findViewById<EditText>(R.id.campoPrecio)?.text.toString().toDouble()
            val tipo = view.findViewById<Spinner>(R.id.campoTipo)?.selectedItem.toString()
            val desc = view.findViewById<EditText>(R.id.campoDescripcion)?.text.toString()
            var hayErrores = false
            var fechaInicioDate : Date? = null
            var fechaFinDate : Date? = null

            val formato = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            formato.isLenient = false

            try {
                fechaInicioDate = formato.parse(fechaInicio)
                fechaFinDate = formato.parse(fechaFin)

                if (fechaInicioDate != null && fechaFinDate != null) {
                    if (fechaFinDate.before(fechaInicioDate)) {
                        hayErrores = true
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                hayErrores = true
            }

            if (hayErrores) {
                Toast.makeText(
                    requireContext(),
                    "Por favor, revisa los campos marcados",
                    Toast.LENGTH_SHORT
                ).show()
            }else {
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        try {
                            var academiaDao = AcademiaDAO()
                            var academia = academiaDao.consultarAcademia((activity as MainActivity).username)
                            val curso =
                                Curso(0, nombre, fechaInicioDate, fechaFinDate, precio, 0.0, desc, tipo, academia.codAcademia)
                            val cursoDAO = CursoDAO()

                            cursoDAO.anadirCurso(curso)
                            withContext(Dispatchers.Main) {
                                gotoPerfil()
                            }
                        } catch (e: Exception) {
                            Log.i("exceptiónc",e.message.toString())

                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    requireContext(),
                                    "Error al validar y agregar curso",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }}
            }

        } catch (ex: Exception) {
            Log.e("exception", ex.toString() )
            Toast.makeText(
                requireContext(),
                "Error al validar y agregar curso",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /***
     * Ir al perfil
     */
    fun gotoPerfil() {
        navController.navigate(R.id.navigation_perfil)
    }
}