package com.skedgo.android.tripkit.booking.ui.viewmodel

import android.content.res.Resources
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import android.os.Bundle
import com.skedgo.android.tripkit.booking.ui.*
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.skedgo.android.tripkit.booking.*
import com.skedgo.android.tripkit.booking.ui.R
import com.skedgo.android.tripkit.booking.ui.activity.*
import com.skedgo.android.tripkit.booking.ui.usecase.GetBookingFormFromAction
import com.skedgo.android.tripkit.booking.ui.usecase.GetBookingFormFromUrl
import com.skedgo.android.tripkit.booking.ui.usecase.IsCancelAction
import com.skedgo.android.tripkit.booking.ui.usecase.IsDoneAction
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class BookingFormViewModel
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

  var bookingForm: BookingForm? = null
  var bookingError: BookingError? = null

  private val onRetryClicked = PublishSubject.create<Unit>()

  fun onAction() {
    when {
      isCancelAction.execute(bookingForm) -> onCancel.onNext(true)
      isDoneAction.execute(bookingForm) -> onDone.onNext(bookingForm!!)
      else -> onNextBookingFormAction.onNext(bookingForm!!)
    }
  }

  fun onRetry() {
    if (bookingError != null) {
      onErrorRetry.onNext(bookingError?.url!!)
    } else {
      onRetryClicked.onNext(Unit)
    }
  }

  fun onCancel() {
    onCancel.onNext(true)
  }

  fun fetchBookingFormAsync(bundle: Bundle): Observable<Any?> {

    val type = bundle.getInt(KEY_TYPE, BOOKING_TYPE_URL)

    return when {
      type == BOOKING_TYPE_URL && bundle.containsKey(KEY_URL) ->
        getBookingFormFromUrl.execute(bundle.getString(KEY_URL))
      type == BOOKING_TYPE_FORM && bundle.containsKey(KEY_FORM) ->
        Observable.just(bundle.getParcelable(KEY_FORM))
      type == BOOKING_TYPE_ACTION && bundle.containsKey(KEY_FORM) ->
        getBookingFormFromAction.execute(bundle.getParcelable(KEY_FORM)!!)
      else -> Observable.error(Error("Wrong booking form request parameter"))
    }
        .observeOn(mainThread())
        .doOnNext { nextBookingForm ->
          if (nextBookingForm == null || nextBookingForm == NullBookingForm) {
            onDone.onNext(NullBookingForm)
          } else {
            bookingForm = nextBookingForm
            updateBookingFormInfo()
            updateFieldList()
          }
        }
        .doOnError { bookingError ->
          isLoading.set(false)
          showError(bookingError)
        }
        .doOnSubscribe {
          hasError.set(false)
          isLoading.set(true)
        }
        .doOnComplete {
          isLoading.set(false)
        }
        .retryWhen {
          it.flatMap {
            if (it is Error) {
              Observable.error<Any>(it)
            } else {
              onRetryClicked
                  .firstOrError().toObservable()
            }
          }
        }
        .cast(Any::class.java)
  }

  fun showError(error: Throwable) {
    if (error is BookingError) {
      // user error case
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
    } else {
      // network error case
      bookingError = null
      hasError.set(true)
      errorTitle.set(resources.getString(R.string.an_error_has_occured))
      errorMessage.set(null)
      showRetry.set(true)
      retryText.set(resources.getString(R.string.retry))
    }
  }

  fun updateBookingFormInfo() {
    onUpdateFormTitle.onNext(bookingForm?.title ?: "")
    actionTitle.set(bookingForm?.action?.title)
    showAction.set(bookingForm?.action != null)
  }

  fun updateFieldList() {
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
              is BookingForm -> items.add(FieldBookingFormViewModel(it, onNextBookingForm))
            }
          }
          if (it.footer != null) {
            items.add(it.footer)
          }
        }

    items.lastOrNull { it is FieldPasswordViewModel }
        ?.let { it as FieldPasswordViewModel }
        ?.let {
          it.imeOptions = EditorInfo.IME_ACTION_SEND
          it.onEditorActionListener = TextView.OnEditorActionListener { _, _, _ ->
            onAction()
            true
          }
        }
  }
}