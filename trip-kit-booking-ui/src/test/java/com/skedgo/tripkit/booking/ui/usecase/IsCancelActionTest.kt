package com.skedgo.tripkit.booking.ui.usecase

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.skedgo.tripkit.booking.BookingForm
import com.skedgo.tripkit.booking.FormField
import com.skedgo.tripkit.booking.FormGroup
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class IsCancelActionTest {
    val isCancelAction: IsCancelAction by lazy {
        IsCancelAction()
    }

    @Test
    fun shouldNotBeCancelActionNull() {

        val actualIsCancelAction = isCancelAction.execute(null)
        assertThat(actualIsCancelAction).isFalse()
    }

    @Test
    fun shouldNotBeCancelActionAny() {

        val bookingForm = mock<BookingForm>()
        val formGroup = mock<FormGroup>()
        val formField = mock<FormField>()
        whenever(formField.getValue()).thenReturn("any action")
        whenever(bookingForm.form).thenReturn(listOf(formGroup))
        whenever(formGroup.fields).thenReturn(listOf(formField))


        val actualIsCancelAction = isCancelAction.execute(bookingForm)
        assertThat(actualIsCancelAction).isFalse()
    }

    @Test
    fun shouldBeCancelAction() {

        val bookingForm = mock<BookingForm>()
        val formGroup = mock<FormGroup>()
        val formField = mock<FormField>()
        whenever(formField.getValue()).thenReturn("Cancelled")
        whenever(bookingForm.form).thenReturn(listOf(formGroup))
        whenever(formGroup.fields).thenReturn(listOf(formField))


        val actualIsCancelAction = isCancelAction.execute(bookingForm)
        assertThat(actualIsCancelAction).isTrue()
    }
}