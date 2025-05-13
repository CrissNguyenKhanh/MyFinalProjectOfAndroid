package com.example.retrofitwrooom.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.retrofitwrooom.model.request.benhAnRequest
import com.example.retrofitwrooom.repository.benhAnRepository
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

class benhanViewModel(val benhanhRepository: benhAnRepository):ViewModel() {


     fun addBenhAn(benhAnRequest: benhAnRequest){
         viewModelScope.launch {
             try {
                  val khanhadd = benhanhRepository.addBenhAn(benhAnRequest)
                  if(khanhadd.isSuccessful){
                      Log.d("benhAn" , "Addd benh an thanhcong")
                  }else{

                      Log.d("benhAn" , "Addd benh an that bai")
                      exitProcess(1) // Dừng chương trình nếu API trả về lỗi

                  }

             }catch(e:Exception){
                  Log.d("benhAn" , "looi he thong ${e.message}")
                 exitProcess(1) // Dừng chương trình nếu API trả về lỗi
             }
         }
     }




}