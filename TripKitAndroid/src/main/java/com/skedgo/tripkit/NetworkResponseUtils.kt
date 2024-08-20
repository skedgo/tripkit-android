package com.skedgo.tripkit

import com.haroldadmin.cnradapter.NetworkResponse
import java.io.IOException

fun <T : Any, U : Any> NetworkResponse<T, U>.toException(): Exception {
    return when (this) {
        is NetworkResponse.ServerError -> IOException("Error: Network response ${this.code}")
        is NetworkResponse.Success -> RuntimeException("You should not see this exception, it is only for completeness. ${this.code}")
        is NetworkResponse.NetworkError -> this.error
        is NetworkResponse.UnknownError -> RuntimeException("Unknown error")
    }
}