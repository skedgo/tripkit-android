package com.skedgo.tripkit.common.model.booking.confirmation

import android.os.Parcel
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.skedgo.tripkit.common.model.booking.confirmation.ImmutableBookingConfirmation
import com.skedgo.tripkit.common.model.booking.confirmation.ImmutableBookingConfirmationAction
import com.skedgo.tripkit.common.model.booking.confirmation.ImmutableBookingConfirmationInputNew
import com.skedgo.tripkit.common.model.booking.confirmation.ImmutableBookingConfirmationInputOptions
import com.skedgo.tripkit.common.model.booking.confirmation.ImmutableBookingConfirmationStatus
import org.assertj.core.api.Java6Assertions
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BookingConfirmationTest {
    @Test
    fun parcel() {
        val expected: BookingConfirmation = ImmutableBookingConfirmation.builder()
            .status(
                ImmutableBookingConfirmationStatus.builder()
                    .title("Some title")
                    .build()
            )
            .addActions(
                ImmutableBookingConfirmationAction.builder()
                    .title("Some title")
                    .type(BookingConfirmationAction.TYPE_CALL)
                    .isDestructive(false)
                    .build()
            )
            .addInput(
                ImmutableBookingConfirmationInputNew.builder()
                    .id("id")
                    .required(true)
                    .type("type")
                    .title("Some title")
                    .addOptions(
                        ImmutableBookingConfirmationInputOptions.builder()
                            .id("id")
                            .title("Some title")
                            .build()
                    )
                    .values(emptyList())
                    .build()
            )
            .purchasedTickets(emptyList())
            .build()

        val parcel = Parcel.obtain()
        expected.writeToParcel(parcel, expected.describeContents())
        parcel.setDataPosition(0)

        val actual = BookingConfirmation.CREATOR.createFromParcel(parcel)

        parcel.recycle()

        Java6Assertions.assertThat(actual).isEqualTo(expected)
    }
}
