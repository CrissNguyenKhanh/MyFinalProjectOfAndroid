package com.example.retrofitwrooom.ui.fragments.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.database.noteDatabase
import com.example.retrofitwrooom.databinding.FragmentNoteHostBinding
import com.example.retrofitwrooom.repository.noteRepository
import com.example.retrofitwrooom.viewModel.note.noteViewModel
import com.example.retrofitwrooom.viewModel.note.noteViewmodelProvider

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class NoteHostFragment : Fragment() {

    private lateinit var noteViewModel: noteViewModel
    private var _binding: FragmentNoteHostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteHostBinding.inflate(inflater, container, false)

        // ViewModel setup
        val repository = noteRepository(noteDatabase(requireContext()))
        val factory = noteViewmodelProvider(requireActivity().application, repository)
        noteViewModel = ViewModelProvider(this, factory)[noteViewModel::class.java]

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: Setup RecyclerView, Observers, etc.
    }
}