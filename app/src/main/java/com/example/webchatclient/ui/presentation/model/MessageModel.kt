package com.example.webchatclient.ui.presentation.model

data class MessageModel(
    val messageId:Int,
    val author: String,
    val timestamp: String,
    val textMessage:String
)
