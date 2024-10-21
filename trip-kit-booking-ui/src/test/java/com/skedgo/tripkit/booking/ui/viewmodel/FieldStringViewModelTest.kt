package com.skedgo.tripkit.booking.ui.viewmodel

import android.text.InputType
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.skedgo.tripkit.booking.StringFormField
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FieldStringViewModelTest {

    @Test
    fun shouldBeHidden() {

        val stringFormField: StringFormField = mock()
        whenever(stringFormField.hidden).thenReturn(true)

        val viewModel: FieldStringViewModel = FieldStringViewModel(stringFormField)

        assertThat(viewModel.isHidden).isTrue()

    }

    @Test
    fun shouldNotBeHidden() {

        val stringFormField: StringFormField = mock()
        whenever(stringFormField.hidden).thenReturn(false)

        val viewModel: FieldStringViewModel = FieldStringViewModel(stringFormField)

        assertThat(viewModel.isHidden).isFalse()

    }

    @Test
    fun shouldBeReadOnly() {

        val stringFormField: StringFormField = mock()
        whenever(stringFormField.hidden).thenReturn(true)

        val viewModel: FieldStringViewModel = FieldStringViewModel(stringFormField)

        assertThat(viewModel.isReadOnly).isTrue()

    }

    @Test
    fun shouldNotBeReadOnly() {

        val stringFormField: StringFormField = mock()
        whenever(stringFormField.hidden).thenReturn(false)

        val viewModel: FieldStringViewModel = FieldStringViewModel(stringFormField)

        assertThat(viewModel.isReadOnly).isFalse()

    }

    @Test
    fun shouldShowValue() {

        val stringFormField: StringFormField = mock()
        whenever(stringFormField.title).thenReturn("title")
        whenever(stringFormField.mValue).thenReturn("value")
        whenever(stringFormField.readOnly).thenReturn(true)

        val viewModel: FieldStringViewModel = FieldStringViewModel(stringFormField)

        assertThat(viewModel.showValue).isTrue()

    }

    @Test
    fun shouldNotShowValueSameTitle() {

        val stringFormField: StringFormField = mock()
        whenever(stringFormField.title).thenReturn("title")
        whenever(stringFormField.mValue).thenReturn("title")
        whenever(stringFormField.readOnly).thenReturn(true)

        val viewModel: FieldStringViewModel = FieldStringViewModel(stringFormField)

        assertThat(viewModel.showValue).isFalse()

    }

    @Test
    fun shouldNotShowValueSameNotReadOnly() {

        val stringFormField: StringFormField = mock()
        whenever(stringFormField.title).thenReturn("title")
        whenever(stringFormField.mValue).thenReturn("value")
        whenever(stringFormField.readOnly).thenReturn(false)

        val viewModel: FieldStringViewModel = FieldStringViewModel(stringFormField)

        assertThat(viewModel.showValue).isFalse()

    }

    @Test
    fun shouldSetTitle() {

        val stringFormField: StringFormField = mock()
        whenever(stringFormField.title).thenReturn("title")

        val viewModel: FieldStringViewModel = FieldStringViewModel(stringFormField)

        assertThat(viewModel.title).isEqualTo("title")

    }

    @Test
    fun shouldSetEmptyTitle() {

        val stringFormField: StringFormField = mock()
        whenever(stringFormField.title).thenReturn(null)

        val viewModel: FieldStringViewModel = FieldStringViewModel(stringFormField)

        assertThat(viewModel.title).isEqualTo("")

    }

    @Test
    fun shouldSetSideTitle() {

        val stringFormField: StringFormField = mock()
        whenever(stringFormField.sidetitle).thenReturn("sideTitle")

        val viewModel: FieldStringViewModel = FieldStringViewModel(stringFormField)

        assertThat(viewModel.sideTitle).isEqualTo("sideTitle")

    }

    @Test
    fun shouldSetEmptySubTitle() {

        val stringFormField: StringFormField = mock()
        whenever(stringFormField.sidetitle).thenReturn(null)

        val viewModel: FieldStringViewModel = FieldStringViewModel(stringFormField)

        assertThat(viewModel.sideTitle).isEqualTo("")

    }

    @Test
    fun shouldSetValue() {

        val stringFormField: StringFormField = mock()
        whenever(stringFormField.mValue).thenReturn("value")

        val viewModel: FieldStringViewModel = FieldStringViewModel(stringFormField)

        assertThat(viewModel.value).isEqualTo("value")

    }

    @Test
    fun shouldSetEmptyValue() {

        val stringFormField: StringFormField = mock()
        whenever(stringFormField.mValue).thenReturn(null)

        val viewModel: FieldStringViewModel = FieldStringViewModel(stringFormField)

        assertThat(viewModel.value).isEqualTo("")

    }

    @Test
    fun shouldSetEditTextValue() {

        val stringFormField: StringFormField = mock()
        whenever(stringFormField.mValue).thenReturn("value")

        val viewModel: FieldStringViewModel = FieldStringViewModel(stringFormField)

        assertThat(viewModel.editText).isEqualTo("value")

    }

    @Test
    fun shouldSetEmptyEditTextValue() {

        val stringFormField: StringFormField = mock()
        whenever(stringFormField.mValue).thenReturn(null)

        val viewModel: FieldStringViewModel = FieldStringViewModel(stringFormField)

        assertThat(viewModel.editText).isEqualTo("")

    }

    @Test
    fun shouldBeKeyboardPhone() {

        val stringFormField: StringFormField = mock()
        whenever(stringFormField.keyboardType).thenReturn("PHONE")

        val viewModel: FieldStringViewModel = FieldStringViewModel(stringFormField)

        assertThat(viewModel.inputType()).isEqualTo(InputType.TYPE_CLASS_PHONE)

    }

    @Test
    fun shouldBeKeyboardEmail() {

        val stringFormField: StringFormField = mock()
        whenever(stringFormField.keyboardType).thenReturn("EMAIL")

        val viewModel: FieldStringViewModel = FieldStringViewModel(stringFormField)

        assertThat(viewModel.inputType()).isEqualTo(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)

    }

    @Test
    fun shouldBeKeyboardText() {

        val stringFormField: StringFormField = mock()
        whenever(stringFormField.keyboardType).thenReturn("other")

        val viewModel: FieldStringViewModel = FieldStringViewModel(stringFormField)

        assertThat(viewModel.inputType()).isEqualTo(InputType.TYPE_CLASS_TEXT)

    }
}