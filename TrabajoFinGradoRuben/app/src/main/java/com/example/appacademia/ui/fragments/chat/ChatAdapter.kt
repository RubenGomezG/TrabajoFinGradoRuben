package com.example.appacademia.ui.fragments.chat

import android.content.Context
import android.graphics.Color.BLUE
import android.graphics.Color.LTGRAY
import android.graphics.Color.RED
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.TEXT_ALIGNMENT_VIEW_END
import android.view.View.TEXT_ALIGNMENT_VIEW_START
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.appacademia.R
import com.example.appacademia.dao.servidorSQL.AcademiaDAO
import com.example.appacademia.dao.servidorSQL.MensajeDAO
import com.example.appacademia.dao.servidorSQL.UsuarioDAO
import com.example.appacademia.ftp.InterfaceFTP
import com.example.appacademia.model.Mensaje
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatAdapter(private val messages: MutableList<Mensaje>,
                  private val listener: OnItemClickListener,
                  private val context : Context)
                  : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    /***
     * Crear la interfaz del evento OnClick
     * para navegar al perfil de usuario desde
     * la foto del usuario. Sólo funcionará en el
     * usuario, no en la academia
     */
    interface OnItemClickListener {
        /**
         * Ocurrirá al hacer click en la imagen del usuario
         */
        fun onUsuarioClick()
    }

    /**
     * Al crear el viewholder de un nuevo item del recycler,
     * inflya el layout y le asigna un recycler contenedor
     *
     * @param parent -  contenedor
     * @param viewType
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tarjeta_mensaje, parent, false)
        return MessageViewHolder(view)
    }

    /**
     * Asigna la posición de cada mensaje dentro del recycler
     *
     * @param holder - la tarjeta de un curso
     * @param position - posición en el recycler
     */
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)
    }

    /**
     * Obtener la cantidad de mensajes existentes
     *
     * @return Int
     */
    override fun getItemCount(): Int = messages.size

    /**
     * Rellenar cada tarjeta de mensaje con sus datos correspondientes
     * para crear su viewHolder.
     * Tendrá que descargar los mensajes de la base de datos y cargar las imágenes
     * vía FTP.
     * Por cada item, asignará según quien lo haya enviado una posición relativa
     * al layout, y en caso de ser enviado por el usuario, la posibilidad de borrarlo.
     * También asignará un color según quien sea el remitente.
     *
     * @param itemView - vista actual
     *
     * @return devuelve un viewHolder
     */
    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val senderTextView: TextView = itemView.findViewById(R.id.senderTextView)
        private val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)
        private val fechaTextView: TextView = itemView.findViewById(R.id.fechaTextView)
        private val fotoUsuario: ShapeableImageView = itemView.findViewById(R.id.fotoUsuario)
        private val linearLayout = itemView.findViewById<LinearLayout>(R.id.contenedorMensaje)

        fun bind(message: Mensaje) {
            if (message.senderUsername == null){
                fechaTextView.textAlignment = TEXT_ALIGNMENT_VIEW_START
                senderTextView.textAlignment = TEXT_ALIGNMENT_VIEW_START
                contentTextView.textAlignment = TEXT_ALIGNMENT_VIEW_START
                GlobalScope.launch {
                    val academiadao = AcademiaDAO()
                    val academia = academiadao.consultarAcademiaConCodigo(message.senderCodAcademia as Int)
                    withContext(Dispatchers.Main) {

                        val interfaceFTP = InterfaceFTP()
                        val bitmap = interfaceFTP.downloadFileFromFTP(academia.imgPerfil)

                        // Haz algo con el bitmap, por ejemplo, mostrarlo en la imagen de la academia
                        if (bitmap != null) {
                            interfaceFTP.colocarImage(bitmap, fotoUsuario)
                        }
                        senderTextView.text = academia.nombre
                        linearLayout.background.setTint(LTGRAY)
                        val layoutParamsFoto = fotoUsuario.layoutParams as ConstraintLayout.LayoutParams
                        layoutParamsFoto.startToEnd = ConstraintLayout.LayoutParams.UNSET
                        layoutParamsFoto.startToStart = R.id.tarjetaMensaje
                        layoutParamsFoto.endToEnd = ConstraintLayout.LayoutParams.UNSET
                        fotoUsuario.layoutParams = layoutParamsFoto

                        val layoutParamsTexto = linearLayout.layoutParams as ConstraintLayout.LayoutParams
                        layoutParamsTexto.startToEnd = R.id.fotoUsuario
                        layoutParamsTexto.startToStart = ConstraintLayout.LayoutParams.UNSET
                        layoutParamsTexto.endToEnd = R.id.tarjetaMensaje
                        layoutParamsTexto.endToStart = ConstraintLayout.LayoutParams.UNSET

                        linearLayout.layoutParams = layoutParamsTexto
                    }
                }
            }
            else{

                GlobalScope.launch {
                    val usuariodao = UsuarioDAO()
                    val usuario = usuariodao.consultarUsuario(message.senderUsername as String)
                    withContext(Dispatchers.Main) {

                        val interfaceFTP = InterfaceFTP()
                        val bitmap = interfaceFTP.downloadFileFromFTP(usuario.img_perfil)

                        // Haz algo con el bitmap, por ejemplo, mostrarlo en la imagen de la academia
                        if (bitmap != null) {
                            interfaceFTP.colocarImage(bitmap, fotoUsuario)
                        }
                        val layoutParamsFoto = fotoUsuario.layoutParams as ConstraintLayout.LayoutParams
                        layoutParamsFoto.startToEnd = R.id.contenedorMensaje
                        layoutParamsFoto.startToStart = ConstraintLayout.LayoutParams.UNSET
                        layoutParamsFoto.endToEnd = R.id.tarjetaMensaje
                        fotoUsuario.layoutParams = layoutParamsFoto

                        val layoutParamsTexto = linearLayout.layoutParams as ConstraintLayout.LayoutParams
                        layoutParamsTexto.startToEnd = ConstraintLayout.LayoutParams.UNSET
                        layoutParamsTexto.startToStart = R.id.tarjetaMensaje
                        layoutParamsTexto.endToEnd = ConstraintLayout.LayoutParams.UNSET
                        layoutParamsTexto.endToStart = R.id.fotoUsuario

                        linearLayout.background.setTintList(null)
                        linearLayout.layoutParams = layoutParamsTexto
                        fotoUsuario.setOnClickListener{listener.onUsuarioClick()}

                    }
                }
            }
            linearLayout.isLongClickable = true
            linearLayout.setOnLongClickListener{f ->
                val mensajeDAO = MensajeDAO()
                val codMensaje = message.codMensaje
                var exito : Boolean = false
                GlobalScope.launch(Dispatchers.IO) {
                    val mensajeABorrar = mensajeDAO.consultarMensaje(codMensaje)
                    withContext(Dispatchers.Main){
                        Toast.makeText(context, "Mensaje eliminado correctamente", Toast.LENGTH_SHORT).show()
                    }
                    if (mensajeABorrar != Mensaje() && mensajeABorrar.senderCodAcademia == null){
                        mensajeDAO.borrarMensaje(mensajeABorrar)
                        exito = true
                    }
                }
                exito
            }
            senderTextView.text = "Yo"
            fechaTextView.textAlignment = TEXT_ALIGNMENT_VIEW_END
            senderTextView.textAlignment = TEXT_ALIGNMENT_VIEW_END
            contentTextView.textAlignment = TEXT_ALIGNMENT_VIEW_END
            fechaTextView.text = message.timestamp.toString().split(".")[0]
            contentTextView.text = message.content
        }
    }
}