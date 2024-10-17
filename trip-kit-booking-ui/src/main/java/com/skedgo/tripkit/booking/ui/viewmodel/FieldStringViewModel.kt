package com.skedgo.tripkit.booking.ui.viewmodel

import android.text.InputType
import com.skedgo.tripkit.booking.StringFormField

class FieldStringViewModel(val stringFormField: StringFormField) : DisposableViewModel() {

    val isHidden get() = stringFormField.hidden
    val isReadOnly get() = stringFormField.readOnly
    val showValue get() = stringFormField.title != stringFormField.mValue && stringFormField.readOnly

    val title get() = stringFormField.title ?: ""
    val sideTitle get() = stringFormField.sidetitle ?: ""
    val value get() = stringFormField.mValue ?: ""
    val editText get() = stringFormField.mValue ?: ""

    fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        stringFormField.mValue = s.toString()
    }

    fun inputType(): Int {
        return when (stringFormField.keyboardType) {
            "PHONE" -> InputType.TYPE_CLASS_PHONE
            "EMAIL" -> InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            else -> InputType.TYPE_CLASS_TEXT
        }
    }
}