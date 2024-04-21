package com.example.appacademia.ui.fragments.buscar

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appacademia.R
import com.example.appacademia.dao.servidorSQL.AcademiaDAO
import com.example.appacademia.dao.servidorSQL.CursoDAO
import com.example.appacademia.databinding.FragmentBuscarBinding
import com.example.appacademia.model.Curso
import com.example.appacademia.ui.activities.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class BuscarFragment : Fragment() {

    private var _binding: FragmentBuscarBinding? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnCargarMapa: Button
    private lateinit var btnBuscar: Button
    private lateinit var edbuscador: EditText
    private lateinit var listaDeCursos: List<Curso>
    private lateinit var spinnerFiltros : Spinner
    private lateinit var spinnerOrdenar : Spinner
    private lateinit var progressBar: ProgressBar
    private lateinit var contentLayout: LinearLayout
    private lateinit var campoFecha : TextView
    private val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    /**
     * Método que crea la vista del Fragment Buscar. Recoge y lanza el layout correspondiente,
     * y asigna el valor a cada objeto interno. También crea los listeners y los asigna para
     * que posteriormente podamos realizar las búsquedas, filtros y ordenar.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     *
     * @return View
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_buscar, container, false)
        val buscarViewModel = ViewModelProvider(this).get(BuscarViewModel::class.java)

        _binding = FragmentBuscarBinding.inflate(inflater, container, false)
        val root: View = binding.root

        spinnerFiltros = root.findViewById(R.id.botonFiltros)

        // Crear un ArrayAdapter usando un arreglo de strings y un diseño predeterminado para el Spinner
        val adapterFiltrar = ArrayAdapter.createFromResource(
            root.context,
            R.array.filtros,
            android.R.layout.simple_spinner_item
        )

        // Especificar el diseño que se utilizará cuando la lista de opciones aparezca
        adapterFiltrar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Asociar el ArrayAdapter con el Spinner
        spinnerFiltros.adapter = adapterFiltrar

        spinnerOrdenar = root.findViewById<Spinner>(R.id.botonOrdenar)

        // Crear un ArrayAdapter usando un arreglo de strings y un diseño predeterminado para el Spinner
        val adapterOrdenar = ArrayAdapter.createFromResource(
            root.context,
            R.array.orden,
            android.R.layout.simple_spinner_item
        )

        // Especificar el diseño que se utilizará cuando la lista de opciones aparezca
        adapterOrdenar.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Asociar el ArrayAdapter con el Spinner
        spinnerOrdenar.adapter = adapterOrdenar

        // Obtén la referencia del RecyclerView desde el layout
        recyclerView = root.findViewById(R.id.listado_recyclerView)

        // Configura el LinearLayoutManager (puedes cambiarlo según tus necesidades)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        edbuscador = root.findViewById(R.id.buscador)

        // Crea una lista de cursos (puedes cargarla desde la base de datos)


        spinnerFiltros.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Obtener el item seleccionado del Spinner
                val selectedItem = parent?.getItemAtPosition(position).toString()

                // Ejecutar la consulta correspondiente según el item seleccionado
                when (selectedItem) {
                    "Académico" -> {
                        // Ejecutar consulta 1
                        // Por ejemplo: consulta1()
                    }

                    "Otro" -> {
                        // Ejecutar consulta 2
                        // Por ejemplo: consulta2()
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        spinnerOrdenar.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Obtener el item seleccionado del Spinner
                val selectedItem = parent?.getItemAtPosition(position).toString()

                // Ejecutar la consulta correspondiente según el item seleccionado
                when (selectedItem) {
                    "Fecha \uD83D\uDD3C" -> {
                        // Ejecutar consulta 1
                        // Por ejemplo: consulta1()
                    }

                    "Academia" -> {
                        // Ejecutar consulta 2
                        // Por ejemplo: consulta2()
                    }
                    "Precio" -> {
                        // Ejecutar consulta 3
                        // Por ejemplo: consulta2()
                    }
                    "Valoración" -> {
                        // Ejecutar consulta 4
                        // Por ejemplo: consulta2()
                    }
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        btnBuscar = root.findViewById(R.id.botonLupa)
        btnBuscar.setOnClickListener {
            buscar(view)
        }

        campoFecha = root.findViewById(R.id.textoFecha)
        campoFecha.setOnClickListener {
            mostrarDatePicker()
        }

        return root
    }

    /**
     * Método que después de haberse creado la vista, lanza un spinner de carga
     * asigna el valor de algún otro item y asigna el evento de cargar el fragment
     * del mapa a su botón. Después de esto, rellena el RecyclerView de la vista.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = view.findViewById(R.id.progressBar)
        contentLayout = view.findViewById(R.id.contentLayout)
        btnCargarMapa = view.findViewById(R.id.buscar_porMapa)
        btnCargarMapa.setOnClickListener {
            val navController = findNavController()
            navController.navigate(R.id.mapaFragment)
        }

        recyclerView = view.findViewById(R.id.listado_recyclerView)
        rellenarRecyclerView()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Método que muestra un DatePicker cuando haces click en su botón correspondiente.
     * Éste se desplegará en un Dialog y asignará a éste la acción al seleccionar una fecha,
     * que será rellenar su campo de texto.
     */
    private fun mostrarDatePicker() {
        // Obtener la fecha actual para preseleccionarla en el DatePicker
        val calendar = Calendar.getInstance()

        val datePickerDialog = DatePickerDialog(
            this.requireContext(),
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)

                // Formatear la fecha seleccionada al formato deseado
                val formattedDate = dateFormat.format(selectedDate.time)

                // Establecer la fecha formateada en el EditText
                campoFecha.setText(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
        )

        // Mostrar el DatePickerDialog
        datePickerDialog.show()
    }

    /**
     * Método que realiza la carga del RecyclerView sobre el BuscarFragment. Lanzará una consulta
     * a la base de datos para obtener una List<Curso> según el nombre del curso, y los filtros
     * y orden seleccionadas por el usuario. Después, llamará al controlador del RecyclerView para
     * asignar a cada item del mismo, el valor de un Curso
     */
    fun rellenarRecyclerView() {
        lifecycleScope.launch(Dispatchers.IO) {
            val cursosDAO = CursoDAO()
            val listaDeCursos = cursosDAO.obtenerCursosConFiltros(edbuscador.text.toString(), spinnerFiltros.selectedItem.toString(), spinnerOrdenar.selectedItem.toString())

            Log.i("BuscarFragment", "Cantidad de cursos: ${listaDeCursos.size}")

            withContext(Dispatchers.Main) {
                val adaptador = RecyclerBuscar(listaDeCursos,requireActivity() as RecyclerBuscar.OnItemClickListener, requireActivity() as RecyclerBuscar.OnItemCheckedChangeListener,(activity as MainActivity).username)

                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = adaptador

                progressBar.visibility = View.GONE
                contentLayout.visibility = View.VISIBLE

            }
        }
    }

    /**
     * Método que realiza la consulta
     *
     * @param View
     */
    fun buscar(view: View) {
        lifecycleScope.launch(Dispatchers.IO) {
            val cursosDAO = CursoDAO()
            val listaDeCursos = cursosDAO.obtenerCursosConFiltros(
                edbuscador.text.toString(),
                spinnerFiltros.selectedItem.toString(),
                spinnerOrdenar.selectedItem.toString()
            )

            Log.i("BuscarFragment", "Cantidad de cursos: ${listaDeCursos.size}")

            withContext(Dispatchers.Main) {
                val adaptador = RecyclerBuscar(listaDeCursos, requireActivity() as RecyclerBuscar.OnItemClickListener, requireActivity() as RecyclerBuscar.OnItemCheckedChangeListener, (activity as MainActivity).username)

                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = adaptador
            }
        }
    }

    fun mostrarCursos(): List<Curso>{
        return listaDeCursos
    }


}