package com.example.retrofitwrooom.ui.fragments.add

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.retrofitwrooom.databinding.FragmentBenhNhanStaticsBinding
import com.example.retrofitwrooom.repository.benhNhanRepository
import com.example.retrofitwrooom.viewModel.benhNhanViewModel
import com.example.retrofitwrooom.viewModel.viewModelFactory.benhNhanViewmodelFactory
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class BenhNhanStaticsFragment : Fragment() {
    private lateinit var viewModel: benhNhanViewModel
    private var _binding: FragmentBenhNhanStaticsBinding? = null
    private val binding get() = _binding!!
    private var tongBenhNhan: Long = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBenhNhanStaticsBinding.inflate(inflater, container, false)
        val vmFactory = benhNhanViewmodelFactory(benhNhanRepository())
        viewModel = ViewModelProvider(this, vmFactory)[benhNhanViewModel::class.java]

        // Observe LiveData khi fragment được tạo
        viewModel.benhNhanCount.observe(viewLifecycleOwner) { total ->
            binding.tvTotalPatients.text = "Tổng số bệnh nhân: $total"
            tongBenhNhan=total
            setUpPieChart(total)
        }

        viewModel.benhNhanCountDaKham.observe(viewLifecycleOwner) { total ->
            binding.tvDaKham.text = "Tổng số bệnh nhân Da Kham: $total"
            setUpPieChartDaKham(total)
        }


        viewModel.benhNhanCountChuaKham.observe(viewLifecycleOwner) { total ->
            binding.tvChuaKham.text = "Tổng số bệnh nhân Chua Kham: $total"
            setUpPieChartChuakham(total)
        }


        binding.imgbackvip.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Gọi API/lấy dữ liệu tổng số bệnh nhân khi view đã được tạo
        viewModel.getBenhNhanCount()
        viewModel.getBenhNhanCountChuaKham()
        viewModel.getBenhNhanCountDaKham()
    }

    private fun setUpPieChart(total: Long) {
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(total.toFloat(), "Bệnh nhân"))
        entries.add(PieEntry(100f - total.toFloat(), "Còn lại")) // giả định max là 100

        val dataSet = PieDataSet(entries, "")
        dataSet.setColors(Color.GREEN, Color.LTGRAY)

        val pieData = PieData(dataSet)
        val pieChart = binding.pieChart

        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.centerText = "Thống kê"
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.animateY(1000)
        pieChart.invalidate()
    }

    private fun setUpPieChartDaKham(total: Long) {
        if (tongBenhNhan == 0L) return

        val daKham = total.toFloat()
        val conLai = tongBenhNhan.toFloat() - daKham

        val entries = arrayListOf(
            PieEntry(daKham, "Đã khám"),
            PieEntry(conLai, "Khác")
        )

        val dataSet = PieDataSet(entries, "")
        dataSet.setColors(Color.BLUE, Color.LTGRAY)
        val pieData = PieData(dataSet)

        binding.pieChartDaKham.apply {
            data = pieData
            description.isEnabled = false
            centerText = "Đã khám"
            setEntryLabelColor(Color.BLACK)
            animateY(1000)
            invalidate()
        }
    }

    private fun setUpPieChartChuakham(total: Long) {
        if (tongBenhNhan == 0L) return

        val chuaKham = total.toFloat()
        val conLai = tongBenhNhan.toFloat() - chuaKham

        val entries = arrayListOf(
            PieEntry(chuaKham, "Chưa khám"),
            PieEntry(conLai, "Khác")
        )

        val dataSet = PieDataSet(entries, "")
        dataSet.setColors(Color.RED, Color.LTGRAY)
        val pieData = PieData(dataSet)

        binding.pieChartChuaKham.apply {
            data = pieData
            description.isEnabled = false
            centerText = "Chưa khám"
            setEntryLabelColor(Color.BLACK)
            animateY(1000)
            invalidate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onResume() {
        super.onResume()
        // Gọi lại các API để cập nhật dữ liệu mới nhất
        viewModel.getBenhNhanCount()
        viewModel.getBenhNhanCountChuaKham()
        viewModel.getBenhNhanCountDaKham()
    }
}
