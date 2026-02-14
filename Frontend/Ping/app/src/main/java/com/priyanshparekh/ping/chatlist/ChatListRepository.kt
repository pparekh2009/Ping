package com.priyanshparekh.ping.chatlist

import android.util.Log
import com.google.gson.Gson
import com.priyanshparekh.ping.chatlist.dto.GetChatResponse
import com.priyanshparekh.ping.network.ApiResponse
import com.priyanshparekh.ping.network.ErrorResponse
import com.priyanshparekh.ping.network.RetrofitInstance

class ChatListRepository {

    suspend fun getChatList(): ApiResponse<List<GetChatResponse>> {
        return try {
            val response = RetrofitInstance.api.getChats()
            val error = response.errorBody()?.string()
            val code = response.code()
            Log.d("TAG", "chatListRepository: getChatList: code: $code")

            if (response.isSuccessful) {
                val body = response.body()
                Log.d("TAG", "chatListRepository: getChatList: body: $body")

                if (body != null) {
                    ApiResponse.SUCCESS(body)
                } else {
                    ApiResponse.ERROR("Empty Response Body")
                }
            } else {
                val error = Gson().fromJson(error, ErrorResponse::class.java)
                ApiResponse.ERROR(error.message)
            }
        } catch (ex: Exception) {
            ApiResponse.ERROR(ex.message.toString())
        }
    }

}