package com.example.webchatclient.ui.presentation.activity

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.webchatclient.R
import com.example.webchatclient.ui.presentation.adapter.ChatsRecyclerAdapter
import com.example.webchatclient.ui.presentation.viewmodel.ChooseChatViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ChooseChatActivity : AppCompatActivity() {

    private lateinit var API_KEY: String

    private lateinit var recyclerView: RecyclerView
    private lateinit var mViewModel: ChooseChatViewModel
    private lateinit var addNewChatFAB: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_chat)
        getExtras()
        findViews()
        initViewModel()
        initObservers()
        initRecyclerView()
        initFAB()
        mViewModel.getChats(API_KEY)
    }

    private fun getExtras() {
        API_KEY = intent.extras?.get(LoginActivity.API_KEY) as String
    }

    private fun findViews() {
        recyclerView = findViewById(R.id.main_recycler)
        addNewChatFAB = findViewById(R.id.new_chat_FAB)
    }

    private fun initViewModel() {
        mViewModel = ViewModelProvider(this)[ChooseChatViewModel::class.java]
    }

    private fun initObservers() {
        mViewModel.getResultOfCreateNewChatLiveData().observe(this){
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            mViewModel.getChats(API_KEY)
        }
    }

    private fun initRecyclerView() {
        val adapter = ChatsRecyclerAdapter {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra(LoginActivity.API_KEY, API_KEY)
            intent.putExtra(ChatActivity.CHAT_ID, it.getTag(R.id.CHAT_ID_VIEW_TAG) as Int)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        mViewModel.getChatsLiveData().observe(this){
            adapter.setItems(it)
        }
    }

    private fun initFAB() {
        addNewChatFAB.setOnClickListener {
            val alert :AlertDialog.Builder= AlertDialog.Builder(this)
            alert.setMessage("Create new chat")
            alert.setTitle("Enter your chat name")
            val edittext =  EditText(this)
            alert.setView(edittext)
            alert.setNeutralButton("Create new chat") { _, _ ->
                mViewModel.createNewChat(API_KEY, edittext.text.toString())
            }
            alert.show()
        }
    }
}