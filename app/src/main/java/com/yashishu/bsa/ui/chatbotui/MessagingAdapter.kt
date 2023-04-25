package com.codepalace.chatbot.ui

import android.annotation.SuppressLint
import android.app.Notification
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yashishu.bsa.R

class MessagingAdapter: RecyclerView.Adapter<MessagingAdapter.MessageViewHolder>() {

    var messagesList = mutableListOf<Notification.MessagingStyle.Message>()

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {

                //Remove message on the item clicked
                messagesList.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val currentMessage = messagesList[position]

//        when (currentMessage.id) {
//            SEND_ID -> {
//                holder.itemView.tv_message.apply {
//                    text = currentMessage.message
//                    visibility = View.VISIBLE
//                }
//                holder.itemView.tv_bot_message.visibility = View.GONE
//            }
//            RECEIVE_ID -> {
//                holder.itemView.tv_bot_message.apply {
//                    text = currentMessage.message
//                    visibility = View.VISIBLE
//                }
//                holder.itemView.tv_message.visibility = View.GONE
//            }
//        }
    }

    fun insertMessage(message: Notification.MessagingStyle.Message) {
        this.messagesList.add(message)
        notifyItemInserted(messagesList.size)
    }

}