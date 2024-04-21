package com.example.appacademia.ui.fragments.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appacademia.R
import com.example.appacademia.model.Mensaje
import com.example.appacademia.ui.activities.MainActivity

class ChatFragment : Fragment() {

    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var editTextMessage: EditText
    private lateinit var btnSendMessage: ImageButton

    private lateinit var messagesAdapter: ChatAdapter
    private val messagesList = mutableListOf<Mensaje>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        messageRecyclerView = view.findViewById(R.id.messageRecyclerView)
        editTextMessage = view.findViewById(R.id.editTxtMessage)
        btnSendMessage = view.findViewById(R.id.btnSendMessage)

        messagesAdapter = ChatAdapter(messagesList)
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

        // Cargar mensajes existentes
        loadMessages()

        return view
    }

    private fun loadMessages() {
        messagesList.clear()
        /*val md = MessageDatabase()
        md.loadMessagesByTime { messages ->
            for (message in messages) {
                println("Message in messages ForumFragment")
                messagesList.add(message)
            }
            messagesAdapter.notifyDataSetChanged()
            messageRecyclerView.scrollToPosition(messagesList.size - 1)
        }
        */

    }

    private fun sendMessage(messageText: String) {
        //val md = MessageDatabase()
        /*val mainActivity = activity as MainActivity
        val message = Mensaje("", mainActivity.username.toString(), messageText, System.currentTimeMillis())

        md.addMessage(message)
        messagesList.add(message)

        messagesAdapter.notifyItemInserted(messagesList.size - 1)
        messageRecyclerView.scrollToPosition(messagesList.size - 1)
        editTextMessage.setText("")*/
    }
}