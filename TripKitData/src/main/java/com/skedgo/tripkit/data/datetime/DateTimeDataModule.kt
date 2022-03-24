package com.skedgo.tripkit.data.datetime

import android.content.Context
import dagger.Module
import dagger.Provides
import com.skedgo.tripkit.datetime.PrintFullDate
import com.skedgo.tripkit.datetime.PrintTime

@Module
class DateTimeDataModule {
  @Provides fun printTime(context: Context): PrintTime
      = PrintTimeImpl(context)

  @Provides fun printFullDate(): PrintFullDate = PrintFullDateImpl()
}