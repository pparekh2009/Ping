package com.priyanshparekh.ping.chat

import android.util.Log
import com.priyanshparekh.ping.network.ApiResponse
import com.priyanshparekh.ping.network.RetrofitInstance

class ChatRepository {

    suspend fun getMessages(chatId: Long): ApiResponse<List<Message>> {
        return try {
            val response = RetrofitInstance.api.getMessages(chatId)
            val code = response.code()
            val error = response.errorBody()?.string()
            Log.d("TAG", "chatRepository: getMessages: code: $code")

            if (response.isSuccessful) {
                val body = response.body()
                Log.d("TAG", "chatRepository: getMessages: body: $body")

                if (body != null) {
                    ApiResponse.SUCCESS(body.map { message ->
                        Log.d("TAG", "chatRepository: getMessages: message.isSent: ${message.isSent}")
                        return@map Message(message.message, message.isSent)
                    })
                } else {
                    ApiResponse.ERROR("Empty response body")
                }
            } else {
                ApiResponse.ERROR(error.toString())
            }
        } catch (ex: Exception) {
            ApiResponse.ERROR(ex.message.toString())
        }
    }

}