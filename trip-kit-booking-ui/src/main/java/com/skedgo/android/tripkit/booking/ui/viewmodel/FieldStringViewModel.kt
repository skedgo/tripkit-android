package com.skedgo.android.tripkit.booking.ui.viewmodel

import android.text.InputType
import com.skedgo.android.tripkit.booking.StringFormField

class FieldStringViewModel(val stringFormField: StringFormField) : DisposableViewModel() {

  val isHidden get() = stringFormField.isHidden
  val isReadOnly get() = stringFormField.isReadOnly
  val showValue get() = stringFormField.title != stringFormField.value && stringFormField.isReadOnly

  val title get() = stringFormField.title ?: ""
  val sideTitle get() = stringFormField.sidetitle ?: ""
  val value get() = stringFormField.value ?: ""
  val editText get() = stringFormField.value ?: ""

  fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
    stringFormField.value = s.toString()
  }

  fun inputType(): Int {
    return when (stringFormField.keyboardType) {
      "PHONE" -> InputType.TYPE_CLASS_PHONE
      "EMAIL" -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
      else -> InputType.TYPE_CLASS_TEXT
    }
  }
}