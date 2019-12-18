package com.skedgo.android.tripkit.booking.ui.usecase

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.skedgo.android.tripkit.booking.BookingAction
import com.skedgo.android.tripkit.booking.BookingForm
import com.skedgo.android.tripkit.booking.BookingService
import io.reactivex.BackpressureStrategy
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.mockito.BDDMockito.given
import io.reactivex.Observable.just

class GetBookingFormFromActionTest {
  val bookingService: BookingService = mock()
  val getBookingFormFromAction: GetBookingFormFromAction by lazy {
    GetBookingFormFromAction(bookingService)
  }

  @Test fun shouldGetBookingFormFromAction() {

    val bookingForm = mock<BookingForm>()
    val bookingAction = mock<BookingAction>()
    whenever(bookingAction.url).thenReturn("action url")
    whenever(bookingForm.action).thenReturn(bookingAction)
    whenever(bookingForm.form).thenReturn(null)

    val expectedBookingForm = mock<BookingForm>()

    given(bookingService.postFormAsync("action url", null)).willReturn(just(expectedBookingForm).toFlowable(BackpressureStrategy.BUFFER))

    val actualBookingForm = getBookingFormFromAction.execute(bookingForm).test()
    assertThat(actualBookingForm.values().first()).isEqualTo(expectedBookingForm)
  }
}