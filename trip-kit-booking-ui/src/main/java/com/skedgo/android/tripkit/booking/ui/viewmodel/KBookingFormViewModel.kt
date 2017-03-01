package com.skedgo.android.tripkit.booking.ui.viewmodel

import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableList
import android.os.Bundle
import com.skedgo.android.tripkit.booking.BookingForm
import com.skedgo.android.tripkit.booking.FormGroup
import com.skedgo.android.tripkit.booking.PasswordFormField
import com.skedgo.android.tripkit.booking.StringFormField
import com.skedgo.android.tripkit.booking.ui.activity.KEY_FORM
import com.skedgo.android.tripkit.booking.ui.activity.KEY_URL
import com.skedgo.android.tripkit.booking.ui.usecase.GetBookingForm
import rx.Observable
import rx.android.schedulers.AndroidSchedulers.mainThread
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
  val actionTitle: ObservableField<String> = ObservableField()
  val showAction = ObservableBoolean(false)

  val onUpdateFormTitle: PublishSubject<String> = PublishSubject.create()
  val onNextBookingForm: PublishSubject<BookingForm> = PublishSubject.create()

  fun fetchBookingFormAsync(bundle: Bundle): Observable<Any?> {

    val url = bundle.getString(KEY_URL)
    val bookingForm: BookingForm? = bundle.getParcelable(KEY_FORM)

    return when {
      url != null -> getBookingForm.execute(url)
      bookingForm != null -> Observable.just(bookingForm)
      else -> Observable.error(Error("Wrong booking form request parameter"))
    }
        .observeOn(mainThread())
        .doOnNext { bookingForm ->
          updateBookingFormInfo(bookingForm)
          updateFieldList(bookingForm.form)
        }
        .doOnSubscribe { isLoading.set(true) }
        .doOnCompleted { isLoading.set(false) }
        .cast(Any::class.java)

  }

  private fun updateBookingFormInfo(bookingForm: BookingForm) {
    onUpdateFormTitle.onNext(bookingForm.title ?: "")
    actionTitle.set(bookingForm.action?.title)
    showAction.set(bookingForm.action != null)
  }

  private fun updateFieldList(form: List<FormGroup>) {
    form
        .flatMap {
          items.add(it.title)
          it.fields
        }
        .forEach {
          when (it) {
            is StringFormField -> items.add(FieldStringViewModel(it))
            is PasswordFormField -> items.add(FieldPasswordViewModel(it))
            is BookingForm -> items.add(BookingFormFieldViewModel(it, onNextBookingForm))
          }
        }
  }


}