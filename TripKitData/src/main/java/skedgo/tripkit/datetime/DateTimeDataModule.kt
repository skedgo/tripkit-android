package skedgo.tripkit.datetime

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class DateTimeDataModule {
  @Provides fun printTime(context: Context): PrintTime
      = PrintTimeImpl(context)

  @Provides fun printFullDate(): PrintFullDate = PrintFullDateImpl()
}