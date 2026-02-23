package com.priyanshparekh.ping.addchat

import android.util.Log
import com.google.gson.Gson
import com.priyanshparekh.ping.addchat.dto.AddChatRequest
import com.priyanshparekh.ping.network.ApiResponse
import com.priyanshparekh.ping.network.ErrorResponse
import com.priyanshparekh.ping.network.RetrofitInstance
import com.priyanshparekh.ping.user.User
import com.priyanshparekh.ping.util.TextResponse

class AddChatRepository {

    private val tag = "Add_Chat_Repository"

    suspend fun search(query: String) : ApiResponse<List<User>> {
        return try {
            val response = RetrofitInstance.api.search(query)
            val error = response.errorBody()?.string()
            val code = response.code()
            Log.d(tag, "search: code: $code")

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null) {
                    Log.d(tag, "search: body: ${body.size}")
                    ApiResponse.SUCCESS(body)
                } else {
                    Log.d(tag, "search: Empty Response Object")
                    ApiResponse.ERROR("Empty Response Object")
                }
            } else {
                val error = Gson().fromJson(error, ErrorResponse::class.java)
                Log.d(tag, "search: error: ${error.message}")
                ApiResponse.ERROR(error.message)
            }
        } catch (ex: Exception) {
            Log.d(tag, "search: ex: ${ex.message}")
            ApiResponse.ERROR(ex.message.toString())
        }
    }

    suspend fun addNewChat(addChatRequest: AddChatRequest): ApiResponse<TextResponse> {
        return try {
            val response = RetrofitInstance.api.addChat(addChatRequest)
            val error = response.errorBody()?.string()
            val code = response.code()
            Log.d(tag, "addNewChat: code: $code")

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null) {
                    ApiResponse.SUCCESS(body)
                } else {
                    ApiResponse.ERROR("Empty Response Object")
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