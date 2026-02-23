package com.priyanshparekh.ping.chat

import android.util.Log
import com.google.gson.Gson
import com.priyanshparekh.ping.Ping.Companion.dataStoreManager
import com.priyanshparekh.ping.network.ApiResponse
import com.priyanshparekh.ping.network.ErrorResponse
import com.priyanshparekh.ping.network.RetrofitInstance
import kotlinx.coroutines.flow.first

class ChatRepository {

    private val tag = "Chat_Repository"

    suspend fun getMessages(chatId: Long): ApiResponse<List<ChatMessageUi>> {
        return try {
            val response = RetrofitInstance.api.getMessages(chatId)
            val code = response.code()
            val error = response.errorBody()?.string()
            Log.d(tag, "getMessages: code: $code")

            if (response.isSuccessful) {
                val body = response.body()
                Log.d(tag, "getMessages: body: $body")

                if (body != null) {
                    ApiResponse.SUCCESS(body.messageList.map { message ->

                        val currentUserId = dataStoreManager.getUserId().first()

                        return@map ChatMessageUi(
                            chatMessage = message.message,
                            isOutgoing = message.senderId == currentUserId,
                            chatMessageStatus = message.chatMessageStatus)
                    })
                } else {
                    ApiResponse.ERROR("Empty response body")
                }
            } else {
                val errorMessage = Gson().fromJson(error, ErrorResponse::class.java)
                ApiResponse.ERROR(errorMessage.message)
            }
        } catch (ex: Exception) {
            ApiResponse.ERROR(ex.message.toString())
        }
    }

}