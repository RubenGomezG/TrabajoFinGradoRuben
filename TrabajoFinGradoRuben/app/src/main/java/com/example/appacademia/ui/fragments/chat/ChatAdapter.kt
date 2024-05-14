package com.example.appacademia.ui.fragments.chat

import android.view.LayoutInflater
import android.view.View
import android.view.View.TEXT_ALIGNMENT_VIEW_END
import android.view.View.TEXT_ALIGNMENT_VIEW_START
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appacademia.R
import com.example.appacademia.model.Mensaje

class ChatAdapter(private val messages: List<Mensaje>) : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.tarjeta_mensaje, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int = messages.size

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val senderTextView: TextView = itemView.findViewById(R.id.senderTextView)
        private val contentTextView: TextView = itemView.findViewById(R.id.contentTextView)

        fun bind(message: Mensaje) {
            if (message.senderUsername == null){
                senderTextView.text = message.senderCodAcademia.toString()
                senderTextView.textAlignment = TEXT_ALIGNMENT_VIEW_START
                contentTextView.textAlignment = TEXT_ALIGNMENT_VIEW_START

            }
            else{
                senderTextView.textAlignment = TEXT_ALIGNMENT_VIEW_END
                contentTextView.textAlignment = TEXT_ALIGNMENT_VIEW_END
                contentTextView.text = message.content
            }
            contentTextView.text = message.content

        }
    }
}