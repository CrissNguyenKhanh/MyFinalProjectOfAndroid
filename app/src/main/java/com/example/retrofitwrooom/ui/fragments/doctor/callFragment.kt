package com.example.retrofitwrooom.ui.fragments.doctor

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.databinding.FragmentCallBinding
import com.example.retrofitwrooom.databinding.FragmentDetailsBinding
import com.example.retrofitwrooom.model.doctor


class callFragment : Fragment() {
    private var _binding: FragmentCallBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        _binding = FragmentCallBinding.inflate(inflater, container, false)
        val doctor = arguments?.getParcelable<doctor>("doctor")

        Glide.with(binding.imageView19.context)
            .load(doctor?.image)
            .placeholder(R.drawable.khanhxingai)
            .error(R.drawable.doctor)
            .into(binding.imageView19)

        binding.button.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:${doctor?.phoneNumber}")
            startActivity(intent)
        }

        return binding.root
    }


}