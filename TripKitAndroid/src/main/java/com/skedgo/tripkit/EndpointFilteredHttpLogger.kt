package com.skedgo.tripkit

import okhttp3.logging.HttpLoggingInterceptor
import java.util.Locale

/**
 * Debug-only log printer filter for OkHttp logging.
 * It reduces noise by allowing logs only for configured endpoint fragments (e.g. hides /locations.json),
 * and never changes request/response behavior; it only controls which lines are printed.
 */
class EndpointFilteredHttpLogger(
    allowlist: List<String>,
    private val delegate: HttpLoggingInterceptor.Logger = HttpLoggingInterceptor.Logger.DEFAULT
) : HttpLoggingInterceptor.Logger {

    private val normalizedAllowlist = allowlist
        .map { it.trim() }
        .filter { it.isNotBlank() }

    private val allowAllLogs = normalizedAllowlist.isEmpty()
    private val shouldLogCurrentCall = ThreadLocal<Boolean?>()

    override fun log(message: String) {
        if (allowAllLogs) {
            delegate.log(message)
            return
        }

        when {
            isRequestStart(message) -> {
                val shouldLog = containsAnyAllowlistedPath(message)
                shouldLogCurrentCall.set(shouldLog)
                if (shouldLog) {
                    delegate.log(message)
                }
            }

            isResponseStart(message) -> {
                val shouldLog = shouldLogCurrentCall.get() ?: containsAnyAllowlistedPath(message)
                shouldLogCurrentCall.set(shouldLog)
                if (shouldLog) {
                    delegate.log(message)
                }
            }

            shouldLogCurrentCall.get() == true -> {
                delegate.log(message)
            }
        }

        if (isRequestEnd(message) || isResponseEnd(message)) {
            shouldLogCurrentCall.remove()
        }
    }

    private fun containsAnyAllowlistedPath(message: String): Boolean {
        return normalizedAllowlist.any { fragment ->
            message.contains(fragment, ignoreCase = true)
        }
    }

    private fun isRequestStart(message: String): Boolean {
        val upper = message.uppercase(Locale.US)
        return upper.startsWith("--> GET ") ||
            upper.startsWith("--> POST ") ||
            upper.startsWith("--> PUT ") ||
            upper.startsWith("--> DELETE ") ||
            upper.startsWith("--> PATCH ") ||
            upper.startsWith("--> HEAD ") ||
            upper.startsWith("--> OPTIONS ")
    }

    private fun isResponseStart(message: String): Boolean {
        return message.startsWith("<-- ")
    }

    private fun isRequestEnd(message: String): Boolean {
        return message.startsWith("--> END ")
    }

    private fun isResponseEnd(message: String): Boolean {
        return message.startsWith("<-- END HTTP")
    }
}
