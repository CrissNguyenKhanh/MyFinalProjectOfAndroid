package com.example.retrofitwrooom.ui.fragments.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.databinding.FragmentMainBinding
import com.example.retrofitwrooom.databinding.FragmentNotificationByMeBinding
import com.example.retrofitwrooom.model.doctor
import com.example.retrofitwrooom.repository.appointmentRepository
import com.example.retrofitwrooom.viewModel.appointmentViewmodel
import com.example.retrofitwrooom.viewModel.viewModelFactory.appointmentViewmodelFacotry

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [notificationByMeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class notificationByMeFragment : Fragment() {
    private lateinit var appontment_viewmodel: appointmentViewmodel
    private var _binding: FragmentNotificationByMeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vmFactory = appointmentViewmodelFacotry(appointmentRepository())
        appontment_viewmodel = ViewModelProvider(this, vmFactory)[appointmentViewmodel::class.java]

        _binding = FragmentNotificationByMeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val doctor = arguments?.getParcelable<doctor>("doctor")

        doctor?.let { doc ->
            appontment_viewmodel.getCountDoctorById(doc.id)

            // Lắng nghe kết quả count từ ViewModel
            appontment_viewmodel.responseDataCountDoctorByid.observe(
                viewLifecycleOwner
            ) { response ->
                if (response.isSuccessful) {
                    val count = response.body() ?: 0
                    binding.badgeText.text = "$count"
                } else {
                    binding.badgeText.text = "0"
                }
            }
        }

        binding.btnNotification.setOnClickListener {
            val bundle = Bundle().apply {
                putParcelable("doctor", doctor)
            }
            findNavController().navigate(
                R.id.action_notificationByMeFragment_to_mailFragment,
                bundle
            )
        }
        super.onViewCreated(view, savedInstanceState)
    }

}