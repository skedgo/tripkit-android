package com.skedgo.android.tripkit.booking.ui.viewmodel

import android.text.InputType
import com.skedgo.android.tripkit.booking.StringFormField

class FieldStringViewModel(val stringFormField: StringFormField) : DisposableViewModel() {

  val isHidden: Boolean get() = stringFormField.isHidden
  val isReadOnly: Boolean get() = stringFormField.isReadOnly
  val showValue: Boolean get() = stringFormField.title != stringFormField.value && stringFormField.isReadOnly

  val title: String get() = stringFormField.title ?: ""
  val sideTitle: String get() = stringFormField.sidetitle ?: ""
  val value: String get() = stringFormField.value ?: ""
  val editText: String get() = stringFormField.value ?: ""

  fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
    stringFormField.setValue(s.toString())
  }

  fun inputType(): Int {
    return when (stringFormField.keyboardType) {
      "PHONE" -> InputType.TYPE_CLASS_PHONE
      "EMAIL" -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
      else -> InputType.TYPE_NULL
    }
  }
}