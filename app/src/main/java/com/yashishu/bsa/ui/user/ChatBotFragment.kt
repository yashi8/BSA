package com.yashishu.bsa.ui.user

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import androidx.fragment.app.Fragment

import androidx.recyclerview.widget.LinearLayoutManager

import com.yashishu.bsa.databinding.FragmentChatBotBinding
import com.yashishu.bsa.ui.data.Message
import com.yashishu.bsa.ui.utils.BotResponse
import com.yashishu.bsa.ui.utils.Constants.OPEN_GOOGLE
import com.yashishu.bsa.ui.utils.Constants.OPEN_SEARCH
import com.yashishu.bsa.ui.utils.Constants.RECEIVE_ID
import com.yashishu.bsa.ui.utils.Constants.SEND_ID
import com.yashishu.bsa.ui.utils.Time
import com.yashishu.ui.chatbotui.MessagingAdapter
import kotlinx.coroutines.*




class ChatBotFragment : Fragment() {
    private var _binding: FragmentChatBotBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ChatBotFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding=FragmentChatBotBinding.inflate(inflater,container,false)
        return binding.root
    }


    var messagesList = mutableListOf<Message>()

    private lateinit var adapter: MessagingAdapter
    private val botList = listOf("Botanica", "BSA", "Guider", "Igor")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // setContentView(R.layout.fragment_chat_bot)

        _binding?.apply {

         //   recyclerView()
            adapter = MessagingAdapter()
            rvMessages.adapter = adapter
            val applicationContext=null
            rvMessages.layoutManager = LinearLayoutManager(applicationContext)

          //  clickEvents()

            btnSend.setOnClickListener {
              //  sendMessage()

                val message = etMessage.text.toString()
                val timeStamp = Time.timeStamp()

                if (message.isNotEmpty()) {
                    //Adds it to our local list
                    messagesList.add(Message(message, SEND_ID, timeStamp))
                    etMessage.setText("")

                    adapter.insertMessage(Message(message, SEND_ID, timeStamp))
                    rvMessages.scrollToPosition(adapter.itemCount - 1)

                    botResponse(message)
                }
            }

            //Scroll back to correct position when user clicks on text view
            etMessage.setOnClickListener {
                GlobalScope.launch {
                    delay(100)

                    withContext(Dispatchers.Main) {
                        rvMessages.scrollToPosition(adapter.itemCount - 1)

                    }
                }
            }



            val random = (0..3).random()
            customBotMessage("Hello! Today you're speaking with ${botList[random]}, how may I help?")
        }
    }

    private fun clickEvents() {

        //Send a message

    }

    private fun recyclerView() {

    }

    override fun onStart() {
        super.onStart()
        _binding?.apply {
            //In case there are messages, scroll to bottom when re-opening app
            GlobalScope.launch {
                delay(100)
                withContext(Dispatchers.Main) {
                    rvMessages.scrollToPosition(adapter.itemCount - 1)
                }
            }
        }
    }

    private fun sendMessage() {

    }

    private fun botResponse(message: String) {
        val timeStamp = Time.timeStamp()
        _binding?.apply {
            GlobalScope.launch {
                //Fake response delay
                delay(1000)

                withContext(Dispatchers.Main) {
                    //Gets the response
                    val response = BotResponse.basicResponses(message)

                    //Adds it to our local list
                    messagesList.add(Message(response, RECEIVE_ID, timeStamp))

                    //Inserts our message into the adapter
                    adapter.insertMessage(Message(response, RECEIVE_ID, timeStamp))

                    //Scrolls us to the position of the latest message
                    rvMessages.scrollToPosition(adapter.itemCount - 1)

                    //Starts Google
                    when (response) {
                        OPEN_GOOGLE -> {
                            val site = Intent(Intent.ACTION_VIEW)
                            site.data = Uri.parse("https://www.google.com/")
                            startActivity(site)
                        }
                        OPEN_SEARCH -> {
                            val site = Intent(Intent.ACTION_VIEW)
                            val searchTerm: String? = message.substringAfterLast("search")
                            site.data = Uri.parse("https://www.google.com/search?&q=$searchTerm")
                            startActivity(site)
                        }

                    }
                }
            }
        }
    }

    private fun customBotMessage(message: String) {
        _binding?.apply {
            GlobalScope.launch {
                delay(1000)
                withContext(Dispatchers.Main) {
                    val timeStamp = Time.timeStamp()
                    messagesList.add(Message(message, RECEIVE_ID, timeStamp))
                    adapter.insertMessage(Message(message, RECEIVE_ID, timeStamp))

                    rvMessages.scrollToPosition(adapter.itemCount - 1)
                }
            }
        }
    }
}