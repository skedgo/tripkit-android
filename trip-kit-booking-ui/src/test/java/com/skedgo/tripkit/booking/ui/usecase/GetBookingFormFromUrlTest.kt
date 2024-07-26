package com.skedgo.tripkit.booking.ui.usecase

import com.nhaarman.mockitokotlin2.mock
import com.skedgo.tripkit.booking.BookingForm
import com.skedgo.tripkit.booking.BookingService
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable.just
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.mockito.BDDMockito.given

class GetBookingFormFromUrlTest {
    val bookingService: BookingService = mock()
    val getBookingFormFromUrl: GetBookingFormFromUrl by lazy {
        GetBookingFormFromUrl(bookingService)
    }

    @Test
    fun shouldGetBookingFormFromUrl() {
        val expectedBookingForm = mock<BookingForm>()
        given(bookingService.getFormAsync("url")).willReturn(
            just(expectedBookingForm).toFlowable(
                BackpressureStrategy.BUFFER
            )
        )

        val actualBookingForm = getBookingFormFromUrl.execute("url").test()
        assertThat(actualBookingForm.values().first()).isEqualTo(expectedBookingForm)
    }
}