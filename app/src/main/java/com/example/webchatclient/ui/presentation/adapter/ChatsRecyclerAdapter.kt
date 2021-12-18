package com.example.webchatclient.ui.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.webchatclient.R
import com.example.webchatclient.ui.presentation.model.ChatModel

class ChatsRecyclerAdapter(
    private val itemClickListener: View.OnClickListener
    ) : RecyclerView.Adapter<ChatsRecyclerAdapter.ChatsViewHolder>() {

    private var mChatsList:List<ChatModel> = ArrayList<ChatModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chat_recycler_layout, parent, false)
        return ChatsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatsViewHolder, position: Int) {
        holder.onBind(mChatsList[position], itemClickListener)
    }

    override fun getItemCount() = mChatsList.size

    fun setItems(items: List<ChatModel>) {
        mChatsList = items
        notifyDataSetChanged()
    }



    inner class ChatsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val chatNameTV = itemView.findViewById<TextView>(R.id.chat_name)
        private val creatorNameTV = itemView.findViewById<TextView>(R.id.creator_name)

        fun onBind(chatModel: ChatModel, itemClickListener: View.OnClickListener) {
            itemView.setTag(R.id.CHAT_ID_VIEW_TAG, chatModel.chatId)
            chatNameTV.text = chatModel.chatName
            creatorNameTV.text = "Creator : ${chatModel.creatorName}"

            itemView.setOnClickListener{
                itemClickListener.onClick(it)
            }
        }
    }

}