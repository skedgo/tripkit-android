package com.skedgo.android.tripkit.booking.ui.viewmodel

import com.skedgo.android.tripkit.booking.PasswordFormField

class FieldPasswordViewModel(val passwordFormField: PasswordFormField) : DisposableViewModel() {

  val isHidden: Boolean get() = passwordFormField.isHidden

  val editText: String get() = passwordFormField.value ?: ""
  val hint: String = passwordFormField.title.orEmpty()
  fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
    passwordFormField.setValue(s.toString())
  }

}