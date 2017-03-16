package com.skedgo.android.tripkit.booking.ui.viewmodel

import com.skedgo.android.tripkit.booking.BookingForm
import rx.subjects.PublishSubject

class FieldBookingFormViewModel(val bookingForm: BookingForm,
                                val onNextBookingForm: PublishSubject<BookingForm>
) : DisposableViewModel() {

  val title get() = bookingForm.title ?: ""
  val subTitle get() = bookingForm.subtitle ?: ""
  val hasImageUrl get() = !bookingForm.imageUrl.isNullOrEmpty()
  val imageUrl get() = bookingForm.imageUrl

  fun onBookingFormAction() {
    onNextBookingForm.onNext(bookingForm)
  }
}

