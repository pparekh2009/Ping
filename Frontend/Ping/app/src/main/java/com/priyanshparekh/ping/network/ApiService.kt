package com.priyanshparekh.ping.network

import com.priyanshparekh.ping.auth.login.dto.UserLoginRequest
import com.priyanshparekh.ping.auth.login.dto.UserLoginResponse
import com.priyanshparekh.ping.auth.signup.dto.UserSignUpRequest
import com.priyanshparekh.ping.auth.signup.dto.UserSignUpResponse
import com.priyanshparekh.ping.addchat.dto.AddChatRequest
import com.priyanshparekh.ping.chat.ChatMessageListResponse
import com.priyanshparekh.ping.chatlist.dto.ChatListResponse
import com.priyanshparekh.ping.user.User
import com.priyanshparekh.ping.util.TextResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("auth/signup")
    suspend fun signUp(@Body userSignUpRequest: UserSignUpRequest): Response<UserSignUpResponse>

    @POST("auth/login")
    suspend fun login(@Body userLoginRequest: UserLoginRequest): Response<UserLoginResponse>


    @GET("search")
    suspend fun search(@Query("query") query: String): Response<List<User>>

    @POST("chat")
    suspend fun addChat(@Body addChatRequest: AddChatRequest): Response<TextResponse>

    @GET("chats")
    suspend fun getChats(): Response<ChatListResponse>

    @GET("messages")
    suspend fun getMessages(@Query("chat_id") chatId: Long): Response<ChatMessageListResponse>
}