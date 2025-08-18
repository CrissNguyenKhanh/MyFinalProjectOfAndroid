package com.example.retrofitwrooom.ui.fragments.doctor

import android.animation.ValueAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.databinding.FragmentBenhNhanStaticsBinding
import com.example.retrofitwrooom.databinding.FragmentDoctorStaticBinding
import com.example.retrofitwrooom.repository.DoctorRepository
import com.example.retrofitwrooom.repository.benhNhanRepository
import com.example.retrofitwrooom.viewModel.benhNhanViewModel
import com.example.retrofitwrooom.viewModel.doctorViewModel
import com.example.retrofitwrooom.viewModel.viewModelFactory.benhNhanViewmodelFactory
import com.example.retrofitwrooom.viewModel.viewModelFactory.doctorViewmodelFactory


class DoctorStaticFragment : Fragment() {
    private lateinit var viewModel: doctorViewModel
    private var _binding: FragmentDoctorStaticBinding? = null
    private val binding get() = _binding!!

    private var countBrain = 0
    private var countTeeth = 0
    private var countHeart = 0
    private var countLung = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoctorStaticBinding.inflate(inflater, container, false)

        val vmFactory = doctorViewmodelFactory(DoctorRepository())
        viewModel = ViewModelProvider(this, vmFactory)[doctorViewModel::class.java]

        // Observer: Total
        viewModel.allDocor.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                val count = response.body()?.size ?: 0
                binding.tvTotalDoctorsTitle.text = "Total Doctors in Hospital: $count"
            }
        }

        // Observer từng chuyên khoa
        viewModel.responseDataKhoanao.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                countBrain = response.body()?.size ?: 0
                binding.chartBrain.findViewById<TextView>(R.id.brainCount)?.text = "$countBrain"
                updateAllHeights()
            }
        }

        viewModel.responseDataKhoarang.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                countTeeth = response.body()?.size ?: 0
                binding.chartTeeth.findViewById<TextView>(R.id.teethCount)?.text = "$countTeeth"
                updateAllHeights()
            }
        }

        viewModel.responseDataKhoatim.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                countHeart = response.body()?.size ?: 0
                binding.chartHeart.findViewById<TextView>(R.id.heartCount)?.text = "$countHeart"
                updateAllHeights()
            }
        }

        viewModel.responseDataKhoaphoi.observe(viewLifecycleOwner) { response ->
            if (response.isSuccessful) {
                countLung = response.body()?.size ?: 0
                binding.chartLung.findViewById<TextView>(R.id.lungCount)?.text = "$countLung"
                updateAllHeights()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getPostItemDoctor()
        viewModel.getPostItemnao()
        viewModel.getPostItemtim()
        viewModel.getPostItemRang()
        viewModel.getPostItemphoi()
    }

    private fun updateAllHeights() {

        val maxCount = listOf(countBrain, countTeeth, countHeart, countLung).maxOrNull() ?: 1
        setColumnHeight(binding.chartBrain.findViewById(R.id.brainGraph), countBrain, maxCount)
        setColumnHeight(binding.chartTeeth.findViewById(R.id.teethGraph), countTeeth, maxCount)
        setColumnHeight(binding.chartHeart.findViewById(R.id.HeartGrapph), countHeart, maxCount)
        setColumnHeight(binding.chartLung.findViewById(R.id.lungGraph), countLung, maxCount)
    }

    private fun setColumnHeight(view: View, count: Int, maxCount: Int) {
        val maxHeightPx = 300
        val newHeight = if (maxCount > 0)
            (count.toFloat() / maxCount * maxHeightPx).toInt().coerceAtLeast(30)
        else 30

        val startHeight = view.height.takeIf { it > 0 } ?: 30

        val animator = ValueAnimator.ofInt(startHeight, newHeight)
        animator.duration = 500 // thời gian 0.5s
        animator.addUpdateListener { animation ->
            val value = animation.animatedValue as Int
            val layoutParams = view.layoutParams
            layoutParams.height = value
            view.layoutParams = layoutParams
        }
        animator.start()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
