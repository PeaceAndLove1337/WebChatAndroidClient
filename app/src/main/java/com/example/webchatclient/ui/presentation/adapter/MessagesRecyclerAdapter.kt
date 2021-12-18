package com.example.webchatclient.ui.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.webchatclient.R
import com.example.webchatclient.ui.presentation.model.MessageModel

class MessagesRecyclerAdapter : RecyclerView.Adapter<MessagesRecyclerAdapter.MessagesViewHolder>() {

    private var mMessageList:MutableList<MessageModel> = ArrayList()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.message_recycler_layout, parent, false)
        return MessagesViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessagesViewHolder, position: Int) {
        holder.onBind(mMessageList[position])
    }

    override fun getItemCount() =mMessageList.size

     fun setItems(items:List<MessageModel>){
         mMessageList = items.toMutableList()
         notifyDataSetChanged()
    }

    fun getLastMessageId()= mMessageList.lastOrNull()?.messageId ?: -1

    fun addMessages(items:List<MessageModel>){
        mMessageList.addAll(items)
        notifyDataSetChanged()
    }

    inner class MessagesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val timestampTV = itemView.findViewById<TextView>(R.id.timestamp_text_view)
        private val authorTV = itemView.findViewById<TextView>(R.id.author_text_view)
        private val messageTV = itemView.findViewById<TextView>(R.id.text_message_text_view)

        fun onBind(messageModel: MessageModel){
            timestampTV.text=messageModel.timestamp
            authorTV.text=messageModel.author
            messageTV.text=messageModel.textMessage

        }

    }
}