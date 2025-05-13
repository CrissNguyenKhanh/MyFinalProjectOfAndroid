package com.example.retrofitwrooom.ui.fragments.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.database.noteDatabase
import com.example.retrofitwrooom.databinding.FragmentAddNoteBinding
import com.example.retrofitwrooom.databinding.FragmentEditNoteBinding
import com.example.retrofitwrooom.model.note.Note
import com.example.retrofitwrooom.repository.noteRepository
import com.example.retrofitwrooom.viewModel.note.noteViewModel
import com.example.retrofitwrooom.viewModel.note.noteViewmodelProvider






class EditNoteFragment : Fragment(R.layout.fragment_edit_note) , MenuProvider {
    private var editNoteBinding:FragmentEditNoteBinding? =null
    private val binding get() = editNoteBinding!!

    private lateinit var notesViewModel: noteViewModel
    private  lateinit var currentNote: Note

    private val args:EditNoteFragmentArgs by navArgs()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        editNoteBinding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        (binding.homeToolbar)?.let {
            (requireActivity() as AppCompatActivity).setSupportActionBar(it)
        }
        // Khởi tạo ViewModel
        val repository = noteRepository(noteDatabase(requireActivity().application))
        val factory = noteViewmodelProvider(requireActivity().application, repository)
        notesViewModel = ViewModelProvider(this, factory)[noteViewModel::class.java]
        currentNote =args.note!!


        binding.editNoteTitle.setText(currentNote.noteTitlet)
        binding.editNoteDesc.setText(currentNote.noteDescription)
        binding.editNoteFab.setOnClickListener {
            val noteTitle = binding.editNoteTitle.text.toString().trim()
            val noteDesc = binding.editNoteDesc.text.toString().trim()

            if(noteTitle.isNotEmpty()){
                val note =Note(currentNote.id,noteTitle,noteDesc)
                notesViewModel.UpdateNote(note)
                view.findNavController().popBackStack(R.id.homeFragment , false)
            }else{
                Toast.makeText(context , "nhap gi di" , Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun deleteNote(){
        AlertDialog.Builder(requireActivity()).apply {
              setTitle("Delete Note")
              setMessage("Do you want to delete this note ?")
              setPositiveButton("Delete"){_,_->
                  notesViewModel.DeleteNote(currentNote)
                  Toast.makeText(context , "Xoa thanh cong! ", Toast.LENGTH_SHORT).show()
                  view?.findNavController()?.popBackStack(R.id.homeFragment , false)
              }
            setNegativeButton("Cancel" , null)
        }.create().show()
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_edit_note,menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return  when(menuItem.itemId){
            R.id.deleteMenu->{
                deleteNote()
                true
            }else->false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        editNoteBinding =null
    }

}