package com.skedgo.tripkit.booking

import org.immutables.value.Value.Default
import org.immutables.value.Value.Immutable
import java.util.UUID

@Immutable
abstract class ExternalOAuth {
    abstract fun authServiceId(): String

    abstract fun token(): String

    abstract fun refreshToken(): String?

    abstract fun expiresIn(): Long

    @Default
    open fun id(): String {
        return UUID.randomUUID().toString()
    }
}
