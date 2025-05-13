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
import com.example.retrofitwrooom.databinding.FragmentShareBinding
import com.example.retrofitwrooom.model.doctor


class shareFragment : Fragment() {
    private var _binding: FragmentShareBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentShareBinding.inflate(inflater, container, false)
        val doctor = arguments?.getParcelable<doctor>("doctor")

        Glide.with(binding.imageCharacter.context)
            .load(doctor?.image)
            .placeholder(R.drawable.khanhxingai)
            .error(R.drawable.doctor)
            .into(binding.imageCharacter)

        binding.buttonShare.setOnClickListener {
            val shareText = """
        Bác sĩ: ${doctor?.fullName}
        Khoa: ${doctor?.department?.name}
        SĐT: ${doctor?.phoneNumber}
    """.trimIndent()

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_SUBJECT, "Thông tin bác sĩ")
                putExtra(Intent.EXTRA_TEXT, shareText)
            }
            startActivity(Intent.createChooser(intent, "Chia sẻ thông tin bác sĩ qua:"))
        }

        return binding.root
    }
}