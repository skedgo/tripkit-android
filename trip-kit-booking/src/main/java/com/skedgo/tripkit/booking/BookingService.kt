package com.skedgo.tripkit.booking

import io.reactivex.Flowable

interface BookingService {
    fun getFormAsync(url: String): Flowable<BookingForm>

    fun postFormAsync(url: String, inputForm: InputForm?): Flowable<BookingForm>

    fun postActionInputAsync(url: String, field: String, value: String): Flowable<BookingForm>
}
