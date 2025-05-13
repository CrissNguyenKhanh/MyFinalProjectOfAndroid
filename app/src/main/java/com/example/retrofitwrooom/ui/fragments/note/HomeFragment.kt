package com.example.retrofitwrooom.ui.fragments.note

import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem

import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.retrofitwrooom.R

import com.example.retrofitwrooom.adapter.NoteAdapater
import com.example.retrofitwrooom.database.noteDatabase
import com.example.retrofitwrooom.databinding.FragmentHomeBinding
import com.example.retrofitwrooom.model.note.Note
import com.example.retrofitwrooom.repository.noteRepository
import com.example.retrofitwrooom.viewModel.note.noteViewModel
import com.example.retrofitwrooom.viewModel.note.noteViewmodelProvider


class HomeFragment : Fragment(R.layout.fragment_home),
    SearchView.OnQueryTextListener,
    MenuProvider {

    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    private lateinit var notesViewModel: noteViewModel
    private lateinit var noteAdapter: NoteAdapater

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Bọc context với theme mong muốn

        val contextWithTheme = ContextThemeWrapper(requireContext(),  com.google.android.material.R.style.Theme_MaterialComponents_DayNight_DarkActionBar)
        val themedInflater = inflater.cloneInContext(contextWithTheme)
        homeBinding = FragmentHomeBinding.inflate(themedInflater, container, false)


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Tạo menu tìm kiếm
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        (binding.homeToolbar)?.let {
            (requireActivity() as AppCompatActivity).setSupportActionBar(it)
        }
        // Khởi tạo ViewModel
        val repository = noteRepository(noteDatabase(requireActivity().application))
        val factory = noteViewmodelProvider(requireActivity().application, repository)
        notesViewModel = ViewModelProvider(this, factory)[noteViewModel::class.java]
         setupHomeRecycleView()

        binding.addNoteFab.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment)
        }

        // TODO: Setup RecyclerView, Adapter, Observer...
    }

    private fun UpdateUi(note : List<Note>?){
        if(note != null){
            if(note.isNotEmpty()){
                binding.emptyNotesImage.visibility = View.GONE
                binding.homeRecyclerView.visibility = View.VISIBLE
            }else{
                binding.emptyNotesImage.visibility = View.VISIBLE
                binding.homeRecyclerView.visibility = View.GONE
            }
        }
    }

    private fun setupHomeRecycleView(){
        noteAdapter = NoteAdapater()
        binding.homeRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = noteAdapter
        }

        activity?.let {
            notesViewModel.getAllNotes().observe(viewLifecycleOwner){    note->
                noteAdapter.differ.submitList(note)
                UpdateUi(note)
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        homeBinding = null
    }

    // Các hàm của SearchView.OnQueryTextListener và MenuProvider (bạn bổ sung tiếp)
    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if(newText != null){
            searchNote(newText)
        }
        return true
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.home_menu, menu)
        val search = menu.findItem(R.id.searchMenu)
        val menuSerach = search.actionView as SearchView
        menuSerach.isSubmitButtonEnabled= false
        menuSerach.setOnQueryTextListener(this)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }

    private fun searchNote(query: String?){
        val searchQuery = "%$query"
        notesViewModel.searchNote(searchQuery).observe(this){list->
             noteAdapter.differ.submitList(list)
        }
    }
}
