package com.skedgo.android.tripkit.booking.ui.viewmodel

import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableList
import android.os.Bundle
import com.skedgo.android.tripkit.booking.StringFormField
import com.skedgo.android.tripkit.booking.ui.activity.BookingActivity.KEY_URL
import com.skedgo.android.tripkit.booking.ui.usecase.GetBookingForm
import rx.Observable
import rx.subjects.PublishSubject
import javax.inject.Inject

class KBookingFormViewModel
@Inject constructor(
    private val getBookingForm: GetBookingForm
) : DisposableViewModel() {

  val hasError = ObservableBoolean(false)
  val isLoading = ObservableBoolean(false)
  val items: ObservableList<Any> = ObservableArrayList()
  val title: ObservableField<String> = ObservableField()

  val onUpdateFormTitle: PublishSubject<String> = PublishSubject.create()

  fun fetchBookingFormAsync(bundle: Bundle): Observable<Any?> {

    val url = bundle.getString(KEY_URL)

    return when {
      url != null -> getBookingForm.fetchBookingFormAsync(url)
      else -> Observable.error(Error("Wrong booking form request parameter"))
    }
        .doOnNext { bookingForm ->

          onUpdateFormTitle.onNext(bookingForm.title ?: "")

          bookingForm.form
              .flatMap {
                it.fields
              }
              .forEach {
                when (it) {
                  is StringFormField -> items.add(FieldStringViewModel(it))
                }
              }
        }
        .doOnSubscribe { isLoading.set(true) }
        .doOnCompleted { isLoading.set(false) }
        .cast(Any::class.java)

  }

}