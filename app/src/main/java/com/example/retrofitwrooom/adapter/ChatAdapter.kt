package com.example.retrofitwrooom.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.model.ChatMessage

class ChatAdapter(private val messages: List<ChatMessage>) :
    RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.textView.text = message.message
        // Đổi background hoặc alignment tùy theo người gửi
        val params = holder.textView.layoutParams as ViewGroup.MarginLayoutParams
        if (message.isSentByUser) {
            holder.textView.setBackgroundResource(R.color.light_blue)
            params.marginStart = 50
            params.marginEnd = 0
        } else {
            holder.textView.setBackgroundResource(R.color.light_gray)
            params.marginStart = 0
            params.marginEnd = 50
        }
        holder.textView.layoutParams = params

    }

    override fun getItemCount() = messages.size
}
