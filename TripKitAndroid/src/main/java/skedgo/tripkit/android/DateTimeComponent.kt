package skedgo.tripkit.android

import dagger.Subcomponent
import skedgo.tripkit.datetime.DateTimeDataModule
import skedgo.tripkit.datetime.PrintFullDate
import skedgo.tripkit.datetime.PrintTime
import javax.inject.Singleton

/**
 * Creates UseCases and Repositories related to the DateTime feature.
 */
@Singleton
@Subcomponent(modules = arrayOf(
    DateTimeDataModule::class
))
interface DateTimeComponent {
  val printTime: PrintTime

  /**
   * For example, `Monday, 1 May 2017`
   * if the default locale is [java.util.Locale.US].
   */
  val printFullDate: PrintFullDate
}