package com.skedgo.tripkit.data

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

/**
 * To make [TripKitSharedPreference] injectable instead of initializing with applicationContext
 * to avoid memory leakage.
 */
@Module
@InstallIn(SingletonComponent::class)
class TripKitPreferencesModule {
    @Provides
    @Named("TripKitPreference")
    internal fun tripGoSharedPreference(context: Context): TripKitSharedPreference {
        return TripKitSharedPreference(context)
    }
}