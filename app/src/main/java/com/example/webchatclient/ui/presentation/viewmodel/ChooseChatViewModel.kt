package com.example.webchatclient.ui.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.webchatclient.ui.data.net.ApiMapperImpl
import com.example.webchatclient.ui.presentation.model.ChatModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class ChooseChatViewModel: ViewModel() {
    private val apiMapper = ApiMapperImpl()

    private val chatsLiveData = MutableLiveData<List<ChatModel>>()
    private val resultOfCreateNewChatLiveData = MutableLiveData<String>()

    fun getChats(apiKey:String){
        apiMapper.getChats(apiKey)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    chatsLiveData.value=it
                },
                {
                    chatsLiveData.value= emptyList()
                })
    }

    fun createNewChat(apiKey:String, chatName:String){
        apiMapper.createNewChat(apiKey, chatName)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    resultOfCreateNewChatLiveData.value=it
                },
                {
                    resultOfCreateNewChatLiveData.value="Server is not responding"
                })
    }

     fun getChatsLiveData():LiveData<List<ChatModel>>{
        return chatsLiveData
    }

    fun getResultOfCreateNewChatLiveData():LiveData<String>{
        return resultOfCreateNewChatLiveData
    }
}