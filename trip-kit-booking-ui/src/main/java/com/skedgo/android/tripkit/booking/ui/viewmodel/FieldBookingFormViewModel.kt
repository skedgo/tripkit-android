package com.skedgo.android.tripkit.booking.ui.viewmodel

import com.skedgo.android.tripkit.booking.BookingForm
import rx.subjects.PublishSubject

class FieldBookingFormViewModel(val bookingForm: BookingForm,
                                val onNextBookingForm: PublishSubject<BookingForm>
) : DisposableViewModel() {

  val title: String get() = bookingForm.title ?: ""
  val subTitle: String get() = bookingForm.subtitle ?: ""
  val hasImageUrl: Boolean get() = !bookingForm.imageUrl.isNullOrEmpty()
  val imageUrl: String? get() = bookingForm.imageUrl

  fun onBookingFormAction() {
    onNextBookingForm.onNext(bookingForm)
  }
}

