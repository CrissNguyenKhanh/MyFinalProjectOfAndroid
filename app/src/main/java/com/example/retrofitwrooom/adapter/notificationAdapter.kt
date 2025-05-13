package com.example.retrofitwrooom.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.model.Notification

class notificationAdapter(
    private val context: Context,
    private var notifications: List<Notification>,
    private val onDelete: (Notification) -> Unit,
    private val onEdit: (Notification) -> Unit
) : RecyclerView.Adapter<notificationAdapter.NotificationViewHolder>() {

    fun updateNotifications(newNotifications: List<Notification>) {
        notifications = newNotifications
        notifyDataSetChanged()
    }

    class NotificationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val titleTextView: TextView = view.findViewById(R.id.tvTen)
        val idTextView: TextView = view.findViewById(R.id.tvMa)
        val detailTextView: TextView = view.findViewById(R.id.tvChiTiet)
        val yearTextView: TextView = view.findViewById(R.id.tvTime)
        val btnEdit: Button = view.findViewById(R.id.btnEdit)
        val btnDelete: Button = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.imageView.setImageResource(notification.image)
        holder.titleTextView.text = notification.title
        holder.idTextView.text = "ID: ${notification.id}"
        holder.detailTextView.text = "Chi tiết: ${notification.detail}"
        holder.yearTextView.text = notification.time

        holder.btnEdit.setOnClickListener { onEdit(notification) }
        holder.btnDelete.setOnClickListener { onDelete(notification) }
    }

    override fun getItemCount(): Int = notifications.size
}
