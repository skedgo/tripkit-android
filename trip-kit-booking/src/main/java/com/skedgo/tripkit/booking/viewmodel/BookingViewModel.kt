package com.skedgo.tripkit.booking.viewmodel

import com.skedgo.tripkit.booking.BookingForm
import com.skedgo.tripkit.booking.LinkFormField
import io.reactivex.Flowable

interface BookingViewModel {
    fun bookingForm(): Flowable<BookingForm>?

    fun nextBookingForm(): Flowable<Param>?

    fun loadForm(param: Param): Flowable<BookingForm>?

    fun isDone(): Flowable<Boolean>?

    fun observeAuthentication(authenticationViewModel: AuthenticationViewModel)

    fun performAction(bookingForm: BookingForm): Flowable<Boolean>?

    fun performAction(linkFormField: LinkFormField): Flowable<Boolean>?

    fun isFetching(): Flowable<Boolean>?

    fun paramFrom(form: BookingForm): Param?
}