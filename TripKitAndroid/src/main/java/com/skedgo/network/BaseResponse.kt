package com.skedgo.network

data class BaseResponse<T>(
    val errorCode: Int = -1,
    val error: String = "",
    val data: T? = null
)