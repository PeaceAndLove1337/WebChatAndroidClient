package com.example.webchatclient.ui.data.net

import com.example.webchatclient.ui.presentation.model.ChatModel
import com.example.webchatclient.ui.presentation.model.MessageModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

/**
 * Интерфейс сущности ходящей в сеть
 */
interface ApiMapper {
    /**
     * Зарегистрировать нового пользователя
     * @param login логин пользователя
     * @param password пароль пользователя
     * @return строка-результат запроса
     */
    fun registerNewUser(login:String, password:String): Single<String>

    /**
     * Залогинить пользователя
     * @param login логин пользователя
     * @param password пароль пользователя
     * @return пару строка-результат запроса - строка - API-токен
     */
    fun loginUser(login:String, password:String): Single<Pair<String, String?>>

    /**
     * Получить список чатов
     * @param apiKey API-токен
     * @return список чатов
     */
    fun getChats(apiKey:String):Single<List<ChatModel>>


    /**
     * Создать новый чат
     * @param apiKey API-токен
     * @param chatName название чата
     * @return строка-результат запроса
     */
    fun createNewChat(apiKey:String, chatName:String):Single<String>


    /**
     * Получить все сообщения из заданного чата
     * @param apiKey API-токен
     * @param chatId идентификатор чата
     * @return список сообщений
     */
    fun getAllMessagesFromChat(apiKey:String, chatId:Int):Single<List<MessageModel>>


    /**
     * Получить все сообщения после сообщения с заданным идентификатором из заданного чата
     * @param apiKey API-токен
     * @param chatId идентификатор чата
     * @return список сообщений после сообщения с заданным идентификатором
     */
    fun getAllMessagesAfterIdFromChat(apiKey:String,chatId:Int, lastMessageId:Int):Observable<List<MessageModel>>

    /**
     * Отправить сообщение в чат
     * @param apiKey API-токен
     * @param chatId идентификатор чата
     * @return результат отправки
     */
    fun sendMessageInChat(apiKey:String,chatId:Int, messageBody:String):Single<String>
}