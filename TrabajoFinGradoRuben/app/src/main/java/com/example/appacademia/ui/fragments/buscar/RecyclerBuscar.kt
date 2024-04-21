package com.example.appacademia.ui.fragments.buscar

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appacademia.R
import com.example.appacademia.dao.servidorSQL.AcademiaDAO
import com.example.appacademia.dao.servidorSQL.FavoritoDAO
import com.example.appacademia.ftp.InterfaceFTP
import com.example.appacademia.model.Curso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



class RecyclerBuscar(private val cursos: List<Curso>, private val listener: OnItemClickListener,
                     private val checkedChangeListener: OnItemCheckedChangeListener,
                     private val username : String) : RecyclerView.Adapter<RecyclerBuscar.ViewHolder>() {

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
        fun onButtonClick(curso: Curso)
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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val row: View = LayoutInflater.from(parent.context).inflate(R.layout.tarjetas_horizontal, parent, false)
        return ViewHolder(row)
    }

    /**
     * Obtener la cantidad de cursos existentes
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
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val curso = cursos[position]
        holder.bindRow(curso)
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
        val titleTextView: TextView = view.findViewById(R.id.tituloCurso)
        val nombreAcademia: TextView = view.findViewById(R.id.nombreAcademia)
        val descAcademia: TextView = view.findViewById(R.id.descAcademia)
        val precioAcademia: TextView = view.findViewById(R.id.info_precio_curso)
        val valoraAcademia: RatingBar = view.findViewById(R.id.valoraAcademia)
        val botonInscribirse : Button = view.findViewById(R.id.btnInscribirse)
        var contenedorDatos :  LinearLayout = view.findViewById(R.id.contenedorDatos)
        var imagenAcademia : ImageView = view.findViewById(R.id.imagenAcademia)

        fun bindRow(curso: Curso) {
            titleTextView.text = curso.nombre
            descAcademia.text = curso.descripcion
            precioAcademia.text = curso.precio.toString() + " €"
            valoraAcademia.progress = (curso.valoracion * 10).toInt()


            GlobalScope.launch {

                val academiadao = AcademiaDAO()
                val academia = academiadao.consultarAcademiaConCodigo(curso.codAcademia)
                val favoritodao = FavoritoDAO()

                val favorito = favoritodao.consultarFavorito(username,curso.codCurso)
                withContext(Dispatchers.Main) {

                    val interfaceFTP = InterfaceFTP()
                    val bitmap = interfaceFTP.downloadFileFromFTP(academia.imgPerfil)
                    Log.i("imag", "bindRow: RECYCLER CURSOS -\n FILENAME: " + academia.imgPerfil + "\nCOD ACADEMIA:" + academia.codAcademia + "\nNOM ACADEMIA:" + academia.nombre)
                    // Haz algo con el bitmap, por ejemplo, mostrarlo en la imagen de la academia
                    if (bitmap != null) {
                        interfaceFTP.colocarImage(bitmap, imagenAcademia)
                    }
                    nombreAcademia.text = academia.nombre
                    if (favorito.username != "" && favorito.username == username && favorito.codCurso == curso.codCurso ){
                        checkFavorito.isChecked = true
                    }
                }
            }
            checkFavorito.setOnCheckedChangeListener { _, isChecked ->
                checkedChangeListener.onItemCheckedChanged(curso, isChecked)
            }
            botonInscribirse.setOnClickListener{listener.onButtonClick(curso)}
            contenedorDatos.setOnClickListener{listener.onItemClick(curso)}
        }
    }
}
