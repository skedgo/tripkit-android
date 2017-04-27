package com.skedgo.android.tripkit.booking.ui.usecase

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.whenever
import com.skedgo.android.tripkit.booking.BookingAction
import com.skedgo.android.tripkit.booking.BookingForm
import com.skedgo.android.tripkit.booking.BookingService
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.mockito.BDDMockito.given
import rx.Observable.just

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

    given(bookingService.postFormAsync("action url", null)).willReturn(just(expectedBookingForm))

    val actualBookingForm = getBookingFormFromAction.execute(bookingForm).toBlocking().first()
    assertThat(actualBookingForm).isEqualTo(expectedBookingForm)
  }
}