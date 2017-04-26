package com.skedgo.android.tripkit.booking.ui.viewmodel

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.skedgo.android.tripkit.booking.PasswordFormField
import com.skedgo.android.tripkit.booking.ui.BuildConfig
import com.skedgo.android.tripkit.booking.ui.TestRunner
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@RunWith(TestRunner::class)
@Config(constants = BuildConfig::class)
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