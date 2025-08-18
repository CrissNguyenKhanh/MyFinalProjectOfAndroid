package com.example.retrofitwrooom.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.databinding.FragmentLogin2Binding
import com.example.retrofitwrooom.databinding.FragmentMainBinding
import com.example.retrofitwrooom.model.doctor
import com.example.retrofitwrooom.repository.appointmentRepository
import com.example.retrofitwrooom.viewModel.appointmentViewmodel
import com.example.retrofitwrooom.viewModel.doctorViewModel
import com.example.retrofitwrooom.viewModel.viewModelFactory.appointmentViewmodelFacotry

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class MainFragment : Fragment() {
    private lateinit var doctor_viewmodel: doctorViewModel
    private lateinit var appontment_viewmodel: appointmentViewmodel
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vmFactory = appointmentViewmodelFacotry(appointmentRepository())
        appontment_viewmodel = ViewModelProvider(this, vmFactory)[appointmentViewmodel::class.java]



        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val doctor = arguments?.getParcelable<doctor>("doctor")
        binding.txtNameDoctor.text = doctor?.fullName ?: "David Khanhnguyen"

        Glide.with(binding.imageView3.context)
            .load(doctor?.image)
            .placeholder(R.drawable.doctor)
            .error(R.drawable.doctor)
            .into(binding.imageView3)

        doctor?.let { doc ->
            appontment_viewmodel.getCountDoctorById(doc.id)

            // Lắng nghe kết quả count từ ViewModel
            appontment_viewmodel.responseDataCountDoctorByid.observe(
                viewLifecycleOwner
            ) { response ->
                if (response.isSuccessful) {
                    val count = response.body() ?: 0
                    binding.notificationBadge.text = "$count"
                } else {
                    binding.notificationBadge.text = "0"
                }
            }
        }
        binding.VdCallIcon.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable("doctor", doctor)
            }
            findNavController().navigate(R.id.action_mainFragment_to_benhNhanFragment, bundle)
        }
        binding.NoteIcon.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_nav_graph)
        }
        binding.NotificationIcon.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_headlinesFragment)
        }
        binding.imageTeeth.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_mainDoctorFragment)
        }
        binding.imageView10.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_mainDoctorNaoFragment)
        }
        binding.analytics.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_benhNhanStaticsFragment)
        }
        binding.imageView9.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_doctorPhoiFragment)
        }
        binding.imageView7.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_benhNhanTimFragment)
        }
        binding.edtClickbell.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_notificationFragment)
        }
        binding.analyticsdoctor.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_doctorStaticFragment2)
        }
        val navigationView =
            requireActivity().findViewById<com.google.android.material.navigation.NavigationView>(R.id.navigation_view)
        val drawerLayout =
            requireActivity().findViewById<androidx.drawerlayout.widget.DrawerLayout>(R.id.drawer_layout)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_logout -> {
                    drawerLayout.closeDrawers()
                    findNavController().navigate(R.id.action_mainFragment_to_login2Fragment)
                    true
                }

                else -> false
            }
        }
        val headerView = navigationView.getHeaderView(0)
        val avatarImageView = headerView.findViewById<ImageView>(R.id.imgAvatar)
        val usernameTextView = headerView.findViewById<TextView>(R.id.txtUsername)
        val departmentTextView = headerView.findViewById<TextView>(R.id.txtDepartment)
        usernameTextView.text = doctor?.fullName ?: "David Khanhnguyen"
        departmentTextView.text = doctor?.department?.name ?: "Khoa phổi"

        Glide.with(this)
            .load(doctor?.image)
            .placeholder(R.drawable.doctor)
            .error(R.drawable.doctor)
            .into(avatarImageView)
    }

}