package com.skedgo.tripkit.booking

import com.skedgo.TripKit
import com.skedgo.tripkit.scope.ExtensionScope
import dagger.Component

/**
 * To initialize this, refer to [DaggerBookingComponent.builder].
 */
@ExtensionScope
@Component(modules = [BookingModule::class], dependencies = [TripKit::class])
interface BookingComponent {
    fun quickBookingApi(): QuickBookingApi?

    fun authService(): AuthService?

    fun bookingService(): BookingService?
}