package com.skedgo.network

data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): Resource<T> = Resource(status = Status.SUCCESS, data = data, message = null)

        fun <T> error(data: T?, message: String): Resource<T> =
                Resource(status = Status.ERROR, data = data, message = message)

        fun <T> loading(data: T?): Resource<T> = Resource(status = Status.LOADING, data = data, message = null)
    }
}

suspend fun <T> Resource<T>.handleResponse(
    onError: (suspend (Resource<T>) -> Unit)? = null,
    onLoading: (suspend (Resource<T>) -> Unit)? = null,
    onSuccess: suspend (Resource<T>) -> Unit,
) {
    when (status) {
        Status.SUCCESS -> {
            onSuccess.invoke(this)
        }
        Status.ERROR -> {
            onError?.invoke(this)
        }
        Status.LOADING -> {
            onLoading?.invoke(this)
        }
    }
}