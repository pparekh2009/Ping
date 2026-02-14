package com.priyanshparekh.ping.network

data class ErrorResponse(
    val code: Int,
    val message: String
) {
    override fun toString(): String {
        return "Code: $code; Message: $message"
    }
}
