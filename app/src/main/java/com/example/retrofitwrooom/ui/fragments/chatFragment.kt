package com.example.retrofitwrooom.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitwrooom.R
import com.example.retrofitwrooom.adapter.ChatAdapter
import com.example.retrofitwrooom.model.ChatMessage


class chatFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var editTextMessage: EditText
    private lateinit var buttonSend: Button
    private lateinit var buttonBack: ImageView
    private lateinit var chatAdapter: ChatAdapter
    private val messages = mutableListOf<ChatMessage>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_chat, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.recyclerViewChat)
        editTextMessage = view.findViewById(R.id.editTextMessage)
        buttonSend = view.findViewById(R.id.buttonSend)
        buttonBack = view.findViewById(R.id.imgchatback)
        chatAdapter = ChatAdapter(messages)
        recyclerView.adapter = chatAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())


        buttonBack.setOnClickListener {
            findNavController().navigate(R.id.action_chatFragment_to_loginbenhnhanFragment)
        }
        buttonSend.setOnClickListener {
            val msg = editTextMessage.text.toString().trim()
            if (msg.isNotEmpty()) {
                messages.add(ChatMessage(msg, true))
                chatAdapter.notifyItemInserted(messages.size - 1)
                recyclerView.scrollToPosition(messages.size - 1)
                editTextMessage.setText("")

                Handler(Looper.getMainLooper()).postDelayed({
                    val aiResponse = getAiResponse(msg)
                    messages.add(ChatMessage("Bác Sĩ: $aiResponse", false))
                    chatAdapter.notifyItemInserted(messages.size - 1)
                    recyclerView.scrollToPosition(messages.size - 1)
                }, 1000)
            }
        }
    }
    private fun getAiResponse(userMessage: String): String {
        return when {
            userMessage.contains("xin chao", ignoreCase = true) -> "Xin chào! Tôi là trợ lý của bác si DavidKhanh, bạn cần hỗ trợ gì?"
            userMessage.contains("dau dau", ignoreCase = true) -> "Bạn có thể uống thuốc paracetamol nếu không dị ứng. Nếu triệu chứng kéo dài, nên đến bác sĩ."
            userMessage.contains("cam on", ignoreCase = true) -> "Rất hân hạnh được hỗ trợ bạn!"
            userMessage.contains("sot", ignoreCase = true) -> "Bạn nên nghỉ ngơi và uống nhiều nước. Nếu sốt cao trên 39 độ, nên đi khám ngay."
            userMessage.contains("ho", ignoreCase = true) -> "Bạn có thể sử dụng siro ho hoặc uống nước ấm. Nếu ho kéo dài hơn 1 tuần, hãy đi khám bác sĩ."
            userMessage.contains("met moi", ignoreCase = true) -> "Mệt mỏi có thể do nhiều nguyên nhân, bạn nên ăn uống đầy đủ và nghỉ ngơi hợp lý."
            userMessage.contains("dau bung", ignoreCase = true) -> "Bạn có thể thử ăn nhẹ và tránh thức ăn khó tiêu. Nếu đau dữ dội, cần đi khám sớm."
            userMessage.contains("bệnh tiểu đường", ignoreCase = true) -> "Bệnh tiểu đường cần kiểm soát đường huyết chặt chẽ và tuân thủ chế độ ăn uống, thuốc theo bác sĩ."
            else -> "Tôi chưa hiểu rõ ý bạn, bạn có thể nói rõ hơn không?"
        }
    }
}
