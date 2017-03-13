package com.skedgo.android.tripkit.booking.ui.usecase

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.skedgo.android.tripkit.booking.*
import org.assertj.core.api.Assertions.assertThat
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

  
}