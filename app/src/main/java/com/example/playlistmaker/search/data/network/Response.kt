package com.example.playlistmaker.search.data.network

open class Response() {
    var resultCode = NO_RESULT

    companion object {
        const val NO_RESULT = 0
        const val NO_INTERNET = -1
        const val BAD_REQUEST = 400
        const val SUCCESS = 200
        const val ERROR = 500

    }
}