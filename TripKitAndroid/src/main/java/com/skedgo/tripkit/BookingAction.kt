package com.skedgo.tripkit

import android.content.Intent
import com.skedgo.tripkit.bookingproviders.BookingProvider
import org.immutables.value.Value.Immutable
import org.immutables.value.Value.Style
import org.immutables.value.Value.Style.BuilderVisibility.PACKAGE
import org.immutables.value.Value.Style.ImplementationVisibility.PRIVATE

@Immutable
@Style(visibility = PRIVATE, builderVisibility = PACKAGE)
abstract class BookingAction {
    @BookingProvider
    abstract fun bookingProvider(): Int

    abstract fun hasApp(): Boolean

    abstract fun data(): Intent

    interface Builder {
        fun bookingProvider(@BookingProvider bookingProvider: Int): Builder

        fun hasApp(hasApp: Boolean): Builder

        fun data(data: Intent?): Builder

        fun build(): BookingAction
    }

    companion object {
        fun builder(): Builder {
            return BookingActionBuilder()
        }
    }
}