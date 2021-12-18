package com.example.webchatclient.ui.data.net

import com.example.webchatclient.ui.presentation.model.ChatModel
import com.example.webchatclient.ui.presentation.model.MessageModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

/**
 * Имлементация ходящей и в сеть сущности
 *
 * (здесь вшита логика походов в сеть и конвертации сущностей, в будущем это нужно разделить на отдельные сущности
 * (напр. конвертеры))
 */
class ApiMapperImpl : ApiMapper {

    private val client = OkHttpClient()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaTypeOrNull()

    override fun registerNewUser(login: String, password: String): Single<String> {
        return Single.fromCallable {
            val jsonObject = JSONObject()
            jsonObject.put("login", login)
            jsonObject.put("password", password)

            val jsonBody = jsonObject.toString().toRequestBody(jsonMediaType)

            val request: Request = Request.Builder()
                .url(localURL.plus("register"))
                .post(jsonBody)
                .build()

            val response = client.newCall(request).execute()
            response.body?.string() ?: "Error"
        }
    }

    override fun loginUser(login: String, password: String): Single<Pair<String, String?>> {
        return Single.fromCallable {
            val jsonObject = JSONObject()
            jsonObject.put("login", login)
            jsonObject.put("password", password)

            val jsonPostBody = jsonObject.toString().toRequestBody(jsonMediaType)

            val request: Request = Request.Builder()
                .url(localURL.plus("login"))
                .post(jsonPostBody)
                .build()

            val response = client.newCall(request).execute()
            Pair(
                response.body?.string() ?: "Error",
                response.headers[apiKeyHeader]
            )
        }
    }

    override fun getChats(apiKey: String): Single<List<ChatModel>> {
        return Single.fromCallable {
            val request: Request = Request.Builder()
                .addHeader("x-auth-key", apiKey)
                .url(localURL.plus("chats"))
                .build()

            val response = client.newCall(request).execute()
            val body = response.body?.string()
            val jsonArrayOfChats = JSONArray(body ?: "")

            val resultList = mutableListOf<ChatModel>()
            for (i in 0 until jsonArrayOfChats.length()) {
                val currItem = jsonArrayOfChats.getJSONObject(i)
                resultList.add(
                    ChatModel(
                        currItem["chatId"] as Int,
                        currItem["chatName"] as String,
                        currItem["creatorName"] as String
                    )
                )
            }

            resultList
        }
    }

    override fun createNewChat(apiKey: String, chatName: String): Single<String> {
        return Single.fromCallable {
            val jsonObject = JSONObject()
            jsonObject.put("chatName", chatName)

            val jsonPostBody = jsonObject.toString().toRequestBody(jsonMediaType)

            val request: Request = Request.Builder()
                .addHeader("x-auth-key", apiKey)
                .post(jsonPostBody)
                .url(localURL.plus("chats"))
                .build()

            val response = client.newCall(request).execute()

            response.body?.string() ?: "Error"
        }
    }

    override fun getAllMessagesFromChat(apiKey: String, chatId: Int): Single<List<MessageModel>> {
        return Single.fromCallable {
            val request: Request = Request.Builder()
                .addHeader("x-auth-key", apiKey)
                .url(localURL.plus("chats/").plus(chatId))
                .build()

            val response = client.newCall(request).execute()
            val body = response.body?.string()

            val jsonArrayOfChats = JSONArray(body ?: "")

            val resultList = mutableListOf<MessageModel>()
            for (i in 0 until jsonArrayOfChats.length()) {
                val currItem = jsonArrayOfChats.getJSONObject(i)
                resultList.add(
                    MessageModel(
                        currItem["messageID"] as Int,
                        currItem["creatorName"] as String,
                        currItem["timeStamp"] as String,
                        currItem["messageBody"] as String
                    )
                )
            }
            resultList
        }
    }

    override fun getAllMessagesAfterIdFromChat(
        apiKey: String,
        chatId: Int,
        lastMessageId: Int,
    ): Observable<List<MessageModel>> {
        if (lastMessageId == -1) {
            return getAllMessagesFromChat(apiKey,chatId).toObservable()
        } else {
            return Observable.fromCallable {
                val request: Request = Request.Builder()
                    .addHeader("x-auth-key", apiKey)
                    .url(
                        localURL
                            .plus("chats/")
                            .plus("$chatId/")
                            .plus("$lastMessageId")
                    )
                    .build()

                val response = client.newCall(request).execute()
                val body = response.body?.string()

                val jsonArrayOfChats = JSONArray(body ?: "")

                val resultList = mutableListOf<MessageModel>()
                for (i in 0 until jsonArrayOfChats.length()) {
                    val currItem = jsonArrayOfChats.getJSONObject(i)
                    resultList.add(
                        MessageModel(
                            currItem["messageID"] as Int,
                            currItem["creatorName"] as String,
                            currItem["timeStamp"] as String,
                            currItem["messageBody"] as String
                        )
                    )
                }

                resultList
            }
        }
    }

    override fun sendMessageInChat(apiKey: String, chatId: Int, messageBody: String): Single<String> {
        return Single.fromCallable {
            val jsonObject = JSONObject()
            jsonObject.put("messageBody", messageBody)

            val jsonPostBody = jsonObject.toString().toRequestBody(jsonMediaType)

            val request: Request = Request.Builder()
                .addHeader("x-auth-key", apiKey)
                .url(localURL.plus("chats/").plus(chatId))
                .post(jsonPostBody)
                .build()

            val response = client.newCall(request).execute()

            response.body?.string() ?: "Error"
        }
    }

    companion object {

        const val testURL = "https://www.boredapi.com/api/activity"

        const val localURL = "http://10.0.2.2:5000/"
        const val apiKeyHeader = "x-auth-key"
    }
}