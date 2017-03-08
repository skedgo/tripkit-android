package com.skedgo.android.tripkit.booking.ui.viewmodel

import android.content.res.Resources
import android.databinding.ObservableArrayList
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableList
import android.os.Bundle
import com.skedgo.android.tripkit.booking.*
import com.skedgo.android.tripkit.booking.ui.activity.*
import com.skedgo.android.tripkit.booking.ui.usecase.GetBookingFormFromAction
import com.skedgo.android.tripkit.booking.ui.usecase.GetBookingFormFromUrl
import com.skedgo.android.tripkit.booking.ui.usecase.IsCancelAction
import com.skedgo.android.tripkit.booking.ui.usecase.IsDoneAction
import rx.Observable
import rx.android.schedulers.AndroidSchedulers.mainThread
import rx.subjects.PublishSubject
import javax.inject.Inject

class KBookingFormViewModel
@Inject constructor(
    private val resources: Resources,
    private val getBookingFormFromUrl: GetBookingFormFromUrl,
    private val getBookingFormFromAction: GetBookingFormFromAction,
    private val isCancelAction: IsCancelAction,
    private val isDoneAction: IsDoneAction
) : DisposableViewModel() {

  val hasError = ObservableBoolean(false)
  val isLoading = ObservableBoolean(false)
  val items: ObservableList<Any> = ObservableArrayList()
  val title: ObservableField<String> = ObservableField()
  val actionTitle: ObservableField<String> = ObservableField()
  val errorTitle: ObservableField<String> = ObservableField()
  val errorMessage: ObservableField<String> = ObservableField()
  val retryText: ObservableField<String> = ObservableField()
  val showAction = ObservableBoolean(false)
  val showRetry = ObservableBoolean(false)

  val onUpdateFormTitle: PublishSubject<String> = PublishSubject.create()
  val onNextBookingForm: PublishSubject<BookingForm> = PublishSubject.create()
  val onNextBookingFormAction: PublishSubject<BookingForm> = PublishSubject.create()
  val onExternalForm: PublishSubject<ExternalFormField> = PublishSubject.create()
  val onDone: PublishSubject<BookingForm> = PublishSubject.create()
  val onCancel: PublishSubject<Boolean> = PublishSubject.create()
  val onErrorRetry: PublishSubject<String> = PublishSubject.create()

  private var bookingForm: BookingForm? = null
  private var bookingError: BookingError? = null

  fun onAction() {
    when {
      isCancelAction.execute(bookingForm) -> onCancel.onNext(true)
      isDoneAction.execute(bookingForm) -> onDone.onNext(bookingForm)
      else -> onNextBookingFormAction.onNext(bookingForm)
    }
  }

  fun onRetry() {
    onErrorRetry.onNext(bookingError?.url)
  }

  fun onCancel() {
    onCancel.onNext(true)
  }

  fun fetchBookingFormAsync(bundle: Bundle): Observable<Any?> {

    val type = bundle.getInt(KEY_TYPE, BOOKING_TYPE_URL)

    return when (type) {
      BOOKING_TYPE_URL -> getBookingFormFromUrl.execute(bundle.getString(KEY_URL))
      BOOKING_TYPE_FORM -> Observable.just(bundle.getParcelable(KEY_FORM))
      BOOKING_TYPE_ACTION -> getBookingFormFromAction.execute(bundle.getParcelable(KEY_FORM))
      else -> Observable.error(Error("Wrong booking form request parameter"))
    }
        .observeOn(mainThread())
        .doOnNext { nextBookingForm ->

          if (nextBookingForm == null) {
            onDone.onNext(nextBookingForm)
          } else {
            bookingForm = nextBookingForm
            updateBookingFormInfo()
            updateFieldList()
          }

        }
        .doOnError { bookingError ->
          isLoading.set(false)
          if (bookingError is BookingError) {
            showError(bookingError)
          }
        }
        .doOnSubscribe { isLoading.set(true) }
        .doOnCompleted { isLoading.set(false) }
        .cast(Any::class.java)

  }

  private fun showError(error: BookingError) {

    bookingError = error

    hasError.set(true)

    errorTitle.set(error.title)
    errorMessage.set(error.error)

    showRetry.set(error.recoveryTitle != null && error.url != null)

    retryText.set(
        if (error.recoveryTitle != null && error.url != null) {
          error.recoveryTitle
        } else {
          resources.getString(R.string.retry)
        })

  }

  private fun updateBookingFormInfo() {
    onUpdateFormTitle.onNext(bookingForm?.title ?: "")
    actionTitle.set(bookingForm?.action?.title)
    showAction.set(bookingForm?.action != null)
  }

  private fun updateFieldList() {
    bookingForm?.form
        ?.forEach {
          if (it.title != null) {
            items.add(it.title)
          }
          it.fields.forEach {
            when (it) {
              is StringFormField -> items.add(FieldStringViewModel(it))
              is PasswordFormField -> items.add(FieldPasswordViewModel(it))
              is ExternalFormField -> items.add(FieldExternalViewModel(it, onExternalForm))
              is BookingForm -> items.add(BookingFormFieldViewModel(it, onNextBookingForm))
            }
          }
          if (it.footer != null) {
            items.add(it.footer)
          }
        }
  }
}