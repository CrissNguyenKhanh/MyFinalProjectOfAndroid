package com.example.retrofitwrooom.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.databinding.FragmentLogin2Binding
import com.example.retrofitwrooom.databinding.FragmentStartBinding


class startFragment : Fragment() {

    private var _binding: FragmentStartBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentStartBinding.inflate(inflater, container, false)

        binding.button2.setOnClickListener {
            findNavController().navigate(R.id.action_startFragment_to_login2Fragment)
        }
        return binding.root
    }


}