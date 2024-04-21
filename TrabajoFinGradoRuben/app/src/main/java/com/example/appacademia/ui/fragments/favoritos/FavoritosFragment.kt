package com.example.appacademia.ui.fragments.favoritos

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appacademia.R
import com.example.appacademia.dao.servidorSQL.CursoDAO
import com.example.appacademia.dao.servidorSQL.FavoritoDAO
import com.example.appacademia.dao.servidorSQL.UsuarioDAO
import com.example.appacademia.databinding.FragmentFavoritosBinding
import com.example.appacademia.ui.activities.MainActivity
import com.example.appacademia.ui.fragments.buscar.RecyclerBuscar
import com.example.appacademia.ui.fragments.cursos.CursosViewModel
import com.example.appacademia.ui.fragments.cursos.RecyclerCursos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritosFragment : Fragment() {
    private var _binding: FragmentFavoritosBinding? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var contenedorTexto: TextView
    private lateinit var progressBar: ProgressBar

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
        _binding = FragmentFavoritosBinding.inflate(inflater, container, false)
        val root: View = binding.root
        if ((activity as MainActivity).loggedIn) {
            val cursosViewModel =
                ViewModelProvider(this).get(CursosViewModel::class.java)

            recyclerView = binding.listaCardsCursos
            cursosViewModel.text.observe(viewLifecycleOwner) {
                rellenarRecyclerView()
            }
        } else {
            contenedorTexto = binding.textoCentradof
            // Muestra el contenedor del texto
            contenedorTexto.visibility = View.VISIBLE
        }

        return root
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

        if ((activity as MainActivity).loggedIn) {
            // Si el usuario está logeado, llenar el RecyclerView
            recyclerView = view.findViewById(R.id.listaCardsCursos)
            progressBar = view.findViewById(R.id.progressBar)
            progressBar.visibility = View.VISIBLE
            rellenarRecyclerView()
        } else {
            // Si el usuario no está logeado muestra un mensaje
            // Obtén el contenedor y el TextView
            val contenedorTexto = (activity as MainActivity).findViewById<ViewGroup>(R.id.contenedorTextof)

            // Oculta el RecyclerView y muestra el contenedor del texto
            // recyclerView.visibility = View.GONE
            contenedorTexto.visibility = View.VISIBLE

            // Crea el TextView dinámicamente
            val textoCentrado = TextView(requireContext())
            textoCentrado.text = "¡Inicia sesión para ver los cursos!"
            textoCentrado.textSize = 24f
            textoCentrado.gravity = Gravity.CENTER

            // Agrega el TextView al contenedor
            contenedorTexto.addView(textoCentrado)
        }
    }

    /***
     * Al destruir la vista, borramos el contenido del binding
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /***
     * Rellena el recycler con los cursos marcados
     * como favoritos por el usuario
     */
    fun rellenarRecyclerView() {
        lifecycleScope.launch(Dispatchers.IO) {
            val favoritoDAO = FavoritoDAO()
            val cursosDAO = CursoDAO()
            val listaFavoritos = favoritoDAO.listaFavoritos((activity as MainActivity).username)


            val listaDeCursos = listaFavoritos.map { favorito ->
                cursosDAO.consultarCurso(favorito.codCurso)
            }
            Log.i("BuscarFragment", "Cantidad de cursos: ${listaDeCursos.size}")

            withContext(Dispatchers.Main) {
                val adaptador = RecyclerFavoritos(listaDeCursos, requireActivity() as RecyclerFavoritos.OnItemClickListener, requireActivity() as RecyclerFavoritos.OnItemCheckedChangeListener,(activity as MainActivity).username)

                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = adaptador
            }
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }

    }

}