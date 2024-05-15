package com.example.appacademia.ui.fragments.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
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
    private var messagesList = mutableListOf<Mensaje>()
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        navController = (activity as MainActivity).getNavController()
        messageRecyclerView = view.findViewById(R.id.messageRecyclerView)
        editTextMessage = view.findViewById(R.id.editTxtMessage)
        btnSendMessage = view.findViewById(R.id.btnSendMessage)

        messagesAdapter = ChatAdapter(messagesList, requireActivity() as ChatAdapter.OnItemClickListener)
        messageRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = messagesAdapter
        }

        btnSendMessage.setOnClickListener {
            val messageText = editTextMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                sendMessage(messageText)
            }
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadMessages()
        progressBar = view.findViewById(R.id.progressBar)
        contentLayout = view.findViewById(R.id.chatContentLayout)
    }

    private fun loadMessages() {
        lifecycleScope.launch(Dispatchers.IO) {
            val mensajeDAO = MensajeDAO()
            val conversacionDAO = ConversacionDAO()
            var conversacion : Conversacion = conversacionDAO.consultarConversacion((requireActivity() as MainActivity).username)
            val listaDeMensajes = mensajeDAO.obtenerTodosLosMensajesDeConversacion(
                                    conversacion.codConversacion)

            withContext(Dispatchers.Main) {
                val adaptador = ChatAdapter(listaDeMensajes,requireActivity() as ChatAdapter.OnItemClickListener)
                messagesList = listaDeMensajes
                messageRecyclerView.layoutManager = LinearLayoutManager(requireContext())
                messageRecyclerView.adapter = adaptador
                messagesAdapter.notifyDataSetChanged()
                messageRecyclerView.scrollToPosition(messagesList.size - 1)

                progressBar.visibility = View.GONE
                contentLayout.visibility = View.VISIBLE
            }
        }
    }

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
            }
            loadMessages()
        }
        messagesAdapter.notifyItemInserted(messagesList.size - 1)
        messageRecyclerView.scrollToPosition(messagesList.size - 1)
        editTextMessage.setText("")
    }
}