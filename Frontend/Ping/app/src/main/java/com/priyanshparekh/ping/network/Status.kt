package com.priyanshparekh.ping.network

sealed class Status<T> {

    class INITIAL<T>(): Status<T>()
    class SUCCESS<T>(val data: T): Status<T>()
    class ERROR<T>(val message: String): Status<T>()
    class LOADING<T>(): Status<T>()

}