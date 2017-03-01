package com.skedgo.android.tripkit.booking.ui.viewmodel

import com.skedgo.android.tripkit.booking.BookingForm
import rx.subjects.PublishSubject

class BookingFormFieldViewModel(val bookingForm: BookingForm,
                                val onNextBookingForm: PublishSubject<BookingForm>
) : DisposableViewModel() {

  val title: String get() = bookingForm.title ?: ""
  val subTitle: String get() = bookingForm.subtitle ?: ""


  fun onBookingFormAction() {
    onNextBookingForm.onNext(bookingForm)
  }
}

