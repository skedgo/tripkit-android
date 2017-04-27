package com.skedgo.android.tripkit.booking.ui.usecase

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.skedgo.android.tripkit.booking.BookingAction
import com.skedgo.android.tripkit.booking.BookingForm
import com.skedgo.android.tripkit.booking.FormField
import com.skedgo.android.tripkit.booking.FormGroup
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.mockito.BDDMockito.given

class IsDoneActionTest {
  val isCancelAction: IsCancelAction = mock()
  val isDoneAction: IsDoneAction by lazy {
    IsDoneAction(isCancelAction)
  }

  @Test fun shouldBeDoneActionNull() {

    val actualIsDoneAction = isDoneAction.execute(null)
    assertThat(actualIsDoneAction).isTrue()
  }

  @Test fun shouldBeDoneUrlNull() {

    val bookingForm = mock<BookingForm>()
    val action = mock<BookingAction>()
    whenever(action.url).thenReturn(null)
    whenever(bookingForm.action).thenReturn(action)

    val actualIsDoneAction = isDoneAction.execute(bookingForm)
    assertThat(actualIsDoneAction).isTrue()
  }

  @Test fun shouldNotBeDoneCancel() {

    val bookingForm = mock<BookingForm>()
    val action = mock<BookingAction>()
    whenever(action.url).thenReturn("url")
    whenever(bookingForm.action).thenReturn(action)
    val formGroup = mock<FormGroup>()
    val formField = mock<FormField>()
    whenever(formField.value).thenReturn("Cancelled")
    whenever(bookingForm.form).thenReturn(listOf(formGroup))
    whenever(formGroup.fields).thenReturn(listOf(formField))

    given(isCancelAction.execute(bookingForm)).willReturn(true)

    val actualIsDoneAction = isDoneAction.execute(bookingForm)
    assertThat(actualIsDoneAction).isFalse()
  }

  @Test fun shouldBeDoneAction() {

    val bookingForm = mock<BookingForm>()
    val action = mock<BookingAction>()
    whenever(action.url).thenReturn(null)
    whenever(bookingForm.action).thenReturn(action)
    val formGroup = mock<FormGroup>()
    val formField = mock<FormField>()
    whenever(formField.value).thenReturn("any action")
    whenever(bookingForm.form).thenReturn(listOf(formGroup))
    whenever(formGroup.fields).thenReturn(listOf(formField))

    given(isCancelAction.execute(bookingForm)).willReturn(false)

    val actualIsDoneAction = isDoneAction.execute(bookingForm)
    assertThat(actualIsDoneAction).isTrue()
  }
}