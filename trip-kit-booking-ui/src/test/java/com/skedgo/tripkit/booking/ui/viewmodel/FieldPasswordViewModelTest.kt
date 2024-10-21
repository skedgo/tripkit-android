package com.skedgo.tripkit.booking.ui.viewmodel

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.skedgo.tripkit.booking.PasswordFormField
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FieldPasswordViewModelTest {

    @Test
    fun shouldBeHidden() {

        val passwordFormField: PasswordFormField = mock()
        whenever(passwordFormField.hidden).thenReturn(true)

        val viewModel: FieldPasswordViewModel = FieldPasswordViewModel(passwordFormField)

        assertThat(viewModel.isHidden).isTrue()

    }

    @Test
    fun shouldNotBeHidden() {

        val passwordFormField: PasswordFormField = mock()
        whenever(passwordFormField.hidden).thenReturn(false)

        val viewModel: FieldPasswordViewModel = FieldPasswordViewModel(passwordFormField)

        assertThat(viewModel.isHidden).isFalse()

    }

    @Test
    fun shouldSetEditTextValue() {

        val passwordFormField: PasswordFormField = mock()
        whenever(passwordFormField.mValue).thenReturn("value")

        val viewModel: FieldPasswordViewModel = FieldPasswordViewModel(passwordFormField)

        assertThat(viewModel.editText).isEqualTo("value")

    }

    @Test
    fun shouldSetEmptyEditTextValue() {

        val passwordFormField: PasswordFormField = mock()
        whenever(passwordFormField.mValue).thenReturn(null)

        val viewModel: FieldPasswordViewModel = FieldPasswordViewModel(passwordFormField)

        assertThat(viewModel.editText).isEqualTo("")

    }

}