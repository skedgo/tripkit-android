package skedgo.tripkit.android

import dagger.Subcomponent
import skedgo.tripkit.datetime.DateTimeDataModule
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
  fun printTime(): PrintTime
}