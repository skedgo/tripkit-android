package com.skedgo.android.tripkit.booking.ui.usecase

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.skedgo.android.tripkit.booking.BookingForm
import com.skedgo.android.tripkit.booking.FormField
import com.skedgo.android.tripkit.booking.FormGroup
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class IsCancelActionTest {
  val isCancelAction: IsCancelAction by lazy {
    IsCancelAction()
  }

  @Test fun shouldNotBeCancelActionNull() {

    val actualIsCancelAction = isCancelAction.execute(null)
    assertThat(actualIsCancelAction).isFalse()
  }

  @Test fun shouldNotBeCancelActionAny() {

    val bookingForm = mock<BookingForm>()
    val formGroup = mock<FormGroup>()
    val formField = mock<FormField>()
    whenever(formField.value).thenReturn("any action")
    whenever(bookingForm.form).thenReturn(listOf(formGroup))
    whenever(formGroup.fields).thenReturn(listOf(formField))


    val actualIsCancelAction = isCancelAction.execute(bookingForm)
    assertThat(actualIsCancelAction).isFalse()
  }

  @Test fun shouldBeCancelAction() {

    val bookingForm = mock<BookingForm>()
    val formGroup = mock<FormGroup>()
    val formField = mock<FormField>()
    whenever(formField.value).thenReturn("Cancelled")
    whenever(bookingForm.form).thenReturn(listOf(formGroup))
    whenever(formGroup.fields).thenReturn(listOf(formField))


    val actualIsCancelAction = isCancelAction.execute(bookingForm)
    assertThat(actualIsCancelAction).isTrue()
  }
}