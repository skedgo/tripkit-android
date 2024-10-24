package com.skedgo.tripkit.booking.ui.usecase

import com.skedgo.tripkit.booking.BookingForm
import javax.inject.Inject

open class IsCancelAction @Inject constructor() {

    open fun execute(bookingForm: BookingForm?): Boolean {
        val bookingStatusField = bookingForm?.form?.firstOrNull()?.fields?.firstOrNull()

        return bookingStatusField?.getValue() == "Cancelled"
    }

}