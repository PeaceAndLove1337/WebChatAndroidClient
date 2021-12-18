package com.example.webchatclient.ui.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.webchatclient.ui.data.net.ApiMapperImpl
import com.example.webchatclient.ui.presentation.model.MessageModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class ChatViewModel : ViewModel() {
    private val apiMapper = ApiMapperImpl()

    private val allMessagesLiveData = MutableLiveData<List<MessageModel>>()
    private val messagesAfterLastIdMessageLiveData = MutableLiveData<List<MessageModel>>()
    private val sendResultLiveData = MutableLiveData<Boolean>()

    fun getAllMessagesFromChat(apiKey: String, chatId: Int) {
        apiMapper.getAllMessagesFromChat(apiKey, chatId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    allMessagesLiveData.value = it
                },
                {
                    allMessagesLiveData.value = emptyList()
                })

    }

    fun getAllMessagesInChatAfterLastMessage(
        apiKey: String,
        chatId: Int,
        lastMessageId: Int){
            apiMapper.getAllMessagesAfterIdFromChat(apiKey, chatId, lastMessageId)
                .delay(2000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        messagesAfterLastIdMessageLiveData.value = it
                    },
                    {
                        messagesAfterLastIdMessageLiveData.value = emptyList()
                    })
    }

    fun sendMessageInChat(apiKey: String, chatId: Int, messageBody: String) {
        apiMapper.sendMessageInChat(apiKey, chatId, messageBody)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    sendResultLiveData.value = it == "Message was send correctly"
                },
                {
                    sendResultLiveData.value = false
                })
    }

    fun getAllMessagesLiveData(): LiveData<List<MessageModel>> {
        return allMessagesLiveData
    }

    fun getSendResultLiveData(): LiveData<Boolean> {
        return sendResultLiveData
    }

    fun getMessagesAfterLastIdMessageLiveData(): LiveData<List<MessageModel>> {
        return messagesAfterLastIdMessageLiveData
    }
}