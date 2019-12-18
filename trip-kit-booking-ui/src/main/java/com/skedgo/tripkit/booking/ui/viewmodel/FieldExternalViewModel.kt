package com.skedgo.tripkit.booking.ui.viewmodel

import com.skedgo.tripkit.booking.ExternalFormField
import io.reactivex.subjects.PublishSubject

class FieldExternalViewModel(val externalFormField: ExternalFormField,
                             val onExternalForm: PublishSubject<ExternalFormField>
) : DisposableViewModel() {

  val title get() = externalFormField.title ?: ""

  fun onExternalFormAction() {
    onExternalForm.onNext(externalFormField)
  }
}