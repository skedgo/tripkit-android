package com.skedgo.tripkit.android

import dagger.Subcomponent
import com.skedgo.tripkit.data.datetime.DateTimeDataModule
import skedgo.tripkit.datetime.PrintFullDate
import skedgo.tripkit.datetime.PrintTime

/**
 * Creates UseCases and Repositories related to the DateTime feature.
 */
@FeatureScope
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