package com.skedgo.android.tripkit.booking.ui.viewmodel

import android.databinding.ObservableBoolean
import com.skedgo.android.tripkit.booking.ui.usecase.GetBookingForm
import rx.Observable
import javax.inject.Inject

class KBookingFormViewModel
@Inject constructor(
    private val getBookingForm: GetBookingForm
) : DisposableViewModel() {

  val isLoading = ObservableBoolean()

  fun fetchBookingFormAsync(url: String): Observable<Any?> {
    return getBookingForm.fetchBookingFormAsync(url)
        .doOnSubscribe { isLoading.set(true) }
        .doOnCompleted { isLoading.set(false) }
        .cast(Any::class.java)
  }

}