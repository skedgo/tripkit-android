// TODO: Unit test - refactor
/* Disabled class due to mockito exception
package com.skedgo.tripkit.booking.ui.usecase

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.skedgo.tripkit.booking.BookingAction
import com.skedgo.tripkit.booking.BookingForm
import com.skedgo.tripkit.booking.BookingService
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable.just
import org.assertj.core.api.Java6Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given

@RunWith(AndroidJUnit4::class)
class GetBookingFormFromActionTest {
    val bookingService: BookingService = mock()
    val getBookingFormFromAction: GetBookingFormFromAction by lazy {
        GetBookingFormFromAction(bookingService)
    }

    @Test
    fun shouldGetBookingFormFromAction() {

        val bookingForm = mock<BookingForm>()
        val bookingAction = mock<BookingAction>()
        whenever(bookingAction.url).thenReturn("action url")
        whenever(bookingForm.action).thenReturn(bookingAction)
        whenever(bookingForm.form).thenReturn(null)

        val expectedBookingForm = mock<BookingForm>()

        given(
            bookingService.postFormAsync(
                "action url",
                null
            )
        ).willReturn(just(expectedBookingForm).toFlowable(BackpressureStrategy.BUFFER))

        val actualBookingForm = getBookingFormFromAction.execute(bookingForm).test()
        assertThat(actualBookingForm.values().first()).isEqualTo(expectedBookingForm)
    }
}
 */