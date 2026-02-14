package com.priyanshparekh.ping.network;

sealed class ApiResponse<T> {

    class SUCCESS<T>(val data: T): ApiResponse<T>()
    class ERROR<T>(val message: String): ApiResponse<T>()

}
