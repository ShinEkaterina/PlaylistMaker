package com.example.playlistmaker.util

sealed class Resource<T>(val data: T? = null, val errorCode: ErrorCode? = null, val message: String? = null) {
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(errorCode: ErrorCode, message: String, data: T? = null): Resource<T>(data,errorCode, message)
}