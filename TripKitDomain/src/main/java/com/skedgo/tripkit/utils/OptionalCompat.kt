package com.skedgo.tripkit.utils

class OptionalCompat<T> private constructor(private val value: T?) {

    companion object {
        fun <T> ofNullable(value: T?): OptionalCompat<T> = OptionalCompat(value)
        fun <T> empty(): OptionalCompat<T> = OptionalCompat(null)
    }

    fun isPresent(): Boolean = value != null

    fun get(): T = value ?: throw NoSuchElementException("No value present")

    fun orElse(other: T): T = value ?: other
}
