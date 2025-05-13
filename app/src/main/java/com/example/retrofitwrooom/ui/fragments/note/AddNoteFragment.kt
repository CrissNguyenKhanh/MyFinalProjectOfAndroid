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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.adapter.NoteAdapater
import com.example.retrofitwrooom.database.noteDatabase
import com.example.retrofitwrooom.databinding.FragmentAddNoteBinding
import com.example.retrofitwrooom.databinding.FragmentHomeBinding
import com.example.retrofitwrooom.model.note.Note
import com.example.retrofitwrooom.repository.noteRepository
import com.example.retrofitwrooom.viewModel.note.noteViewModel
import com.example.retrofitwrooom.viewModel.note.noteViewmodelProvider

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddNoteFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddNoteFragment : Fragment(R.layout.fragment_add_note) , MenuProvider {

    private var addNoteBinding: FragmentAddNoteBinding? = null
    private val binding get() = addNoteBinding!!

    private lateinit var notesViewModel: noteViewModel
    private lateinit var addNoteView: View
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        addNoteBinding = FragmentAddNoteBinding.inflate(inflater, container, false)
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
        addNoteView = view


    }

    private  fun saveNote(view :View){
        val noteTitle = binding.addNoteTitle.text.toString().trim()
        val noteDesc = binding.addNoteDesc.text.toString().trim()

        if(noteTitle.isNotEmpty()){
            val note = Note(0,noteTitle , noteDesc)
            notesViewModel.addNote(note)

            Toast.makeText(addNoteView.context , "add thanh cong" ,Toast.LENGTH_SHORT).show()
            view.findNavController().popBackStack(R.id.homeFragment, false)
        }else{
            Toast.makeText(addNoteView.context , "add thaat bai" ,Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_add_note,menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return  when(menuItem.itemId){
            R.id.saveMenu ->{
                saveNote(addNoteView)
                true
            } else ->false
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        addNoteBinding = null
    }


}