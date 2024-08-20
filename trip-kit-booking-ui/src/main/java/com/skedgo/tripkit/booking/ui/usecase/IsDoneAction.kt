package com.skedgo.tripkit.booking.ui.usecase

import com.skedgo.tripkit.booking.BookingForm
import javax.inject.Inject

open class IsDoneAction @Inject constructor(
    private val isCancelAction: IsCancelAction
) {

    open fun execute(bookingForm: BookingForm?): Boolean {
        return bookingForm?.action?.url == null && !isCancelAction.execute(bookingForm)
    }

}