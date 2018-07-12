package com.skedgo.android.tripkit.booking.ui.viewmodel

import android.view.inputmethod.EditorInfo
import android.view.inputmethod.EditorInfo.IME_ACTION_NEXT
import android.view.inputmethod.EditorInfo.IME_ACTION_SEND
import android.widget.TextView
import com.skedgo.android.tripkit.booking.PasswordFormField

class FieldPasswordViewModel(val passwordFormField: PasswordFormField) : DisposableViewModel() {

  val isHidden: Boolean get() = passwordFormField.isHidden

  val editText: String get() = passwordFormField.value ?: ""
  val hint: String = passwordFormField.title.orEmpty()

  var onEditorActionListener: TextView.OnEditorActionListener? = null
  var imeOptions: Int = IME_ACTION_NEXT

  fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
    passwordFormField.value = s.toString()
  }
}