package com.skedgo.tripkit.utils.async

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: String) : Result<Nothing>()
    object Loading : Result<Nothing>()

    companion object {
        fun <T> success(data: T) = Success(data)
        fun error(exception: String) = Error(exception)
        fun loading() = Loading
    }
}