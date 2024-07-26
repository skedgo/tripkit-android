package com.skedgo.tripkit.data.datetime

import android.content.Context
import com.skedgo.tripkit.datetime.PrintFullDate
import com.skedgo.tripkit.datetime.PrintTime
import dagger.Module
import dagger.Provides

@Module
class DateTimeDataModule {
    @Provides
    fun printTime(context: Context): PrintTime = PrintTimeImpl(context)

    @Provides
    fun printFullDate(): PrintFullDate = PrintFullDateImpl()
}