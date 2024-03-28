package com.example.appacademia.ui.fragments.cursos

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appacademia.R
import com.example.appacademia.model.Curso
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.example.appacademia.dao.servidorSQL.AcademiaDAO
import com.example.appacademia.dao.servidorSQL.FavoritoDAO
import com.example.appacademia.ftp.InterfaceFTP
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecyclerCursos(private val cursos: List<Curso>, private val listener : OnItemClickListener,
                     private val checkedChangeListener: OnItemCheckedChangeListener,
                     private val username : String) : RecyclerView.Adapter<RecyclerCursos.ViewHolder>() {

    /***
     * Crear la interfaz de los distintos eventos OnClick
     * contenidos en cada elemento individual del recycler
     */
    interface OnItemClickListener {
        /**
         * Ocurrirá al hacer click sobre el área superior de la tarjeta informativa
         *
         * @param curso - curso correspondiente a la tarjeta seleccionada
         */
        fun onItemClick(curso: Curso)

        /**
         * Ocurrirá al hacer click sobre el botón inferior de la tarjeta informativa
         *
         * @param curso - curso correspondiente a la tarjeta seleccionada
         */
        fun onClickCancelarSub(curso: Curso)
    }

    /**
     * Inteterfaz para los eventos del check de cada tarjeta del recycler
     */
    interface OnItemCheckedChangeListener{
        /**
         * Evento que tiene lugar al hacer click sobre el check
         * de la estrella, que marca el curso como favorito
         *
         * @param curso - curso correspondiente a la tarjeta seleccionada
         */
        fun onItemCheckedChanged(curso: Curso, isChecked: Boolean)
    }

    /**
     * Al crear el viewholder de un nuevo item del recycler,
     * inflya el layout y le asigna un recycler contenedor
     *
     * @param parent -  contenedor
     * @param viewType
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerCursos.ViewHolder {
        val row: View = LayoutInflater.from(parent.context).inflate(R.layout.tarjetas_cursos_inscritos, parent, false)
        return ViewHolder(row)
    }

    /**
     * Obtener la cantidad de cursos a los
     * que un usuario está inscrito
     *
     * @return tamaño de los cursos
     */
    override fun getItemCount() = cursos.size

    /**
     * Asigna la posición de cada curso dentro del recycler
     *
     * @param holder - la tarjeta de un curso
     * @param position - posición en el recycler
     */
    override fun onBindViewHolder(holder: RecyclerCursos.ViewHolder, position: Int) {
        val curso = cursos[position]
        holder.bindRow(curso)
        holder.itemView.setOnClickListener{
            listener.onItemClick(curso)
        }
    }

    /**
     * Rellenar los cada tarjeta de curso con sus datos correspondientes
     * para crear su viewHolder
     *
     * @param view - vista actual
     *
     * @return devuelve un viewHolder
     */
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var checkFavorito : CheckBox = view.findViewById(R.id.checkFavorito)
        val titleTextView: TextView = view.findViewById(R.id.nombreCurso)
        val nombreAcademia: TextView = view.findViewById(R.id.tituloAcademia)
        val valoraAcademia: RatingBar = view.findViewById(R.id.valoraAcademia)
        val botonCancelar : Button = view.findViewById(R.id.btnCancelar)
        var imagenAcademia : ImageView = view.findViewById(R.id.imagenAcademia)

        fun bindRow(curso: Curso) {
            titleTextView.text = curso.nombre
            valoraAcademia.progress = (curso.valoracion * 10).toInt()


            GlobalScope.launch {

                val academiadao = AcademiaDAO()
                val academia = academiadao.consultarAcademiaConCodigo(curso.codAcademia)
                val favoritodao = FavoritoDAO()
                val favorito = favoritodao.consultarFavorito(username,curso.codCurso)

                withContext(Dispatchers.Main) {

                    val interfaceFTP = InterfaceFTP()
                    val bitmap = interfaceFTP.downloadFileFromFTP(academia.imgPerfil)

                    // Haz algo con el bitmap, por ejemplo, mostrarlo en la imagen de la academia
                    if (bitmap != null) {
                        interfaceFTP.colocarImage(bitmap, imagenAcademia)
                    }
                    if (favorito.username != "" && favorito.username == username && favorito.codCurso == curso.codCurso ){
                        checkFavorito.isChecked = true
                    }
                }
                nombreAcademia.text = academia.nombre

            }

            checkFavorito.setOnCheckedChangeListener { _, isChecked ->
                checkedChangeListener.onItemCheckedChanged(curso, isChecked)
            }
            botonCancelar.setOnClickListener{listener.onClickCancelarSub(curso)}
        }
    }
}