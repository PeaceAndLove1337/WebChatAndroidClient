package com.example.webchatclient.ui.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.webchatclient.R
import com.example.webchatclient.ui.presentation.adapter.MessagesRecyclerAdapter
import com.example.webchatclient.ui.presentation.model.MessageModel
import com.example.webchatclient.ui.presentation.viewmodel.ChatViewModel

class ChatActivity : AppCompatActivity() {

    private lateinit var API_KEY:String

    private lateinit var messageText: EditText
    private lateinit var sendButton: Button

    private lateinit var recyclerView:RecyclerView

    private lateinit var recyclerAdapter: MessagesRecyclerAdapter
    private lateinit var mViewModel: ChatViewModel

    /**
     * Идентификатор чата
     */
    private var currentChatId=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        getExtras()
        findViews()
        initViewModel()
        initRecyclerView()
        initSendButton()

        mViewModel.getAllMessagesFromChat(API_KEY, currentChatId)
        mViewModel.getAllMessagesInChatAfterLastMessage(API_KEY, currentChatId, -1)
    }

    private fun getExtras(){
        currentChatId = intent.extras?.get(CHAT_ID) as Int
        API_KEY = intent.extras?.get(LoginActivity.API_KEY) as String
    }

    private fun initViewModel(){
        mViewModel = ViewModelProvider(this)[ChatViewModel::class.java]
    }

    private fun initObservers(adapter: MessagesRecyclerAdapter){
        mViewModel.getAllMessagesLiveData().observe(this){
            adapter.setItems(it)
            scrollRecyclerToBottom()
        }

        mViewModel.getMessagesAfterLastIdMessageLiveData().observe(this){
            adapter.addMessages(it)

            mViewModel.getAllMessagesInChatAfterLastMessage(API_KEY,
                currentChatId,
                adapter.getLastMessageId()
            )
            scrollRecyclerToBottom()
        }

        mViewModel.getSendResultLiveData().observe(this){
            if (it){
                messageText.setText("")
                mViewModel.getAllMessagesFromChat(API_KEY, currentChatId)
            }else{
                Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun findViews(){
        messageText=findViewById(R.id.edit_text_message_text)
        sendButton=findViewById(R.id.send_button)
        recyclerView=findViewById(R.id.main_recycler)
    }

    private fun initSendButton() {
        sendButton.setOnClickListener {
            mViewModel.sendMessageInChat(API_KEY, currentChatId, messageText.text.toString())
        }
    }

    private fun initRecyclerView(){
        recyclerAdapter = MessagesRecyclerAdapter()
        recyclerView.adapter=recyclerAdapter
        initObservers(recyclerAdapter)
    }

    private fun scrollRecyclerToBottom(){
        recyclerView.scrollToPosition(recyclerAdapter.itemCount-1)
    }


    companion object{
        const val CHAT_ID="CHAT_ID"
    }
}