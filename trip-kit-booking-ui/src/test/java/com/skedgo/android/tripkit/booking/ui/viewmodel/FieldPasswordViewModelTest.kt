package com.skedgo.android.tripkit.booking.ui.viewmodel

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.skedgo.android.tripkit.booking.PasswordFormField
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FieldPasswordViewModelTest {

  @Test fun shouldBeHidden() {

    val passwordFormField: PasswordFormField = mock()
    whenever(passwordFormField.isHidden).thenReturn(true)

    val viewModel: FieldPasswordViewModel = FieldPasswordViewModel(passwordFormField)

    assertThat(viewModel.isHidden).isTrue()

  }

  @Test fun shouldNotBeHidden() {

    val passwordFormField: PasswordFormField = mock()
    whenever(passwordFormField.isHidden).thenReturn(false)

    val viewModel: FieldPasswordViewModel = FieldPasswordViewModel(passwordFormField)

    assertThat(viewModel.isHidden).isFalse()

  }

  @Test fun shouldSetEditTextValue() {

    val passwordFormField: PasswordFormField = mock()
    whenever(passwordFormField.value).thenReturn("value")

    val viewModel: FieldPasswordViewModel = FieldPasswordViewModel(passwordFormField)

    assertThat(viewModel.editText).isEqualTo("value")

  }

  @Test fun shouldSetEmptyEditTextValue() {

    val passwordFormField: PasswordFormField = mock()
    whenever(passwordFormField.value).thenReturn(null)

    val viewModel: FieldPasswordViewModel = FieldPasswordViewModel(passwordFormField)

    assertThat(viewModel.editText).isEqualTo("")

  }

}