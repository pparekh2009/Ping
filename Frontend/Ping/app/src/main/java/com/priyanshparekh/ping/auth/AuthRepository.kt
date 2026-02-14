package com.priyanshparekh.ping.auth

import android.util.Log
import com.google.gson.Gson
import com.priyanshparekh.ping.Ping.Companion.dataStoreManager
import com.priyanshparekh.ping.auth.login.dto.UserLoginRequest
import com.priyanshparekh.ping.auth.login.dto.UserLoginResponse
import com.priyanshparekh.ping.auth.signup.dto.UserSignUpRequest
import com.priyanshparekh.ping.auth.signup.dto.UserSignUpResponse
import com.priyanshparekh.ping.network.ApiResponse
import com.priyanshparekh.ping.network.ErrorResponse
import com.priyanshparekh.ping.network.RetrofitInstance

class AuthRepository {


    fun getAccessToken() = dataStoreManager.getAccessToken()


    suspend fun signUp(userSignUpRequest: UserSignUpRequest): ApiResponse<UserSignUpResponse> {
        return try {
            val response = RetrofitInstance.api.signUp(userSignUpRequest)
            val code = response.code()
            val error = response.errorBody()?.string()
            Log.d("TAG", "authRepository: signUp: code: $code")

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null) {
                    dataStoreManager.setAccessToken(body.accessToken)
                    dataStoreManager.setUserId(body.userId)
                    ApiResponse.SUCCESS(body)
                } else {
                    ApiResponse.ERROR("Empty Response Object")
                }
            } else {
                val errorResponse = Gson().fromJson(error, ErrorResponse::class.java)
                ApiResponse.ERROR(errorResponse.message)
            }
        } catch (ex: Exception) {
            ApiResponse.ERROR(ex.message ?: "Network Error")
        }
    }

    suspend fun login(userLoginRequest: UserLoginRequest): ApiResponse<UserLoginResponse> {
        return try {
            val response = RetrofitInstance.api.login(userLoginRequest)
            val code = response.code()
            val error = response.errorBody()?.string()
            Log.d("TAG", "authRepository: login: code: $code")

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null) {
                    dataStoreManager.setAccessToken(body.accessToken)
                    dataStoreManager.setUserId(body.userId)
                    ApiResponse.SUCCESS(body)
                } else {
                    ApiResponse.ERROR("Empty Response Object")
                }
            } else {
                Log.d("TAG", "authRepository: login: error: $error")
                val errorResponse = Gson().fromJson(error, ErrorResponse::class.java)
                ApiResponse.ERROR(errorResponse.message)
            }
        } catch (ex: Exception) {
            ApiResponse.ERROR(ex.message ?: "Network Error")
        }
    }

    suspend fun logout() {
        dataStoreManager.clearAccessToken()
    }

}