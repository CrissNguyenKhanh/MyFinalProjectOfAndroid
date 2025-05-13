package com.example.retrofitwrooom.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitwrooom.databinding.NoteLayoutBinding
import com.example.retrofitwrooom.model.note.Note
import com.example.retrofitwrooom.ui.fragments.note.HomeFragmentDirections

class NoteAdapater:RecyclerView.Adapter<NoteAdapater.NoteViewHolder>() {

    inner class NoteViewHolder(var itemBinding: NoteLayoutBinding)
        : RecyclerView.ViewHolder(itemBinding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<Note>(){
        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.noteTitlet == newItem.noteTitlet &&
                    oldItem.noteDescription == newItem.noteDescription
        }

        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this , differCallBack)

    override fun getItemCount(): Int {
       return  differ.currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            NoteLayoutBinding.inflate(LayoutInflater.from(parent.context) , parent , false
            )
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
       val currentNote = differ.currentList[position]
        holder.itemBinding.noteTitle.text = currentNote.noteTitlet
        holder.itemBinding.noteDesc.text = currentNote.noteDescription


        holder.itemView.setOnClickListener {
            val direction = HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(currentNote)
            it.findNavController().navigate(direction)
        }
    }
}