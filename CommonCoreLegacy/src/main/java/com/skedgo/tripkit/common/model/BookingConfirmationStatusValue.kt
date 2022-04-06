package com.skedgo.tripkit.common.model

import androidx.annotation.StringDef

@Retention(AnnotationRetention.RUNTIME)
@StringDef(
        BookingConfirmationStatusValue.PROCESSING,
        BookingConfirmationStatusValue.PROVIDER_ACCEPTED,
        BookingConfirmationStatusValue.USER_ACCEPTED,
        BookingConfirmationStatusValue.ARRIVING,
        BookingConfirmationStatusValue.IN_PROGRESS,
        BookingConfirmationStatusValue.PROVIDER_CANCELED,
        BookingConfirmationStatusValue.USER_CANCELED,
        BookingConfirmationStatusValue.COMPLETED
)
annotation class BookingConfirmationStatusValue {
    companion object {
        const val PROCESSING = "PROCESSING"
        const val PROVIDER_ACCEPTED = "PROVIDER_ACCEPTED"
        const val USER_ACCEPTED = "USER_ACCEPTED"
        const val ARRIVING = "ARRIVING"
        const val IN_PROGRESS = "IN_PROGRESS"
        const val PROVIDER_CANCELED = "PROVIDER_CANCELED"
        const val USER_CANCELED = "USER_CANCELED"
        const val COMPLETED = "COMPLETED"
        const val PENDING_CHANGES = "PENDING_CHANGES"
    }
}