package com.example.appacademia.ui.fragments.chat

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appacademia.R
import com.example.appacademia.dao.servidorSQL.ConversacionDAO
import com.example.appacademia.dao.servidorSQL.MensajeDAO
import com.example.appacademia.model.Conversacion
import com.example.appacademia.model.Mensaje
import com.example.appacademia.ui.activities.MainActivity
import com.example.appacademia.ui.fragments.cursos.CursosViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.sql.Timestamp
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

class ChatFragment : Fragment() {



    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var editTextMessage: EditText
    private lateinit var btnSendMessage: ImageButton
    private lateinit var progressBar: ProgressBar
    private lateinit var contentLayout: LinearLayout
    private lateinit var messagesAdapter: ChatAdapter
    private lateinit var navController: NavController
    private lateinit var contenedorTexto: TextView

    /**
     * Método que crea la vista del Fragment Chat. Recoge y lanza el layout correspondiente,
     * y asigna el valor a cada objeto interno. También crea el listeners y los asigna para
     * del botón de enviar.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     *
     * @return View
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        navController = (activity as MainActivity).getNavController()
        editTextMessage = view.findViewById(R.id.editTxtMessage)
        btnSendMessage = view.findViewById(R.id.btnSendMessage)
        progressBar = view.findViewById(R.id.progressBar)
        contentLayout = view.findViewById(R.id.chatContentLayout)

        if ((activity as MainActivity).loggedIn) {
            val cursosViewModel =
                ViewModelProvider(this).get(CursosViewModel::class.java)

            cursosViewModel.text.observe(viewLifecycleOwner) {
                messageRecyclerView = view.findViewById(R.id.messageRecyclerView)
                btnSendMessage.setOnClickListener {
                    val messageText = editTextMessage.text.toString().trim()
                    if (messageText.isNotEmpty()) {
                        sendMessage(messageText)
                    }
                }
                loadMessages()
            }
        } else {
            btnSendMessage.visibility = View.GONE
            editTextMessage.visibility = View.GONE
            contenedorTexto = view.findViewById(R.id.textoCentrado)
            // Muestra el contenedor del texto
            contenedorTexto.visibility = View.VISIBLE
        }

        return view
    }

    /**
     * Método que después de haberse creado la vista, lanza un spinner de carga
     * asigna el valor de algún otro item y se encarga de asignar el texto por
     * defecto si el usuario no está conectado. En caso de estarlo después de esto,
     * rellena el RecyclerView de la vista.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if ((activity as MainActivity).loggedIn) {
            // Si el usuario está logeado, llenar el RecyclerView
            messageRecyclerView = view.findViewById(R.id.messageRecyclerView)
            progressBar = view.findViewById(R.id.progressBar)
            progressBar.visibility = View.VISIBLE

            loadMessages()
        } else {
            // Si el usuario no está logeado muestra un mensaje
            // Obtén el contenedor y el TextView
            val contenedorTexto = (activity as MainActivity).findViewById<ViewGroup>(R.id.contenedorTexto)

            // Oculta el RecyclerView y muestra el contenedor del texto
            contenedorTexto.visibility = View.VISIBLE

            // Crea el TextView dinámicamente
            val textoCentrado = TextView(requireContext())
            textoCentrado.text = getString(R.string.conectateMensajes)
            textoCentrado.textSize = 24f
            textoCentrado.gravity = Gravity.CENTER

            // Agrega el TextView al contenedor
            contenedorTexto.addView(textoCentrado)
        }
        progressBar.visibility = View.GONE
        contentLayout.visibility = View.VISIBLE
    }

    /**
     * Método que realiza la carga del RecyclerView sobre el ChatFragment. Lanzará una consulta
     * a la base de datos para obtener una List<Mensaje>. Después, llamará al controlador del
     * RecyclerView para asignar a cada item del mismo, el valor de un Mensaje.
     */
    private fun loadMessages() {
        lifecycleScope.launch(Dispatchers.IO) {
            val mensajeDAO = MensajeDAO()
            val conversacionDAO = ConversacionDAO()
            var conversacion : Conversacion = conversacionDAO.consultarConversacion((requireActivity() as MainActivity).username)
            val listaDeMensajes = mensajeDAO.obtenerTodosLosMensajesDeConversacion(
                                    conversacion.codConversacion)

            withContext(Dispatchers.Main) {
                messagesAdapter = ChatAdapter(listaDeMensajes, requireActivity() as ChatAdapter.OnItemClickListener,requireActivity() as MainActivity)
                messageRecyclerView.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = messagesAdapter
                }
                messagesAdapter.notifyDataSetChanged()
                messageRecyclerView.scrollToPosition(listaDeMensajes.size - 1)
            }
        }
    }

    /**
     * Método que realiza el envío de mensajes. Recoge el valor del campo de texto y carga
     * la fecha y hora actuales, junto al remitente y el código de la conversación en una
     * instancia de Mensaje. Después de eso, hará una llamada a la base de datos, y recargará
     * la vista.
     */
    private fun sendMessage(messageText: String) {
        val mensajeDAO = MensajeDAO()
        val conversacionDAO = ConversacionDAO()
        val usuario = (requireActivity() as MainActivity).username
        Log.i("TAG", usuario)
        lifecycleScope.launch {
            withContext(Dispatchers.IO){
                val conversacion : Conversacion = conversacionDAO.consultarConversacion(usuario)
                val codConversacion = conversacion.codConversacion
                val cal: Calendar = GregorianCalendar()
                val date : Date = cal.time
                val mensaje = Mensaje(0, codConversacion, usuario,0, messageText, date as Date?)
                mensajeDAO.anadirMensaje(mensaje)
                loadMessages()
            }
        }
        editTextMessage.setText("")
    }
}